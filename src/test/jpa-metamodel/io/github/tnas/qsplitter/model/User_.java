package io.github.tnas.qsplitter.model;

import javax.annotation.processing.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(User.class)
public abstract class User_ {

	public static volatile SingularAttribute<User, String> country;
	public static volatile SingularAttribute<User, String> streetName;
	public static volatile SingularAttribute<User, String> city;
	public static volatile SingularAttribute<User, String> name;
	public static volatile SingularAttribute<User, Long> id;
	public static volatile SingularAttribute<User, String> email;

	public static final String COUNTRY = "country";
	public static final String STREET_NAME = "streetName";
	public static final String CITY = "city";
	public static final String NAME = "name";
	public static final String ID = "id";
	public static final String EMAIL = "email";

}

