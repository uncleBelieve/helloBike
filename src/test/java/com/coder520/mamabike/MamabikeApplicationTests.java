package com.coder520.mamabike;

import com.coder520.mamabike.user.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MamabikeApplication.class,webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MamabikeApplicationTests {


	@Autowired
	private TestRestTemplate restTemplate;
	@LocalServerPort
	private int port;


	@Autowired
	@Qualifier("userServiceImpl")//有时候bean注入不成功时可尝试该注解
	private UserService userService;


	@Test
	public void contextLoads() {
		String result = restTemplate.getForObject("/user/testDB",String.class);
		System.out.println(result);
	}

//	@Test
//	public void test(){
//		Logger logger = LoggerFactory.getLogger(MamabikeApplicationTests.class);
//       try{
//       	userService.login(data, key);
//	   }catch (Exception e){
//       	logger.error("出错了",e);
//	   }
//	}

}
