package com.github.alexandrenavarro.activemqartemissample;

import org.apache.activemq.artemis.jms.client.ActiveMQQueue;
import org.apache.activemq.artemis.jms.server.embedded.EmbeddedJMS;
import org.apache.activemq.artemis.rest.integration.ActiveMQBootstrapListener;
import org.apache.activemq.artemis.rest.integration.RestMessagingBootstrapListener;
import org.jboss.resteasy.plugins.server.servlet.FilterDispatcher;
import org.jboss.resteasy.plugins.server.servlet.ResteasyBootstrap;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.jms.Queue;
import javax.servlet.Filter;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletException;


@SpringBootApplication
@EnableJms
@EnableWebMvc
public class ActivemqArtemisSampleApplication  {

	//TODO
	// Not use xml files, use embedded configuration
	// Configure security

	@Bean
	public Queue queue() {
		return new ActiveMQQueue("sample.queue");
	}

	@Bean
	public ServletContextListener listener() {
		return new ResteasyBootstrap();
	}

	@Bean
	public ServletContextListener listener2(EmbeddedJMS embeddedJMS) {
		return new ActiveMQBootstrapListener();
	}

	@Bean
	public ServletContextInitializer initializer() {
		return new ServletContextInitializer() {

			@Override
			public void onStartup(ServletContext servletContext) throws ServletException {
				servletContext.setInitParameter("rest.messaging.config.file", "rest.xml");
			}
		};
	}

	@Bean
	public ServletContextListener listener3() {
		return new RestMessagingBootstrapListener();
	}


	@Bean
	public Filter filterDispatcher() {
		return new FilterDispatcher();
	}

	@Bean
	public FilterRegistrationBean someFilterRegistration() {
		final FilterRegistrationBean registration = new FilterRegistrationBean();
		registration.setFilter(filterDispatcher());
		registration.addUrlPatterns("/*");
		registration.setName("Rest-Messaging");
		registration.setOrder(1);
		return registration;
	}



	public static void main(String[] args) {
		SpringApplication.run(ActivemqArtemisSampleApplication.class, args);
	}
}
