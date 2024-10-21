package io.github.tnas.qsplitter.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.IntStream;

public class QSplitter<T> {

	public static final int MAX_ORACLE_IN_CLAUSE_ELEMENTS = 1000; // ORA-01795
	public static final int MAX_ORACLE_RETRIEVE_ELEMENTS = 65535; // ORA-00913

	public List<List<T>> splitCollection(Collection<T> collection) {
		return splitCollection(collection, MAX_ORACLE_IN_CLAUSE_ELEMENTS);
	}
	
	public List<List<List<T>>> splitAndGroupCollection(Collection<T> collection) {
		
		var groupedCollection = new ArrayList<List<List<T>>>();
		
		var splitCollection = splitCollection(collection, MAX_ORACLE_IN_CLAUSE_ELEMENTS);
		
		if (collection.size() <= MAX_ORACLE_RETRIEVE_ELEMENTS) {
			groupedCollection.add(splitCollection);
			return groupedCollection;
		}
		
		groupedCollection.add(new ArrayList<>());
		
		splitCollection.forEach(partition -> {
			
			if (groupedCollection.getLast().size() * MAX_ORACLE_IN_CLAUSE_ELEMENTS + partition.size() > MAX_ORACLE_RETRIEVE_ELEMENTS) {
				groupedCollection.add(new ArrayList<>());
			} 
			
			groupedCollection.getLast().add(partition);
		});
		
		return groupedCollection;
	}

	@SuppressWarnings("unchecked")
	public List<List<T>> splitCollection(Collection<T> collection, int maxCollectionSize) {
		
		if (Objects.isNull(collection) || collection.isEmpty()) {
			return Collections.emptyList();
		}
		
		var collectionArray = collection.toArray();
		var splitCollection = new ArrayList<List<T>>();
		
		var partitions = (collection.size() / maxCollectionSize)
				+ (collection.size() % maxCollectionSize == 0 ? 0 : 1);
		
		Spliterator<T>[] spliterators = new Spliterator[partitions];
		
		IntStream.range(0, partitions).forEach(n -> {
			var fromIndex = n * maxCollectionSize;
			var toIndex = fromIndex + maxCollectionSize > collection.size()
					? fromIndex + collection.size() % maxCollectionSize : fromIndex + maxCollectionSize;
			spliterators[n] = Spliterators
					.spliterator(collectionArray, fromIndex, toIndex, Spliterator.SIZED);
			splitCollection.add(new ArrayList<>());
		});
		
		IntStream.range(0, partitions)
				.forEach(n -> spliterators[n].forEachRemaining(splitCollection.get(n)::add));
		
		return splitCollection;
	}
}
