package org.crops.fitserver.global.security;

import lombok.RequiredArgsConstructor;
import org.crops.fitserver.global.exception.UnauthorizedException;
import org.crops.fitserver.user.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {

  private final UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return PrincipalDetails.from(
        userRepository.findById(Long.parseLong(username))
            .orElseThrow(() -> {
              throw new UsernameNotFoundException("해당 유저를 찾을 수 없습니다.");
            }));
  }
}
