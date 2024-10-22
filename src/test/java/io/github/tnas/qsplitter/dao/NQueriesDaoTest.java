package io.github.tnas.qsplitter.dao;

class NQueriesDaoTest extends QSplitterDaoTest {

	public NQueriesDaoTest() {
		this.qSplitterDao = new NQueriesDao<>(this.em);
	}
	
}
