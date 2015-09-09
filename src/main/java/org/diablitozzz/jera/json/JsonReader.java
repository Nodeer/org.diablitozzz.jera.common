package org.diablitozzz.jera.json;

import java.io.IOException;
import java.io.Reader;

public class JsonReader {
    
    private final JsonTokenizer tokenizer;
    
    public JsonReader(final Reader in) {
        this.tokenizer = new JsonTokenizer(in);
    }
    
    public boolean isLenient() {
        return this.tokenizer.isLenient();
    }
    
    public JsonObject read() throws JsonException, IOException {
        if (this.tokenizer.peek() == JsonTokenizer.Token.BEGIN_OBJECT) {
            return this.readObject();
        } else if (this.tokenizer.peek() == JsonTokenizer.Token.BEGIN_ARRAY) {
            return this.readArray();
        }
        return null;
    }
    
    public JsonObject readArray() throws JsonException, IOException {
        final JsonObject out = new JsonObject();
        
        this.tokenizer.beginArray();
        
        while (true) {
            //читаем значение
            final JsonTokenizer.Token token = this.tokenizer.peek();
            if (token == JsonTokenizer.Token.BOOLEAN) {
                out.add(this.tokenizer.nextBoolean());
            } else if (token == JsonTokenizer.Token.NULL) {
                out.add(this.tokenizer.nextNull());
            } else if (token == JsonTokenizer.Token.NUMBER) {
                out.add(this.tokenizer.nextNumber());
            } else if (token == JsonTokenizer.Token.STRING) {
                out.add(this.tokenizer.nextString());
            } else if (token == JsonTokenizer.Token.BEGIN_OBJECT) {
                out.add(this.readObject());
            } else if (token == JsonTokenizer.Token.BEGIN_ARRAY) {
                out.add(this.readArray());
            } else {
                break;
            }
            if (this.tokenizer.peek() == JsonTokenizer.Token.END_ARRAY) {
                break;
            }
        }
        this.tokenizer.endArray();
        
        return out;
    }
    
    public JsonObject readObject() throws JsonException, IOException {
        final JsonObject out = new JsonObject();
        
        this.tokenizer.beginObject();
        
        while (true) {
            JsonTokenizer.Token token = this.tokenizer.peek();
            
            //читаем имя
            if (token != JsonTokenizer.Token.NAME) {
                break;
                //throw new JsonException("Object has no key");
            }
            
            final String key = this.tokenizer.nextName();
            
            //читаем значение
            token = this.tokenizer.peek();
            
            if (token == JsonTokenizer.Token.BOOLEAN) {
                out.set(key, this.tokenizer.nextBoolean());
            } else if (token == JsonTokenizer.Token.NULL) {
                out.set(key, this.tokenizer.nextNull());
            } else if (token == JsonTokenizer.Token.NUMBER) {
                out.set(key, this.tokenizer.nextNumber());
            } else if (token == JsonTokenizer.Token.STRING) {
                out.set(key, this.tokenizer.nextString());
            } else if (token == JsonTokenizer.Token.BEGIN_OBJECT) {
                out.set(key, this.readObject());
            } else if (token == JsonTokenizer.Token.BEGIN_ARRAY) {
                out.set(key, this.readArray());
            } else {
                break;
            }
            if (this.tokenizer.peek() == JsonTokenizer.Token.END_OBJECT) {
                break;
            }
        }
        this.tokenizer.endObject();
        return out;
    }
    
    public void setLenient(final boolean lenient) {
        this.tokenizer.setLenient(lenient);
    }
}
