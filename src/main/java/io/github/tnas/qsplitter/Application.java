package io.github.tnas.qsplitter;

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

import io.github.tnas.qsplitter.model.User;
import io.github.tnas.qsplitter.service.Tracer;
import io.github.tnas.qsplitter.service.UserService;

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
			Integer.parseInt(args[0]) >= 0 && Integer.parseInt(args[0]) <= 5;
	
	private static List<String> functionsNames = List.of(
	   			"findUsersByNQuery",
	   			"findUsersByDisjunctionsOfExpressionLists", 
	   			"findUsersByDisjunctionsOfIds", 
	   			"findUsersByTempTable",
	   			"findUsersByUnionAll",
				"findUsersByTuples");
	
    public static void main(String[] arguments) {

    	var userService = new UserService();
    	var tracer = new Tracer();
    	
    	List<Function<List<Long>, List<User>>> findFunctions = List.of(
    			userService::findUsersByNQuery,
    			userService::findUsersByDisjunctionsOfExpressionLists,
    			userService::findUsersByDisjunctionsOfIds, 
    			userService::findUsersByTempTable,
    			userService::findUsersByUnionAll,
    			userService::findUsersByTuples);
    	
    	BiConsumer<Integer, Integer> traceFindFunction = (index, numOfIds) -> {
    		var ids = LongStream.rangeClosed(1, numOfIds).boxed().toList();
	    	var start = Instant.now();
			var users = findFunctions.get(index).apply(ids);
			var elapsedTime = Duration.between(start, Instant.now()).toMillis();
			var params = new Object[] { functionsNames.get(index), elapsedTime, users.size() };
			logger.log(Level.INFO, "Elapsed Time [{0}]: {1} milliseconds for {2} users", params);
			tracer.traceInFile(functionsNames.get(index), numOfIds, elapsedTime);
    	};
    	
    	if (isArgsValid.test(arguments)) {
    		traceFindFunction.accept(Integer.parseInt(arguments[0]), Integer.parseInt(arguments[1]));
    	} else {
    		logger.log(Level.WARNING, "Invalid argments. Running default set of tests.");
    		IntStream.range(0, findFunctions.size()).forEach(index -> traceFindFunction.accept(index, DEFAULT_NUM_OF_IDS));
    	}
    }
}
