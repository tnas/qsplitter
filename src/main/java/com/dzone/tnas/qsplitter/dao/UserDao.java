package com.dzone.tnas.qsplitter.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Collection;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.dzone.tnas.qsplitter.exception.SQLRuntimeException;
import com.dzone.tnas.qsplitter.model.User;
import com.github.javafaker.Faker;

public class UserDao {

	private Connection getConnection() throws SQLException {
		return DriverManager.getConnection("jdbc:hsqldb:file:src/main/resources/hsqldb/qsplitter", "dzone", "dzone");
	}
	
	public void insertRandomCollection(int size) {
	
		var insertQuery = "INSERT INTO user(name, email, streetname, city, country) VALUES(?, ?, ?, ?, ?)";
		
		try (var conn = this.getConnection()) {
			
			Consumer<User> persistUser = user -> {
				
				try (var stmt = conn.prepareStatement(insertQuery)) {
					stmt.setString(1, user.name());
					stmt.setString(2, user.email());
					stmt.setString(3, user.streetName());
					stmt.setString(4, user.city());
					stmt.setString(5, user.country());
					stmt.execute();
				} catch (SQLException ex) {
					throw new SQLRuntimeException(ex);
				}
			};
			
			var faker = new Faker();
			
			IntStream.range(0, size)
			.mapToObj(i -> new User(null, faker.name().fullName(), faker.internet().emailAddress(), 
					faker.address().streetName(), faker.address().cityName(), faker.country().name()))
			.forEach(persistUser);
		} catch (Exception e) {
			throw new SQLRuntimeException(e);
		}
	}
	
	public void findAll() {
		
		var selectQuery = "SELECT id, name, email, streetname, city, country FROM user ORDER BY id";
		
		try (var rs = this.getConnection().createStatement().executeQuery(selectQuery)) {
			while (rs.next()) {
				var user = new User(rs.getLong(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6));
				System.out.println(user.toString());
			}
		} catch (Exception e) {
			throw new SQLRuntimeException(e);
		}
	}
	
	
	public void findByIds(Collection<Long> ids) {

		var queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT id, name, email, streetname, city, country FROM user WHERE id IN (");
		queryBuilder.append(ids.stream().map(Object::toString).collect(Collectors.joining(",")));
		queryBuilder.append(") ORDER BY id");
		
		try (var stmt = this.getConnection().prepareStatement(queryBuilder.toString())) {
			
			try (var rs = stmt.executeQuery()) {
				while (rs.next()) {
					var user = new User(rs.getLong(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6));
					System.out.println(user.toString());
				}
			}
			
		} catch (Exception e) {
			throw new SQLRuntimeException(e);
		}
	}
}
