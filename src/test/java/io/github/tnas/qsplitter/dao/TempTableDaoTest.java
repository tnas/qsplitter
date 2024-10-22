package io.github.tnas.qsplitter.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.stream.LongStream;

import org.junit.jupiter.api.Test;

import io.github.tnas.qsplitter.model.User;
import io.github.tnas.qsplitter.model.UserId;
import io.github.tnas.qsplitter.model.User_;

class TempTableDaoTest extends QSplitterDaoTest {

	private TempTableDao<User, Long, UserId> qSplitterDao;
	
	public TempTableDaoTest() {
		this.qSplitterDao = new TempTableDao<>();
	}
	
	@Override
	@Test
	void shouldSelectAmongFirst10KUsersFromNepal() {
		var ids = LongStream.rangeClosed(1, TOTAL_RECORDS).boxed().toList();
		this.query.where(builder.equal(root.get(User_.country), "Nepal"));
		var entities = this.qSplitterDao.select(ids, query, em, UserId.class, User_.replicatedId);
		assertEquals(503, entities.size());
	}
}
