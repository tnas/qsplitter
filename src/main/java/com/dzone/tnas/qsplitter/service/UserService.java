package com.dzone.tnas.qsplitter.service;

import java.util.Collection;
import java.util.List;

import com.dzone.tnas.qsplitter.dao.UserDao;
import com.dzone.tnas.qsplitter.model.User;

public class UserService {

	private QSplitter qsplitter;
	
	private UserDao dao;
	
	public UserService() {
		this.dao = new UserDao();
		this.qsplitter = new QSplitter();
	}
	
	public void insertRandomCollection(int size) {
		this.dao.insertRandomCollection(size);
	}
	
	public List<User> findUsersByIds(List<Long> ids) {
		return this.dao.findByIds(ids);
	}
	
	public List<User> findUsersBySplittingIds(List<Long> ids) {
		return this.qsplitter
			.splitCollection(ids)
			.stream()
			.map(this.dao::findByIds)
			.flatMap(Collection::stream)
			.toList();
	}
	
	public List<User> findUsersByDisjunctionsOfInIds(List<Long> ids) {
		return this.dao.findByDisjunctionsOfInIds(this.qsplitter.splitAndGroupCollection(ids));
	}
	
	public List<User> findUsersByDisjunctionsOfIds(List<Long> ids) {
		return this.dao.findByDisjunctionsOfIds(this.qsplitter.splitCollection(ids, QSplitter.MAX_ORACLE_RETRIEVE_ELEMENTS));
	}
	
	public List<User> findUsersByTempTableOfIds(List<Long> ids) {
		return this.dao.findByTemporaryTableOfIds(ids);
	}
	
	public void printUsers(Collection<User> users) {
		users.stream().forEach(System.out::println);
	}
}
