package io.github.tnas.qsplitter.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.stream.LongStream;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.github.tnas.qsplitter.model.User;
import io.github.tnas.qsplitter.model.User_;

abstract class QSplitterDaoTest {
	
	static final int TOTAL_RECORDS = 99765; // Nepal = 503
	
	QSplitterJpaDao<User, Long> qSplitterDao;
	
	EntityManager em;
	CriteriaBuilder builder;
	CriteriaQuery<User> query;
	Root<User> root;
	
	QSplitterDaoTest() {
		this.em = Persistence.createEntityManagerFactory("qsplitter-pu").createEntityManager();
	}
	
	@BeforeEach
	void setUp() {
		this.builder = this.em.getCriteriaBuilder();
		this.query = builder.createQuery(User.class);
		this.root = query.from(User.class);
	}
	
	@Test
	void shouldSelectAmongFirst10KUsersFromNepal() {
		var ids = LongStream.rangeClosed(1, TOTAL_RECORDS).boxed().toList();
		this.query.where(builder.equal(root.get(User_.country), "Nepal"));
		var entities = this.qSplitterDao.select(ids, User_.id, query, em);
		assertEquals(503, entities.size());
	}
}
