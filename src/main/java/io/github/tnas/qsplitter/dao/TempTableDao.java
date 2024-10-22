package io.github.tnas.qsplitter.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.SingularAttribute;

public class TempTableDao<E, T, U> extends QSplitterJpaDao<E, T> {

	@Override
	public List<E> select(List<T> ids, SingularAttribute<E, T> idAttribute, CriteriaQuery<E> criteriaQuery,
			EntityManager em) {
		throw new UnsupportedOperationException();
	}

	@SuppressWarnings("unchecked")
	public List<E> select(List<T> ids, CriteriaQuery<E> criteriaQuery, EntityManager em, 
			Class<U> tempEntityClass, SingularAttribute<E, U> tempEntityAttribute) {

		em.getTransaction().begin();
		
		ids.forEach(id -> {
			try {
				em.persist(tempEntityClass.getConstructor(Long.class).newInstance(id));
			} catch (Exception e) {
				throw new RuntimeException(e);
			} 
		});

		em.getTransaction().commit();
		
		((Root<E>) criteriaQuery.getRoots().iterator().next()).join(tempEntityAttribute);
		
		return em.createQuery(criteriaQuery).getResultList();
	}
	
}
