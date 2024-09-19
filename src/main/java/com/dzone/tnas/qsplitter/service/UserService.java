package com.dzone.tnas.qsplitter.service;

import java.util.stream.IntStream;

import com.dzone.tnas.qsplitter.dao.UserDao;

public class UserService {

	private UserDao dao;
	
	public UserService() {
		this.dao = new UserDao();
	}
	
	public void listUsersByIdRange(int from, int to) {
		this.dao.findByIds(IntStream.rangeClosed(from, to).mapToObj(Long::valueOf).toList());
	}
}
