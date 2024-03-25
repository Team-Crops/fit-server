package org.crops.fitserver.global.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.openapitools.jackson.nullable.JsonNullableModule;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class ObjectMapperConfig {

  @Bean
  @Primary
  public Jackson2ObjectMapperBuilderCustomizer jsonCustomizer() {
    return builder -> {
      builder.serializationInclusion(JsonInclude.Include.ALWAYS);
      builder.featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
      builder.featuresToDisable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
      builder.featuresToDisable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

      builder.modules(jsonNullableModule(), javaTimeModule());
    };
  }

  @Bean
  public JsonNullableModule jsonNullableModule() {
    return new JsonNullableModule();
  }

  @Bean
  public JavaTimeModule javaTimeModule(){
    return new JavaTimeModule();
  }
}
