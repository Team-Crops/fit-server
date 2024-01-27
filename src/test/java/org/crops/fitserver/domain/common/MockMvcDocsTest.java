package org.crops.fitserver.domain.common;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.operation.preprocess.Preprocessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@WebMvcTest
@AutoConfigureRestDocs
@ExtendWith(RestDocumentationExtension.class)
public abstract class MockMvcDocsTest {

  protected MockMvc mockMvc;

  @Autowired
  protected WebApplicationContext context;

  @Autowired
  protected ObjectMapper objectMapper;

  @BeforeEach
  public void setUp(RestDocumentationContextProvider restDocumentation) {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
        .apply(
            documentationConfiguration(restDocumentation)
                .operationPreprocessors()
                .withResponseDefaults(Preprocessors.prettyPrint())
        )
        .alwaysDo(print())
        .build();
  }
}