package com.shopping.config;

import com.oracle.bmc.auth.InstancePrincipalsAuthenticationDetailsProvider;
import com.oracle.bmc.objectstorage.ObjectStorageClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.oracle.bmc.Region;

@Configuration
public class ObjectStorageConfig {

    @Bean
    public ObjectStorageClient objectStorageClient() {

        InstancePrincipalsAuthenticationDetailsProvider provider =
                InstancePrincipalsAuthenticationDetailsProvider.builder()
                        .build();

        return ObjectStorageClient.builder()
                .region(Region.AP_CHUNCHEON_1)
                .build(provider);
    }
}