/*
 * Copyright (c) 2016 Foxconn Corporation and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.ocpjava.protocol.impl.core;

import org.junit.Test;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.core.IsNull.nullValue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Verifies the basic functionality of the {@link OCPXmlDecoder}.
 */
public class OCPXmlDecoderTest {  
    private static final Logger LOG = LoggerFactory.getLogger(OCPXmlDecoderTest.class);
    private static final XmlDocumentEnd XML_DOCUMENT_END = XmlDocumentEnd.INSTANCE;

    /**
     * This test feeds basic XML and verifies the resulting messages
     */
    @Test
    public void shouldDecodeRequestWithSimpleXml() {
        XmlDocumentStart temp = new XmlDocumentStart("UTF-8", "1.0", false, null);
        LOG.debug("temp = {}", temp);
        assertThat(temp, instanceOf(XmlDocumentStart.class));
        assertThat(((XmlDocumentStart) temp).version(), is("1.0"));
        assertThat(((XmlDocumentStart) temp).encoding(), is("UTF-8"));
        assertThat(((XmlDocumentStart) temp).standalone(), is(false));
        assertThat(((XmlDocumentStart) temp).encodingScheme(), is(nullValue()));
        assertThat(((XmlDocumentStart) temp).equals(temp), is(true));

        //<employee xmlns:nettya=\"http://netty.io/netty/a\">
        XmlElementStart temp2 = new XmlElementStart("employee", "", "");
        XmlNamespace xmlns = new XmlNamespace("nettya", "http://netty.io/netty/a");
        temp2.namespaces().add(xmlns);
        LOG.debug("temp2 = {}", temp2);
        assertThat(temp2, instanceOf(XmlElementStart.class));
        assertThat(((XmlElementStart) temp2).name(), is("employee"));
        assertThat(((XmlElementStart) temp2).prefix(), is(""));
        assertThat(((XmlElementStart) temp2).namespace(), is(""));
        assertThat(((XmlElementStart) temp2).attributes().size(), is(0));
        assertThat(((XmlElementStart) temp2).namespaces().size(), is(1));
        assertThat(((XmlElementStart) temp2).namespaces().get(0).prefix(), is("nettya"));
        assertThat(((XmlElementStart) temp2).namespaces().get(0).uri(), is("http://netty.io/netty/a"));
        assertThat(((XmlElementStart) temp2).equals(temp2), is(true));

        XmlElementStart temp3 = new XmlElementStart("id", "http://netty.io/netty/a", "nettya");
        LOG.debug("temp3 = {}", temp3);
        assertThat(temp3, instanceOf(XmlElementStart.class));
        assertThat(((XmlElementStart) temp3).name(), is("id"));
        assertThat(((XmlElementStart) temp3).prefix(), is("nettya"));
        assertThat(((XmlElementStart) temp3).namespace(), is("http://netty.io/netty/a"));
        assertThat(((XmlElementStart) temp3).attributes().size(), is(0));
        assertThat(((XmlElementStart) temp3).namespaces().size(), is(0));

        XmlCharacters temp4 = new XmlCharacters("1");
        LOG.debug("temp4 = {}", temp4);
        assertThat(temp4, instanceOf(XmlCharacters.class));
        assertThat(((XmlCharacters) temp4).data(), is("1"));

        XmlElementEnd temp5 = new XmlElementEnd("id", "http://netty.io/netty/a", "nettya");
        LOG.debug("temp5 = {}", temp5);
        assertThat(temp5, instanceOf(XmlElementEnd.class));
        assertThat(((XmlElementEnd) temp5).name(), is("id"));
        assertThat(((XmlElementEnd) temp5).prefix(), is("nettya"));
        assertThat(((XmlElementEnd) temp5).namespace(), is("http://netty.io/netty/a"));
        assertThat(((XmlElementEnd) temp5).equals(temp5), is(true));

        XmlCharacters temp6 = new XmlCharacters("\n");
        LOG.debug("temp6 = {}", temp6);
        assertThat(temp6, instanceOf(XmlCharacters.class));
        assertThat(((XmlCharacters) temp6).data(), is("\n"));
        assertThat(((XmlCharacters) temp6).equals(temp6), is(true));
        
        //<name type=\"given\">
        XmlElementStart temp7 = new XmlElementStart("name", "", "");
        XmlAttribute typ= new XmlAttribute("", "type", "", "", "given");
        temp7.attributes().add(typ);
        LOG.debug("temp7 = {}", temp7);
        assertThat(temp7, instanceOf(XmlElementStart.class));
        assertThat(((XmlElementStart) temp7).name(), is("name"));
        assertThat(((XmlElementStart) temp7).prefix(), is(""));
        assertThat(((XmlElementStart) temp7).namespace(), is(""));
        assertThat(((XmlElementStart) temp7).attributes().size(), is(1));
        assertThat(((XmlElementStart) temp7).attributes().get(0).name(), is("type"));
        assertThat(((XmlElementStart) temp7).attributes().get(0).value(), is("given"));
        assertThat(((XmlElementStart) temp7).attributes().get(0).prefix(), is(""));
        assertThat(((XmlElementStart) temp7).attributes().get(0).namespace(), is(""));
        assertThat(((XmlElementStart) temp7).namespaces().size(), is(0));

        //<nettyb:salary xmlns:nettyb=\"http://netty.io/netty/b\" nettyb:period=\"weekly\">
        XmlElementStart temp8 = new XmlElementStart("salary", "http://netty.io/netty/b", "nettyb");
        XmlAttribute attr= new XmlAttribute("", "period", "nettyb", "http://netty.io/netty/b", "weekly");
        temp8.attributes().add(attr);
        XmlNamespace nSpace = new XmlNamespace("nettya", "http://netty.io/netty/a");
        temp8.namespaces().add(nSpace);
        LOG.debug("temp8 = {}", temp8);
        assertThat(temp8, instanceOf(XmlElementStart.class));
        assertThat(((XmlElementStart) temp8).name(), is("salary"));
        assertThat(((XmlElementStart) temp8).prefix(), is("nettyb"));
        assertThat(((XmlElementStart) temp8).namespace(), is("http://netty.io/netty/b"));
        assertThat(((XmlElementStart) temp8).attributes().size(), is(1));
        assertThat(((XmlElementStart) temp8).attributes().get(0).name(), is("period"));
        assertThat(((XmlElementStart) temp8).attributes().get(0).value(), is("weekly"));
        assertThat(((XmlElementStart) temp8).attributes().get(0).prefix(), is("nettyb"));
        assertThat(((XmlElementStart) temp8).attributes().get(0).namespace(), is("http://netty.io/netty/b"));
        assertThat(((XmlElementStart) temp8).namespaces().size(), is(1));
        assertThat(((XmlElementStart) temp8).namespaces().get(0).prefix(), is("nettya"));
        assertThat(((XmlElementStart) temp8).namespaces().get(0).uri(), is("http://netty.io/netty/a"));
    }
}
