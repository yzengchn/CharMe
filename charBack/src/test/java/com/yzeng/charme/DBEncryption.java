package com.yzeng.charme;

import org.jasypt.encryption.StringEncryptor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import junit.framework.Assert;

@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
public class DBEncryption {

	@Autowired
	StringEncryptor encryptor;
	
	@Test
	public void getPassword() {
		String name = encryptor.encrypt("root");
		String pwd = encryptor.encrypt("root");
		System.out.println(name);
		System.out.println(pwd);
	}
	
}
