package org.bob.learn.common.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Redis工具类
 */
@Slf4j
public final class RedisUtils {

    private static StringRedisTemplate redisTemplate;

    /**
     * 私有构造器
     * 禁止通过反射构造类实例
     * @throws IllegalAccessException 非法访问异常
     */
    private RedisUtils() throws IllegalAccessException {
        throw new IllegalAccessException("禁止访问RedisUtils私有构造方法");
    }

    /**
     * Redis底层对象
     */
    @Component
    final static class Redis {
        Redis(@Autowired StringRedisTemplate redisTemplate){
            RedisUtils.redisTemplate = redisTemplate;
        }
    }

    /**
     * 获取Redis模板
     * @return Redis模板
     */
    public static RedisTemplate getRedisTemplate() {
        return redisTemplate;
    }

    /************************************ key 相关的工具方法 ********************************************/
    /**
     * @see RedisTemplate#hasKey(Object)
     */
    public static boolean exists(String key){
        return unbox(redisTemplate.hasKey(key));
    }

    /**
     * @see RedisTemplate#getExpire(Object)
     */
    public static long ttl(String key){
        return unbox(redisTemplate.getExpire(key));
    }

    /**
     * @see RedisTemplate#getExpire(Object, TimeUnit)
     */
    public static long ttl(String key, TimeUnit timeUnit){
        return unbox(redisTemplate.getExpire(key,timeUnit));
    }

    /**
     * @see RedisTemplate#keys(Object)
     */
    public static Set<String> keys(String pattern){
        return redisTemplate.keys(pattern);
    }

    /**
     * @see RedisTemplate#expire(Object, long, TimeUnit)
     */
    public static boolean expire(String key, long timeout, TimeUnit timeUnit){
       return unbox(redisTemplate.expire(key,timeout,timeUnit));
    }

    /**
     * @see RedisTemplate#expireAt(Object, Date)
     */
    public static boolean expireAt(String key, Date date){
        return unbox(redisTemplate.expireAt(key,date));
    }

    /**
     * @see RedisTemplate#type(Object)
     */
    public static DataType type(String key){
        return redisTemplate.type(key);
    }

    /**
     * @see RedisTemplate#delete(Object)
     */
    public static boolean del(String key){
        return unbox(redisTemplate.delete(key));
    }


   /****************************** value操作工具方法 ******************************************************/

    /**
     * @see org.springframework.data.redis.core.ValueOperations#set(Object, Object)
     */
   public static void set(String key, String value){
       redisTemplate.opsForValue().set(key, value);
   }

    /**
     * @see RedisUtils#set(String, String, long, TimeUnit)
     */
    public static void set(String key, String value, long timeout){
        set(key,value,timeout, TimeUnit.SECONDS);
    }

    /**
     * @see org.springframework.data.redis.core.ValueOperations#set(Object, Object, long, TimeUnit)
     */
   public static void set(String key, String value, long timeout, TimeUnit timeUnit){
        redisTemplate.opsForValue().set(key, value,timeout,timeUnit);
    }

    /**
     * @see org.springframework.data.redis.core.ValueOperations#setIfPresent(Object, Object)
     */
    public static boolean setex(String key, String value){
        return unbox(redisTemplate.opsForValue().setIfPresent(key, value));
    }

    /**
     * @see RedisUtils#setex(String, String, long, TimeUnit)
     */
    public static boolean setex(String key, String value, long timeout){
        return setex(key, value,timeout, TimeUnit.SECONDS);
    }

    /**
     * @see org.springframework.data.redis.core.ValueOperations#setIfPresent(Object, Object, long, TimeUnit)
     */
    public static boolean setex(String key, String value, long timeout, TimeUnit timeUnit){
        return unbox(redisTemplate.opsForValue().setIfPresent(key, value,timeout,timeUnit));
    }

    /**
     * @see org.springframework.data.redis.core.ValueOperations#setIfAbsent(Object, Object)
     */
    public static boolean setnx(String key, String value){
        return unbox(redisTemplate.opsForValue().setIfAbsent(key, value));
    }

    /**
     * @see RedisUtils#setnx(String, String, long, TimeUnit)
     */
    public static boolean setnx(String key, String value, long timeout){
        return setnx(key, value,timeout, TimeUnit.SECONDS);
    }

    /**
     * @see org.springframework.data.redis.core.ValueOperations#setIfAbsent(Object, Object, long, TimeUnit)
     */
    public static boolean setnx(String key, String value, long timeout, TimeUnit timeUnit){
        return unbox(redisTemplate.opsForValue().setIfAbsent(key, value,timeout,timeUnit));
    }

    /**
     * @see org.springframework.data.redis.core.ValueOperations#get(Object)
     */
    public static String get(String key){
        return o2s(redisTemplate.opsForValue().get(key));
    }

    /***************************** set 操作工具方法 ****************************************/

    /**
     * @see org.springframework.data.redis.core.SetOperations#add(Object, Object[])
     */
    public static long sadd(String key, String...value){
      return unbox(redisTemplate.opsForSet() .add(key,value));
    }

    /**
     * @see org.springframework.data.redis.core.SetOperations#remove(Object, Object...)
     */
    public static long srem(String key, Object... value){
        return unbox(redisTemplate.opsForSet().remove(key,value));
    }

    /**
     * @see org.springframework.data.redis.core.SetOperations#isMember(Object, Object)
     */
    public static boolean sismember(String key, Object value){
        return unbox(redisTemplate.opsForSet().isMember(key,value));
    }

    /**
     * @see org.springframework.data.redis.core.SetOperations#members(Object)
     */
    public static Set<String> smembers(String key){
        return redisTemplate.opsForSet().members(key);
    }

    /**
     * @see org.springframework.data.redis.core.SetOperations#members(Object)
     */
    public static long scard(String key){
        return unbox(redisTemplate.opsForSet().size(key));
    }

    /**
     * @see org.springframework.data.redis.core.SetOperations#union(Object, Object)
     */
    public static Set<String> sunion(String key, String otherKey){
        return redisTemplate.opsForSet().union(key, otherKey);
    }

    /**
     * @see org.springframework.data.redis.core.SetOperations#union(Object, Collection)
     */
    public static Set<String> sunion(String key, Collection<String> otherKeys){
        return redisTemplate.opsForSet().union(key,otherKeys);
    }

    /**
     * @see org.springframework.data.redis.core.SetOperations#unionAndStore(Object, Object, Object)
     */
    public static long sunionstore(String key, String otherKey, String destKey){
        return unbox(redisTemplate.opsForSet().unionAndStore(key, otherKey, destKey));
    }

    /**
     * @see org.springframework.data.redis.core.SetOperations#unionAndStore(Object, Collection, Object)
     */
    public static long sunionstore(String key, Collection<String> otherKeys, String destKey){
        return unbox(redisTemplate.opsForSet().unionAndStore(key,otherKeys,destKey));
    }

    /**
     * @see org.springframework.data.redis.core.SetOperations#difference(Object, Object)
     */
    public static Set<String> sdiff(String key, String otherKey){
        return redisTemplate.opsForSet().difference(key, otherKey);
    }

    /**
     * @see org.springframework.data.redis.core.SetOperations#difference(Object, Collection)
     */
    public static Set<String> sdiff(String key, Collection<String> otherKeys){
        return redisTemplate.opsForSet().difference(key,otherKeys);
    }

    /**
     * @see org.springframework.data.redis.core.SetOperations#intersect(Object, Object)
     */
    public static Set<String> sinter(String key, String otherKey){
        return redisTemplate.opsForSet().intersect(key,otherKey);
    }

    /**
     * @see org.springframework.data.redis.core.SetOperations#intersect(Object, Collection)
     */
    public static Set<String> sinter(String key, Collection<String> otherKeys){
        return redisTemplate.opsForSet().intersect(key,otherKeys);
    }

    /**
     * @see org.springframework.data.redis.core.SetOperations#intersect(Object, Object)
     */
    public static long sinterstore(String key, String otherKey, String destKey){
        return unbox(redisTemplate.opsForSet().intersectAndStore(key,otherKey,destKey));
    }

    /**
     * @see org.springframework.data.redis.core.SetOperations#intersect(Object, Collection)
     */
    public static long sinterstore(String key, Collection<String> otherKeys, String destKey){
        return unbox(redisTemplate.opsForSet().intersectAndStore(key,otherKeys,destKey));
    }

    /***********************************哈希表操作*************************************/
    /**
     * @see org.springframework.data.redis.core.HashOperations#hasKey(Object, Object)
     */
    public static boolean hexists(String key, String hashKey)  {
        return unbox(redisTemplate.opsForHash().hasKey(key,hashKey));
    }


    /**
     * @see org.springframework.data.redis.core.HashOperations#put(Object, Object, Object)
     */
    public static void hset(String key, String hashKey, String hashValue) {
        redisTemplate.opsForHash().put(key,hashKey,hashValue);
    }

    /**
     * @see org.springframework.data.redis.core.HashOperations#putIfAbsent(Object, Object, Object)
     */
    public static boolean hsetnx(String key, String hashKey, String hashValue) {
        return unbox(redisTemplate.opsForHash().putIfAbsent(key,hashKey,hashValue));
    }

    /**
     * @see org.springframework.data.redis.core.HashOperations#putAll(Object, Map)
     */
    public static void hmset(String key, Map<String, String> hashMap) {
        redisTemplate.opsForHash().putAll(key,hashMap);
    }

    /**
     * @see org.springframework.data.redis.core.HashOperations#get(Object, Object)
     */
    public static String hget(String key, String hashKey) {
        return o2s(redisTemplate.opsForHash().get(key,hashKey));
    }

    /**
     * 注意：不同Collection实现中排序和去重特性
     * @see org.springframework.data.redis.core.HashOperations#get(Object, Object)
     */
    public static List<String> hmget(String key, Collection<Object> hashKeys) {
        List<Object> hashValues = redisTemplate.opsForHash().multiGet(key, hashKeys);
        return ((CollectionUtils.isNotEmpty(hashValues))? hashValues.stream().map(RedisUtils::o2s).collect(Collectors.toList()):null);
    }
    /**
     * 注意：不同Collection实现中排序和去重特性
     * @see org.springframework.data.redis.core.HashOperations#get(Object, Object)
     */
    public static Map<String, String> entries(String key) {
        Map<Object, Object> entries = redisTemplate.opsForHash().entries(key);
        return (entries.isEmpty()? null:(entries.entrySet().stream().collect(Collectors.toMap(RedisUtils::o2s, RedisUtils::o2s))));
    }


    /**
     * 对象转String
     * @param value 值对象
     * @return 值String类型对象
     */
    private static String o2s(Object value){
        return (null==value)?null:value.toString();
    }
    /**
     * Boolean对象拆包
     */
    private static boolean unbox(Boolean value){
        return value!=null?value:false;
    }

    /**
     * Long对象拆包
     */
    private static long unbox(Long value){
        return value!=null?value:0L;
    }

}
