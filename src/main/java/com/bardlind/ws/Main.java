package com.bardlind.ws;

import org.slf4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Startup of Netty Websocket input/output forwarding
 *
 */
public class Main {
    private static final Logger log = getLogger(Main.class);
    public static void main(String[] args) {
        log.info("Loading Context");
        ClassPathXmlApplicationContext springContext = new ClassPathXmlApplicationContext("spring-context.xml");

        log.debug("Hello World!");
    }
}
