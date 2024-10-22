package io.github.tnas.qsplitter.dao;

import javax.persistence.metamodel.SingularAttribute;

import io.github.tnas.qsplitter.model.User;

class DisjunctionsDaoTest extends QSplitterDaoTest<SingularAttribute<User, Long>> {

	public DisjunctionsDaoTest() {
		this.qSplitterDao = new DisjunctionsDao<>(this.em);
	}
}
