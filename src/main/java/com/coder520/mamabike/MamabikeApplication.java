package com.coder520.mamabike;

import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.web.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.HttpMessageConverter;

@SpringBootApplication(scanBasePackages={
"com.coder520.mamabike.user.dao", "com.coder520.mamabike.user.controller",
		"com.coder520.mamabike.user.service","com.coder520.mamabike.cache","com.coder520.mamabike.common","com.coder520.mamabike.sms","com.coder520.mamabike.jms"})
public class MamabikeApplication {

	public static void main(String[] args) {
		SpringApplication.run(MamabikeApplication.class, args);
	}

	@Value("${config.laowang}")
	private String laowang;

	@Bean//覆盖原有的HttpMessageConverters
	public HttpMessageConverters fastJsonHttpMessageConverters() {
		FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();
		HttpMessageConverter<?> converter = fastConverter;


		System.out.println(laowang);  //测试取出yml中配置文件



		return new HttpMessageConverters(converter);



	}
}
