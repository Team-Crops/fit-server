package org.crops.fitserver.auth.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.crops.fitserver.auth.facade.AuthFacade;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/auth")
public class AuthController {

	private final AuthFacade authFacade;

	@GetMapping("/social")
	public ResponseEntity socialLogin(
			HttpServletRequest request,
			@RequestParam(name = "code") String code,
			@RequestParam(name = "socialPlatform") SocialPlatform socialPlatform
	) {
		authFacade.socialLogin(
				request.getRequestURL().toString(),
				code,
				socialPlatform);
		return ResponseEntity.ok(null);
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
