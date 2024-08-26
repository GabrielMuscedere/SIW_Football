package it.uniroma3.siw.config;

import jakarta.servlet.MultipartConfigElement;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.unit.DataSize;

@Configuration
public class MultipartConfig {

    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setMaxFileSize(DataSize.ofMegabytes(10)); // Imposta la dimensione massima del file a 10MB
        factory.setMaxRequestSize(DataSize.ofMegabytes(10)); // Imposta la dimensione massima della richiesta a 10MB
        return factory.createMultipartConfig();
    }
}
