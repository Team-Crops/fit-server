package org.crops.fitserver.auth.controller;

import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.crops.fitserver.user.domain.SocialPlatform;
import org.crops.fitserver.auth.facade.AuthFacade;
import org.crops.fitserver.auth.facade.dto.TokenResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/auth")
public class AuthController {

	private final AuthFacade authFacade;

	@GetMapping("/social/{socialPlatform}")
	public ResponseEntity<TokenResponse> socialLogin(
			HttpServletRequest request,
			@RequestParam(name = "code") String code,
			@PathVariable(name = "socialPlatform") String socialPlatform
	) {
		TokenResponse tokenResponse = authFacade.socialLogin(
				request.getRequestURL().toString(),
				code,
				SocialPlatform.of(socialPlatform));
		return ResponseEntity.ok(tokenResponse);
	}

	@PostMapping("/login")
	public ResponseEntity login() {
		return ResponseEntity.ok(null);
	}

	@PostMapping("/signup")
	public ResponseEntity signup() {
		return ResponseEntity.ok(null);
	}
}
