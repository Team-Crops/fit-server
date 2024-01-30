package org.crops.fitserver.domain.auth.controller;

import static com.epages.restdocs.apispec.ResourceDocumentation.parameterWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.handler;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.Schema;
import org.crops.fitserver.domain.auth.facade.AuthFacade;
import org.crops.fitserver.domain.auth.facade.dto.SocialLoginPageResponse;
import org.crops.fitserver.domain.auth.facade.dto.TokenResponse;
import org.crops.fitserver.domain.common.MockMvcDocsTest;
import org.crops.fitserver.domain.user.domain.SocialPlatform;
import org.crops.fitserver.global.jwt.TokenCollection;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;

@WebMvcTest(AuthController.class)
@DisplayName("[Auth][Controller] AuthController Test")
class AuthControllerTest extends MockMvcDocsTest {

  @MockBean
  AuthFacade authFacade;

  @Nested
  @DisplayName("[GET] OAuth Login 테스트")
  class OAuthLoginTest {

    private static final String URL = "http://localhost:8080/v1/auth/social/{socialPlatform}/login";
    private static final String AUTHORIZATION_CODE = "testAuthorizationCode";
    private static final String REDIRECT_URI = "http://localhost:3000/login/callback";

    @Nested
    @DisplayName("성공")
    class Success {

      @DisplayName("[200] 카카오, 구글 테스트 성공")
      @ParameterizedTest
      @EnumSource(SocialPlatform.class)
      void kakaoOAuthLogin(SocialPlatform socialPlatform) throws Exception {
        //given
        given(
            authFacade.socialLogin(anyString(), anyString(), any(SocialPlatform.class)))
            .willReturn(
                TokenResponse.from(
                    TokenCollection.of(
                        "accessToken example",
                        "refreshToken example")));

        //when
        var result = mockMvc.perform(
            get(URL, socialPlatform.getName())
                .contentType(MediaType.APPLICATION_JSON)
                .queryParam("code", AUTHORIZATION_CODE)
                .queryParam("redirect_url", REDIRECT_URI));

        //then
        result.andExpect(status().isOk())
            .andExpect(handler().handlerType(AuthController.class))
            .andDo(
                document("OAuth Login Success",
                    resource(ResourceSnippetParameters.builder()
                        .tag("Auth")
                        .summary("OAuth Login api")
                        .description("소셜 로그인 api")
                        .pathParameters(
                            parameterWithName("socialPlatform")
                                .description("소셜 플랫폼 - { kakao, google }")
                                .defaultValue("kakao, google"))
                        .queryParameters(
                            parameterWithName("code")
                                .description("OAuth Authorization Code"),
                            parameterWithName("redirect_url")
                                .description("OAuth redirect url"))
                        .responseSchema(
                            Schema.schema("TokenResponse"))
                        .responseFields(
                            fieldWithPath("accessToken")
                                .type(JsonFieldType.STRING)
                                .description("User Access Token"),
                            fieldWithPath("refreshToken")
                                .type(JsonFieldType.STRING)
                                .description("Refresh Token"))
                        .build())));
      }
    }

    @Nested
    @DisplayName("실패")
    class Fail {

      @DisplayName("[400] 카카오, 구글 외의 올바르지 않은 소셜 플랫폼 요청 실패")
      @ParameterizedTest
      @ValueSource(strings = {"wrongSocialPlatformName", "wrongSocialPlatformName2"})
      void notSupportSocialPlatformType(String wrongSocialPlatformName) throws Exception {

        //when
        var result = mockMvc.perform(
                get(URL, wrongSocialPlatformName)
                    .contentType(MediaType.APPLICATION_JSON)
                    .queryParam("code", AUTHORIZATION_CODE)
                    .queryParam("redirect_uri", REDIRECT_URI));

        //then
        result.andExpect(status().isBadRequest())
            .andExpect(handler().handlerType(AuthController.class))
            .andDo(
                document("OAuth Login Failed",
                    resource(ResourceSnippetParameters.builder()
                        .tag("Auth")
                        .summary("OAuth Login api")
                        .description("소셜 로그인 api")
                        .responseSchema(
                            Schema.schema("ErrorResponse"))
                        .responseFields(
                            fieldWithPath("code")
                                .type(JsonFieldType.STRING)
                                .description("F-it Error Code"),
                            fieldWithPath("message")
                                .type(JsonFieldType.STRING)
                                .description("Error Message"))
                        .build())));
      }

      @Test
      @DisplayName("[400] Query Parameter의 code가 존재하지 않는 경우")
      void notExistCodeQueryParameter() throws Exception {
        //given
        SocialPlatform socialPlatform = SocialPlatform.KAKAO;

        //when
        var result = mockMvc.perform(
                get(URL, socialPlatform.getName())
                    .contentType(MediaType.APPLICATION_JSON)
                    .queryParam("redirect_uri", REDIRECT_URI));

        //then
        result.andExpect(status().isBadRequest())
            .andExpect(handler().handlerType(AuthController.class))
            .andDo(
                document("OAuth Login Failed",
                    resource(ResourceSnippetParameters.builder()
                        .tag("Auth")
                        .summary("OAuth Login api")
                        .description("소셜 로그인 api")
                        .responseSchema(
                            Schema.schema("ErrorResponse"))
                        .responseFields(
                            fieldWithPath("code")
                                .type(JsonFieldType.STRING)
                                .description("F-it Error Code"),
                            fieldWithPath("message")
                                .type(JsonFieldType.STRING)
                                .description("Error Message"))
                        .build())));
      }

      @Test
      @DisplayName("[400] Query Parameter의 redirect_uri가 존재하지 않는 경우")
      void notExistRedirectUriQueryParameter() throws Exception {
        //given
        SocialPlatform socialPlatform = SocialPlatform.KAKAO;

        //when
        var result = mockMvc.perform(
                get(URL, socialPlatform.getName())
                    .contentType(MediaType.APPLICATION_JSON)
                    .queryParam("code", AUTHORIZATION_CODE));

        //then
        result.andExpect(status().isBadRequest())
            .andExpect(handler().handlerType(AuthController.class))
            .andDo(
                document("OAuth Login Failed",
                    resource(ResourceSnippetParameters.builder()
                        .tag("Auth")
                        .summary("OAuth Login api")
                        .description("소셜 로그인 api")
                        .responseSchema(
                            Schema.schema("ErrorResponse"))
                        .responseFields(
                            fieldWithPath("code")
                                .type(JsonFieldType.STRING)
                                .description("F-it Error Code"),
                            fieldWithPath("message")
                                .type(JsonFieldType.STRING)
                                .description("Error Message"))
                        .build())));
      }
    }
  }

  @Nested
  @DisplayName("[GET] OAuth Login page 가져오기 테스트")
  class GetSocialLoginPageTest {

    private static final String URL = "http://localhost:8080/v1/auth/social/{socialPlatform}/login-page";

    @Nested
    @DisplayName("성공")
    class Success {

      @DisplayName("[200] 카카오, 구글 성공")
      @ParameterizedTest
      @EnumSource(SocialPlatform.class)
      void kakaoOAuthLogin(SocialPlatform socialPlatform) throws Exception {
        //given
        given(
            authFacade.getSocialLoginPageUrl(any(SocialPlatform.class)))
            .willReturn(
                SocialLoginPageResponse.from(
                    "http://test-login-page-url.com"));

        //when
        var result = mockMvc.perform(
                get(URL, socialPlatform.getName())
                    .contentType(MediaType.APPLICATION_JSON));

        //then
        result.andExpect(status().isOk())
            .andExpect(handler().handlerType(AuthController.class))
            .andDo(
                document("Get OAuth Login Page Success",
                    resource(ResourceSnippetParameters.builder()
                        .tag("Auth")
                        .summary("Get OAuth Login Page api")
                        .description("소셜 로그인 페이지 조회 api")
                        .pathParameters(
                            parameterWithName("socialPlatform")
                                .description("소셜 플랫폼 - { kakao, google }")
                                .defaultValue("kakao, google"))
                        .responseSchema(
                            Schema.schema("SocialLoginPageResponse"))
                        .responseFields(
                            fieldWithPath("loginPageUrl")
                                .type(JsonFieldType.STRING)
                                .description("Social Login Page Url"))
                        .build())));
      }
    }

    @Nested
    @DisplayName("실패")
    class FAIL {

      @DisplayName("[400] 카카오, 구글 외의 올바르지 않은 소셜 플랫폼 요청 실패")
      @ParameterizedTest
      @ValueSource(strings = {"wrongSocialPlatformName", "wrongSocialPlatformName2"})
      void notSupportSocialPlatformType(String wrongSocialPlatformName) throws Exception {
        //when
        var result = mockMvc.perform(
                get(URL, wrongSocialPlatformName)
                    .contentType(MediaType.APPLICATION_JSON));

        //then
        result.andExpect(status().isBadRequest())
            .andExpect(handler().handlerType(AuthController.class))
            .andDo(
                document("Get OAuth Login Page Failed",
                    resource(ResourceSnippetParameters.builder()
                        .tag("Auth")
                        .summary("Get OAuth Login Page api")
                        .description("소셜 로그인 페이지 조회 api")
                        .responseSchema(
                            Schema.schema("ErrorResponse"))
                        .responseFields(
                            fieldWithPath("code")
                                .type(JsonFieldType.STRING)
                                .description("F-it Error Code"),
                            fieldWithPath("message")
                                .type(JsonFieldType.STRING)
                                .description("Error Message"))
                        .build())));
      }
    }
  }
}