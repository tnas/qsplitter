package io.github.tnas.qsplitter.exception;

import java.util.function.Consumer;

public class ThrowingConsumerWrapper {

    public <T> Consumer<T> wrap(ThrowingConsumer<T, Exception> throwingConsumer) {
        return t ->  {
    		try {
    			throwingConsumer.accept(t);
    		} catch (Exception e) {
    			throw new EntityManagerException(e);
    		}
        };
    }
}
