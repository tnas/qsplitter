package io.github.tnas.qsplitter.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.SingularAttribute;

import io.github.tnas.qsplitter.TempTableRelation;
import io.github.tnas.qsplitter.exception.ThrowingConsumerWrapper;

public class TempTableDao<E, T, U> extends QSplitterJpaDao<E, T> {

	private final ThrowingConsumerWrapper wrapper;
	
	public TempTableDao(EntityManager em) {
		super(em);
		this.wrapper = new ThrowingConsumerWrapper();
	}
	
	@Override
	public List<E> select(List<T> ids, SingularAttribute<E, T> idAttribute, CriteriaQuery<E> criteriaQuery) {
		throw new UnsupportedOperationException();
	}

	@SuppressWarnings("unchecked")
	public List<E> select(List<T> ids, CriteriaQuery<E> criteriaQuery, TempTableRelation<E, T, U> tempRelation) {

		em.getTransaction().begin();
		
		ids.forEach(this.wrapper.wrap(id -> em.persist(
				tempRelation.getTempClass().getConstructor(tempRelation.getKeyClass()).newInstance(id))));
		
		em.getTransaction().commit();
		
		((Root<E>) criteriaQuery.getRoots().iterator().next()).join(tempRelation.getRelationAttribute());
		
		return em.createQuery(criteriaQuery).getResultList();
	}
	
}
