package org.openspigot.openfarming.database.gson;

import com.google.gson.*;
import org.bukkit.Location;
import org.openspigot.openfarming.database.BlockStore;
import org.openspigot.openfarming.database.PersistentBlock;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class BlockAdapter<T extends PersistentBlock> implements JsonDeserializer<Map<Location, T>> {

    private final Class<T> clazz;
    private final BlockStore<?> store;

    public BlockAdapter(BlockStore<?> store, Class<T> clazz) {
        this.clazz = clazz;
        this.store = store;
    }

    @SuppressWarnings("deprecation")
    @Override
    public Map<Location, T> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        Map<Location, T> result = new HashMap<>();

        for(JsonElement jsonElement : json.getAsJsonArray()) {
            JsonArray elm = jsonElement.getAsJsonArray();

            T castedClazz = context.deserialize(elm.get(1), clazz);
            castedClazz.setStore(store);

            result.put(context.deserialize(elm.get(0), Location.class), castedClazz);
        }

        return result;
    }
}
