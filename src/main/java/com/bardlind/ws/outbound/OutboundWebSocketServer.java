package com.bardlind.ws.outbound;


import com.bardlind.ws.config.SpringBootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import static org.slf4j.LoggerFactory.getLogger;


/**
 * A HTTP server which serves Web Socket requests at:
 *
 * http://localhost:8080/websocket
 *
 * Open your browser at http://localhost:8080/, then the demo page will be loaded and a Web Socket connection will be
 * made automatically.
 *
 * This server illustrates support for the different web socket specification versions and will work with:
 *
 * <ul>
 * <li>Safari 5+ (draft-ietf-hybi-thewebsocketprotocol-00)
 * <li>Chrome 6-13 (draft-ietf-hybi-thewebsocketprotocol-00)
 * <li>Chrome 14+ (draft-ietf-hybi-thewebsocketprotocol-10)
 * <li>Chrome 16+ (RFC 6455 aka draft-ietf-hybi-thewebsocketprotocol-17)
 * <li>Firefox 7+ (draft-ietf-hybi-thewebsocketprotocol-10)
 * <li>Firefox 11+ (RFC 6455 aka draft-ietf-hybi-thewebsocketprotocol-17)
 * </ul>
 */
@Service
public final class OutboundWebSocketServer {
    private static final Logger log = getLogger(OutboundWebSocketServer.class);

    static final boolean SSL = System.getProperty("ssl") != null;

    private final SpringBootstrap serverBootstrap;
    private int httpPort;
    private boolean ssl;

    private ChannelFuture serverChannelFuture;
    private boolean serverStarted = false;

    @Autowired
    public OutboundWebSocketServer(SpringBootstrap serverBootstrap, @Value("${http.outbound.port}") int httpPort, @Value("${ssl}") boolean ssl) {
        this.serverBootstrap = serverBootstrap;
        this.httpPort = httpPort;
        this.ssl = ssl;
    }

    @PostConstruct
    public void start() throws Exception {
        log.info("Starting server at " + httpPort);
        System.out.println("Starting server at " + httpPort);
        ServerBootstrap b = serverBootstrap.outputBootstrap();
        serverChannelFuture = b.bind(httpPort).sync();
        log.info("Open your web browser and navigate to " +
                (ssl ? "https" : "http") + "://127.0.0.1:" + httpPort + '/');
        serverStarted=true;
    }

    @PreDestroy
    public void stop() throws Exception {
        serverChannelFuture.channel().closeFuture().sync();
    }

    public ServerBootstrap getB() {
        return serverBootstrap.outputBootstrap();
    }

//    public void setB(ServerBootstrap b) {
//        this.b = b;
//    }


    public int gethttpPort() {
        return httpPort;
    }

    public void sethttpPort(int httpPort) {
        this.httpPort = httpPort;
    }

    public boolean isSsl() {
        return ssl;
    }

    public void setSsl(boolean ssl) {
        this.ssl = ssl;
    }

    public boolean isServerStarted() {
        return serverStarted;
    }
}