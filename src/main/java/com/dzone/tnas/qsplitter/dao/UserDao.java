package com.dzone.tnas.qsplitter.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.function.Consumer;
import java.util.stream.IntStream;

import com.dzone.tnas.qsplitter.model.User;
import com.github.javafaker.Faker;

public class UserDao {

	private Connection getConnection() throws SQLException {
		return DriverManager.getConnection("jdbc:hsqldb:file:src/main/resources/hsqldb/qsplitter", "dzone", "dzone");
	}
	
	public void insertRandomCollection(int size) throws SQLException {
	
		var insertQuery = "INSERT INTO user(name, email, streetname, city, country) VALUES(?, ?, ?, ?, ?)";
		
		try(var conn = this.getConnection()) {
			
			Consumer<User> persistUser = user -> {
				try {
					var stmt = conn.prepareStatement(insertQuery);
					stmt.setString(1, user.name());
					stmt.setString(2, user.email());
					stmt.setString(3, user.streetName());
					stmt.setString(4, user.city());
					stmt.setString(5, user.country());
					stmt.execute();
				} catch (SQLException e) {
					throw new RuntimeException(e);
				}
			};
			
			var faker = new Faker();
			
			IntStream.range(0, size)
			.mapToObj(i -> new User(null, faker.name().fullName(), faker.internet().emailAddress(), 
					faker.address().streetName(), faker.address().cityName(), faker.country().name()))
			.forEach(persistUser);
		}
	}
	
	public void findAll() throws SQLException {
		
		var selectQuery = "SELECT id, name, email, streetname, city, country FROM user ORDER BY id";
		
		try(var rs = this.getConnection().createStatement().executeQuery(selectQuery)) {
			while (rs.next()) {
				var user = new User(rs.getLong(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6));
				System.out.println(user.toString());
			}
		}
	}
	
}
