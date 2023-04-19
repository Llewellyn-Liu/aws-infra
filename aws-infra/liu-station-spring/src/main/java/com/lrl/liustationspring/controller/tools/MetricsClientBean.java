package com.lrl.liustationspring.controller.tools;


import com.timgroup.statsd.NoOpStatsDClient;
import com.timgroup.statsd.NonBlockingStatsDClient;
import com.timgroup.statsd.StatsDClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MetricsClientBean {

    private boolean publishMetrics=true;

    private String metricsServerHost="localhost";

    private int metricsServerPort=8125;

    @Bean
    public StatsDClient metricsClient(){
        if(publishMetrics){
            return new NonBlockingStatsDClient("csye6225", metricsServerHost, metricsServerPort);
        }

        return new NoOpStatsDClient();
    }

}
