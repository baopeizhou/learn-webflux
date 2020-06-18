package org.bob.learn.service.impl;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class RedisMessageListener implements MessageListener {

    @Override
    public void onMessage(Message message, byte[] bytes) {
        BloomFilter<Integer> bloomFilter = BloomFilter.create(Funnels.integerFunnel(), 1000000, 0.001);
        log.info(new String(message.getBody()));
        log.info(new String(bytes));
    }
}
