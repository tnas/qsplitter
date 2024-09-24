package com.dzone.tnas.qsplitter;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

import org.apache.commons.lang3.StringUtils;

import com.dzone.tnas.qsplitter.model.User;
import com.dzone.tnas.qsplitter.service.UserService;

/**
 * https://stackoverflow.com/questions/17842453/is-there-a-workaround-for-ora-01795-maximum-number-of-expressions-in-a-list-is
 * https://dzone.com/articles/does-postgresql-have-an-ora-01795-like-limit
 * https://forums.oracle.com/ords/apexds/post/ora-00913-too-many-values-unexpectedly-raised-4055
 */
public class Application {
	
	private static final Logger logger = Logger.getLogger(Application.class.getName());
	
	private static final int DEFAULT_NUM_OF_IDS = 10000;
	
	private static Predicate<String[]> isArgsValid = args ->
			args.length == 2 && 
			StringUtils.isNumeric(args[0]) && StringUtils.isNumeric(args[1]) &&
			Integer.parseInt(args[0]) >= 0 && Integer.parseInt(args[0]) <= 3;
	
	private static List<String> functionsNames = List.of(
	   			"findUsersByIsolatedInClauses", 
	   			"findUsersByDisjunctionsOfInClauses", 
	   			"findUsersByDisjunctionsOfIds", 
	   			"findUsersByTempTableOfIds");
	
    public static void main(String[] arguments) {

    	var userService = new UserService();
    	
    	List<Function<List<Long>, List<User>>> findFunctions = List.of(
    			userService::findUsersByIsolatedInClauses,
    			userService::findUsersByDisjunctionsOfInClauses,
    			userService::findUsersByDisjunctionsOfIds, 
    			userService::findUsersByTempTableOfIds);
    	
    	BiConsumer<Integer, Integer> traceFindFunction = (index, numOfIds) -> {
    		var ids = LongStream.rangeClosed(1, numOfIds).boxed().toList();
	    	var start = Instant.now();
			var users = findFunctions.get(index).apply(ids);
			var end = Instant.now();
			var params = new Object[] { functionsNames.get(index), Duration.between(start, end).toMillis(), users.size() };
			logger.log(Level.INFO, "Elapsed Time [{0}]: {1} milliseconds for {2} users", params);
    	};
    	
    	if (isArgsValid.test(arguments)) {
    		traceFindFunction.accept(Integer.parseInt(arguments[0]), Integer.parseInt(arguments[1]));
    	} else {
    		IntStream.range(0, findFunctions.size()).forEach(index -> traceFindFunction.accept(index, DEFAULT_NUM_OF_IDS));
    	}
    }
}
