package io.github.tnas.qsplitter.dao;

import javax.persistence.metamodel.SingularAttribute;

public class TableRelationship<E, T, U> {

	private Class<U> tempClass;
	
	private Class<T> keyClass;
	
	private SingularAttribute<E, U> relationAttribute;

	public TableRelationship() { }
	
	public TableRelationship(Class<U> tempClass, Class<T> keyClass, SingularAttribute<E, U> relationAttribute) {
		this.tempClass = tempClass;
		this.keyClass = keyClass;
		this.relationAttribute = relationAttribute;
	}

	public Class<U> getTempClass() {
		return tempClass;
	}

	public void setTempClass(Class<U> tempClass) {
		this.tempClass = tempClass;
	}

	public Class<T> getKeyClass() {
		return keyClass;
	}

	public void setKeyClass(Class<T> keyClass) {
		this.keyClass = keyClass;
	}

	public SingularAttribute<E, U> getRelationAttribute() {
		return relationAttribute;
	}

	public void setRelationAttribute(SingularAttribute<E, U> relationAttribute) {
		this.relationAttribute = relationAttribute;
	}
	
}
