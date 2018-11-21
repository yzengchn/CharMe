package com.yzeng.charme;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import com.yzeng.charme.server.SocketServer;

@SpringBootApplication
public class CharMeApplication {

	public static void main(String[] args) {
				SpringApplication.run(CharMeApplication.class, args);
	}
}
