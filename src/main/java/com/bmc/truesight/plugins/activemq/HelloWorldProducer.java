package com.bmc.truesight.plugins.activemq;

import org.apache.activemq.ActiveMQConnectionFactory;
import javax.jms.*;

public class HelloWorldProducer implements Runnable {

    private static int DEFAULT_NUMBER_OF_MESSAGES = 1;
    private static String DEFAULT_BROKER_URL = "tcp://localhost:61616";

    private String brokerURL;
    private int numberOfMessages;

    public HelloWorldProducer() {
        this(DEFAULT_BROKER_URL, DEFAULT_NUMBER_OF_MESSAGES);
    }

    public HelloWorldProducer(String brokerURL, int numberOfMessages) {
        this.brokerURL = brokerURL;
        this.numberOfMessages = numberOfMessages;
    }
    public void run() {
        try {
            // Create a ConnectionFactory
            ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(this.brokerURL);
            // Create a Connection
            Connection connection = connectionFactory.createConnection();
            connection.start();
            // Create a Session
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            // Create the destination (Topic or Queue)
            Destination destination = session.createQueue("TEST.FOO");
            // Create a MessageProducer from the Session to the Topic or Queue
            MessageProducer producer = session.createProducer(destination);
            producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
            // Create a messages
            String text = "Hello world! From: " + Thread.currentThread().getName() + " : " + this.hashCode();
            TextMessage message = session.createTextMessage(text);
            for (int i = 1; i <= this.numberOfMessages; i++) {
                producer.send(message);
            }
            // Clean up
            session.close();
            connection.close();
        }
        catch (Exception e) {
            System.out.println("Caught: " + e);
            e.printStackTrace();
        }
    }
}