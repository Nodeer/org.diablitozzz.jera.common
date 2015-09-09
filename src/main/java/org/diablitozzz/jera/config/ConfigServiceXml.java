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
package org.diablitozzz.jera.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.diablitozzz.jera.util.UtilIO;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class ConfigServiceXml {

    public static final String ATTR_VALUE = "value";
    public static final String ATTR_KEY = "key";
    public static final String NODE_ITEM = "item";

    public Document configNodeToDomDocument(ConfigNode configNode) throws ParserConfigurationException
    {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = factory.newDocumentBuilder();

        Document document = docBuilder.newDocument();
        document.setXmlStandalone(true);

        this.configNodeToDomElement(configNode, document, null);

        return document;
    }

    public void configNodeToDomElement(ConfigNode configNode, Document document, Element root)
    {
        //make node
        Element node = document.createElement(ConfigServiceXml.NODE_ITEM);
        node.setAttribute(ConfigServiceXml.ATTR_KEY, configNode.getName());

        if (!configNode.isValueNull()) {
            node.setAttribute(ConfigServiceXml.ATTR_VALUE, configNode.getValueAsString());
        }

        if (root == null) {
            document.appendChild(node);
        }
        else {
            root.appendChild(node);
        }

        //make childs
        for (ConfigNode configChild : configNode.getChilds()) {
            this.configNodeToDomElement(configChild, document, node);
        }
    }

    public ConfigNode domDocumentToConfigNode(Document document)
    {
        return this.domElementToConfigNode(document.getDocumentElement());
    }

    public ConfigNode domElementToConfigNode(Element element)
    {
        //make node
        ConfigNode node = new ConfigNode(element.getAttributeNode(ConfigServiceXml.ATTR_KEY).getTextContent());

        if (element.hasAttribute(ConfigServiceXml.ATTR_VALUE)) {
            node.setValue(element.getAttributeNode(ConfigServiceXml.ATTR_VALUE).getTextContent());
        }

        //make childs
        if (element.hasChildNodes()) {

            NodeList elementChilds = element.getChildNodes();
            int length = elementChilds.getLength();
            for (int i = 0; i < length; i++) {

                Node elementChild = elementChilds.item(i);
                if (!(elementChild instanceof Element)) {
                    continue;
                }
                ConfigNode child = this.domElementToConfigNode((Element) elementChild);
                node.addChild(child);
            }
        }

        return node;
    }

    public ConfigNode loadFromFile(File file) throws FileNotFoundException, ParserConfigurationException, SAXException, IOException
    {
        FileInputStream inputStream = new FileInputStream(file);
        return this.loadFromStream(inputStream);
    }

    public ConfigNode loadFromFile(String fileName) throws FileNotFoundException, ParserConfigurationException, SAXException, IOException
    {
        File file = new File(fileName);
        return this.loadFromFile(file);
    }

    public ConfigNode loadFromStream(InputStream inStream) throws ParserConfigurationException, SAXException, IOException
    {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        documentBuilderFactory.setIgnoringComments(true);
        documentBuilderFactory.setIgnoringElementContentWhitespace(true);

        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document document = documentBuilder.parse(inStream);

        UtilIO.closeForce(inStream);

        return this.domDocumentToConfigNode(document);
    }

    public void saveToFile(ConfigNode configNode, File file, Charset charset) throws TransformerException, ParserConfigurationException, IOException
    {
        FileOutputStream outStream = new FileOutputStream(file);
        this.saveToStream(configNode, outStream, charset);
    }

    public void saveToFile(ConfigNode configNode, String fileName, String charsetName) throws TransformerException,
            ParserConfigurationException, IOException
    {
        File file = new File(fileName);
        Charset charset = Charset.forName(charsetName);
        this.saveToFile(configNode, file, charset);
    }

    public void saveToStream(ConfigNode configNode, OutputStream outStream, Charset charset) throws TransformerException, ParserConfigurationException,
            IOException
    {
        //make xml
        Document document = this.configNodeToDomDocument(configNode);

        //save to xml
        TransformerFactory factory = TransformerFactory.newInstance();
        factory.setAttribute("indent-number", 4);
        Transformer transformer = factory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");

        //fix bug for indent fo apache xalan
        try {
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
        } catch (Exception e) {

        }

        transformer.setOutputProperty(OutputKeys.ENCODING, charset.displayName());

        StreamResult streamResult = new StreamResult(outStream);
        DOMSource domSource = new DOMSource(document);
        transformer.transform(domSource, streamResult);

        outStream.close();
    }

}
