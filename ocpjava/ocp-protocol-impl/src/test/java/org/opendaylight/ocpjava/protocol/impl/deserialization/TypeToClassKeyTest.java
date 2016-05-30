/*
 * Copyright (c) 2014 Pantheon Technologies s.r.o. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.ocpjava.protocol.impl.deserialization;

import org.junit.Assert;
import org.junit.Test;
import org.opendaylight.ocpjava.protocol.api.util.EncodeConstants;
import org.opendaylight.ocpjava.protocol.impl.util.TypeToClassKey;
/**
 *
 * @author madamjak
 *
 */
public class TypeToClassKeyTest {

    /**
     * Test equals
     */
    @Test
    public void test(){
        final short ver = EncodeConstants.OCP_VERSION_ID;
        final int type1 = 1;
        TypeToClassKey typeToClsKey = new TypeToClassKey(ver,type1);
        Assert.assertTrue("Wrong - equals to same object", typeToClsKey.equals(typeToClsKey));
        Assert.assertFalse("Wrong - equals to null", typeToClsKey.equals(null));
        Assert.assertFalse("Wrong - equals to different class", typeToClsKey.equals(new Object()));
    }
}
