package dev.ohate.survivalmultiplayer.database;

import lombok.Getter;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.io.Closeable;
import java.util.function.Function;

@Getter
public class Redis implements Closeable {

    private JedisPool jedisPool;

    public boolean isConnected() {
        return jedisPool != null && !jedisPool.isClosed();
    }

    public void connect(String redisUri) {
        jedisPool = new JedisPool(redisUri);
    }

    @Override
    public void close() {
        if (jedisPool != null && !jedisPool.isClosed()) {
            jedisPool.close();
        }
    }

    public <T> T runRedisCommand(Function<Jedis, T> lambda) {
        if (jedisPool == null || jedisPool.isClosed()) {
            throw new IllegalStateException("A connection to the redis server couldn't be established or has been forcefully closed");
        }

        Jedis jedis = null;

        try {
            jedis = jedisPool.getResource();
            return lambda.apply(jedis);
        } catch (Exception e) {
            throw new RuntimeException("Could not use resource and return", e);
        } finally {
            if (jedis != null && jedis.isConnected()) {
                jedis.close();
            }
        }
    }

}
