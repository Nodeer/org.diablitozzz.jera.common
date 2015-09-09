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

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

public class ConfigNode implements Cloneable {

    static public final String VALUE_TRUE = "true";
    static public final String VALUE_FALSE = "false";
    static public final String VALUE_NULL = "null";
    static public final String VALUE_1 = "1";
    static public final String VALUE_0 = "0";

    private final String name;
    private Object value;

    private final Map<String, ConfigNode> childs;

    public ConfigNode(String name)
    {
        this.childs = new LinkedHashMap<String, ConfigNode>();
        this.name = name;
    }

    public ConfigNode addChild(ConfigNode child)
    {
        this.childs.put(child.getName(), child);
        return this;
    }

    @Override
    public ConfigNode clone()
    {
        ConfigNode node = new ConfigNode(this.name);
        node.value = this.value;
        //make childs
        for (Entry<String, ConfigNode> entry : this.childs.entrySet()) {
            node.childs.put(entry.getKey(), entry.getValue().clone());
        }
        return node;
    }

    @Override
    public boolean equals(Object object)
    {
        if (!(object instanceof ConfigNode)) {
            return false;
        }

        ConfigNode inObject = (ConfigNode) object;

        //check name
        if (!this.name.equals(inObject.name)) {
            return false;
        }

        //check value
        if (this.value == null && inObject.value != null) {
            return false;
        }
        if (!this.getValueAsString().equals(inObject.getValueAsString())) {
            return false;
        }
        //check childs
        if (this.childs.size() != inObject.childs.size()) {
            return false;
        }
        for (ConfigNode child : this.childs.values()) {
            if (!inObject.hasChild(child.getName())) {
                return false;
            }
            if (!child.equals(inObject.childs.get(child.getName()))) {
                return false;
            }
        }
        return true;
    }

    public ConfigNode getChild(String name)
    {
        return this.getChild(name, false);
    }

    public ConfigNode getChild(String name, boolean createIfNotExists)
    {
        if (this.childs.containsKey(name)) {
            return this.childs.get(name);
        }
        else if (createIfNotExists) {
            ConfigNode child = new ConfigNode(name);
            this.addChild(child);
            return child;
        }
        return null;
    }

    public Collection<ConfigNode> getChilds()
    {
        return this.childs.values();
    }

    public String getName()
    {
        return this.name;
    }

    public boolean getValueAsBoolean()
    {
        return this.getValueAsBoolean(false);
    }

    public boolean getValueAsBoolean(boolean defaultValue)
    {
        if (this.value == null) {
            return defaultValue;
        }
        String v = this.value.toString();
        if (v.equals(ConfigNode.VALUE_1) || v.equals(ConfigNode.VALUE_TRUE)) {
            return true;
        }
        if (v.equals(ConfigNode.VALUE_0) || v.equals(ConfigNode.VALUE_FALSE)) {
            return false;
        }
        return defaultValue;
    }

    public double getValueAsDouble()
    {
        return this.getValueAsDouble(0D);
    }

    public double getValueAsDouble(double defaultValue)
    {
        if (this.value == null) {
            return defaultValue;
        }
        return Double.parseDouble(this.value.toString());
    }

    public float getValueAsFloat()
    {
        return this.getValueAsFloat(0F);
    }

    public float getValueAsFloat(float defaultValue)
    {
        if (this.value == null) {
            return defaultValue;
        }
        return Float.parseFloat(this.value.toString());
    }

    public int getValueAsInt()
    {
        return this.getValueAsInt(0);
    }

    public int getValueAsInt(int defaultValue)
    {
        if (this.value == null) {
            return defaultValue;
        }
        return Integer.parseInt(this.value.toString());
    }

    public long getValueAsLong()
    {
        return this.getValueAsLong(0L);
    }

    public long getValueAsLong(long defaultValue)
    {
        if (this.value == null) {
            return defaultValue;
        }
        return Long.parseLong(this.value.toString());
    }

    public Object getValueAsObject()
    {
        return this.getValueAsObject(null);
    }

    public Object getValueAsObject(Object defaultValue)
    {
        if (this.value == null) {
            return defaultValue;
        }
        return this.value;
    }

    public short getValueAsShort()
    {
        return this.getValueAsShort((short) 0);
    }

    public short getValueAsShort(short defaultValue)
    {
        if (this.value == null) {
            return defaultValue;
        }
        return Short.parseShort(this.value.toString());
    }

    public String getValueAsString()
    {
        return this.getValueAsString(ConfigNode.VALUE_NULL);
    }

    public String getValueAsString(String defaultValue)
    {
        if (this.value == null) {
            return defaultValue;
        }
        return this.value.toString();
    }

    public boolean hasChild(String name)
    {
        return this.childs.containsKey(name);
    }

    public boolean hasChilds()
    {
        return this.childs.isEmpty();
    }

    public boolean isValueBoolean()
    {
        if (this.value instanceof Boolean) {
            return true;
        }
        if (this.value instanceof String) {
            if (this.value.equals(ConfigNode.VALUE_TRUE) || this.value.equals(ConfigNode.VALUE_FALSE)) {
                return true;
            }
        }
        return false;
    }

    public boolean isValueNull()
    {
        return this.value == null;
    }

    public void removeChild(String childName)
    {
        this.childs.remove(childName);
    }

    public void setValue(Object value)
    {
        this.value = value;
    }

    @Override
    public String toString()
    {
        int childCount = this.childs.size();
        StringBuilder builder = new StringBuilder(16 * (childCount + 1));

        builder.append('{');
        //make value
        builder.append('"');
        builder.append(this.name.replace("\"", "\\\""));
        builder.append('"');
        builder.append(':');
        builder.append('"');
        builder.append(this.getValueAsString().replace("\"", "\\\""));
        builder.append('"');

        if (childCount > 0) {
            //make childs
            builder.append(",\"_childs\":");
            builder.append('[');

            int i = 0;
            for (ConfigNode child : this.childs.values()) {
                builder.append(child.toString());
                if (++i < childCount) {
                    builder.append(',');
                }
            }
            builder.append(']');
        }

        builder.append('}');
        return builder.toString();
    }
}
