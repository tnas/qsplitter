package com.dzone.tnas.qsplitter.dao;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.dzone.tnas.qsplitter.exception.IORuntimeException;
import com.dzone.tnas.qsplitter.exception.SQLRuntimeException;
import com.dzone.tnas.qsplitter.model.User;
import com.github.javafaker.Faker;

public class UserDao {

	private Properties datasource;
	
	public UserDao() {
		
		try (var input = new FileInputStream("src/main/resources/oracle/datasource.properties")) {
			this.datasource = new Properties();
			this.datasource.load(input);
		} catch (IOException e) {
			throw new IORuntimeException(e);
		}
	}
	
	private Function<Collection<Long>, String> buildSimpleSelect = ids -> 
			new StringBuilder()
				.append("SELECT id, name, email, street_name, city, country FROM employee WHERE id IN (")
				.append(ids.stream().map(Object::toString).collect(Collectors.joining(",")))
				.append(") ORDER BY id")
				.toString();
	
	private Function<List<List<Long>>, String> buildSelectOfDisjunctions = idsList -> 
		new StringBuilder("SELECT id, name, email, street_name, city, country FROM employee WHERE ")
				.append(idsList.stream()
						.map(ids -> new StringBuilder()
								.append("id IN (").append(ids.stream().map(Object::toString).collect(Collectors.joining(","))).append(")"))
						.collect(Collectors.joining(" OR ")))
				.append(" ORDER BY id")
				.toString();
			
	@SuppressWarnings("unused")
	private Connection getHsqldbConnection() throws SQLException {
		return DriverManager.getConnection("jdbc:hsqldb:file:src/main/resources/hsqldb/qsplitter", "dzone", "dzone");
	}
	
	private Connection getOracleConnection() throws SQLException {
		return DriverManager.getConnection(this.datasource.getProperty("db.url"), 
				this.datasource.getProperty("db.user"), this.datasource.getProperty("db.password"));
	}
	
	public void insertRandomCollection(int size) {
	
		var insertQuery = "INSERT INTO employee(name, email, street_name, city, country) VALUES(?, ?, ?, ?, ?)";
		
		try (var conn = this.getOracleConnection()) {
			
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
	
	public List<User> findAll() {
		
		var selectQuery = "SELECT id, name, email, street_name, city, country FROM employee ORDER BY id";
		
		try (var rs = this.getOracleConnection().createStatement().executeQuery(selectQuery)) {
			
			var users = new ArrayList<User>();
			
			while (rs.next()) {
				users.add(new User(rs.getLong(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6)));
			}
			
			return users;
		} catch (Exception e) {
			throw new SQLRuntimeException(e);
		}
	}
	
	
	public List<User> findByIds(Collection<Long> ids) {
		
		try (var stmt = this.getOracleConnection().prepareStatement(buildSimpleSelect.apply(ids))) {
			
			var users = new ArrayList<User>();
			
			try (var rs = stmt.executeQuery()) {
				while (rs.next()) {
					users.add(new User(rs.getLong(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6)));
				}
			}
			
			return users;
			
		} catch (Exception e) {
			throw new SQLRuntimeException(e);
		}
	}
	
	public List<User> findByDisjunctionsOfIds(List<List<Long>> ids) {
		
		try (var stmt = this.getOracleConnection().prepareStatement(buildSelectOfDisjunctions.apply(ids))) {
			
			var users = new ArrayList<User>();
			
			try (var rs = stmt.executeQuery()) {
				while (rs.next()) {
					users.add(new User(rs.getLong(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6)));
				}
			}
			
			return users;
			
		} catch (Exception e) {
			throw new SQLRuntimeException(e);
		}
	}
	
	public List<User> findByTemporaryTableOfIds(List<Long> ids) {
		
		var queryInsertTempTable = "INSERT INTO employee_id (emp_id) VALUES (?)";
		var querySelectUsers = "SELECT id, name, email, street_name, city, country FROM employee JOIN employee_id ON id = emp_id ORDER BY id";
		
		try (var conn = this.getOracleConnection()) {
			
			try (var pstmt = conn.prepareStatement(queryInsertTempTable)) {
				
				for (var id : ids) {
					pstmt.setLong(1, id);
					pstmt.addBatch();
				}
				
				pstmt.executeBatch();
			}
			
			var users = new ArrayList<User>();
			
			try (var rs = conn.prepareStatement(querySelectUsers).executeQuery()) {
				while (rs.next()) {
					users.add(new User(rs.getLong(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6)));
				}
			}
			
			return users;
			
		} catch (Exception e) {
			throw new SQLRuntimeException(e);
		}
	}
	
}
