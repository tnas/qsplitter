package io.github.tnas.qsplitter.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.metamodel.SingularAttribute;

public class DisjunctionsDao<E, T> extends QSplitterDao<E, T> {

	public List<E> select(final List<T> ids, SingularAttribute<E, T> idAttribute, CriteriaQuery<E> criteriaQuery, EntityManager em) {
		
		var entities = new ArrayList<E>();
		var cb = em.getCriteriaBuilder();
		
		var restrictions = new Predicate[2]; 
		restrictions[0] = criteriaQuery.getRestriction();
	
		var predicates = new ArrayList<Predicate>();
		
		this.qSplitter.splitAndGroupCollection(ids)
			.forEach(subsetIds -> {
				
				subsetIds.stream()
					.map(idsList -> this.getPredicateExpressionList(criteriaQuery, idAttribute, ids))
					.forEach(predicates::add);
				
				restrictions[1] = cb.or(predicates.toArray(new Predicate[predicates.size()]));
				
				criteriaQuery.where(restrictions);
				entities.addAll(em.createQuery(criteriaQuery).getResultList());
				predicates.clear();
			});
		
		return entities;
	}
}
