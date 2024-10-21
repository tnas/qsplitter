package io.github.tnas.qsplitter.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import io.github.tnas.qsplitter.dao.UserDao;
import io.github.tnas.qsplitter.model.User;

public class UserService {

	private static final Logger logger = Logger.getLogger(UserService.class.getName());
	
	private final QSplitter<Long> qsplitter;
	
	private final UserDao dao;
	
	public UserService() {
		this.dao = new UserDao();
		this.qsplitter = new QSplitter<>();
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
	
	public List<User> findUsersByNQuery(List<Long> ids) {
		var users = new ArrayList<User>();
		this.qsplitter.splitCollection(ids).stream().map(this.dao::findByIds).forEach(users::addAll);
		return users;
	}
	
	public List<User> findUsersByDisjunctionsOfExpressionLists(List<Long> ids) {
		return this.dao.findByDisjunctionsOfExpressionLists(this.qsplitter.splitAndGroupCollection(ids));
	}
	
	public List<User> findUsersByDisjunctionsOfIds(List<Long> ids) {
		return this.dao.findByDisjunctionsOfIds(this.qsplitter.splitCollection(ids, QSplitter.MAX_ORACLE_RETRIEVE_ELEMENTS));
	}
	
	public List<User> findUsersByTempTable(List<Long> ids) {
		return this.dao.findByTemporaryTable(ids);
	}
	
	public List<User> findUsersByUnionAll(List<Long> ids) {
		return this.dao.findByUnionAll(this.qsplitter.splitCollection(ids));
	}
	
	public List<User> findUsersByTuples(List<Long> ids) {
		return this.dao.findByTuples(this.qsplitter.splitCollection(ids, QSplitter.MAX_ORACLE_RETRIEVE_ELEMENTS));
	}
}
