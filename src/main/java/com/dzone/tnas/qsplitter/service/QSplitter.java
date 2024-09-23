package com.dzone.tnas.qsplitter.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.IntStream;

public class QSplitter {

	private static final int MAX_COLLECTION_SIZE = 1000; // ORA-01795
	private static final int MAX_ELEMENTS_ALL_COLLECTIONS = 65535; // ORA-00913

	public <T> List<List<T>> splitCollection(Collection<T> collection) {
		return splitCollection(collection, MAX_COLLECTION_SIZE);
	}

	@SuppressWarnings("unchecked")
	public <T> List<List<T>> splitCollection(Collection<T> collection, int maxCollectionSize) {
		
		if (Objects.isNull(collection) || collection.isEmpty()) {
			return Collections.emptyList();
		}
		
		var collectionArray = collection.toArray();
		var splittedCollection = new ArrayList<List<T>>();
		
		var partitions = (collection.size() / maxCollectionSize)
				+ (collection.size() % maxCollectionSize == 0 ? 0 : 1);
		
		Spliterator<T>[] spliterators = new Spliterator[partitions];
		
		IntStream.range(0, partitions).forEach(n -> {
			var fromIndex = n * maxCollectionSize;
			var toIndex = fromIndex + maxCollectionSize > collection.size()
					? fromIndex + collection.size() % maxCollectionSize : fromIndex + maxCollectionSize;
			spliterators[n] = Spliterators
					.spliterator(collectionArray, fromIndex, toIndex, Spliterator.SIZED);
			splittedCollection.add(new ArrayList<>());
		});
		
		IntStream.range(0, partitions)
				.forEach(n -> spliterators[n].forEachRemaining(splittedCollection.get(n)::add));
		
		return splittedCollection;
	}
}
