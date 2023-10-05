package dev.ohate.survivalmultiplayer.util.json;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class JsonChain {

    private final JsonObject json = new JsonObject();

    public JsonChain addProperty(String property, String value) {
        json.addProperty(property, value);
        return this;
    }

    public JsonChain addProperty(String property, Number value) {
        json.addProperty(property, value);
        return this;
    }

    public JsonChain addProperty(String property, Boolean value) {
        json.addProperty(property, value);
        return this;
    }

    public JsonChain addProperty(String property, Character value) {
        json.addProperty(property, value);
        return this;
    }

    public JsonChain add(String property, JsonElement element) {
        json.add(property, element);
        return this;
    }

    public JsonObject get() {
        return json;
    }

}
