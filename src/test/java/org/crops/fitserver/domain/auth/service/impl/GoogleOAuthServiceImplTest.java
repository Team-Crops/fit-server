package org.crops.fitserver.domain.auth.service.impl;

import static org.junit.jupiter.api.Assertions.*;

import org.crops.fitserver.domain.user.repository.SocialUserInfoRepository;
import org.crops.fitserver.domain.user.repository.UserRepository;
import org.crops.fitserver.global.feign.oauth.google.GoogleAuthServerClient;
import org.crops.fitserver.global.feign.oauth.google.GoogleClientProperty;
import org.crops.fitserver.global.feign.oauth.google.GoogleServerClient;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("[Auth][Service] GoogleOAuthServiceImpl Test")
class GoogleOAuthServiceImplTest {

  @InjectMocks
  GoogleOAuthServiceImpl googleOAuthServiceImpl;

  @Mock
  GoogleAuthServerClient googleAuthServerClient;

  @Mock
  GoogleServerClient googleServerClient;

  @Mock
  GoogleClientProperty googleClientProperty;

  @Mock
  SocialUserInfoRepository socialUserInfoRepository;

  @Mock
  UserRepository userRepository;

  @Nested
  @DisplayName("구글 플랫폼 지원 테스트")
  class SupportTest {

    @Nested
    @DisplayName("성공")
    class Success {

    }

    @Nested
    @DisplayName("실패")
    class Fail {

    }
  }
}