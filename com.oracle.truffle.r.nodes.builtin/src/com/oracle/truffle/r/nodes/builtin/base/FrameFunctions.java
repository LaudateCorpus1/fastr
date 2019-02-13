/*
 * Copyright (c) 2014, 2019, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 3 only, as
 * published by the Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 3 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 3 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */
package com.oracle.truffle.r.nodes.builtin.base;

import static com.oracle.truffle.r.runtime.builtins.RBehavior.COMPLEX;
import static com.oracle.truffle.r.runtime.builtins.RBuiltinKind.INTERNAL;
import static com.oracle.truffle.r.runtime.builtins.RBuiltinKind.SUBSTITUTE;

import java.util.ArrayList;

import com.oracle.truffle.api.CompilerAsserts;
import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.CompilerDirectives.TruffleBoundary;
import com.oracle.truffle.api.dsl.Cached;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.Frame;
import com.oracle.truffle.api.frame.FrameInstance.FrameAccess;
import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.MaterializedFrame;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.ExplodeLoop;
import com.oracle.truffle.api.profiles.BranchProfile;
import com.oracle.truffle.api.profiles.ConditionProfile;
import com.oracle.truffle.api.profiles.ValueProfile;
import com.oracle.truffle.r.nodes.RASTUtils;
import com.oracle.truffle.r.nodes.RRootNode;
import com.oracle.truffle.r.nodes.access.ConstantNode;
import com.oracle.truffle.r.nodes.builtin.RBuiltinNode;
import com.oracle.truffle.r.nodes.builtin.base.FrameFunctionsFactory.SysFrameNodeGen;
import com.oracle.truffle.r.nodes.function.ArgumentMatcher;
import com.oracle.truffle.r.nodes.function.CallArgumentsNode;
import com.oracle.truffle.r.nodes.function.GetCallerFrameNode;
import com.oracle.truffle.r.nodes.function.PromiseHelperNode.PromiseDeoptimizeFrameNode;
import com.oracle.truffle.r.nodes.function.PromiseNode;
import com.oracle.truffle.r.nodes.function.PromiseNode.VarArgNode;
import com.oracle.truffle.r.nodes.function.PromiseNode.VarArgsPromiseNode;
import com.oracle.truffle.r.nodes.function.RCallNode;
import com.oracle.truffle.r.runtime.ArgumentsSignature;
import com.oracle.truffle.r.runtime.CallerFrameClosure;
import com.oracle.truffle.r.runtime.HasSignature;
import com.oracle.truffle.r.runtime.RArguments;
import com.oracle.truffle.r.runtime.RCaller;
import com.oracle.truffle.r.runtime.RCaller.UnwrapPromiseCallerProfile;
import com.oracle.truffle.r.runtime.RCaller.UnwrapSysParentProfile;
import com.oracle.truffle.r.runtime.RError;
import com.oracle.truffle.r.runtime.RError.Message;
import com.oracle.truffle.r.runtime.RInternalError;
import com.oracle.truffle.r.runtime.RRuntime;
import com.oracle.truffle.r.runtime.Utils;
import com.oracle.truffle.r.runtime.builtins.RBuiltin;
import com.oracle.truffle.r.runtime.context.RContext;
import com.oracle.truffle.r.runtime.data.Closure;
import com.oracle.truffle.r.runtime.data.RArgsValuesAndNames;
import com.oracle.truffle.r.runtime.data.RDataFactory;
import com.oracle.truffle.r.runtime.data.RExpression;
import com.oracle.truffle.r.runtime.data.RFunction;
import com.oracle.truffle.r.runtime.data.RIntVector;
import com.oracle.truffle.r.runtime.data.RNull;
import com.oracle.truffle.r.runtime.data.RPairList;
import com.oracle.truffle.r.runtime.data.RPromise;
import com.oracle.truffle.r.runtime.env.REnvironment;
import com.oracle.truffle.r.runtime.env.frame.FrameSlotChangeMonitor;
import com.oracle.truffle.r.runtime.gnur.SEXPTYPE;
import com.oracle.truffle.r.runtime.nodes.RBaseNode;
import com.oracle.truffle.r.runtime.nodes.RNode;
import com.oracle.truffle.r.runtime.nodes.RSyntaxLookup;
import com.oracle.truffle.r.runtime.nodes.RSyntaxNode;

/**
 * sys.R. See
 * <a href="https://stat.ethz.ch/R-manual/R-devel/library/base/html/sys.parent.html">here</a>. N.B.
 * The frame for the sys functions themselves is not counted in the R spec. Frames are numbered 0,
 * 1, .. starting from .GlobalEnv. Non-negative arguments are frame numbers, negative arguments are
 * relative to the current frame.
 *
 * The main thing to note is distinction between {@code sys.parent} and {@code sys.frame}.
 * {@code sys.parent} gives you "logical" parent, but not necessarily the frame preceding the
 * current frame on the call stack. In FastR this is captured by {@link RCaller#getPrevious()}.
 * {@code sys.frame(n)} gives you frame with number {@code n} and traverses the stack without taking
 * the parent relation into account. In FastR the frame number is captured in
 * {@link RCaller#getDepth()}. See also builtins in {@code FrameFunctions} for more details.
 *
 * @see RArguments
 * @see RCaller
 */
public class FrameFunctions {

    public static final class FrameHelper extends RBaseNode {

        private final ConditionProfile currentFrameProfile = ConditionProfile.createBinaryProfile();
        private final ConditionProfile globalFrameProfile = ConditionProfile.createBinaryProfile();
        private final BranchProfile iterateProfile = BranchProfile.create();
        private final BranchProfile slowPathProfile = BranchProfile.create();
        private final UnwrapPromiseCallerProfile unwrapCallerProfile = new UnwrapPromiseCallerProfile();

        /**
         * Determine the frame access mode of a subclass. The rule of thumb is that subclasses that
         * only use the frame internally should not materialize it, i.e., they should use
         * {@link FrameAccess#READ_ONLY} or {@link FrameAccess#READ_WRITE}.
         */
        private final FrameAccess access;

        private final boolean ignoreDotInternal;

        public FrameHelper(FrameAccess access) {
            this(access, false);
        }

        public FrameHelper(FrameAccess access, boolean ignoreDotInternal) {
            this.access = access;
            this.ignoreDotInternal = ignoreDotInternal;
        }

        // Note: the helper methods either return MaterializedFrame or they read some specific
        // argument from the target frame, but without returning it, in which case we may avoid
        // frame materialization in some cases. If a method returns both virtual and materialized
        // frame disguised under common Frame, the compiler may have some issue in finding out that
        // the virtual frame does not escape.

        protected MaterializedFrame getMaterializedFrame(VirtualFrame frame, int n) {
            assert access == FrameAccess.MATERIALIZE;
            RCaller c = RArguments.getCall(frame);
            int actualFrame = decodeFrameNumber(c, n);
            MaterializedFrame numberedFrame = (MaterializedFrame) getNumberedFrame(frame, actualFrame, true);
            return RInternalError.guaranteeNonNull(numberedFrame);
        }

        protected RFunction getFunction(VirtualFrame frame, int n) {
            assert access == FrameAccess.READ_ONLY;
            RCaller currentCall = RArguments.getCall(frame);
            int actualFrame = decodeFrameNumber(currentCall, n);
            Frame targetFrame = getNumberedFrame(frame, actualFrame, false);
            return RArguments.getFunction(targetFrame);
        }

        // TODO: this doesn't have to go via caller frames, it can traverse the RCaller chain?
        protected RCaller getCall(VirtualFrame frame, int n) {
            assert access == FrameAccess.READ_ONLY;
            RCaller currentCall = RArguments.getCall(frame);
            int actualFrame = decodeFrameNumber(currentCall, n);
            Frame targetFrame = getNumberedFrame(frame, actualFrame, false);
            return RArguments.getCall(targetFrame);
        }

        /**
         * Handles n > 0 and n < 0 and errors relating to stack depth.
         */
        private int decodeFrameNumber(RCaller currentCall, int n) {
            RCaller call = currentCall;
            if (!ignoreDotInternal) {
                call = call.getPrevious();
            }
            call = RCaller.unwrapPromiseCaller(call, unwrapCallerProfile);
            int depth = call.getDepth();
            if (n > 0) {
                if (n > depth) {
                    throw error(RError.Message.NOT_THAT_MANY_FRAMES);
                }
                return n;
            } else {
                if (-n > depth) {
                    throw error(RError.Message.NOT_THAT_MANY_FRAMES);
                }
                return depth + n;
            }
        }

        // TODO: allow to configure these two fields for testing purposes
        private static final int ITERATE_LEVELS = 3;
        private static final boolean NOTIFY_CALLERS = true;

        @ExplodeLoop
        protected Frame getNumberedFrame(VirtualFrame frame, int actualFrame, boolean materialize) {
            if (currentFrameProfile.profile(RArguments.isRFrame(frame) && RArguments.getDepth(frame) == actualFrame)) {
                return materialize ? frame.materialize() : frame;
            } else if (globalFrameProfile.profile(actualFrame == 0)) {
                // Note: this is optimization and necessity, because in the case of invocation of R
                // function from another "master" language, there will be no actual Truffle frame
                // for global environment
                return REnvironment.globalEnv().getFrame();
            } else {
                MaterializedFrame current = null;
                if (RArguments.getDepth(frame) - actualFrame <= ITERATE_LEVELS) {
                    iterateProfile.enter();
                    current = getCallerFrame(frame);
                    for (int i = 0; i < ITERATE_LEVELS; i++) {
                        if (current == null) {
                            break;
                        }
                        MaterializedFrame result = RArguments.getActualMaterializedFrame(current, actualFrame, false);
                        if (result != null) {
                            return result;
                        }
                        current = getCallerFrame(current);
                    }
                }
                slowPathProfile.enter();
                return getNumberedFrameSlowPath(current == null ? frame.materialize() : current.materialize(), actualFrame, materialize);
            }
        }

        private Frame getNumberedFrameSlowPath(MaterializedFrame frame, int actualFrame, boolean materialize) {
            Frame resultViaCaller = getNumberedCallerFrameSlowPath(frame, actualFrame);
            if (resultViaCaller != null) {
                return materialize ? (MaterializedFrame) resultViaCaller : resultViaCaller;
            }
            Frame result = Utils.getStackFrame(access, actualFrame, NOTIFY_CALLERS);
            return materialize ? (MaterializedFrame) result : result;
        }

        @TruffleBoundary
        private static Frame getNumberedCallerFrameSlowPath(MaterializedFrame frame, int actualFrame) {
            /*
             * Even in the slow path case, we need to walk all frames and call
             * "setNeedsCallerFrame", so that subsequent calls will not need Utils.getStackFrame.
             */
            MaterializedFrame current = frame;
            while (current != null) {
                MaterializedFrame result = RArguments.getActualMaterializedFrame(current, actualFrame, true);
                if (result != null) {
                    return result;
                }
                current = getCallerFrame(current);
            }
            return null;
        }

        private static MaterializedFrame getCallerFrame(Frame current) {
            Object callerFrame = RArguments.getCallerFrame(current);
            if (callerFrame instanceof CallerFrameClosure) {
                if (NOTIFY_CALLERS) {
                    CallerFrameClosure closure = (CallerFrameClosure) callerFrame;
                    closure.setNeedsCallerFrame();
                    return closure.getMaterializedCallerFrame();
                } else {
                    return null;
                }
            }
            assert callerFrame == null || callerFrame instanceof Frame;
            return (MaterializedFrame) callerFrame;
        }
    }

    @RBuiltin(name = "sys.call", kind = INTERNAL, parameterNames = {"which"}, behavior = COMPLEX)
    public abstract static class SysCall extends RBuiltinNode.Arg1 {

        @Child private FrameHelper helper = new FrameHelper(FrameAccess.READ_ONLY);

        static {
            Casts casts = new Casts(SysCall.class);
            casts.arg("which").asIntegerVector().findFirst();
        }

        @Specialization
        protected Object sysCall(VirtualFrame frame, int which) {
            /*
             * sys.call preserves provided names but does not create them, unlike match.call.
             */
            return createCall(helper.getCall(frame, which));
        }

        @TruffleBoundary
        private static Object createCall(RCaller call) {
            assert call == null || !call.isPromise();
            if (call == null || !call.isValidCaller()) {
                return RNull.instance;
            }
            return RContext.getRRuntimeASTAccess().getSyntaxCaller(call);
        }
    }

    /**
     * Generate a call object in which all of the arguments are fully qualified. Unlike
     * {@code sys.call}, named arguments are re-ordered to match the order in the signature. Plus,
     * unlike, {@code sys.call}, the {@code call} argument can be provided by the caller. "..." is a
     * significant complication for two reasons:
     * <ol>
     * <li>If {@code expand.dots} is {@code false} the "..." args are wrapped in a {@code pairlist}
     * </li>
     * <li>One of the args might itself be "..." in which case the values have to be retrieved from
     * the environment associated with caller of the function containing {@code match.call}.</li>
     * </ol>
     * In summary, although the simple cases are indeed simple, there are many possible variants
     * using "..." that make the code a lot more complex that it seems it ought to be.
     */
    @RBuiltin(name = "match.call", kind = INTERNAL, parameterNames = {"definition", "call", "expand.dots", "envir"}, behavior = COMPLEX)
    public abstract static class MatchCall extends RBuiltinNode.Arg4 {

        static {
            Casts casts = new Casts(MatchCall.class);
            casts.arg("definition").mustBe(RFunction.class);
            casts.arg("call").mustBe(RPairList.class);
            casts.arg("expand.dots").asLogicalVector().findFirst();
            casts.arg("envir").mustBe(REnvironment.class, Message.MUST_BE_ENVIRON, "envir");
        }

        @Specialization
        protected RPairList matchCall(RFunction definition, Object callObj, byte expandDotsL, REnvironment env) {
            /*
             * definition==null in the standard (default) case, in which case we get the RFunction
             * from the calling frame
             */
            RPairList call = checkCall(callObj);
            if (expandDotsL == RRuntime.LOGICAL_NA) {
                throw error(RError.Message.INVALID_ARGUMENT, "expand.dots");
            }
            boolean expandDots = RRuntime.fromLogical(expandDotsL);

            return doMatchCall(env.getFrame(), definition, call, expandDots);
        }

        @TruffleBoundary
        private static RPairList doMatchCall(MaterializedFrame cframe, RFunction definition, RPairList call, boolean expandDots) {
            /*
             * We have to ensure that all parameters are named, in the correct order, and deal with
             * "...". This process has a lot in common with MatchArguments, which we use as a
             * starting point
             */
            RCallNode callNode = (RCallNode) call.getSyntaxElement();
            CallArgumentsNode callArgs = callNode.createArguments(null, false, true);
            ArgumentsSignature inputVarArgSignature = callArgs.containsVarArgsSymbol() ? CallArgumentsNode.getVarargsAndNames(cframe).getSignature() : null;
            RNode[] matchedArgNodes = ArgumentMatcher.matchArguments((RRootNode) definition.getRootNode(), callArgs, inputVarArgSignature, null, null, true).getArguments();
            ArgumentsSignature sig = ((HasSignature) definition.getRootNode()).getSignature();
            // expand any varargs
            ArrayList<RSyntaxNode> nodes = new ArrayList<>();
            ArrayList<String> names = new ArrayList<>();

            FrameSlot varArgSlot = cframe.getFrameDescriptor().findFrameSlot(ArgumentsSignature.VARARG_NAME);
            RArgsValuesAndNames varArgParameter = varArgSlot == null ? null : (RArgsValuesAndNames) FrameSlotChangeMonitor.getValue(varArgSlot, cframe);

            for (int i = 0; i < sig.getLength(); i++) {
                RNode arg = matchedArgNodes[i];
                arg = checkForVarArgNode(varArgParameter, arg);

                if (isMissing(arg)) {
                    // nothing to do
                } else if (arg instanceof VarArgsPromiseNode) {
                    VarArgsPromiseNode vararg = (VarArgsPromiseNode) arg;

                    ArgumentsSignature varArgSignature = vararg.getSignature();
                    Closure[] closures = vararg.getClosures();

                    RNode[] varArgNodes = new RNode[varArgSignature.getLength()];
                    for (int i2 = 0; i2 < varArgNodes.length; i2++) {
                        Closure cl = closures[i2];
                        RNode n = (RNode) RASTUtils.unwrap(cl.getExpr());
                        n = checkForVarArgNode(varArgParameter, n);

                        if (n instanceof PromiseNode) {
                            varArgNodes[i2] = ((PromiseNode) n).getPromiseExpr().asRNode();
                        } else {
                            varArgNodes[i2] = n;
                        }
                    }

                    if (expandDots) {
                        for (int i2 = 0; i2 < varArgNodes.length; i2++) {
                            nodes.add(varArgNodes[i2].asRSyntaxNode());
                            names.add(varArgSignature.getName(i2));
                        }
                    } else {
                        // GnuR appears to create a pairlist rather than a list
                        RPairList head = RDataFactory.createPairList();
                        head.setType(SEXPTYPE.LISTSXP);
                        RPairList pl = head;
                        RPairList prev = null;
                        for (int i2 = 0; i2 < varArgNodes.length; i2++) {
                            RNode n = varArgNodes[i2];
                            Object listValue = RASTUtils.createLanguageElement(n.asRSyntaxNode());
                            pl.setCar(listValue);
                            if (varArgSignature.getName(i2) != null) {
                                pl.setTag(RDataFactory.createSymbolInterned(varArgSignature.getName(i2)));
                            }
                            if (prev != null) {
                                prev.setCdr(pl);
                            }
                            prev = pl;
                            if (i2 != varArgSignature.getLength() - 1) {
                                pl = RDataFactory.createPairList();
                                pl.setType(SEXPTYPE.LISTSXP);
                            }
                        }
                        nodes.add(ConstantNode.create(head));
                        names.add(ArgumentsSignature.VARARG_NAME);
                    }
                } else if (arg instanceof PromiseNode) {
                    nodes.add(((PromiseNode) arg).getPromiseExpr());
                    names.add(sig.getName(i));
                } else {
                    nodes.add(arg.asRSyntaxNode());
                    names.add(sig.getName(i));
                }
            }
            sig = ArgumentsSignature.get(names.toArray(new String[names.size()]));
            RSyntaxNode[] newArgs = nodes.toArray(new RSyntaxNode[nodes.size()]);

            RSyntaxNode modCallNode = RASTUtils.createCall(callNode.getFunction(), false, sig, newArgs);
            return RDataFactory.createLanguage(Closure.createLanguageClosure(modCallNode.asRNode()));
        }

        private static RNode checkForVarArgNode(RArgsValuesAndNames varArgParameter, RNode arg) {
            if (arg instanceof VarArgNode) {
                Object argument = varArgParameter.getArgument(((VarArgNode) arg).getIndex());
                if (argument instanceof RPromise) {
                    RNode unwrapped = (RNode) RASTUtils.unwrap(((RPromise) argument).getRep());
                    return unwrapped instanceof ConstantNode ? unwrapped : RContext.getASTBuilder().lookup(RSyntaxNode.LAZY_DEPARSE, createVarArgName((VarArgNode) arg), false).asRNode();
                } else {
                    return ConstantNode.create(argument);
                }
            }
            return arg;
        }

        private static boolean isMissing(RNode node) {
            return ConstantNode.isMissing(node) || isEmptyVarArg(node);
        }

        private static boolean isEmptyVarArg(RNode node) {
            if (node instanceof ConstantNode && ((ConstantNode) node).getValue() instanceof RArgsValuesAndNames) {
                RArgsValuesAndNames rvn = (RArgsValuesAndNames) ((ConstantNode) node).getValue();
                return rvn.getLength() == 0;
            }
            return false;
        }

        private static String createVarArgName(VarArgNode varArgNode) {
            CompilerAsserts.neverPartOfCompilation(); // for string concatenation and interning
            int vn = varArgNode.getIndex() + 1;
            return RSyntaxLookup.getVariadicComponentSymbol(vn);
        }

        @Specialization
        @SuppressWarnings("unused")
        protected RPairList matchCall(Object definition, Object call, Object expandDots, Object envir) {
            throw error(RError.Message.INVALID_OR_UNIMPLEMENTED_ARGUMENTS);
        }

        private RPairList checkCall(Object obj) throws RError {
            Object callObj = obj;
            while (callObj instanceof RExpression) {
                callObj = ((RExpression) callObj).getDataAt(0);
            }
            if ((callObj instanceof RPairList && ((RPairList) callObj).isLanguage())) {
                return (RPairList) callObj;
            }
            throw error(RError.Message.INVALID_ARGUMENT, "call");
        }
    }

    @RBuiltin(name = "sys.nframe", kind = INTERNAL, parameterNames = {}, behavior = COMPLEX)
    public abstract static class SysNFrame extends RBuiltinNode.Arg0 {
        private final UnwrapPromiseCallerProfile unwrapCallerProfile1 = new UnwrapPromiseCallerProfile();
        private final UnwrapPromiseCallerProfile unwrapCallerProfile2 = new UnwrapPromiseCallerProfile();

        @Specialization
        protected int sysNFrame(VirtualFrame frame) {
            RCaller currentCaller = RCaller.unwrapPromiseCaller(RArguments.getCall(frame), unwrapCallerProfile1);
            RCaller call = RCaller.unwrapPromiseCaller(currentCaller.getPrevious(), unwrapCallerProfile2);
            return call.getDepth();
        }
    }

    @RBuiltin(name = "sys.frame", kind = INTERNAL, parameterNames = {"which"}, behavior = COMPLEX)
    public abstract static class SysFrame extends RBuiltinNode.Arg1 {

        @Child private FrameHelper helper;
        @Child private PromiseDeoptimizeFrameNode deoptFrameNode = new PromiseDeoptimizeFrameNode();

        private final ConditionProfile zeroProfile = ConditionProfile.createBinaryProfile();
        private final boolean skipDotInternal;

        public SysFrame() {
            this(false);
        }

        public SysFrame(boolean skipDotInternal) {
            this.skipDotInternal = skipDotInternal;
        }

        public abstract REnvironment executeInt(VirtualFrame frame, int which);

        public static SysFrame create() {
            return SysFrameNodeGen.create();
        }

        public static SysFrame create(boolean skipDotInternal) {
            return SysFrameNodeGen.create(skipDotInternal);
        }

        static {
            Casts casts = new Casts(SysFrame.class);
            casts.arg("which").asIntegerVector().findFirst();
        }

        @Specialization
        protected REnvironment sysFrame(VirtualFrame frame, int which) {
            REnvironment result;
            if (zeroProfile.profile(which == 0)) {
                result = REnvironment.globalEnv();
            } else {
                MaterializedFrame callerFrame = getFrameHelper().getMaterializedFrame(frame, which);
                result = REnvironment.frameToEnvironment(callerFrame);
            }

            // Deoptimize every promise which is now in this frame, as it might leave it's stack
            deoptFrameNode.deoptimizeFrame(RArguments.getArguments(result.getFrame()));
            return result;
        }

        private FrameHelper getFrameHelper() {
            if (helper == null) {
                CompilerDirectives.transferToInterpreterAndInvalidate();
                helper = insert(new FrameHelper(FrameAccess.MATERIALIZE, skipDotInternal));
            }
            return helper;
        }
    }

    @RBuiltin(name = "sys.frames", kind = INTERNAL, parameterNames = {}, behavior = COMPLEX)
    public abstract static class SysFrames extends RBuiltinNode.Arg0 {

        @Child private FrameHelper helper = new FrameHelper(FrameAccess.MATERIALIZE);
        @Child private PromiseDeoptimizeFrameNode deoptFrameNode = new PromiseDeoptimizeFrameNode();

        @Specialization
        protected Object sysFrames(VirtualFrame frame) {
            int depth = RArguments.getDepth(frame);
            if (depth == 1) {
                return RNull.instance;
            } else {
                RPairList result = RDataFactory.createPairList();
                RPairList next = result;
                for (int i = 1; i < depth; i++) {
                    MaterializedFrame mf = (MaterializedFrame) helper.getNumberedFrame(frame, i, true);
                    deoptFrameNode.deoptimizeFrame(RArguments.getArguments(mf));
                    next.setCar(REnvironment.frameToEnvironment(mf));
                    if (i != depth - 1) {
                        RPairList pl = RDataFactory.createPairList();
                        next.setCdr(pl);
                        next = pl;
                    } else {
                        next.setCdr(RNull.instance);
                    }
                }
                return result;
            }
        }
    }

    @RBuiltin(name = "sys.calls", kind = INTERNAL, parameterNames = {}, behavior = COMPLEX)
    public abstract static class SysCalls extends RBuiltinNode.Arg0 {

        private final UnwrapPromiseCallerProfile unwrapCallerProfile1 = new UnwrapPromiseCallerProfile();
        private final UnwrapPromiseCallerProfile unwrapCallerProfile2 = new UnwrapPromiseCallerProfile();

        @Specialization
        protected Object sysCalls(VirtualFrame frame) {
            RCaller prevCall = RCaller.unwrapPromiseCaller(RArguments.getCall(frame), unwrapCallerProfile1);
            RCaller call = RCaller.unwrapPromiseCaller(prevCall.getPrevious(), unwrapCallerProfile2);
            int depth = call.getDepth();
            if (depth == 0) {
                return RNull.instance;
            } else {
                final Object[] result = {RNull.instance};
                RCaller.iterateCallers(call, c -> result[0] = RDataFactory.createPairList(createCall(c), result[0]));
                return result[0];
            }
        }

        @TruffleBoundary
        private static Object createCall(RCaller call) {
            assert call != null;
            return RContext.getRRuntimeASTAccess().getSyntaxCaller(call);
        }
    }

    @RBuiltin(name = "sys.parent", kind = INTERNAL, parameterNames = {"n"}, behavior = COMPLEX)
    public abstract static class SysParent extends RBuiltinNode.Arg1 {

        private final BranchProfile nullCallerProfile = BranchProfile.create();
        private final UnwrapPromiseCallerProfile unwrapCallerProfile1 = new UnwrapPromiseCallerProfile();
        private final UnwrapPromiseCallerProfile unwrapCallerProfile2 = new UnwrapPromiseCallerProfile();
        private final UnwrapPromiseCallerProfile unwrapCallerProfile3 = new UnwrapPromiseCallerProfile();
        private final UnwrapPromiseCallerProfile unwrapResultCallerProfile = new UnwrapPromiseCallerProfile();

        static {
            Casts casts = new Casts(SysParent.class);
            casts.arg("n").asIntegerVector().findFirst();
        }

        @Specialization
        protected int sysParent(VirtualFrame frame, int n) {
            RCaller originalCall = RArguments.getCall(frame);
            // Because this builtin is not Kind == SUBSTITUTE (unlike parent.frame) we have to get
            // the parent frame
            RCaller unwrapped = RCaller.unwrapPromiseCaller(originalCall, unwrapCallerProfile1);
            RCaller call = RCaller.unwrapPromiseCaller(unwrapped.getPrevious(), unwrapCallerProfile2);
            if (n <= 0) {
                // Undocumented feature of GNU-R parent.frame(n) with n <= 0 returns the current
                // frame number
                return call.getDepth();
            }

            // IMPORTANT NOTE: when changing the logic in the loop below, review also SysParents and
            // ParentFrame

            // TODO: finish comment: The difference between sys.parent and sys.parents is that...

            int i = 0;
            RCaller prevCall = call;
            while (i < n) {
                if (!RCaller.isValidCaller(call)) {
                    nullCallerProfile.enter();
                    return 0;
                }
                if (call.isNonFunctionSysParent()) {
                    // For environments that are not function frames, GNU-R uses the depth of the
                    // last function frame encountered.
                    return prevCall.getDepth();
                }
                // If current RCaller is for artificial promise evaluation frame, then follow the
                // RCaller#payload to get the RCaller for the real frame where the promise should be
                // logically evaluated
                call = RCaller.unwrapPromiseCaller(call, unwrapCallerProfile3);
                prevCall = call;
                // Get the real parent frame
                if (call != null) {
                    call = call.getParent();
                }
                i++;
            }
            if (!RCaller.isValidCaller(call)) {
                nullCallerProfile.enter();
                return 0;
            }
            if (call.isNonFunctionSysParent()) {
                // For environments that are not function frames, GNU-R uses the depth of the
                // last function frame encountered.
                return prevCall.getDepth();
            }
            call = RCaller.unwrapPromiseCaller(call, unwrapResultCallerProfile);
            return call.getDepth();
        }
    }

    @RBuiltin(name = "sys.function", kind = INTERNAL, parameterNames = {"which"}, splitCaller = true, alwaysSplit = true, behavior = COMPLEX)
    public abstract static class SysFunction extends RBuiltinNode.Arg1 {

        @Child private FrameHelper helper = new FrameHelper(FrameAccess.READ_ONLY);

        public abstract Object executeObject(VirtualFrame frame, int which);

        static {
            Casts casts = new Casts(SysFunction.class);
            casts.arg("which").asIntegerVector().findFirst();
        }

        @Specialization
        protected Object sysFunction(VirtualFrame frame, int which) {
            // N.B. Despite the spec, n==0 is treated as the current function
            RFunction func = helper.getFunction(frame, which);

            if (func == null) {
                return RNull.instance;
            } else {
                return func;
            }
        }
    }

    @RBuiltin(name = "sys.parents", kind = INTERNAL, parameterNames = {}, behavior = COMPLEX)
    public abstract static class SysParents extends RBuiltinNode.Arg0 {

        private final UnwrapPromiseCallerProfile unwrapCallerProfile1 = new UnwrapPromiseCallerProfile();
        private final UnwrapPromiseCallerProfile unwrapCallerProfile2 = new UnwrapPromiseCallerProfile();
        private final UnwrapPromiseCallerProfile unwrapCallerProfile3 = new UnwrapPromiseCallerProfile();

        @Specialization
        protected RIntVector sysParents(VirtualFrame frame) {
            RCaller originalCall = RArguments.getCall(frame);
            // Because this builtin is not Kind == SUBSTITUTE, we're have to get the parent frame
            RCaller unwrapped = RCaller.unwrapPromiseCaller(originalCall, unwrapCallerProfile1);
            RCaller call = RCaller.unwrapPromiseCaller(unwrapped.getPrevious(), unwrapCallerProfile2);
            if (!RCaller.isValidCaller(call)) {
                // sys.parents called at the top level
                return RDataFactory.createEmptyIntVector();
            }
            if (call.getPrevious() == null) {
                // sys.parents called from a top level function?
                return RDataFactory.createIntVectorFromScalar(0);
            }

            // "* 2" to play it safe, call.getDepth() should be enough?
            int[] result = new int[call.getDepth() * 2];
            int resultIdx = 0;
            while (RCaller.isValidCaller(call)) {
                RCaller parent = call.getParent();
                if (parent.isNonFunctionSysParent()) {
                    // For environments that are not function frames, GNU-R uses the depth of the
                    // last function frame encountered.
                    result[resultIdx++] = call.getDepth();
                } else {
                    parent = RCaller.unwrapPromiseCaller(parent, unwrapCallerProfile3);
                    if (parent == null) {
                        break;
                    }
                    result[resultIdx++] = parent.getDepth();
                }
                // Jump to the next real (i.e. not artificial promise evaluation) frame on the
                // evaluation stack, not to the logical parent (is the sense of parent.frame)
                call = RCaller.unwrapPrevious(call.getPrevious());
            }

            int[] reversed = new int[resultIdx];
            for (int i = 0; i < reversed.length; i++) {
                reversed[i] = result[resultIdx - 1 - i];
            }
            return RDataFactory.createIntVector(reversed, RDataFactory.COMPLETE_VECTOR);
        }
    }

    /**
     * The environment of the caller of the function that called parent.frame.
     */
    @RBuiltin(name = "parent.frame", kind = SUBSTITUTE, parameterNames = {"n"}, behavior = COMPLEX)
    public abstract static class ParentFrame extends RBuiltinNode.Arg1 {

        @Child private FrameHelper helper;
        @Child private GetCallerFrameNode getCaller;

        public abstract REnvironment execute(VirtualFrame frame, int n);

        static {
            Casts casts = new Casts(ParentFrame.class);
            casts.arg("n").asIntegerVector().findFirst();
        }

        @Override
        public Object[] getDefaultParameterValues() {
            return new Object[]{1};
        }

        @Specialization
        protected REnvironment parentFrame(VirtualFrame frame, int nIn,
                        @Cached("create()") BranchProfile nullCallerProfile,
                        @Cached("new()") UnwrapPromiseCallerProfile unwrapOriginalCallerProfile,
                        @Cached("new()") UnwrapPromiseCallerProfile unwrapCallerProfile,
                        @Cached("new()") UnwrapPromiseCallerProfile unwrapResultCallerProfile,
                        @Cached("new()") UnwrapSysParentProfile unwrapSysParentProfile,
                        @Cached("createEqualityProfile()") ValueProfile nProfile,
                        @Cached("createBinaryProfile()") ConditionProfile useGetCallerFrameNode) {
            int n = nProfile.profile(nIn);
            if (n <= 0) {
                throw error(RError.Message.INVALID_VALUE, "n");
            }
            RCaller originalCall = RArguments.getCall(frame);
            RCaller call = RCaller.unwrapPromiseCaller(originalCall, unwrapOriginalCallerProfile);

            int i = 0;
            while (i < n) {
                if (!RCaller.isValidCaller(call)) {
                    nullCallerProfile.enter();
                    return REnvironment.globalEnv();
                }
                // If current RCaller is for artificial promise evaluation frame, then follow the
                // RCaller#payload to get the RCaller for the real frame where the promise should be
                // logically evaluated
                call = RCaller.unwrapPromiseCaller(call, unwrapCallerProfile);
                // Get the real parent frame
                if (call != null) {
                    call = call.getParent();
                }
                i++;
            }
            if (!RCaller.isValidCaller(call)) {
                nullCallerProfile.enter();
                return REnvironment.globalEnv();
            }
            REnvironment sysParent = RCaller.unwrapSysParent(call, unwrapSysParentProfile);
            if (sysParent != null) {
                return sysParent;
            }
            call = RCaller.unwrapPromiseCaller(call, unwrapResultCallerProfile);
            if (useGetCallerFrameNode.profile(originalCall.getDepth() == call.getDepth() + 1)) {
                // If the parent frame is the caller frame, we can use more efficient code:
                return REnvironment.frameToEnvironment(getCallerFrameNode().execute(frame));
            }
            return REnvironment.frameToEnvironment((MaterializedFrame) getFrameHelper().getNumberedFrame(frame, call.getDepth(), true));
        }

        private FrameHelper getFrameHelper() {
            if (helper == null) {
                CompilerDirectives.transferToInterpreterAndInvalidate();
                helper = insert(new FrameHelper(FrameAccess.MATERIALIZE));
            }
            return helper;
        }

        private GetCallerFrameNode getCallerFrameNode() {
            if (getCaller == null) {
                CompilerDirectives.transferToInterpreterAndInvalidate();
                getCaller = insert(new GetCallerFrameNode());
            }
            return getCaller;
        }
    }
}
