package com.kfit.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * redis连接池初始类
 * @author Josh
 * @date 2018年8月23日 下午5:53:44
 */
@Configuration
@EnableCaching
public class RedisConfig extends CachingConfigurerSupport{
	
	private static final Logger logger = LoggerFactory.getLogger(RedisConfig.class);
	
	@Value("${spring.redis.host}")
	private String host;

	@Value("${spring.redis.port}")
	private int port;

	@Value("${spring.redis.database}")
	private int database;

	@Value("${spring.redis.timeout}")
	private int timeout;

	@Value("${spring.redis.password}")
	private String pass;

	@Value("${spring.redis.jedis.pool.max-idle}")
	private int maxIdle;

	@Value("${spring.redis.jedis.pool.max-wait}")
	private long maxWaitMillis;

	@Value("${spring.redis.maxTotal}")
	private int maxTotal;

	@Bean
	public JedisPool redisPoolFactory() {
		logger.info("redis地址：" + host + ":" + port);
		JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
		jedisPoolConfig.setMaxIdle(maxIdle);
		jedisPoolConfig.setMaxWaitMillis(maxWaitMillis);
		jedisPoolConfig.setMaxTotal(maxTotal);
		if (pass.length() == 0){
			pass = null;
		}
		JedisPool jedisPool = new JedisPool(jedisPoolConfig, host, port,
				timeout, pass, database);
		logger.info("JedisPool注入成功！！");
		return jedisPool;
	}
}
