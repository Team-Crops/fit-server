package org.crops.fitserver.global.jwt;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TokenCollection {

	private final String accessToken;
	private final String refreshToken;
	public static TokenCollection of(String accessToken, String refreshToken) {
		return new TokenCollection(accessToken, refreshToken);
	}
}