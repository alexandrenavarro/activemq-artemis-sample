package com.github.alexandrenavarro.activemqartemissample;

import org.apache.activemq.artemis.jms.client.ActiveMQQueue;
import org.apache.activemq.artemis.rest.integration.RestMessagingBootstrapListener;
import org.jboss.resteasy.plugins.server.servlet.FilterDispatcher;
import org.jboss.resteasy.plugins.server.servlet.ResteasyBootstrap;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.annotation.EnableJms;

import javax.jms.Queue;


@SpringBootApplication
@EnableJms
//@EnableWebMvc
public class ActivemqArtemisSampleApplication  {

	@Bean
	public Queue queue() {
		return new ActiveMQQueue("sample.queue");
	}
//
//	@Bean
//	public ServletContextListener listener() {
//		return new ResteasyBootstrap();
//	}
//
//	@Bean
//	public ServletContextListener listener2() {
//		return new RestMessagingBootstrapListener();
//	}
//
//	@Bean
//	public Filter filter() {
//		return new FilterDispatcher();
//	}
//
//	@Bean
//	public FilterRegistrationBean someFilterRegistration() {
//		FilterRegistrationBean registration = new FilterRegistrationBean();
//		registration.setFilter(filter());
//		registration.addUrlPatterns("/*");
//		registration.setName("Rest-Messaging");
//		registration.setOrder(1);
//		return registration;
//	}


	public static void main(String[] args) {
		SpringApplication.run(ActivemqArtemisSampleApplication.class, args);
	}
}
