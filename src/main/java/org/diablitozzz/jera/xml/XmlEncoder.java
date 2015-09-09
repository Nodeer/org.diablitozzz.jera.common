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

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.diablitozzz.jera.data.MixedMap;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class XmlEncoder {

    public static final String NODE_ITEM = "item";
    public static final String NODE_ROOT = "root";

    private static final Pattern PATTERN_IS_NUMERIC = Pattern.compile("[0-9]*");

    public Document make(Object data) throws ParserConfigurationException
    {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = factory.newDocumentBuilder();

        Document document = docBuilder.newDocument();
        document.setXmlStandalone(true);

        // создаём root элемент
        Element rootNode = document.createElement(XmlEncoder.NODE_ROOT);
        document.appendChild(rootNode);

        // заполняем значениями
        this.makeValue(rootNode, data, document);
        return document;

    }

    @SuppressWarnings("unchecked")
    public void makeValue(Element el, Object value, Document document)
    {
        if (value instanceof MixedMap)
        {
            MixedMap map = (MixedMap) value;
            if (map.isArray()) {
                this.makeValue(el, map.toArray(), document);
            }
            else {
                this.makeValue(el, map.toMap(), document);
            }
        }
        else if (value instanceof Map)
        {
            Map<String, Object> vals = (Map<String, Object>) value;
            for (String key : vals.keySet()) {
                this.makeValueNode(key, vals.get(key), document, el);
            }
        }
        else if (value instanceof Iterable)
        {
            Iterable<Object> vals = (Iterable<Object>) value;
            for (Object v : vals) {
                this.makeValueNode(XmlEncoder.NODE_ITEM, v, document, el);
            }
        }
        else if (value instanceof Object[])
        {
            Object[] vals = (Object[]) value;
            for (Object v : vals) {
                this.makeValueNode(XmlEncoder.NODE_ITEM, v, document, el);
            }
        }
        else if (value instanceof int[])
        {
            int[] vals = (int[]) value;
            for (Object v : vals) {
                this.makeValueNode(XmlEncoder.NODE_ITEM, v, document, el);
            }
        }
        else if (value instanceof short[])
        {
            short[] vals = (short[]) value;
            for (Object v : vals) {
                this.makeValueNode(XmlEncoder.NODE_ITEM, v, document, el);
            }
        }
        else if (value instanceof long[])
        {
            long[] vals = (long[]) value;
            for (Object v : vals) {
                this.makeValueNode(XmlEncoder.NODE_ITEM, v, document, el);
            }
        }
        else if (value instanceof float[])
        {
            float[] vals = (float[]) value;
            for (Object v : vals) {
                this.makeValueNode(XmlEncoder.NODE_ITEM, v, document, el);
            }
        }
        else if (value instanceof double[])
        {
            double[] vals = (double[]) value;
            for (Object v : vals) {
                this.makeValueNode(XmlEncoder.NODE_ITEM, v, document, el);
            }
        }
        else if (value instanceof boolean[])
        {
            boolean[] vals = (boolean[]) value;
            for (Object v : vals) {
                this.makeValueNode(XmlEncoder.NODE_ITEM, v, document, el);
            }
        }
        else if (value instanceof char[])
        {
            char[] vals = (char[]) value;
            for (Object v : vals) {
                this.makeValueNode(XmlEncoder.NODE_ITEM, v, document, el);
            }
        }
        else if (value instanceof byte[])
        {
            byte[] vals = (byte[]) value;
            for (Object v : vals) {
                this.makeValueNode(XmlEncoder.NODE_ITEM, v, document, el);
            }
        }
        else if (value == null) {
            // skip
            // el.setTextContent("null");
        }
        else {
            el.setTextContent(value.toString());
        }
    }

    private void makeValueNode(String keyIn, Object value, Document document, Element root)
    {
        String key = keyIn;
        // хак если ключ число то item
        Matcher m = XmlEncoder.PATTERN_IS_NUMERIC.matcher(key);
        if (m.matches()) {
            key = XmlEncoder.NODE_ITEM;
        }

        Element el = document.createElement(key);
        root.appendChild(el);
        this.makeValue(el, value, document);
    }
}
