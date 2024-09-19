package com.dzone.tnas.qsplitter;

import com.dzone.tnas.qsplitter.service.UserService;

public class App {
	
    public static void main(String[] args) {
    	var userService = new UserService();
    	userService.listUsersByIdRange(1, 10000);
    }
}
