/*
 * Copyright (c) 2016 Foxconn Corporation and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.ocpjava.protocol.impl.core.connection;

import org.junit.Assert;
import org.junit.Test;
import org.opendaylight.ocpjava.protocol.impl.core.connection.MessageListenerWrapper;
import org.opendaylight.ocpjava.protocol.impl.core.connection.SimpleRpcListener;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.extension.rev150811.HelloInput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.extension.rev150811.HelloInputBuilder;

/**
 * @author Marko Lai <marko.ch.lai@foxconn.com>
 *
 */
public class MessageListenerWrapperTest {

    /**
     * Test MessageListenerWrapper creation
     */

    @Test
    public void test() {
    	HelloInputBuilder builder = new HelloInputBuilder();
    	HelloInput hello = builder.build();
        SimpleRpcListener listener = new SimpleRpcListener(hello, "Hello");
        MessageListenerWrapper wrapper = new MessageListenerWrapper(hello, listener);
        Assert.assertEquals("Wrong message", hello, wrapper.getMsg());
        Assert.assertEquals("Wrong listener", listener, wrapper.getListener());
    }

}
