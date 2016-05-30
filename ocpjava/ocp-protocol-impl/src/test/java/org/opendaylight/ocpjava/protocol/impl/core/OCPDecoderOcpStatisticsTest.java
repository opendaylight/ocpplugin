/*
 * Copyright (c) 2016 Foxconn Corporation and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.ocpjava.protocol.impl.core;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyShort;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.when;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.util.ArrayList;
import java.util.List;


import org.opendaylight.ocpjava.protocol.impl.core.XmlElementStart;
import org.opendaylight.ocpjava.protocol.impl.core.XmlElementEnd;
import org.opendaylight.ocpjava.protocol.impl.core.XmlCharacters;
import org.opendaylight.ocpjava.protocol.impl.core.XmlAttribute;
import org.opendaylight.ocpjava.protocol.impl.core.XmlDocumentStart;
import org.opendaylight.ocpjava.protocol.impl.core.XmlDocumentEnd;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.opendaylight.ocpjava.protocol.impl.deserialization.DeserializationFactory;
import org.opendaylight.ocpjava.statistics.CounterEventTypes;
import org.opendaylight.ocpjava.statistics.OcpStatisticsCounters;
import org.opendaylight.ocpjava.util.ByteBufUtils;
import org.opendaylight.yangtools.yang.binding.DataObject;

/**
 * Test to count decoder events (counters US_DECODE_SUCCESS, US_DECODE_FAIL and
 * US_RECEIVED_IN_OCPJAVA have to be enabled)
 *
 * @author Marko Lai <marko.ch.lai@foxconn.com>
 *
 */
public class OCPDecoderOcpStatisticsTest {

    @Mock ChannelHandlerContext mockChHndlrCtx;
    @Mock DeserializationFactory mockDeserializationFactory;
    @Mock DataObject mockDataObject;

    private OCPDecoder ocpDecoder;
    private List<Object> writeObj;
    private DefaultMessageWrapper inMsg;
    private List<Object> outList;
    private OcpStatisticsCounters statCounters;

    private static final XmlDocumentEnd XML_DOCUMENT_END = XmlDocumentEnd.INSTANCE;

    /**
     * Sets up test environment Start counting and reset counters before each
     * test
     */
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        ocpDecoder = new OCPDecoder();
        ocpDecoder.setDeserializationFactory(mockDeserializationFactory);
        outList = new ArrayList<>();
        statCounters = OcpStatisticsCounters.getInstance();
        statCounters.startCounting(false, 0);
    }

    /**
     * Stop counting after each test
     */
    @After
    public void tierDown() {
        statCounters.stopCounting();
    }

    /**
     * Test decode success counter
     */
    @Test
    public void testDecodeSuccesfullCounter() {
        if (!statCounters.isCounterEnabled(CounterEventTypes.US_DECODE_SUCCESS)) {
            Assert.fail("Counter " + CounterEventTypes.US_DECODE_SUCCESS + " is not enable");
        }
        if (!statCounters.isCounterEnabled(CounterEventTypes.US_DECODE_FAIL)) {
            Assert.fail("Counter " + CounterEventTypes.US_DECODE_FAIL + " is not enable");
        }
        if (!statCounters
                .isCounterEnabled(CounterEventTypes.US_RECEIVED_IN_OCPJAVA)) {
            Assert.fail("Counter " + CounterEventTypes.US_RECEIVED_IN_OCPJAVA + " is not enable");
        }
        int count = 4;
        when(mockDeserializationFactory.deserialize( anyShort(), anyInt(), any(List.class))).thenReturn(mockDataObject);
        try {
            for (int i = 0; i < count; i++) {
                List<Object >msg = getTestSampleMessage();
                inMsg = new DefaultMessageWrapper( (short)1, 0, msg );
                ocpDecoder.decode(mockChHndlrCtx, inMsg, outList);
            }
        } catch (Exception e) {
            Assert.fail();
        }
        Assert.assertEquals("Wrong - bad counter value for OCPEncoder encode succesfully ",
                count,statCounters.getCounter(CounterEventTypes.US_DECODE_SUCCESS).getCounterValue());
        Assert.assertEquals(
                "Wrong - different between RECEIVED_IN_OCPJAVA and (US_DECODE_SUCCESS + US_DECODE_FAIL)",
                statCounters.getCounter(CounterEventTypes.US_RECEIVED_IN_OCPJAVA).getCounterValue(),
                statCounters.getCounter(CounterEventTypes.US_DECODE_SUCCESS).getCounterValue()
                + statCounters.getCounter(CounterEventTypes.US_DECODE_FAIL).getCounterValue());
    }

    /**
     * Test fail decode counter
     */
    @Test
    public void testDecodeFailCounter() {
        if (!statCounters.isCounterEnabled(CounterEventTypes.US_DECODE_SUCCESS)) {
            Assert.fail("Counter " + CounterEventTypes.US_DECODE_SUCCESS + " is not enable");
        }
        if (!statCounters.isCounterEnabled(CounterEventTypes.US_DECODE_FAIL)) {
            Assert.fail("Counter " + CounterEventTypes.US_DECODE_FAIL + " is not enable");
        }
        if (!statCounters.isCounterEnabled(CounterEventTypes.US_RECEIVED_IN_OCPJAVA)) {
            Assert.fail("Counter " + CounterEventTypes.US_RECEIVED_IN_OCPJAVA + " is not enable");
        }
        int count = 2;
        when( mockDeserializationFactory.deserialize( anyShort(), anyInt(), any(List.class))).thenThrow(new IllegalArgumentException());
        try {
            for (int i = 0; i < count; i++) {
                List<Object >msg = getTestSampleMessage();
                inMsg = new DefaultMessageWrapper( (short)1, 0, msg );
                ocpDecoder.decode(mockChHndlrCtx, inMsg, outList);
            }
        } catch (Exception e) {
            Assert.fail();
        }
        Assert.assertEquals(
                "Wrong - bad counter value for OCPEncoder encode succesfully ",
                count, statCounters.getCounter(CounterEventTypes.US_DECODE_FAIL).getCounterValue());
        Assert.assertEquals(
                "Wrong - different between RECEIVED_IN_OCPJAVA and (US_DECODE_SUCCESS + US_DECODE_FAIL)",
                statCounters.getCounter(CounterEventTypes.US_RECEIVED_IN_OCPJAVA).getCounterValue(),
                statCounters.getCounter(CounterEventTypes.US_DECODE_SUCCESS).getCounterValue()
                + statCounters.getCounter(CounterEventTypes.US_DECODE_FAIL).getCounterValue());
    }

    public List<Object> getTestSampleMessage(){
        writeObj = new ArrayList<>();
        writeObj.add(new XmlDocumentStart("UTF-8", "1.0", false, ""));
        writeObj.add(XML_DOCUMENT_END);
        XmlElementStart msg = new XmlElementStart("msg", "", "");
        XmlAttribute xmlns = new XmlAttribute("", "xmlns", "", "http://uri.etsi.org/ori/002-2/v4.1.1", "");
        msg.attributes().add(xmlns);
        writeObj.add(msg);
        writeObj.add(new XmlElementStart("header", "", ""));
        writeObj.add(new XmlElementStart("msgType", "", ""));
            writeObj.add(new XmlCharacters("IND"));
            writeObj.add(new XmlElementEnd("msgType", "", ""));
            writeObj.add(new XmlElementStart("msgUID", "", ""));
                writeObj.add(new XmlCharacters("0"));
            writeObj.add(new XmlElementEnd("msgUID", "", ""));
        writeObj.add(new XmlElementEnd("header", "", ""));
        writeObj.add(new XmlElementStart("body", "", ""));
            writeObj.add(new XmlElementStart("helloInd", "", ""));
                writeObj.add(new XmlElementStart("version", "", ""));
                    writeObj.add(new XmlCharacters("4.1.1"));
                writeObj.add(new XmlElementEnd("version", "", ""));
                writeObj.add(new XmlElementStart("versionId", "", ""));
                    writeObj.add(new XmlCharacters("MTI"));
                writeObj.add(new XmlElementEnd("versionId", "", ""));
                writeObj.add(new XmlElementStart("serialNumber", "", ""));
                    writeObj.add(new XmlCharacters("123"));
                writeObj.add(new XmlElementEnd("serialNumber", "", ""));
            writeObj.add(new XmlElementEnd("helloInd", "", ""));
        writeObj.add(new XmlElementEnd("body", "", ""));
        writeObj.add(new XmlElementEnd("msg", "", ""));
        return writeObj;
    }
}
