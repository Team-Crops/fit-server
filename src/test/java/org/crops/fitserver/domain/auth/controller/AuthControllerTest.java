package org.crops.fitserver.domain.auth.controller;

import static com.epages.restdocs.apispec.ResourceDocumentation.parameterWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static com.epages.restdocs.apispec.Schema.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.handler;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import org.crops.fitserver.domain.auth.dto.request.SocialLoginRequest;
import org.crops.fitserver.domain.auth.facade.AuthFacade;
import org.crops.fitserver.domain.auth.dto.response.SocialLoginPageResponse;
import org.crops.fitserver.domain.auth.dto.response.TokenResponse;
import org.crops.fitserver.global.exception.ErrorCode;
import org.crops.fitserver.util.MockMvcDocs;
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
class AuthControllerTest extends MockMvcDocs {

  @MockBean
  AuthFacade authFacade;

  @Nested
  @DisplayName("[POST] OAuth Login 테스트")
  class OAuthLoginTest {

    private static final String URL = "http://localhost:8080/v1/auth/social/{socialPlatform}/login";
    private static final String AUTHORIZATION_CODE = "testAuthorizationCode";
    private static final String ACCESS_TOKEN = "accessToken example";
    private static final String REFRESH_TOKEN = "refreshToken example";

    @Nested
    @DisplayName("성공")
    class Success {

      @DisplayName("[200] 카카오, 구글 테스트 성공")
      @ParameterizedTest
      @EnumSource(SocialPlatform.class)
      void oAuthLogin(SocialPlatform socialPlatform) throws Exception {
        //given
        given(
            authFacade.socialLogin(anyString(), any(SocialPlatform.class)))
            .willReturn(TokenResponse.from(
                    TokenCollection.of(
                        ACCESS_TOKEN,
                        REFRESH_TOKEN)));
        SocialLoginRequest request = new SocialLoginRequest(AUTHORIZATION_CODE);

        //when
        var result = mockMvc.perform(
            post(URL, socialPlatform.getName())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        );

        //then
        result.andExpect(status().isOk())
            .andExpect(handler().handlerType(AuthController.class))
            .andExpect(jsonPath("$.accessToken").value(ACCESS_TOKEN))
            .andExpect(jsonPath("$.refreshToken").value(REFRESH_TOKEN))
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
                        .requestSchema(
                            schema("SocialLoginRequest"))
                        .requestFields(
                            fieldWithPath("code")
                                .type(JsonFieldType.STRING)
                                .description("OAuth Authorization Code"))
                        .responseSchema(
                            schema("TokenResponse"))
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
        //given
        SocialLoginRequest request = new SocialLoginRequest(AUTHORIZATION_CODE);

        //when
        var result = mockMvc.perform(
            post(URL, wrongSocialPlatformName)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        //then
        result.andExpect(status().isBadRequest())
            .andExpect(handler().handlerType(AuthController.class))
            .andExpect(jsonPath("$.code").value(ErrorCode.INVALID_INPUT_VALUE.getCode()))
            .andDo(
                document("OAuth Login Failed",
                    resource(ResourceSnippetParameters.builder()
                        .tag("Auth")
                        .summary("OAuth Login api")
                        .description("소셜 로그인 api")
                        .responseSchema(
                            schema("ErrorResponse"))
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
        SocialLoginRequest request = new SocialLoginRequest(null);

        //when
        var result = mockMvc.perform(
            post(URL, socialPlatform.getName())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        //then
        result.andExpect(status().isBadRequest())
            .andExpect(handler().handlerType(AuthController.class))
            .andExpect(jsonPath("$.code").value(ErrorCode.INVALID_INPUT_VALUE.getCode()))
            .andDo(
                document("OAuth Login Failed",
                    resource(ResourceSnippetParameters.builder()
                        .tag("Auth")
                        .summary("OAuth Login api")
                        .description("소셜 로그인 api")
                        .responseSchema(
                            schema("ErrorResponse"))
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
    private static final String LOGIN_PAGE_URL = "http://test-login-page-url.com";

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
            .willReturn(SocialLoginPageResponse.from(LOGIN_PAGE_URL));

        //when
        var result = mockMvc.perform(
                get(URL, socialPlatform.getName())
                    .contentType(MediaType.APPLICATION_JSON));

        //then
        result.andExpect(status().isOk())
            .andExpect(handler().handlerType(AuthController.class))
            .andExpect(jsonPath("$.loginPageUrl").value(LOGIN_PAGE_URL))
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
                            schema("SocialLoginPageResponse"))
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
            .andExpect(jsonPath("$.code").value(ErrorCode.INVALID_INPUT_VALUE.getCode()))
            .andDo(
                document("Get OAuth Login Page Failed",
                    resource(ResourceSnippetParameters.builder()
                        .tag("Auth")
                        .summary("Get OAuth Login Page api")
                        .description("소셜 로그인 페이지 조회 api")
                        .responseSchema(
                            schema("ErrorResponse"))
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