/*
 * Copyright (c) 2012, diablitozzz.org All rights reserved. Redistribution
 * and use in source and binary forms, with or without modification, are
 * permitted provided that the following conditions are met: * Redistributions
 * of source code must retain the above copyright notice, this list of
 * conditions and the following disclaimer. * Redistributions in binary form
 * must reproduce the above copyright notice, this list of conditions and the
 * following disclaimer in the documentation and/or other materials provided
 * with the distribution. * Neither the name of diablitozzz.org nor the names of
 * its contributors may be used to endorse or promote products derived from this
 * software without specific prior written permission. THIS SOFTWARE IS PROVIDED
 * BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO
 * EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS;
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.diablitozzz.jera.xml;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.diablitozzz.jera.util.UtilIO;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

public class XmlUtils {

    public static Document loadDocument(InputStream inputStream) throws SAXException, IOException, ParserConfigurationException
    {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setIgnoringElementContentWhitespace(true);
        factory.setIgnoringComments(true);
        factory.setValidating(false);
        factory.setNamespaceAware(true);
        DocumentBuilder docBuilder = factory.newDocumentBuilder();
        Document document = docBuilder.parse(inputStream);
        document.setXmlStandalone(true);
        UtilIO.closeForce(inputStream);
        return document;
    }

    public static String nodeToString(Node node) throws TransformerException
    {
        return XmlUtils.nodeToString(node, true);
    }

    public static String nodeToString(Node node, boolean indent) throws TransformerException
    {
        Source source = new DOMSource(node);

        StringWriter stringWriter = new StringWriter();
        Result result = new StreamResult(stringWriter);

        TransformerFactory factory = TransformerFactory.newInstance();
        if (indent) {
            factory.setAttribute("indent-number", new Integer(2));
        }
        Transformer transformer = factory.newTransformer();

        if (indent) {
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            // transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount",
            // "2");            
        }

        transformer.transform(source, result);

        return stringWriter.getBuffer().toString();
    }

    public static Document xslTransform(Document xsl, Document xml) throws TransformerException
    {
        Source xslSource = new DOMSource(xsl);
        Source xmlSource = new DOMSource(xml);
        DOMResult domResult = new DOMResult();
        TransformerFactory factory = TransformerFactory.newInstance();
        Transformer transformer = factory.newTransformer(xslSource);
        transformer.transform(xmlSource, domResult);
        return (Document) domResult.getNode();
    }
}
