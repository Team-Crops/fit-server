package org.crops.fitserver.domain.auth.service.provider;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.lenient;

import java.util.List;
import org.crops.fitserver.domain.auth.service.OAuthService;
import org.crops.fitserver.domain.auth.service.impl.GoogleOAuthServiceImpl;
import org.crops.fitserver.domain.auth.service.impl.KakaoOAuthServiceImpl;
import org.crops.fitserver.domain.user.domain.SocialPlatform;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("[Auth][Service] OAuthServiceProvider Test")
class OAuthServiceProviderTest {

  OAuthServiceProvider oAuthServiceProvider;

  @Mock
  KakaoOAuthServiceImpl kakaoOAuthService;

  @Mock
  GoogleOAuthServiceImpl googleOAuthService;

  @BeforeEach
  void setUp() {
    oAuthServiceProvider = new OAuthServiceProvider(
        List.of(kakaoOAuthService, googleOAuthService));
  }

  @Nested
  @DisplayName("Get OAuth Service 테스트")
  class getSocialServiceTest {

    @Nested
    @DisplayName("성공")
    class Success {

      @DisplayName("카카오 플랫폼 성공")
      @Test
      void getOAuthService() {
        // given
        SocialPlatform socialPlatform = SocialPlatform.KAKAO;
        lenient().when(kakaoOAuthService.support(SocialPlatform.KAKAO))
            .thenReturn(true);
        lenient().when(googleOAuthService.support(SocialPlatform.KAKAO))
            .thenReturn(false);
        // when
        OAuthService result = oAuthServiceProvider.getService(socialPlatform);

        // then
        assertThat(result)
            .isEqualTo(kakaoOAuthService);
      }

      @DisplayName("구글 플랫폼 성공")
      @Test
      void getGoogleService() {
        // given
        SocialPlatform socialPlatform = SocialPlatform.GOOGLE;
        lenient().when(kakaoOAuthService.support(SocialPlatform.GOOGLE))
            .thenReturn(false);
        lenient().when(googleOAuthService.support(SocialPlatform.GOOGLE))
            .thenReturn(true);
        // when
        OAuthService result = oAuthServiceProvider.getService(socialPlatform);

        // then
        assertThat(result)
            .isEqualTo(googleOAuthService);
      }
    }
  }
}