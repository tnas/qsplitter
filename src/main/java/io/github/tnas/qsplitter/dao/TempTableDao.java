package io.github.tnas.qsplitter.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import io.github.tnas.qsplitter.exception.ThrowingConsumerWrapper;

public class TempTableDao<E, T, U> extends QSplitterJpaDao<E, T, TableRelationship<E, T, U>> {

	private final ThrowingConsumerWrapper wrapper;
	
	public TempTableDao(EntityManager em) {
		super(em);
		this.wrapper = new ThrowingConsumerWrapper();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<E> select(List<T> ids, CriteriaQuery<E> criteriaQuery, TableRelationship<E, T, U> relationship) {

		em.getTransaction().begin();
		
		ids.forEach(this.wrapper.wrap(id -> em.persist(
				relationship.getTempClass().getConstructor(relationship.getKeyClass()).newInstance(id))));
		
		em.getTransaction().commit();
		
		((Root<E>) criteriaQuery.getRoots().iterator().next()).join(relationship.getRelationAttribute());
		
		return em.createQuery(criteriaQuery).getResultList();
	}
	
}
