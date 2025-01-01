package com.example.apigateway.Configuration;

import com.example.apigateway.Clients.Client;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Method;

@Configuration
public class GrpcClientInitializer implements BeanPostProcessor {

    private String defaultHost;

    private int defaultPort;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) {
        Class<?> clazz = bean.getClass();

        if (clazz.isAnnotationPresent(Client.class)) {
            Client annotation = clazz.getAnnotation(Client.class);
            String hostProperty = annotation.host();
            String portProperty = annotation.port();

            try {
                String host = System.getProperty(hostProperty, defaultHost);
                int port = Integer.parseInt(System.getProperty(portProperty, String.valueOf(defaultPort)));

                ManagedChannel channel = ManagedChannelBuilder.forAddress(host, port)
                        .usePlaintext()
                        .build();

                Method initMethod = clazz.getDeclaredMethod("init", ManagedChannel.class);
                initMethod.invoke(null, channel);

            } catch (Exception e) {
                throw new RuntimeException("Failed to initialize gRPC client for " + clazz.getSimpleName(), e);
            }
        }

        return bean;
    }
}
