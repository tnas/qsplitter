package com.dzone.tnas.qsplitter;

import java.time.Duration;
import java.time.Instant;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.LongStream;

import com.dzone.tnas.qsplitter.service.UserService;

/**
 * https://stackoverflow.com/questions/17842453/is-there-a-workaround-for-ora-01795-maximum-number-of-expressions-in-a-list-is
 * https://dzone.com/articles/does-postgresql-have-an-ora-01795-like-limit
 * https://forums.oracle.com/ords/apexds/post/ora-00913-too-many-values-unexpectedly-raised-4055
 */
public class Application {
	
	private static final Logger logger = Logger.getLogger(Application.class.getName());
	
    public static void main(String[] args) {
    	
    	var ids = LongStream.rangeClosed(1, 100000).boxed().toList(); 
    	
    	var userService = new UserService();
    	
//    	userService.insertRandomCollection(90000);
    	
    	var start = Instant.now();
//    	userService.findUsersBySplittingIds(ids);
    	var end = Instant.now();
    	logger.log(Level.INFO, "Elapsed Time [findUsersBySplittingIds]: {0} milliseconds", Duration.between(start, end).toMillis());
    	
    	start = Instant.now();
    	userService.findUsersByDisjunctionsOfInIds(ids);
    	end = Instant.now();
    	logger.log(Level.INFO, "Elapsed Time [findUsersByDisjunctionsOfInIds]: {0} milliseconds", Duration.between(start, end).toMillis());
    	
//    	start = Instant.now();
//    	userService.findUsersByDisjunctionsOfIds(ids);
//    	end = Instant.now();
//    	logger.log(Level.INFO, "Elapsed Time [findUsersByDisjunctionsOfIds]: {0} milliseconds", Duration.between(start, end).toMillis());
    	
    	start = Instant.now();
    	userService.findUsersByTempTableOfIds(ids);
    	end = Instant.now();
    	logger.log(Level.INFO, "Elapsed Time [findUsersByTempTableOfIds]: {0} milliseconds", Duration.between(start, end).toMillis());
    	
    	
//    	userService.printUsers(users);
    }
}
