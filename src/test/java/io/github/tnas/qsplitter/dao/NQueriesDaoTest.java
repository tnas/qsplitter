package io.github.tnas.qsplitter.dao;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;

import org.junit.jupiter.api.Test;

import io.github.tnas.qsplitter.model.User;

class NQueriesDaoTest {

	private QSplitterDao<User, Long> qSplitterDao;
	private EntityManager em;
	
	public NQueriesDaoTest() {
		this.qSplitterDao = new NQueriesDao<>();
		this.em = Persistence.createEntityManagerFactory("qsplitter-pu").createEntityManager();
	}
	
	@Test
	void testSimple() {
		assertNotNull(this.em);
	}
}
