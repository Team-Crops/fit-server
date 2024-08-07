package org.crops.fitserver.domain.auth.facade.impl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.assertj.core.api.Assertions.assertThat;

import org.crops.fitserver.domain.auth.dto.response.SocialLoginPageResponse;
import org.crops.fitserver.domain.auth.dto.response.TokenResponse;
import org.crops.fitserver.domain.auth.service.OAuthService;
import org.crops.fitserver.domain.auth.service.provider.OAuthServiceProvider;
import org.crops.fitserver.domain.user.domain.SocialPlatform;
import org.crops.fitserver.domain.user.domain.User;
import org.crops.fitserver.domain.user.domain.UserRole;
import org.crops.fitserver.global.jwt.JwtProvider;
import org.crops.fitserver.global.jwt.TokenCollection;
import org.crops.fitserver.global.jwt.TokenInfo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("[Auth][Facade] AuthService Test")
class AuthFacadeImplTest {

  @InjectMocks
  AuthFacadeImpl authFacadeImpl;

  @Mock
  OAuthServiceProvider oAuthServiceProvider;

  @Mock
  OAuthService oAuthService;

  @Mock
  JwtProvider jwtProvider;

  @Nested
  @DisplayName("OAuth Login 테스트")
  class SocialLoginTest {

    private static final String ORIGIN = "https://origin";
    private static final String AUTHORIZATION_CODE = "testAuthorizationCode";
    private User user = User.builder()
        .id(1L)
        .userRole(UserRole.NON_MEMBER)
        .build();
    private TokenCollection tokenCollection = TokenCollection.of(
        "test accessToken",
        "test refreshToken");


    @Nested
    @DisplayName("성공")
    class Success {

      @DisplayName("모든 소셜 플랫폼 성공")
      @ParameterizedTest
      @EnumSource(SocialPlatform.class)
      void socialLogin(SocialPlatform socialPlatform) {
        // given
        given(oAuthServiceProvider.getService(any(SocialPlatform.class)))
            .willReturn(oAuthService);
        given(oAuthService.socialUserLogin(anyString(), anyString()))
            .willReturn(user);
        given(jwtProvider.createTokenCollection(any(TokenInfo.class)))
            .willReturn(tokenCollection);

        // when
        var actual = authFacadeImpl.socialLogin(
            ORIGIN,
            socialPlatform,
            AUTHORIZATION_CODE);

        // then
        var expected = TokenResponse.from(tokenCollection);
        assertThat(actual)
            .isEqualTo(expected);
      }
    }
  }

  @Nested
  @DisplayName("OAuth Login Page 조회 테스트")
  class GetSocialLoginPageTest {

    private static final String ORIGIN = "https://origin";
    private static final String SOCIAL_LOGIN_PAGE = "test_kakao_login_page_url";

    @Nested
    @DisplayName("성공")
    class Success {

      @DisplayName("모든 소셜 플랫폼 성공")
      @ParameterizedTest
      @EnumSource(SocialPlatform.class)
      void getKakaoSocialLoginPage(SocialPlatform socialPlatform) {
        // given
        given(oAuthServiceProvider.getService(any(SocialPlatform.class)))
            .willReturn(oAuthService);
        given(oAuthService.getLoginPageUrl(anyString()))
            .willReturn(SOCIAL_LOGIN_PAGE);

        // when
        var actual = authFacadeImpl.getSocialLoginPageUrl(ORIGIN, socialPlatform);

        // then
        var expected = SocialLoginPageResponse.from(SOCIAL_LOGIN_PAGE);
        assertThat(actual)
            .isEqualTo(expected);
      }
    }
  }
}