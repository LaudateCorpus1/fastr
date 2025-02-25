/*
 * Copyright (c) 2014, 2022, Oracle and/or its affiliates. All rights reserved.
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
package com.oracle.truffle.r.runtime.nodes;

import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.CompilerDirectives.CompilationFinal;
import com.oracle.truffle.api.CompilerDirectives.TruffleBoundary;
import com.oracle.truffle.api.dsl.ImportStatic;
import com.oracle.truffle.api.instrumentation.Tag;
import com.oracle.truffle.api.nodes.LoopNode;
import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.api.nodes.NodeVisitor;
import com.oracle.truffle.api.nodes.RootNode;
import com.oracle.truffle.api.source.SourceSection;
import com.oracle.truffle.r.runtime.DSLConfig;
import com.oracle.truffle.r.runtime.RError;
import com.oracle.truffle.r.runtime.RError.Message;
import com.oracle.truffle.r.runtime.RError.RErrorException;
import com.oracle.truffle.r.runtime.RInternalError;
import com.oracle.truffle.r.runtime.conn.RConnection;
import com.oracle.truffle.r.runtime.context.RContext;
import com.oracle.truffle.r.runtime.context.TruffleRLanguage;
import com.oracle.truffle.r.runtime.data.RArgsValuesAndNames;
import com.oracle.truffle.r.runtime.data.RAttributable;
import com.oracle.truffle.r.runtime.data.RDoubleVector;
import com.oracle.truffle.r.runtime.data.RExpression;
import com.oracle.truffle.r.runtime.data.RFunction;
import com.oracle.truffle.r.runtime.data.RPairList;
import com.oracle.truffle.r.runtime.data.RList;
import com.oracle.truffle.r.runtime.data.RMissing;
import com.oracle.truffle.r.runtime.data.RNull;
import com.oracle.truffle.r.runtime.data.RPromise;
import com.oracle.truffle.r.runtime.data.RSymbol;
import com.oracle.truffle.r.runtime.data.RComplexVector;
import com.oracle.truffle.r.runtime.data.model.RAbstractContainer;
import com.oracle.truffle.r.runtime.data.RIntVector;
import com.oracle.truffle.r.runtime.data.model.RAbstractListVector;
import com.oracle.truffle.r.runtime.data.RLogicalVector;
import com.oracle.truffle.r.runtime.data.RStringVector;
import com.oracle.truffle.r.runtime.data.model.RAbstractAtomicVector;
import com.oracle.truffle.r.runtime.data.model.RAbstractVector;
import com.oracle.truffle.r.runtime.env.REnvironment;

/**
 * This class should be used as the superclass for all instances of nodes in the FastR AST in
 * preference to {@link Node}. Its basic function is to provide access to the {@link RSyntaxNode}
 * after replace transformations have taken place that replace the {@link RSyntaxNode} with a
 * subclass of this class. The mechanism for locating the original {@link RSyntaxNode} is
 * subclass-specific, specified by the {@link #getRSyntaxNode()} method, the implementation of which
 * defaults to a hierarchical search, since many replacement strategies fit that structure.
 *
 * It also provides implementations of {@link #getSourceSection()} and
 * {@link #getEncapsulatingSourceSection()} to enforce the FastR invariant that <b>only</b>nodes
 * that implement {@link RSyntaxNode} should have a {@link SourceSection} attribute (typically
 * subclasses of {@link RSourceSectionNode}).
 *
 * Is it ever acceptable to subclass {@link Node} directly? The answer is yes, with the following
 * caveats:
 * <ul>
 * <li>The code in the subclass does not invoke methods in the {@link RError} class <b>or</b>takes
 * the responsibility to locate the appropriate {@link RBaseNode} to pass (
 * {@link RError#error(Node, RError.Message)} can be used find the base node hierarchically)</li>
 * <li>An instance of the subclass is never used to {@link #replace} an instance of
 * {@link RBaseNode}.</li>
 * </ul>
 */
@ImportStatic(DSLConfig.class)
public abstract class RBaseNode extends Node {

    @CompilationFinal(dimensions = 1) public static final RNode[] EMTPY_RNODE_ARRAY = new RNode[0];
    @CompilationFinal(dimensions = 1) protected static final Object[] EMPTY_OBJECT_ARRAY = new Object[0];

    /**
     * Since {@link RSyntaxNode}s are sometimes used (for convenience) in non-syntax contexts, this
     * function also checks the {@link RSyntaxNode#isSyntax()} method. This method should always be
     * used in preference to {@code instanceof RSyntaxNode}.
     */
    public boolean isRSyntaxNode() {
        return this instanceof RSyntaxNode && ((RSyntaxNode) this).isSyntax();
    }

    /**
     * Convenience method for working with {@link Node}, e.g. in {@link NodeVisitor}.
     */
    public static boolean isRSyntaxNode(Node node) {
        return node instanceof RSyntaxNode && ((RSyntaxNode) node).isSyntax();
    }

    /**
     * Handles the discovery of the {@link RSyntaxNode} that this node is derived from.
     */
    public final RSyntaxNode asRSyntaxNode() {
        if (isRSyntaxNode()) {
            return (RSyntaxNode) this;
        } else {
            return getRSyntaxNode();
        }
    }

    /**
     * See comment on {@link #checkGetRSyntaxNode()}.
     */
    public final RSyntaxNode checkasRSyntaxNode() {
        if (isRSyntaxNode()) {
            return (RSyntaxNode) this;
        } else {
            return checkGetRSyntaxNode();
        }
    }

    /**
     * Many nodes organize themselves in such a way that the relevant {@link RSyntaxNode} can be
     * found by following the parent chain, which is therefore the default implementation.
     */
    protected RSyntaxNode getRSyntaxNode() {
        RSyntaxNode result = checkGetRSyntaxNode();
        RInternalError.guarantee(result != null, "getRSyntaxNode");
        return result;
    }

    /**
     * If every {@link RBaseNode} subclass either overrides {@link #getRSyntaxNode()} or works
     * correctly with the default hierarchical implementation, this method would be redundant.
     * However, currently that is not always the case, so this method can be used by defensive code.
     * It returns {@code null} if the {@link RSyntaxNode} cannot be located.
     */
    @TruffleBoundary
    private RSyntaxNode checkGetRSyntaxNode() {
        Node current = this;
        while (current != null) {
            if (current instanceof RSyntaxNode && ((RSyntaxNode) current).isSyntax()) {
                if (current instanceof RSyntaxCall) {
                    RSyntaxCall call = (RSyntaxCall) current;
                    if (call.getSyntaxArguments().length > 0 && call.getSyntaxLHS() instanceof RSyntaxLookup &&
                                    ((RSyntaxLookup) call.getSyntaxLHS()).getIdentifier().equals(".Internal")) {
                        // unwrap .Internal calls
                        RSyntaxElement firstArg = call.getSyntaxArguments()[0];
                        if (firstArg instanceof RSyntaxNode) {
                            return (RSyntaxNode) firstArg;
                        }
                    }
                }
                return (RSyntaxNode) current;
            }
            current = current.getParent();
        }
        return null;
    }

    @Override
    public SourceSection getSourceSection() {
        /*
         * All the RSyntaxNode implementors (should) override this method, but it may be called on
         * any Node by the Truffle instrumentation machinery, in which case we return null.
         */
        if (this instanceof RSyntaxNode) {
            throw RInternalError.shouldNotReachHere("getSourceSection in RBaseNode");
        } else {
            return null;
        }
    }

    /**
     * If this node happens to be a value of an argument, then the promise for that argument will
     * always be forced. See
     * {@link com.oracle.truffle.r.runtime.builtins.FastPathFactory#forcedEagerPromise(int)}, which
     * allows functions to choose which arguments should be forced eagerly.
     *
     * Note: the eagerness requested by {@code FastPathFactory#forcedEagerPromise} is cancelled in
     * case of events that may cause modifications to the global state, which is a measure to
     * preserve correctness. Forced evaluation requested by this method is never cancelled.
     */
    public boolean forceEagerEvaluation() {
        return false;
    }

    @Override
    /**
     * Returns the {@link SourceSection} for this node, by locating the associated
     * {@link RSyntaxNode}. We do not want any code in FastR calling this method as it is subsumed
     * by {@link #getRSyntaxNode}. However, tools code may call it, so we simply delegate the call.
     */
    public SourceSection getEncapsulatingSourceSection() {
        return getRSyntaxNode().getSourceSection();
    }

    public boolean hasTag(@SuppressWarnings("unused") Class<? extends Tag> tag) {
        // RNode, which is instrumentable, overrides this to actually check if the node has the tag
        return false;
    }

    private static final long WORK_SCALE_FACTOR = 100;

    /**
     * Nodes that can do a significant amount of work in one execution (like arithmetic on vectors)
     * can use this method to report the work to the Truffle system, similar to loop counts.
     *
     * @param amount an approximation of the number of operations
     */
    protected void reportWork(long amount) {
        reportWork(this, amount);
    }

    public static void reportWork(Node base, long amount) {
        if (CompilerDirectives.hasNextTier()) {
            if (amount >= WORK_SCALE_FACTOR) {
                int scaledAmount = (int) (amount / WORK_SCALE_FACTOR);
                if (amount > 0) {
                    LoopNode.reportLoopCount(base, scaledAmount);
                }
            }
        }
    }

    protected final TruffleRLanguage getRLanguage() {
        // Note: we cannot move from the deprecated API since new API unlike the deprecated API
        // checks runtime objects sharing between contexts that happens in our unit tests
        return getRootNode().getLanguage(RContext.getTruffleRLanguage());
    }

    protected static boolean isRAbstractContainer(Object value) {
        return value instanceof RAbstractContainer;
    }

    protected static boolean isRAbstractVector(Object value) {
        return value instanceof RAbstractVector;
    }

    protected static boolean isRAbstractAtomicVector(Object value) {
        return value instanceof RAbstractAtomicVector;
    }

    protected static boolean isRDoubleVector(Object value) {
        return value instanceof RDoubleVector;
    }

    protected static boolean isRIntVector(Object value) {
        return value instanceof RIntVector;
    }

    protected static boolean isRComplexVector(Object value) {
        return value instanceof RComplexVector;
    }

    protected static boolean isRLogicalVector(Object value) {
        return value instanceof RLogicalVector;
    }

    protected static boolean isRStringVector(Object value) {
        return value instanceof RStringVector;
    }

    protected static boolean isRAbstractListVector(Object value) {
        return value instanceof RAbstractListVector;
    }

    protected static boolean isRList(Object value) {
        return value instanceof RList;
    }

    protected static boolean isRPromise(Object value) {
        return value instanceof RPromise;
    }

    protected static boolean isRExpression(Object value) {
        return value instanceof RExpression;
    }

    protected static boolean isRFunction(Object value) {
        return value instanceof RFunction;
    }

    protected static boolean isREnvironment(Object value) {
        return value instanceof REnvironment;
    }

    protected static boolean isRConnection(Object value) {
        return value instanceof RConnection;
    }

    protected static boolean isRPairList(Object value) {
        return value instanceof RPairList;
    }

    protected static boolean isRSymbol(Object value) {
        return value instanceof RSymbol;
    }

    protected static boolean isRArgsValuesAndNames(Object value) {
        return value instanceof RArgsValuesAndNames;
    }

    protected static boolean isRMissing(Object value) {
        return value == RMissing.instance;
    }

    protected static boolean isRNull(Object value) {
        return value == RNull.instance;
    }

    protected static boolean isRAttributable(Object value) {
        return value instanceof RAttributable;
    }

    /*
     * Error handling-related functionality.
     */

    /**
     * Find the proper context for errors or warnings produced by this node. The default behavior is
     * to call getErrorContext() of the parent RBaseNode recursively.
     *
     * Certain types of nodes, e.g., builtins, will supply the correct context for themselves.
     */
    public RBaseNode getErrorContext() {
        Node node = getParent();
        while (node != null) {
            if (node instanceof RBaseNode) {
                return ((RBaseNode) node).getErrorContext();
            }
            if (node instanceof RootNode) {
                return RError.NO_CALLER;
            }
            node = node.getParent();
        }
        throw RInternalError.shouldNotReachHere("trying to get error context of node without parent");
    }

    /**
     * @see #error(Message)
     */
    public final RError error(RErrorException exception) {
        CompilerDirectives.transferToInterpreter();
        throw RError.error(getErrorContext(), exception);
    }

    /**
     * Raises the given error with the error context determined by {@link #getErrorContext()}. This
     * function is always considered to be a slow-path operation and will transferToInterpreter.
     *
     * @return an RError so that this function can be called as {@code throw error(...);}.
     */
    public final RError error(RError.Message message) {
        CompilerDirectives.transferToInterpreter();
        throw RError.error(getErrorContext(), message);
    }

    /**
     * @see #error(Message)
     */
    public final RError error(RError.Message message, Object arg) {
        CompilerDirectives.transferToInterpreter();
        throw RError.error(getErrorContext(), message, arg);
    }

    /**
     * @see #error(Message)
     */
    public final RError error(RError.Message message, Object arg1, Object arg2) {
        CompilerDirectives.transferToInterpreter();
        throw RError.error(getErrorContext(), message, arg1, arg2);
    }

    /**
     * @see #error(Message)
     */
    public final RError error(RError.Message message, Object arg1, Object arg2, Object arg3) {
        CompilerDirectives.transferToInterpreter();
        throw RError.error(getErrorContext(), message, arg1, arg2, arg3);
    }

    /**
     * @see #error(Message)
     */
    public final RError error(RError.Message message, Object arg1, Object arg2, Object arg3, Object arg4) {
        CompilerDirectives.transferToInterpreter();
        throw RError.error(getErrorContext(), message, arg1, arg2, arg3, arg4);
    }

    /**
     * @see #error(Message)
     */
    public final RError error(RError.Message message, Object arg1, Object arg2, Object arg3, Object arg4, Object arg5) {
        CompilerDirectives.transferToInterpreter();
        throw RError.error(getErrorContext(), message, arg1, arg2, arg3, arg4, arg5);
    }

    /**
     * @see #error(Message)
     */
    public final RError error(RError.Message message, Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6) {
        CompilerDirectives.transferToInterpreter();
        throw RError.error(getErrorContext(), message, arg1, arg2, arg3, arg4, arg5, arg6);
    }
}
