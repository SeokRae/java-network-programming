package com.example.utils;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public final class HttpRequestUtils {
	
	private HttpRequestUtils() {}
	public static Map<String, String> parseQueryString(String queryString) {
		if(Strings.isNullOrEmpty(queryString)) {
			return Maps.newHashMap();
		}
		String[] tokens = queryString.split("&");
		return Arrays.stream(tokens)
				.map(s -> parsePair(s, "="))
				.filter(Objects::nonNull)
				.collect(Collectors.toMap(Pair::getKey, Pair::getValue));
	}
	
	public static Pair parseHeader(String header) {
		return parsePair(header, ": ");
	}
	
	static Pair parsePair(String keyValue, String regex) {
		if(Strings.isNullOrEmpty(regex)) {
			return null;
		}
		
		String[] tokens = keyValue.split(regex);
		if(tokens.length != 2) {
			return null;
		}
		
		return new Pair(tokens[0], tokens[1]);
	}
	
	static class Pair {
		private final String key;
		private final String value;
		
		Pair(String key, String value) {
			this.key = key;
			this.value = value;
		}
		
		public String getKey() {
			return key;
		}
		
		public String getValue() {
			return value;
		}
		
		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (!(o instanceof Pair)) return false;
			Pair pair = (Pair) o;
			return Objects.equals(key, pair.key)
					&& Objects.equals(value, pair.value);
		}
		
		@Override
		public int hashCode() {
			return Objects.hash(key, value);
		}
		
		@Override
		public String toString() {
			return "Pair{" +
					"key='" + key + '\'' +
					", value='" + value + '\'' +
					'}';
		}
	}
}
