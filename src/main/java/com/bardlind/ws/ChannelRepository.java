package com.bardlind.ws;

import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.slf4j.Logger;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Created by baardl on 09.10.15.
 */
@Repository
public class ChannelRepository {
    private static final Logger log = getLogger(ChannelRepository.class);

    private List<Channel> attachedChannels = new ArrayList<Channel>();

    public void addChannel(Channel channel) {
        attachedChannels.add(channel);
    }

    public void removeChannel(Channel channel) {
        attachedChannels.remove(channel);
    }

    public void publishToAll(String message) {
        for (Channel attachedChannel : attachedChannels) {
            attachedChannel.writeAndFlush(new TextWebSocketFrame(message));
        }
    }

    public List<Channel> getAttachedChannels() {
        if (attachedChannels == null) {
            attachedChannels = new ArrayList<Channel>();
        }
        return attachedChannels;
    }

    @PostConstruct
    public void start() throws Exception {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                do {
                    long countChannels = attachedChannels.size();
                    log.info("Publish to all. Size {}. Instance Hash {}" ,countChannels,this.hashCode());
                    publishToAll("Processor is saying Hello. " + Instant.now());
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } while (true);

            }
        };
        Thread t = new Thread(r);
        t.start();

    }

}
