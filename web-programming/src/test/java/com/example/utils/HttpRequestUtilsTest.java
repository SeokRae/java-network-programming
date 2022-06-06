package com.example.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

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
	}
	
	@DisplayName("쿼리 스트링 존재하지 않는 값 확인 테스트")
	@NullAndEmptySource
	@ParameterizedTest(name = "QueryString 빈 값:{0} 체크")
	void testCase2(String empty) {
		Map<String, String> queryStringMap = HttpRequestUtils.parseQueryString(empty);
		assertThat(queryStringMap).isEmpty();
	}
	
	@DisplayName("유효하지 않은 쿼리스트링 테스트")
	@Test
	void testCase3() {
		String queryString = "userId=seok&password";
		Map<String, String> parameters = HttpRequestUtils.parseQueryString(queryString);
		assertAll(
				"Invalid QueryString"
				, () -> assertThat(parameters).containsEntry("userId", "seok")
				, () -> assertThat(parameters.get("password")).isNull()
		);
	}
	
	@DisplayName("쿼리 스트링의 Key Value 파싱 테스트")
	@Test
	void testCase4() {
		HttpRequestUtils.Pair pair = HttpRequestUtils.parsePair("userId=seok", "=");
		assertThat(pair).isEqualTo(new HttpRequestUtils.Pair("userId", "seok"));
	}
	
	@DisplayName("사용자의 요청 header 값 정보 파싱 테스트")
	@Test
	void testCase5() {
		String header = "Content-Type: application/json";
		HttpRequestUtils.Pair pair = HttpRequestUtils.parseHeader(header);
		assertThat(pair).isEqualTo(new HttpRequestUtils.Pair("Content-Type", "application/json"));
	}
}