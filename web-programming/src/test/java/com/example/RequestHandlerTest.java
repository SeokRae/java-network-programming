package com.example;

import com.example.utils.HttpRequestUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("RequestHandler 클래스")
class RequestHandlerTest {

	@Nested
	@DisplayName("HttpRequestUtils 클래스 (QueryString)")
	class HttpRequestUtilsTest {
		
		@DisplayName("쿼리 스트링 파싱 및 값 체크")
		@Test
		void testCase1() {
			String queryString = "userId=seok&password=password&name=seokrae&email=seokrae@gmail.com";
			Map<String, String> queryStringMap = HttpRequestUtils.parseQueryString(queryString);
			assertAll(
					"QueryString Pair Confirm"
					,	() -> assertThat(queryStringMap).containsEntry("userId", "seok")
					, () -> assertThat(queryStringMap).containsEntry("password", "password")
					, () -> assertThat(queryStringMap).containsEntry("name", "seokrae")
					, () -> assertThat(queryStringMap).containsEntry("email", "seokrae@gmail.com")
			);
			;
		}
	}
}