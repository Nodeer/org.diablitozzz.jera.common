package org.diablitozzz.jera.json;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

public class Json {
    
    public static JsonObject decode(final String json) throws JsonException {
        return Json.decode(json, false);
    }
    
    public static JsonObject decode(final String json, final boolean lenient) throws JsonException {
        if (json == null) {
            return new JsonObject();
        }
        
        final StringReader stringReader = new StringReader(json);
        final JsonReader jsonReader = new JsonReader(stringReader);
        jsonReader.setLenient(lenient);
        
        try {
            return jsonReader.read();
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    public static String encode(final Object data) {
        
        return Json.encode(data, false);
    }
    
    public static String encode(final Object data, final boolean longAsString) {
        final StringWriter stringWriter = new StringWriter();
        final JsonWriter jsonWriter = new JsonWriter(stringWriter);
        jsonWriter.setLongAsString(longAsString);
        try {
            jsonWriter.write(data);
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
        return stringWriter.toString();
    }
    
}
