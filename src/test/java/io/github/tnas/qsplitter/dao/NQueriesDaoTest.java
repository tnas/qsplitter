package io.github.tnas.qsplitter.dao;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.github.tnas.qsplitter.model.User;

class NQueriesDaoTest {

	private QSplitterDao<User, Long> qSplitterDao;
	
	private EntityManager em;
	private CriteriaBuilder builder;
	private CriteriaQuery<User> query;
	private Root<User> root;
	
	public NQueriesDaoTest() {
		this.qSplitterDao = new NQueriesDao<>();
		this.em = Persistence.createEntityManagerFactory("qsplitter-pu").createEntityManager();
	}
	
	@BeforeEach
	public void setUp() {
		this.builder = this.em.getCriteriaBuilder();
		this.query = builder.createQuery(User.class);
		this.root = query.from(User.class);
	}
	
	@Test
	void testSimple() {
		
	}
}
