package ru.melodrom.amor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.data.redis.config.ConfigureRedisAction;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.session.data.redis.config.annotation.web.http.RedisHttpSessionConfiguration;

@EnableRedisHttpSession
@Configuration
public class SessionConfig extends RedisHttpSessionConfiguration {

    final static Logger LOG = LoggerFactory.getLogger(SessionConfig.class);

    @Bean
    public static ConfigureRedisAction configureRedisAction() {
        // fix for "ERR unknown command 'CONFIG' when using Secured Redis" on AWS ElastiCache
        // https://github.com/spring-projects/spring-session/issues/124
        LOG.debug("Set ConfigureRedisAction.NO_OP");
        return ConfigureRedisAction.NO_OP;
    }

}
