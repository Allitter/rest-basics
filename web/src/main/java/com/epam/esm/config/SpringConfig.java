package com.epam.esm.config;

import com.epam.esm.serializer.LocalDateDeserializer;
import com.epam.esm.serializer.LocalDateSerializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.LocalDate;
import java.util.List;

@EnableWebMvc
@Configuration
@ComponentScan(basePackages = "com.epam.esm")
public class SpringConfig implements WebMvcConfigurer {
    private static final String EXCEPTION_MESSAGE_BUNDLE = "exception.message";
    private static final String DEFAULT_ENCODING = "UTF-8";

    @Bean
    public MessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename(EXCEPTION_MESSAGE_BUNDLE);
        messageSource.setUseCodeAsDefaultMessage(true);
        messageSource.setDefaultEncoding(DEFAULT_ENCODING);
        return messageSource;
    }

    @Bean
    public GsonHttpMessageConverter gsonHttpMessageConverter() {
        GsonHttpMessageConverter gsonConverter = new GsonHttpMessageConverter();
        gsonConverter.setGson(gson());

        return gsonConverter;
    }

    @Bean
    public Gson gson() {
        return new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateSerializer())
                .registerTypeAdapter(LocalDate.class, new LocalDateDeserializer())
                .create();
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.clear();
        converters.add(gsonHttpMessageConverter());
    }
}
