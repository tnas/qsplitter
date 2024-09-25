package com.dzone.tnas.qsplitter.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.dzone.tnas.qsplitter.dao.UserDao;
import com.dzone.tnas.qsplitter.model.User;

public class UserService {

	private static final Logger logger = Logger.getLogger(UserService.class.getName());
	
	private QSplitter qsplitter;
	
	private UserDao dao;
	
	public UserService() {
		this.dao = new UserDao();
		this.qsplitter = new QSplitter();
	}
	
	public void insertRandomCollection(int size) {
		this.dao.insertRandomCollection(size);
	}
	
	public void printUsers(Collection<User> users) {
		users.stream().forEach(u -> logger.log(Level.INFO, u.toString()));
	}
	
	public List<User> findUsersByIds(List<Long> ids) {
		return this.dao.findByIds(ids);
	}
	
	public List<User> findUsersByIsolatedInClauses(List<Long> ids) {
		var users = new ArrayList<User>();
		this.qsplitter.splitCollection(ids).stream().map(this.dao::findByIds).forEach(users::addAll);
		return users;
	}
	
	public List<User> findUsersByDisjunctionsOfInClauses(List<Long> ids) {
		return this.dao.findByDisjunctionsOfInClauses(this.qsplitter.splitAndGroupCollection(ids));
	}
	
	public List<User> findUsersByDisjunctionsOfIds(List<Long> ids) {
		return this.dao.findByDisjunctionsOfIds(this.qsplitter.splitCollection(ids, QSplitter.MAX_ORACLE_RETRIEVE_ELEMENTS));
	}
	
	public List<User> findUsersByTempTableOfIds(List<Long> ids) {
		return this.dao.findByTemporaryTableOfIds(ids);
	}
	
	public List<User> findUsersByUnionAll(List<Long> ids) {
		return this.dao.findByUnionAll(this.qsplitter.splitCollection(ids));
	}
	
	public List<User> findUsersByMultiValueInClause(List<Long> ids) {
		return this.dao.findByMultiValueIn(ids);
	}
}
