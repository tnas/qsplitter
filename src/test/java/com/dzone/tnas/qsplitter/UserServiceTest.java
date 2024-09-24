package com.dzone.tnas.qsplitter;

import java.time.Duration;
import java.time.Instant;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.LongStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.dzone.tnas.qsplitter.service.UserService;


class UserServiceTest {

	private static final Logger logger = Logger.getLogger(UserServiceTest.class.getName());
	
	private UserService userService;
	
	@BeforeEach
	void setUp() {
		this.userService = new UserService();
	}
	
    @ParameterizedTest
    @ValueSource(ints = { 50000, 50000, 50000 })
    void shouldAnswerWithTrue(int numOfIds) {
    	var ids = LongStream.rangeClosed(1, numOfIds).boxed().toList();
    	var start = Instant.now();
    	userService.findUsersByIsolatedInClauses(ids);
    	var end = Instant.now();
    	logger.log(Level.INFO, "Elapsed Time [findUsersBySplittingIds]: {0} milliseconds", Duration.between(start, end).toMillis());
    }
	
}
