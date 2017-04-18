package com.github.alexandrenavarro.activemqartemissample;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;

import org.apache.activemq.artemis.jms.client.ActiveMQDestination;
import org.apache.activemq.artemis.jms.client.ActiveMQJMSConnectionFactory;

public class JmsSender {

    public static void main(String[] args) throws Exception {
        ConnectionFactory factory = new ActiveMQJMSConnectionFactory("tcp://localhost:61616");
        Destination destination = ActiveMQDestination.fromPrefixedName("topic://orders2");

        try (Connection conn = factory.createConnection()) {
            Session session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
            MessageProducer producer = session.createProducer(destination);
            ObjectMessage message = session.createObjectMessage();

            Order order = new Order("Bill", "$199.99", "iPhone4");
            message.setObject(order);
            producer.send(message);
        }
    }
}