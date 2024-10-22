package io.github.tnas.qsplitter.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.stream.LongStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.github.tnas.qsplitter.TempTableRelation;
import io.github.tnas.qsplitter.model.User;
import io.github.tnas.qsplitter.model.UserId;
import io.github.tnas.qsplitter.model.User_;

class TempTableDaoTest extends QSplitterDaoTest {

	private TempTableDao<User, Long, UserId> qSplitterDao;
	
	public TempTableDaoTest() {
		this.qSplitterDao = new TempTableDao<>(this.em);
	}
	
	@Override
	@BeforeEach
	void setUp() {
		super.setUp();
		this.em.getTransaction().begin();
		// Cleaning the Temporary Table
		this.em.createNativeQuery("DELETE FROM PUBLIC.USER_ID").executeUpdate();
		this.em.getTransaction().commit();
	}
	
	@Override
	@Test
	void shouldSelectAmongFirst10KUsersFromNepal() {
		var ids = LongStream.rangeClosed(1, TOTAL_RECORDS).boxed().toList();
		this.query.where(builder.equal(root.get(User_.country), "Nepal"));
		var tempRelation = new TempTableRelation<>(UserId.class, Long.class, User_.replicatedId);
		var entities = this.qSplitterDao.select(ids, query, tempRelation);
		assertEquals(503, entities.size());
	}
}
