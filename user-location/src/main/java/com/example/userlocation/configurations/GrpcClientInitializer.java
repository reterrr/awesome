package com.example.userlocation.configurations;

import com.example.userlocation.Client;
import com.example.userlocation.clients.LocationClient;
import io.grpc.Channel;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.reflections.Reflections;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.lang.reflect.Method;
import java.util.Set;


@Configuration
public class GrpcClientInitializer {

    private final Environment environment;

    public GrpcClientInitializer(Environment environment) {
        this.environment = environment;
        initializeClients();
    }

    private void initializeClients() {

        Reflections reflections = new Reflections(
                new ConfigurationBuilder()
                        .setUrls(ClasspathHelper
                                .forPackage(LocationClient.class.getPackageName())
                        )
        );

        // Log all scanned classes

        Set<Class<?>> allClasses = reflections.getTypesAnnotatedWith(Client.class);

        for (Class<?> clientClass : allClasses) {
            Client clientAnnotation = clientClass.getAnnotation(Client.class);

            String host = environment.resolvePlaceholders(clientAnnotation.host());
            int port = Integer.parseInt(environment.resolvePlaceholders(clientAnnotation.port()));


            // Create a ManagedChannel
            ManagedChannel channel = ManagedChannelBuilder.forAddress(host, port)
                    .usePlaintext()
                    .build();

            try {
                // Reflect the init method and invoke it
                Method initMethod = clientClass.getDeclaredMethod("init", Channel.class);
                initMethod.invoke(null, channel); // Call the static init method
                System.out.printf("Initialized client: %s with host: %s and port: %d%n", clientClass.getName(), host, port);
            } catch (Exception e) {
                throw new RuntimeException("Failed to initialize client: " + clientClass.getName(), e);
            }
        }
    }
}

