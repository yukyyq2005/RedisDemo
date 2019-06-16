package com.kfit.cache;

import com.kfit.cache.MethodUtil;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.exceptions.JedisConnectionException;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.SocketTimeoutException;
import java.util.*;

/**
 * Jedis方法公共类
 * @author Josh
 * @date 2018年3月22日 下午5:56:00
 */
@Component
public class JedisUtil {

	private static final Logger logger = LoggerFactory.getLogger(JedisUtil.class);

	@Autowired
	private JedisPool jedisPool;

	/**
	 * @Method: getJedis
	 * @Description: 获取连接
	 * @return
	 */
	public synchronized Jedis getJedis() {
		int timeoutCount = 0;
		while (true) // 如果是网络超时则多试几次
		{
			try {
				Jedis jedis = jedisPool.getResource();
				return jedis;
			} catch (Exception e) {
				// 底层原因是SocketTimeoutException，不过redis已经捕捉且抛出JedisConnectionException，不继承于前者
				if (e instanceof JedisConnectionException
						|| e instanceof SocketTimeoutException) {
					timeoutCount++;
					if (timeoutCount >= 3) {
						logger.error(
								Thread.currentThread().getStackTrace()[1]
										.getMethodName() + "getJedis exception",
								e);
						break;
					}
				} else {
					logger.error(
							Thread.currentThread().getStackTrace()[1]
									.getMethodName() + "getJedis exception", e);
					break;
				}
			}
		}
		return null;
	}

	/**
	 * @Method: returnResource
	 * @Description: 归还资源
	 * @param jedis
	 */
	public void returnResource(Jedis jedis) {
		try {
			jedis.close();
		} catch (Exception e) {
			logger.error(
					Thread.currentThread().getStackTrace()[1].getMethodName()
							+ " Exception", e);
		}

	}

	public boolean hexists(String key, String field) {
		Jedis jedis = getJedis();
		try {
			return jedis.hexists(key, field);
		} catch (Exception e) {
			logger.error(
					Thread.currentThread().getStackTrace()[1].getMethodName()
							+ " Exception", e);
		} finally {
			returnResource(jedis);
		}
		return false;
	}

	public String hget(String key, String field) {
		Jedis jedis = getJedis();
		try {
			return jedis.hget(key, field);
		} catch (Exception e) {
			logger.error(
					Thread.currentThread().getStackTrace()[1].getMethodName()
							+ " Exception", e);
		} finally {
			returnResource(jedis);
		}
		return null;
	}
	public void desr(String key) {
		Jedis jedis = getJedis();
		try {
			 jedis.decr(key);
		} catch (Exception e) {
			logger.error(
					Thread.currentThread().getStackTrace()[1].getMethodName()
					+ " Exception", e);
		} finally {
			returnResource(jedis);
		}
	}

	public Long hset(String key, String field, String value) {
		Jedis jedis = getJedis();
		try {
			return jedis.hset(key, field, value);
		} catch (Exception e) {
			logger.error(
					Thread.currentThread().getStackTrace()[1].getMethodName()
							+ " Exception", e);
		} finally {
			returnResource(jedis);
		}
		return null;
	}

	public String hmset(String key, Map<String, String> hash) {
		Jedis jedis = getJedis();
		try {
			// 去空去异常
			Map<String, String> newHash = new HashMap<String, String>();
			Set<String> set = hash.keySet();
			Iterator<String> iterator = set.iterator();
			while (iterator.hasNext()) {
				String hashKey = iterator.next();
				try {
					String hashValue = hash.get(hashKey);
					if (hashValue != null) {
						newHash.put(hashKey, hashValue);
					}
				} catch (Exception e) {
				}
			}
			return jedis.hmset(key, newHash);
		} catch (Exception e) {
			logger.error(
					Thread.currentThread().getStackTrace()[1].getMethodName()
							+ " Exception", e);
		} finally {
			returnResource(jedis);
		}
		return null;
	}

	public List<String> hmget(String key, String... fields) {
		Jedis jedis = getJedis();
		try {
			return jedis.hmget(key, fields);
		} catch (Exception e) {
			logger.error(
					Thread.currentThread().getStackTrace()[1].getMethodName()
							+ " Exception", e);
		} finally {
			returnResource(jedis);
		}
		return null;
	}

	public Long hdel(String key, String... fields) {
		Jedis jedis = getJedis();
		try {
			return jedis.hdel(key, fields);
		} catch (Exception e) {
			logger.error(
					Thread.currentThread().getStackTrace()[1].getMethodName()
							+ " Exception", e);
		} finally {
			returnResource(jedis);
		}
		return null;
	}

	public Long hincrBy(String key, String field, long value) {
		Jedis jedis = getJedis();
		try {
			return jedis.hincrBy(key, field, value);
		} catch (Exception e) {
			logger.error(
					Thread.currentThread().getStackTrace()[1].getMethodName()
							+ " Exception", e);
		} finally {
			returnResource(jedis);
		}
		return null;
	}

	public Set<String> hkeys(String key) {
		Jedis jedis = getJedis();
		try {
			return jedis.hkeys(key);
		} catch (Exception e) {
			logger.error(
					Thread.currentThread().getStackTrace()[1].getMethodName()
							+ " Exception", e);
		} finally {
			returnResource(jedis);
		}
		return null;
	}

	public Map<String,String> hgetall (String key) {
		Jedis jedis = getJedis();
		try {
			return jedis.hgetAll(key);
		} catch (Exception e) {
			logger.error(
					Thread.currentThread().getStackTrace()[1].getMethodName()
							+ " Exception", e);
		} finally {
			returnResource(jedis);
		}
		return null;
	}

	public Long hlen(String key) {
		Jedis jedis = getJedis();
		try {
			return jedis.hlen(key);
		} catch (Exception e) {
			logger.error(
					Thread.currentThread().getStackTrace()[1].getMethodName()
							+ " Exception", e);
		} finally {
			returnResource(jedis);
		}
		return 0L;
	}

	public Long incr(String key) {
		Jedis jedis = getJedis();
		try {
			return jedis.incr(key);
		} catch (Exception e) {
			logger.error(
					Thread.currentThread().getStackTrace()[1].getMethodName()
							+ " Exception", e);
		} finally {
			returnResource(jedis);
		}
		return null;
	}

	public Long incrBy(String key, long value) {
		Jedis jedis = getJedis();
		try {
			return jedis.incrBy(key, value);
		} catch (Exception e) {
			logger.error(
					Thread.currentThread().getStackTrace()[1].getMethodName()
							+ " Exception", e);
		} finally {
			returnResource(jedis);
		}
		return null;
	}

	public String get(String key) {
		Jedis jedis = getJedis();
		try {
			return jedis.get(key);
		} catch (Exception e) {
			logger.error(
					Thread.currentThread().getStackTrace()[1].getMethodName()
							+ " Exception", e);
		} finally {
			returnResource(jedis);
		}
		return null;
	}

	public String set(String key, String value) {
		Jedis jedis = getJedis();
		try {
			return jedis.set(key, value);
		} catch (Exception e) {
			logger.error(
					Thread.currentThread().getStackTrace()[1].getMethodName()
							+ " Exception", e);
		} finally {
			returnResource(jedis);
		}
		return null;
	}

	public String set(String key, String value, int time) {
		Jedis jedis = getJedis();
		try {
			String returnKey = jedis.set(key, value);
			if (returnKey != null) {
				jedis.expire(key, time);
			}
			return returnKey;
		} catch (Exception e) {
			logger.error(
					Thread.currentThread().getStackTrace()[1].getMethodName()
							+ " Exception", e);
		} finally {
			returnResource(jedis);
		}
		return null;
	}

	public Long expire(String key, int seconds) {
		Jedis jedis = getJedis();
		try {
			return jedis.expire(key, seconds);
		} catch (Exception e) {
			logger.error(
					Thread.currentThread().getStackTrace()[1].getMethodName()
							+ " Exception", e);
		} finally {
			returnResource(jedis);
		}
		return null;
	}

	public Long ttl(String key) {
		Jedis jedis = getJedis();
		try {
			return jedis.ttl(key);
		} catch (Exception e) {
			logger.error(
					Thread.currentThread().getStackTrace()[1].getMethodName()
							+ " Exception", e);
		} finally {
			returnResource(jedis);
		}
		return null;
	}

	public boolean exists(String key) {
		Jedis jedis = getJedis();
		try {
			return jedis.exists(key);
		} catch (Exception e) {
			logger.error(
					Thread.currentThread().getStackTrace()[1].getMethodName()
							+ " Exception", e);
		} finally {
			returnResource(jedis);
		}
		return false;
	}

	public Long del(String key) {
		Jedis jedis = getJedis();
		try {
			return jedis.del(key);
		} catch (Exception e) {
			logger.error(
					Thread.currentThread().getStackTrace()[1].getMethodName()
							+ " Exception", e);
		} finally {
			returnResource(jedis);
		}
		return null;
	}

	public Set<String> keys(String key) {
		Jedis jedis = getJedis();
		try {
			return jedis.keys(key);
		} catch (Exception e) {
			logger.error(
					Thread.currentThread().getStackTrace()[1].getMethodName()
							+ " Exception", e);
		} finally {
			returnResource(jedis);
		}
		return null;
	}

	public Long zrank(String key, String member) {
		Jedis jedis = getJedis();
		try {
			return jedis.zrank(key, member);
		} catch (Exception e) {
			logger.error(
					Thread.currentThread().getStackTrace()[1].getMethodName()
							+ " Exception", e);
		} finally {
			returnResource(jedis);
		}
		return null;
	}

	public Long zrevrank(String key, String member) {
		Jedis jedis = getJedis();
		try {
			return jedis.zrevrank(key, member);
		} catch (Exception e) {
			logger.error(
					Thread.currentThread().getStackTrace()[1].getMethodName()
							+ " Exception", e);
		} finally {
			returnResource(jedis);
		}
		return null;
	}

	public Long zcard(String key) {
		Jedis jedis = getJedis();
		try {
			return jedis.zcard(key);
		} catch (Exception e) {
			logger.error(
					Thread.currentThread().getStackTrace()[1].getMethodName()
							+ " Exception", e);
		} finally {
			returnResource(jedis);
		}
		return null;
	}

	public Long zadd(String key, double score, String member) {
		Jedis jedis = getJedis();
		try {
			return jedis.zadd(key, score, member);
		} catch (Exception e) {
			logger.error(
					Thread.currentThread().getStackTrace()[1].getMethodName()
							+ " Exception", e);
		} finally {
			returnResource(jedis);
		}
		return null;
	}

	public Set<String> zrange(String key, long start, long end) {
		Jedis jedis = getJedis();
		try {
			return jedis.zrange(key, start, end);
		} catch (Exception e) {
			logger.error(
					Thread.currentThread().getStackTrace()[1].getMethodName()
							+ " Exception", e);
		} finally {
			returnResource(jedis);
		}
		return null;
	}

	public Set<String> zrevrange(String key, long start, long end) {
		Jedis jedis = getJedis();
		try {
			return jedis.zrevrange(key, start, end);
		} catch (Exception e) {
			logger.error(
					Thread.currentThread().getStackTrace()[1].getMethodName()
							+ " Exception", e);
		} finally {
			returnResource(jedis);
		}
		return null;
	}

	public Double zscore(String key, String member) {
		Jedis jedis = getJedis();
		try {
			return jedis.zscore(key, member);
		} catch (Exception e) {
			logger.error(
					Thread.currentThread().getStackTrace()[1].getMethodName()
							+ " Exception", e);
		} finally {
			returnResource(jedis);
		}
		return null;
	}

	public Long zrem(String key, String member) {
		Jedis jedis = getJedis();
		try {
			return jedis.zrem(key, member);
		} catch (Exception e) {
			logger.error(
					Thread.currentThread().getStackTrace()[1].getMethodName()
							+ " Exception", e);
		} finally {
			returnResource(jedis);
		}
		return null;
	}

	public Long lpush(String key, String... strings) {
		Jedis jedis = getJedis();
		try {
			return jedis.lpush(key, strings);
		} catch (Exception e) {
			logger.error(
					Thread.currentThread().getStackTrace()[1].getMethodName()
							+ " Exception", e);
		} finally {
			returnResource(jedis);
		}
		return null;
	}

	public String lpop(String key) {
		Jedis jedis = getJedis();
		try {
			return jedis.lpop(key);
		} catch (Exception e) {
			logger.error(
					Thread.currentThread().getStackTrace()[1].getMethodName()
							+ " Exception", e);
		} finally {
			returnResource(jedis);
		}
		return null;
	}

	public Long rpush(String key, String... strings) {
		Jedis jedis = getJedis();
		try {
			return jedis.rpush(key, strings);
		} catch (Exception e) {
			logger.error(
					Thread.currentThread().getStackTrace()[1].getMethodName()
							+ " Exception", e);
		} finally {
			returnResource(jedis);
		}
		return null;
	}

	public String rpop(String key) {
		Jedis jedis = getJedis();
		try {
			return jedis.rpop(key);
		} catch (Exception e) {
			logger.error(
					Thread.currentThread().getStackTrace()[1].getMethodName()
							+ " Exception", e);
		} finally {
			returnResource(jedis);
		}
		return null;
	}

	public Long lrem(String key, long count, String value) {
		Jedis jedis = getJedis();
		try {
			return jedis.lrem(key, count, value);
		} catch (Exception e) {
			logger.error(
					Thread.currentThread().getStackTrace()[1].getMethodName()
							+ " Exception", e);
		} finally {
			returnResource(jedis);
		}
		return null;
	}

	public Long llen(String key) {
		Jedis jedis = getJedis();
		try {
			return jedis.llen(key);
		} catch (Exception e) {
			logger.error(
					Thread.currentThread().getStackTrace()[1].getMethodName()
							+ " Exception", e);
		} finally {
			returnResource(jedis);
		}
		return null;
	}

	public Long sadd(String key, String... members) {
		Jedis jedis = getJedis();
		try {
			return jedis.sadd(key, members);
		} catch (Exception e) {
			logger.error(
					Thread.currentThread().getStackTrace()[1].getMethodName()
							+ " Exception", e);
		} finally {
			returnResource(jedis);
		}
		return null;
	}

	public Long srem(String key, String... members) {
		Jedis jedis = getJedis();
		try {
			return jedis.srem(key, members);
		} catch (Exception e) {
			logger.error(
					Thread.currentThread().getStackTrace()[1].getMethodName()
							+ " Exception", e);
		} finally {
			returnResource(jedis);
		}
		return null;
	}

	public Long scard(String key) {
		Jedis jedis = getJedis();
		try {
			return jedis.scard(key);
		} catch (Exception e) {
			logger.error(
					Thread.currentThread().getStackTrace()[1].getMethodName()
							+ " Exception", e);
		} finally {
			returnResource(jedis);
		}
		return null;
	}

	public Boolean sismember(String key, String member) {
		Jedis jedis = getJedis();
		try {
			return jedis.sismember(key, member);
		} catch (Exception e) {
			logger.error(
					Thread.currentThread().getStackTrace()[1].getMethodName()
							+ " Exception", e);
		} finally {
			returnResource(jedis);
		}
		return false;
	}

	public Set<String> smembers(String key) {
		Jedis jedis = getJedis();
		try {
			return jedis.smembers(key);
		} catch (Exception e) {
			logger.error(
					Thread.currentThread().getStackTrace()[1].getMethodName()
							+ " Exception", e);
		} finally {
			returnResource(jedis);
		}
		return null;
	}

	public Object eval(String script, List<String> keys, List<String> args) {
		Jedis jedis = getJedis();
		try {
			return jedis.eval(script, keys, args);
		} catch (Exception e) {
			logger.error(
					Thread.currentThread().getStackTrace()[1].getMethodName()
							+ " Exception", e);
		} finally {
			returnResource(jedis);
		}

		return null;
	}

	/**
	 * @Method: isLock
	 * @Description: 缓存分布式锁
	 * @param key
	 *            键
	 * @param seconds
	 *            过期时间 以秒为单位
	 * @return
	 */
	public boolean isLock(String key, int seconds) {
		Jedis jedis = getJedis();
		try {
			Long count = jedis.incr(key);
			if (count == 1) {
				// 添加过期时间
				jedis.expire(key, seconds);
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			logger.error(
					Thread.currentThread().getStackTrace()[1].getMethodName()
							+ " Exception", e);
		} finally {
			returnResource(jedis);
		}
		return false;
	}

	/**
	 * 用hmap保存对象（注意：只能保存简单对象 属性支持类型：JAVA四类八种基本数据类型）
	 * 
	 * @param key
	 *            唯一key
	 * @param obj
	 *            对象
	 * @return
	 */
	public String setObj(String key, Object obj) {
		Jedis jedis = getJedis();
		try {
			return jedis.hmset(key, objectToMap(obj));
		} catch (Exception e) {
			logger.error(
					Thread.currentThread().getStackTrace()[1].getMethodName()
							+ " Exception", e);
		} finally {
			returnResource(jedis);
		}
		return null;
	}

	/**
	 * 用hmap保存对象（注意：只能保存简单对象 属性支持类型：JAVA四类八种基本数据类型）
	 * 
	 * @param key
	 *            唯一key
	 * @param obj
	 *            对象
	 * @return
	 */
	public String setObj(String key, Object obj, int time) {
		Jedis jedis = getJedis();
		try {
			String value = jedis.hmset(key, objectToMap(obj));
			if (value != null) {
				jedis.expire(key, time);
			}
			return value;
		} catch (Exception e) {
			logger.error(
					Thread.currentThread().getStackTrace()[1].getMethodName()
							+ " Exception", e);
		} finally {
			returnResource(jedis);
		}
		return null;
	}

	/**
	 * 获取对象（注意：只能保存简单对象 属性支持类型：JAVA四类八种基本数据类型）
	 * 
	 * @param key
	 * @param clazz
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> T getObj(String key, Class<T> clazz) {
		Jedis jedis = getJedis();
		try {
			Set<String> hkeys = jedis.hkeys(key);
			if (hkeys == null || hkeys.size() == 0) {
				return null;
			}
			Map<String, String> map = new HashMap<String, String>();
			Iterator<String> iterator = hkeys.iterator();
			while (iterator.hasNext()) {
				String field = iterator.next();
				map.put(field, jedis.hget(key, field));
			}
			return (T) mapToObject(map, clazz);
		} catch (Exception e) {
			logger.error(
					Thread.currentThread().getStackTrace()[1].getMethodName()
							+ " Exception", e);
		} finally {
			returnResource(jedis);
		}
		return null;
	}

	private static Field[] getBeanFields(Class<?> cls, Field[] fs) {
		fs = (Field[]) ArrayUtils.addAll(fs, cls.getDeclaredFields());
		if (cls.getSuperclass() != null) {
			Class<?> clsSup = cls.getSuperclass();
			fs = getBeanFields(clsSup, fs);
		}
		return fs;
	}

	private static Method getMethodValue(String str, Object obj, Class<?> cls,
			Class<?> clsType) throws Exception {
		Method method = null;
		if (cls.getSuperclass() != null) {
			try {
				Class<?> clsSup = cls.getSuperclass();
				if (clsType != null) {
					method = clsSup.getDeclaredMethod(str, clsType);
				} else {
					method = clsSup.getDeclaredMethod(str);
				}
			} catch (NoSuchMethodException e) {
			}
		}
		if (method == null) {
			if (clsType != null) {
				method = cls.getDeclaredMethod(str, clsType);
			} else {
				method = cls.getDeclaredMethod(str);
			}
		}
		return method;
	}

	/**
	 * 返回由对象的属性为key,值为map的value的Map集合
	 * 
	 * @param obj
	 *            Object
	 * @return mapValue Map<String,String>
	 * @throws Exception
	 */
	private static Map<String, String> objectToMap(Object obj) {
		Map<String, String> mapValue = new HashMap<String, String>();
		Class<?> cls = obj.getClass();
		Field[] fields = getBeanFields(cls, new Field[0]);
		for (Field field : fields) {
			try {
				String name = field.getName();
				String strGet = "get" + name.substring(0, 1).toUpperCase()
						+ name.substring(1, name.length());
				Method method = getMethodValue(strGet, obj, cls, null);
				Object object = method.invoke(obj);
				if (object == null) {
					continue;
				}
				String value = object.toString();
				mapValue.put(MethodUtil.underscoreName(name), value);
			} catch (Exception e) {
				continue;
			}
		}
		return mapValue;
	}

	/**
	 * 返回由Map的key对属性，value对应值组成的对应
	 * 
	 * @param map
	 *            Map<String,String>
	 * @param cls
	 *            Class
	 * @return obj Object
	 * @throws Exception
	 */
	private static Object mapToObject(Map<String, String> map, Class<?> cls)
			throws Exception {
		Field[] fields = getBeanFields(cls, new Field[0]);
		Object obj = cls.newInstance();
		for (Field field : fields) {
			try {
				Class<?> clsType = field.getType();
				String name = field.getName();
				String strSet = "set" + name.substring(0, 1).toUpperCase()
						+ name.substring(1, name.length());

				Method method = getMethodValue(strSet, obj, cls, clsType);
				name = MethodUtil.underscoreName(name);
				if (map.containsKey(name)) {
					Object objValue = typeConversion(clsType, map.get(name));
					if (objValue == null) {
						continue;
					}
					method.invoke(obj, objValue);
				}
			} catch (Exception e) {
				continue;
			}
		}
		return obj;
	}

	@SuppressWarnings("deprecation")
	private static Object typeConversion(Class<?> cls, String str) {
		Object obj = null;
		String nameType = cls.getSimpleName();
		if (!"String".equals(nameType)
				&& (str == null || str.trim().equals(""))) {
			return null;
		}
		if ("String".equals(nameType)) {
			obj = str;
		} else if ("Integer".equalsIgnoreCase(nameType)) {
			obj = Integer.valueOf(str);
		} else if ("int".equalsIgnoreCase(nameType)) {
			obj = Integer.valueOf(str);
		} else if ("Float".equalsIgnoreCase(nameType)) {
			obj = Float.valueOf(str);
		} else if ("Double".equalsIgnoreCase(nameType)) {
			obj = Double.valueOf(str);
		} else if ("Boolean".equalsIgnoreCase(nameType)) {
			obj = Boolean.valueOf(str);
		} else if ("Long".equalsIgnoreCase(nameType)) {
			obj = Long.valueOf(str);
		} else if ("Short".equalsIgnoreCase(nameType)) {
			obj = Short.valueOf(str);
		} else if ("Character".equalsIgnoreCase(nameType)) {
			obj = str.charAt(1);
		} else if ("BigInteger".equalsIgnoreCase(nameType)) {
			obj = BigInteger.valueOf(Long.valueOf(str));
		} else if ("BigDecimal".equalsIgnoreCase(nameType)) {
			obj = new BigDecimal(str);
		} else if ("Byte".equalsIgnoreCase(nameType)) {
			obj = Byte.valueOf(str);
		} else if ("Date".equalsIgnoreCase(nameType)) {
			obj = new Date(Date.parse(str));
		} else {
			System.out.println(nameType + "=" + str);
		}
		return obj;
	}

}
