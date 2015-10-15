package com.bardlind.ws;


import com.bardlind.ws.config.SpringBootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.net.InetSocketAddress;

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
public final class WebSocketServer {
    private static final Logger log = getLogger(WebSocketServer.class);

    static final boolean SSL = System.getProperty("ssl") != null;

    private final SpringBootstrap serverBootstrap;
    private InetSocketAddress tcpSocketAddress;
    private boolean ssl;

    private ChannelFuture serverChannelFuture;
    private boolean serverStarted = false;

    @Autowired
    public WebSocketServer(SpringBootstrap serverBootstrap, InetSocketAddress tcpSocketAddress, @Value("${ssl}") boolean ssl) {
        this.serverBootstrap = serverBootstrap;
        this.tcpSocketAddress = tcpSocketAddress;
        this.ssl = ssl;
    }

    @PostConstruct
    public void start() throws Exception {
        log.info("Starting server at " + tcpSocketAddress);
        System.out.println("Starting server at " + tcpSocketAddress);
        ServerBootstrap b = serverBootstrap.bootstrap();
        serverChannelFuture = b.bind(tcpSocketAddress).sync();
        log.info("Open your web browser and navigate to " +
                (ssl ? "https" : "http") + "://127.0.0.1:" + tcpSocketAddress + '/');
        serverStarted=true;
    }

    @PreDestroy
    public void stop() throws Exception {
        serverChannelFuture.channel().closeFuture().sync();
    }

    public ServerBootstrap getB() {
        return serverBootstrap.bootstrap();
    }

//    public void setB(ServerBootstrap b) {
//        this.b = b;
//    }


    public InetSocketAddress getTcpSocketAddress() {
        return tcpSocketAddress;
    }

    public void setTcpSocketAddress(InetSocketAddress tcpSocketAddress) {
        this.tcpSocketAddress = tcpSocketAddress;
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