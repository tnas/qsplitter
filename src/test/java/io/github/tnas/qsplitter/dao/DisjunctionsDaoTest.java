package io.github.tnas.qsplitter.dao;

class DisjunctionsDaoTest extends QSplitterDaoTest {

	public DisjunctionsDaoTest() {
		this.qSplitterDao = new DisjunctionsDao<>(this.em);
	}
}
