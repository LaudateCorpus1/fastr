/*
 * Copyright (c) 2013, 2016, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */
package com.oracle.truffle.r.nodes.access;

import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.NodeCost;
import com.oracle.truffle.api.nodes.NodeInfo;
import com.oracle.truffle.api.source.SourceSection;
import com.oracle.truffle.r.nodes.RASTUtils;
import com.oracle.truffle.r.runtime.ArgumentsSignature;
import com.oracle.truffle.r.runtime.RSerialize;
import com.oracle.truffle.r.runtime.context.RContext;
import com.oracle.truffle.r.runtime.env.REnvironment;
import com.oracle.truffle.r.runtime.nodes.RNode;
import com.oracle.truffle.r.runtime.nodes.RSyntaxCall;
import com.oracle.truffle.r.runtime.nodes.RSyntaxElement;
import com.oracle.truffle.r.runtime.nodes.RSyntaxLookup;
import com.oracle.truffle.r.runtime.nodes.RSyntaxNode;

/**
 * The "syntax" variant corresponding to {@code x <- y} in the source.
 */
@NodeInfo(cost = NodeCost.NONE)
public class WriteCurrentVariableNode extends WriteVariableNodeSyntaxHelper implements RSyntaxNode, RSyntaxCall {
    @Child private WriteLocalFrameVariableNode writeLocalFrameVariableNode;

    protected WriteCurrentVariableNode(SourceSection src) {
        super(src);
    }

    static WriteCurrentVariableNode create(SourceSection src, String name, RNode rhs) {
        WriteCurrentVariableNode result = new WriteCurrentVariableNode(src);
        result.writeLocalFrameVariableNode = result.insert(WriteLocalFrameVariableNode.create(name, rhs, Mode.REGULAR));
        return result;
    }

    @Override
    public Object getName() {
        return writeLocalFrameVariableNode.getName();
    }

    @Override
    public RNode getRhs() {
        return writeLocalFrameVariableNode.getRhs();
    }

    @Override
    public Object execute(VirtualFrame frame) {
        Object result = writeLocalFrameVariableNode.execute(frame);
        RContext.getInstance().setVisible(false);
        return result;
    }

    @Override
    public void execute(VirtualFrame frame, Object value) {
        writeLocalFrameVariableNode.execute(frame, value);
    }

    @Override
    public void serializeImpl(RSerialize.State state) {
        serializeHelper(state, "<-");
    }

    @Override
    public RSyntaxNode substituteImpl(REnvironment env) {
        String name = getName().toString();
        RSyntaxNode nameSub = RASTUtils.substituteName(name, env);
        if (nameSub != null) {
            name = RASTUtils.expectName(nameSub.asRNode());
        }
        RNode rhsSub = null;
        if (getRhs() != null) {
            rhsSub = getRhs().substitute(env).asRNode();
        }
        return create(RSyntaxNode.EAGER_DEPARSE, name, rhsSub);
    }

    @Override
    public RSyntaxElement getSyntaxLHS() {
        return RSyntaxLookup.createDummyLookup(null, "<-", true);
    }

    @Override
    public RSyntaxElement[] getSyntaxArguments() {
        return new RSyntaxElement[]{RSyntaxLookup.createDummyLookup(getSourceSection(), (String) getName(), false), getRhs().asRSyntaxNode()};
    }

    @Override
    public ArgumentsSignature getSyntaxSignature() {
        return ArgumentsSignature.empty(2);
    }
}
