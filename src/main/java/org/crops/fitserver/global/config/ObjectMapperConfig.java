package org.crops.fitserver.global.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

      final SimpleModule localDateTimeModule = new SimpleModule();
      localDateTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(
          DateTimeFormatter.ISO_DATE_TIME));
      localDateTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(
          DateTimeFormatter.ISO_DATE_TIME));
      builder.modules(jsonNullableModule(), localDateTimeModule);
    };
  }

  @Bean
  public JsonNullableModule jsonNullableModule() {
    return new JsonNullableModule();
  }
}
