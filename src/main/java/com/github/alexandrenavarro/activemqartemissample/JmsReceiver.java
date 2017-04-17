package com.github.alexandrenavarro.activemqartemissample;

/**
 * Created by anavarro on 17/04/17.
 */
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;

import org.apache.activemq.artemis.jms.client.ActiveMQDestination;
import org.apache.activemq.artemis.jms.client.ActiveMQJMSConnectionFactory;
import org.apache.activemq.artemis.rest.Jms;

public class JmsReceiver {

    public static void main(String[] args) throws Exception {
        System.out.println("Receive Setup...");
        ConnectionFactory factory = new ActiveMQJMSConnectionFactory("tcp://localhost:61616");
        Destination destination = ActiveMQDestination.fromPrefixedName("queue://orders");

        try (Connection conn = factory.createConnection()) {
            Session session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
            MessageConsumer consumer = session.createConsumer(destination);
            consumer.setMessageListener(new MessageListener() {
                @Override
                public void onMessage(Message message) {
                    System.out.println("Received Message: ");
                    Order order = Jms.getEntity(message, Order.class);
                    System.out.println(order);
                }
            });
            conn.start();
            Thread.sleep(1000000);
        }
    }
}