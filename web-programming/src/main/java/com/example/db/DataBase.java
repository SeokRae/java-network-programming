package com.example.db;

import com.example.model.User;

import java.util.HashMap;
import java.util.Map;

public final class DataBase {
	
	private DataBase() {}
	
	public static final Map<String, User> users = new HashMap<>();
	
	public static void add(User user) {
		users.put(user.getUserId(), user);
	}
	
	public static User getUser(String id) {
		return users.get(id);
	}
}
