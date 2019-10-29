//package linghao.codenote.Utils;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.dao.DataAccessException;
//import org.springframework.data.redis.connection.RedisConnection;
//import org.springframework.data.redis.core.RedisCallback;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.data.redis.core.ValueOperations;
//import org.springframework.data.redis.serializer.RedisSerializer;
//import org.springframework.stereotype.Component;
//
//import java.io.Serializable;
//import java.util.Map;
//import java.util.Set;
//import java.util.concurrent.TimeUnit;
//
///**
// * redis工具类
// */
//@Component
//public class RedisUtils {
//
//    @Autowired
//    private RedisTemplate redisTemplate;
//
//    /**
//     * 批量删除对应的value
//     *
//     * @param keys
//     */
//    public void remove(final String... keys) {
//        for (String key : keys) {
//            remove(key);
//        }
//    }
//
//    /**
//     * 批量删除key
//     *
//     * @param pattern
//     */
//    public void removePattern(final String pattern) {
//        Set<Serializable> keys = redisTemplate.keys(pattern);
//        if (keys.size() > 0) {
//            redisTemplate.delete(keys);
//        }
//    }
//
//    /**
//     * 删除对应的value
//     *
//     * @param key
//     */
//    public void remove(final String key) {
//        if (exists(key)) {
//            redisTemplate.delete(key);
//        }
//    }
//
//    /**
//     * 判断缓存中是否有对应的value
//     *
//     * @param key
//     * @return
//     */
//    public boolean exists(final String key) {
//        return redisTemplate.hasKey(key);
//    }
//
//    /**
//     * 读取缓存
//     *
//     * @param key
//     * @return
//     */
//    public Object get(final String key) {
//        Object result = null;
//        ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
//        result = operations.get(key);
//        return result;
//    }
//
//    /**
//     * 写入缓存
//     *
//     * @param key
//     * @param value
//     * @return
//     */
//    public boolean set(final String key, Object value) {
//        boolean result = false;
//        try {
//            ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
//            operations.set(key, value);
//            result = true;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return result;
//    }
//
//    /**
//     * 写入缓存
//     *
//     * @param key
//     * @param value
//     * @return
//     */
//    public boolean set(final String key, Object value, Long expireTime) {
//        boolean result = false;
//        try {
//            ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
//            operations.set(key, value);
//            redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
//            result = true;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return result;
//    }
//
//    /**
//     * 设置超时时间
//     *
//     * @param key
//     * @param seconds
//     * @return
//     */
//    public boolean expire(final String key, Long seconds) {
//        boolean result = false;
//        try {
//            redisTemplate.expire(key, seconds,TimeUnit.SECONDS);
//            result = true;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return result;
//    }
//
//    /**
//     * 返回一个key还能活多久，单位为秒
//     *
//     * @param key
//     * @return 如果该key本来并没有设置过期时间，则返回-1，如果该key不存在，则返回-2
//     */
//    public long ttl(final String key) {
//        long result = (long) redisTemplate.execute(new RedisCallback<Long>() {
//            @Override
//            public Long doInRedis(RedisConnection connection) throws DataAccessException {
//                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
//                long count = connection.ttl(serializer.serialize(key));
//                return count;
//            }
//        });
//        return result;
//    }
//
//    /**
//     * List操作，在名称为key的list尾添加一个值为obj的元素
//     *
//     * @param key
//     * @param value
//     * @return 返回value在该list中的次序
//     */
//    public long rpush(final String key, String value) {
//        long result = (long) redisTemplate.execute(new RedisCallback<Long>() {
//            @Override
//            public Long doInRedis(RedisConnection connection) throws DataAccessException {
//                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
//                long count = connection.rPush(serializer.serialize(key), serializer.serialize(value));
//                return count;
//            }
//        });
//        return result;
//    }
//
//    /**
//     * List操作，返回名称为key的list的长度
//     * @param key
//     * @return
//     */
//    public long llen(final String key) {
//        long result = (long) redisTemplate.execute(new RedisCallback<Long>() {
//            @Override
//            public Long doInRedis(RedisConnection connection) throws DataAccessException {
//                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
//                long length = connection.lLen(serializer.serialize(key));
//                return length;
//            }
//        });
//        return result;
//    }
//
//    /**
//     * List操作，返回并删除名称为key的list中的首元素
//     * @param key
//     * @return
//     */
//    public String lpop(String key) {
//        String result = (String) redisTemplate.execute(new RedisCallback<String>() {
//            @Override
//            public String doInRedis(RedisConnection connection) throws DataAccessException {
//                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
//                byte[] res =  connection.lPop(serializer.serialize(key));
//                return serializer.deserialize(res);
//            }
//        });
//        return result;
//    }
//
//    //================================Map=================================
//    /**
//     * HashGet
//     * @param key 键 不能为null
//     * @param item 项 不能为null
//     * @return 值
//     */
//    public Object hget(String key,String item){
//        return redisTemplate.opsForHash().get(key, item);
//    }
//
//    /**
//     * 获取hashKey对应的所有键值
//     * @param key 键
//     * @return 对应的多个键值
//     */
//    public Map<String,Object> hmget(String key){
//        return redisTemplate.opsForHash().entries(key);
//    }
//
//    /**
//     * HashSet
//     * @param key 键
//     * @param map 对应多个键值
//     * @return true 成功 false 失败
//     */
//    public boolean hmset(String key, Map<String,Object> map) {
//        try {
//            redisTemplate.opsForHash().putAll(key, map);
//            return true;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        }
//    }
//
//}