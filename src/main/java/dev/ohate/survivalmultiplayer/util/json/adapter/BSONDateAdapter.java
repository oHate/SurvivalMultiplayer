package dev.ohate.survivalmultiplayer.util.json.adapter;

import com.google.gson.*;
import dev.ohate.survivalmultiplayer.util.json.JsonChain;

import java.lang.reflect.Type;
import java.time.Instant;

public class BSONDateAdapter implements JsonDeserializer<Instant>, JsonSerializer<Instant> {

    @Override
    public Instant deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return Instant.parse(json.getAsJsonObject().get("$date").getAsString());
    }

    @Override
    public JsonElement serialize(Instant src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonChain().addProperty("$date", src.toString()).get();
    }

}
