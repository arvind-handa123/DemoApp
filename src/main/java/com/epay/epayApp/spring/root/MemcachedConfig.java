/**
 * 
 */
package com.epay.epayApp.spring.root;

import net.spy.memcached.ConnectionFactoryBuilder;
import net.spy.memcached.MemcachedClient;
import net.spy.memcached.spring.MemcachedClientFactoryBean;
import net.spy.memcached.transcoders.SerializingTranscoder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

/**
 * 
 * @author Asad Ali
 *
 */
@Configuration("memCachedConfiguration")
@PropertySource(value = {"classpath:/memcached.properties"})
public class MemcachedConfig {

    @Autowired
    Environment environment;

    @Bean(name = "memCachedClient")
    public MemcachedClient memcachedClient() throws Exception {
        MemcachedClientFactoryBean memcachedClientFactoryBean = new MemcachedClientFactoryBean();
        memcachedClientFactoryBean.setServers(environment.getProperty("MEMCACHED_SERVERS"));
        memcachedClientFactoryBean.setProtocol(ConnectionFactoryBuilder.Protocol.BINARY);
        memcachedClientFactoryBean.setOpTimeout(3000);
        memcachedClientFactoryBean.setUseNagleAlgorithm(false);
        memcachedClientFactoryBean.setTimeoutExceptionThreshold(1998);
        memcachedClientFactoryBean.setLocatorType(ConnectionFactoryBuilder.Locator.CONSISTENT);
        SerializingTranscoder serializingTranscoder = new SerializingTranscoder();
        serializingTranscoder.setCompressionThreshold(500000);
        memcachedClientFactoryBean.setTranscoder(serializingTranscoder);
        return (MemcachedClient) memcachedClientFactoryBean.getObject();
    }
}
