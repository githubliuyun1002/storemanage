package com.xiabuxiabu.storemanage.alrmtask;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * 表明该类是个配置类
 */
@Configuration
@EnableAsync
public class AsyncConfig {
    @Value("${spring.task.execution.pool.core-size}")
    private int corePoolSize;

    @Value("${spring.task.execution.pool.max-size}")
    private int maxPoolSize;

    @Value("${spring.task.execution.pool.queue-capacity}")
    private int queueCapacity;

    @Value("${spring.task.execution.pool.keep-alive}")
    private int keepAliveSeconds;

    @Bean
    public Executor taskExecutor() {

        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        //线程池维护线程的最少数量
        executor.setCorePoolSize(corePoolSize);
        //线程池维护线程的最大数量
        executor.setMaxPoolSize(maxPoolSize);
        //缓存队列
        executor.setQueueCapacity(queueCapacity);
        //允许的空闲时间
        executor.setKeepAliveSeconds(keepAliveSeconds);
        executor.initialize();
        return executor;

    }

}
