package org.crops.fitserver.config;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import org.crops.fitserver.domain.user.domain.User;
import org.crops.fitserver.domain.user.domain.UserRole;
import org.crops.fitserver.global.security.PrincipalDetails;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.operation.preprocess.Preprocessors;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

public abstract class MockMvcDocsWithLogin extends MockMvcDocs {

  protected User loginUser = User.builder().id(1L).userRole(UserRole.MEMBER).build();
  protected PrincipalDetails loginPrincipal = PrincipalDetails.from(loginUser);

  @Override
  @BeforeEach
  protected void setUp(RestDocumentationContextProvider restDocumentation) {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
        .apply(springSecurity())
        .apply(
            documentationConfiguration(restDocumentation)
                .operationPreprocessors()
                .withResponseDefaults(Preprocessors.prettyPrint())
        )
        .alwaysDo(print())
        .build();
  }

}
