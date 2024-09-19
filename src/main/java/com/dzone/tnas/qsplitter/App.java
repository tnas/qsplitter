package com.dzone.tnas.qsplitter;

import java.sql.SQLException;

import com.dzone.tnas.qsplitter.dao.UserDao;

public class App {
	
    public static void main(String[] args) throws SQLException {

    	var userDao = new UserDao();
    	userDao.insertRandomCollection(10);
    	userDao.findAll();
    }
}
