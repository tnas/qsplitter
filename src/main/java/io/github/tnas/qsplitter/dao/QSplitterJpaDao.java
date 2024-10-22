package io.github.tnas.qsplitter.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.SingularAttribute;

import io.github.tnas.qsplitter.QSplitter;

public abstract class QSplitterJpaDao<E, T> {

	protected QSplitter<T> qSplitter;

	protected QSplitterJpaDao() {
		this.qSplitter = new QSplitter<>();
	}

	public abstract List<E> select(final List<T> ids, SingularAttribute<E, T> idAttribute, CriteriaQuery<E> criteriaQuery, EntityManager em);
	
	@SuppressWarnings("unchecked")
	protected Predicate getPredicateExpressionList(CriteriaQuery<E> criteriaQuery, SingularAttribute<E, T> idAttribute, List<T> ids) {
		return ((Root<E>) criteriaQuery.getRoots().iterator().next()).get(idAttribute).in(ids);
	}

}