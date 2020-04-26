package com.moonlight.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

/**
 * redis工具类
 * 
 * @author sunan
 * @since 2020-04-24
 */
@SuppressWarnings("rawtypes")
@Component
public class RedisUtil {

	@Autowired
	RedisTemplate redisTemplate;

	public String get(String key) {
		return redisTemplate.opsForValue().get(key).toString();
	}
	
	public Integer getInt(String key) {
		String value = redisTemplate.opsForValue().get(key).toString();
		if(value!=null) {
			return Integer.parseInt(value);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public void set(String key, String value) {
		redisTemplate.opsForValue().set(key, value);
	}
	
	
	@SuppressWarnings("unchecked")
	public long incrby(String key,long delta) {
		return redisTemplate.opsForValue().increment(key, delta);
	}
	
	@SuppressWarnings("unchecked")
	public long incr(String key) {
		return redisTemplate.opsForValue().increment(key);
	}
	
	@SuppressWarnings("unchecked")
	public long decr(String key) {
		return redisTemplate.opsForValue().decrement(key);
	}
	
	@SuppressWarnings("unchecked")
	public long decrby(String key,long delta) {
		return redisTemplate.opsForValue().decrement(key, delta);
	}
}
