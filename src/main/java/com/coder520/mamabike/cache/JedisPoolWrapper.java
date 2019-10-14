package com.coder520.mamabike.cache;

import com.coder520.mamabike.common.constants.Parameters;
import com.coder520.mamabike.common.exception.MaMaBikeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import javax.annotation.PostConstruct;


//redis master
@Component
@Service
@Slf4j
public class JedisPoolWrapper {


	private JedisPool jedisPool = null;

	@Autowired
	private Parameters parameters;
	

	public  void init() throws MaMaBikeException {
		try {
			System.out.println("jedis初始化.........");
			JedisPoolConfig config = new JedisPoolConfig();
			config.setMaxTotal(parameters.getRedisMaxTotal());
			config.setMaxIdle(parameters.getRedisMaxIdle());
			config.setMaxWaitMillis(parameters.getRedisMaxWaitMillis());
			jedisPool = new JedisPool(config,parameters.getRedisHost(),parameters.getRedisPort(),10000);
		} catch (Exception e) {
			log.error("Fail to initialize jedis pool", e);
			throw new MaMaBikeException("Fail to initialize jedis pool");
		}
	}
	//本方法获取jedisPool前如何保证init方法执行初始化了，spring提供了注解@PostConstruct，类实例化后马上执行
	public JedisPool getJedisPool() {
		JedisPoolConfig config = new JedisPoolConfig();
		config.setMaxTotal(10);
		config.setMaxIdle(5);
		config.setMaxWaitMillis(3000);
		 new JedisPool(config,"127.0.0.1",6379,2000);
		return new JedisPool(config,"127.0.0.1",6379,2000);

	}
	
}
