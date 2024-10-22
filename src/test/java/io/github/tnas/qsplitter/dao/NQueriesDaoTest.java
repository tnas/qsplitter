package io.github.tnas.qsplitter.dao;

import javax.persistence.metamodel.SingularAttribute;

import io.github.tnas.qsplitter.model.User;

class NQueriesDaoTest extends QSplitterDaoTest<SingularAttribute<User, Long>> {

	public NQueriesDaoTest() {
		this.qSplitterDao = new NQueriesDao<>(this.em);
	}
	
}
