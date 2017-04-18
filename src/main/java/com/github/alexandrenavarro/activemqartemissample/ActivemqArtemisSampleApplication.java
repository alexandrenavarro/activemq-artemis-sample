package com.github.alexandrenavarro.activemqartemissample;

import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.artemis.api.core.TransportConfiguration;
import org.apache.activemq.artemis.core.config.Configuration;
import org.apache.activemq.artemis.core.config.impl.ConfigurationImpl;
import org.apache.activemq.artemis.core.remoting.impl.invm.InVMAcceptorFactory;
import org.apache.activemq.artemis.core.remoting.impl.netty.NettyAcceptorFactory;
import org.apache.activemq.artemis.jms.client.ActiveMQQueue;
import org.apache.activemq.artemis.jms.server.config.JMSConfiguration;
import org.apache.activemq.artemis.jms.server.config.impl.JMSConfigurationImpl;
import org.apache.activemq.artemis.jms.server.embedded.EmbeddedJMS;
import org.apache.activemq.artemis.rest.integration.ActiveMQBootstrapListener;
import org.apache.activemq.artemis.rest.integration.RestMessagingBootstrapListener;
import org.apache.activemq.artemis.rest.integration.ServletContextBindingRegistry;
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
import javax.servlet.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;


@SpringBootApplication
@EnableJms
@EnableWebMvc
@Slf4j
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
	public EmbeddedJMS artemisServer() {
		EmbeddedJMS server = new EmbeddedJMS();
		final Configuration configuration = new ConfigurationImpl();
		configuration.setSecurityEnabled(false);
		configuration.setPersistenceEnabled(false);
		Map<String, Object> parameters = new HashMap();
		parameters.put("serverId", Integer.valueOf(0));
		TransportConfiguration transportConfiguration = new TransportConfiguration(InVMAcceptorFactory.class.getName(), parameters);
		Map<String, Object> parameters2 = new HashMap();
		parameters2.put("serverId", Integer.valueOf(0));
		TransportConfiguration transportConfiguration2 = new TransportConfiguration(NettyAcceptorFactory.class.getName());
		configuration.getAcceptorConfigurations().add(transportConfiguration);
		configuration.getAcceptorConfigurations().add(transportConfiguration2);
		//<acceptor name="in-vm">vm://0</acceptor>
		server.setConfiguration(configuration);
		server.setJmsConfiguration(new JMSConfigurationImpl());
		return server;
	}

	@Bean
	public ServletContextListener listener2(EmbeddedJMS embeddedJMS) {
//		return new ActiveMQBootstrapListener();

//		final EmbeddedJMS embeddedJMS1 = new EmbeddedJMS();
//		final Configuration configuration = new ConfigurationImpl();
//
//		embeddedJMS1.setConfiguration(new ConfigurationImpl());
//		try {
//
//			embeddedJMS1.start();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		return new ServletContextListener() {


			@Override
			public void contextInitialized(ServletContextEvent servletContextEvent) {
				try {
				ServletContext context = servletContextEvent.getServletContext();

					//embeddedJMS.stop();
					embeddedJMS.setRegistry(new ServletContextBindingRegistry(context));
					embeddedJMS.start();
				} catch (Exception e) {
					e.printStackTrace();
				}


			}

			@Override
			public void contextDestroyed(ServletContextEvent servletContextEvent) {

			}
		};

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
