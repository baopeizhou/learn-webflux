package org.bob.learn.config;

import io.lettuce.core.ReadFrom;
import io.lettuce.core.cluster.ClusterClientOptions;
import io.lettuce.core.cluster.ClusterTopologyRefreshOptions;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.data.redis.LettuceClientConfigurationBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

import java.time.Duration;

/**
 * Redis Config
 */
@Configuration
@AutoConfigureBefore(LettuceConnectionFactory.class)
public class RedisConfig implements LettuceClientConfigurationBuilderCustomizer {

    /**
     * 个性化配置Redis
     * @param clientConfigurationBuilder
     */
    @Override
    public void customize(LettuceClientConfiguration.LettuceClientConfigurationBuilder clientConfigurationBuilder) {
        clientConfigurationBuilder.readFrom(ReadFrom.MASTER_PREFERRED);
        ClusterTopologyRefreshOptions topologyRefreshOptions = ClusterTopologyRefreshOptions.builder()
                .enablePeriodicRefresh(Duration.ofMinutes(10))
                .enableAllAdaptiveRefreshTriggers()
                .build();
        clientConfigurationBuilder.clientOptions(ClusterClientOptions.builder()
                .topologyRefreshOptions(topologyRefreshOptions)
                .build());
    }


    @Bean
    RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory,
                                            MessageListener redisMessageListener) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        //订阅了一个叫chanel01的通道，这个container可以添加多个messageListener
        container.addMessageListener(new MessageListenerAdapter(redisMessageListener)  , new PatternTopic("__sentinel__:hello"));
        return container;
    }
}
