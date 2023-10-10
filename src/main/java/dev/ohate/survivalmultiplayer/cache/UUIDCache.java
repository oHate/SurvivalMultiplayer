package dev.ohate.survivalmultiplayer.cache;

import dev.ohate.survivalmultiplayer.SurvivalMultiplayer;
import dev.ohate.survivalmultiplayer.database.Redis;
import redis.clients.jedis.Pipeline;

import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class UUIDCache {

    private static final String UUID_TO_NAME_KEY = "smp:uuid-name";
    private static final String NAME_TO_UUID_KEY = "smp:name-uuid";

    public static CompletableFuture<String> getName(UUID uuid) {
        Redis redis = SurvivalMultiplayer.getInstance().getRedis();

        return CompletableFuture.supplyAsync(() -> {
            return redis.runRedisCommand(jedis -> jedis.hget(UUID_TO_NAME_KEY, uuid.toString()));
        });
    }

    public static CompletableFuture<UUID> getUuid(String name) {
        Redis redis = SurvivalMultiplayer.getInstance().getRedis();

        return CompletableFuture.supplyAsync(() -> {
            String uuid = redis.runRedisCommand(jedis -> jedis.hget(NAME_TO_UUID_KEY, name.toLowerCase()));
            return uuid != null ? UUID.fromString(uuid) : null;
        });
    }

    public static void updateCache(UUID uuid, String name) {
        Redis redis = SurvivalMultiplayer.getInstance().getRedis();

        CompletableFuture.runAsync(() -> {
            redis.runRedisCommand(jedis -> {
                String oldName = jedis.hget(UUID_TO_NAME_KEY, uuid.toString());
                Pipeline pipeline = jedis.pipelined();

                if (oldName != null && !oldName.equalsIgnoreCase(name)) {
                    pipeline.hdel(NAME_TO_UUID_KEY, oldName);
                }

                pipeline.hset(NAME_TO_UUID_KEY, name.toLowerCase(Locale.ENGLISH), uuid.toString());
                pipeline.hset(UUID_TO_NAME_KEY, uuid.toString(), name);
                pipeline.sync();

                return null;
            });
        });

    }

}
