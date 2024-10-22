package io.github.tnas.qsplitter.dao;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.commons.lang3.StringUtils;

import com.github.javafaker.Faker;

import io.github.tnas.qsplitter.exception.EntityManagerException;
import io.github.tnas.qsplitter.exception.SQLRuntimeException;
import io.github.tnas.qsplitter.model.User;

public class UserDao {

	private static final Logger logger = Logger.getLogger(UserDao.class.getName());
	private static final String ORDER_BY_ID = " ORDER BY id";
	
	private Properties datasource;
	
	public UserDao() {
		
		try (var input = new FileInputStream("src/main/resources/oracle/datasource.properties")) {
			this.datasource = new Properties();
			this.datasource.load(input);
		} catch (IOException e) {
			
			logger.log(Level.SEVERE, e.getMessage());
			logger.log(Level.INFO, "Trying to load datasource properties from oracle/datasource.properties");
			
			try (var input = getClass().getClassLoader().getResourceAsStream("oracle/datasource.properties")) {
				this.datasource = new Properties();
				this.datasource.load(input);
				Class.forName("oracle.jdbc.OracleDriver");
			} catch (IOException | ClassNotFoundException ex) {
				throw new EntityManagerException(ex);
			}
		}
	}
	
	private Function<Collection<Long>, String> buildSimpleSelectIn = ids -> 
			new StringBuilder()
				.append("SELECT id, name, email, streetname, city, country FROM user WHERE id IN (")
				.append(ids.stream().map(Object::toString).collect(Collectors.joining(",")))
				.append(")").append(ORDER_BY_ID)
				.toString();
	
	private Function<List<List<Long>>, String> buildSelectDisjunctions = idsList -> 
		new StringBuilder("SELECT id, name, email, streetname, city, country FROM user WHERE ")
				.append(idsList.stream()
						.map(ids -> new StringBuilder()
								.append("id IN (").append(ids.stream().map(Object::toString).collect(Collectors.joining(","))).append(")"))
						.collect(Collectors.joining(" OR ")))
				.append(ORDER_BY_ID)
				.toString();
		
	private Function<List<Long>, String> buildSelectOfDisjunctions = ids -> 
		new StringBuilder("SELECT id, name, email, streetname, city, country FROM user WHERE ")
				.append(ids.stream().map(id -> String.format("id = %d", id)).collect(Collectors.joining(" OR ")))
				.append(ORDER_BY_ID)
				.toString();
	
	private Function<Collection<Long>, String> buildSelectTuples = ids -> 
		new StringBuilder()
			.append("SELECT id, name, email, streetname, city, country FROM employee WHERE ('nil', id) IN (")
			.append(ids.stream().map(id -> String.format("('nil', %d)", id)).collect(Collectors.joining(",")))
			.append(")")
			.append(ORDER_BY_ID)
			.toString();
		
	private Connection getConnection() throws SQLException {
		return DriverManager.getConnection("jdbc:hsqldb:file:src/test/resources/hsqldb/qsplitter", "dzone", "dzone");
	}
	
	public void insertRandomCollection(int size) {
	
		var insertQuery = "INSERT INTO user(name, email, streetname, city, country) VALUES(?, ?, ?, ?, ?)";
		
		try (var stmt = this.getConnection().prepareStatement(insertQuery)) {
			
			Consumer<User> addBatchUser = user -> {
				try {
					stmt.setString(1, user.getName());
					stmt.setString(2, user.getEmail());
					stmt.setString(3, user.getStreetName());
					stmt.setString(4, user.getCity());
					stmt.setString(5, user.getCountry());
					stmt.addBatch();
				} catch (SQLException ex) {
					throw new SQLRuntimeException(ex);
				}
			};
			
			var faker = new Faker();
			
			IntStream.range(0, size)
				.mapToObj(i -> new User(null, faker.name().fullName(), faker.internet().emailAddress(), 
						faker.address().streetName(), faker.address().cityName(), faker.country().name()))
				.forEach(addBatchUser);
			
			stmt.executeBatch();
			
		} catch (Exception e) {
			throw new SQLRuntimeException(e);
		}
	}
	
	public List<User> findAll() {
		
		var selectQuery = "SELECT id, name, email, streetname, city, country FROM user ORDER BY id";
		
		try (var rs = this.getConnection().createStatement().executeQuery(selectQuery)) {
			var users = new ArrayList<User>();
			this.resultSetToUsers(rs, users);
			return users;
		} catch (Exception e) {
			throw new SQLRuntimeException(e);
		}
	}
	
	public List<User> findByIds(Collection<Long> ids) {
		
		try (var rs = this.getConnection().prepareStatement(buildSimpleSelectIn.apply(ids)).executeQuery()) {
			var users = new ArrayList<User>();
			this.resultSetToUsers(rs, users);
			return users;
		} catch (Exception e) {
			throw new SQLRuntimeException(e);
		}
	}
	
	public List<User> findByDisjunctionsOfExpressionLists(List<List<List<Long>>> idsList) {
		
		var users = new ArrayList<User>();
		
		try (var conn = this.getConnection()) {
			
			for (var ids : idsList) {
				
				try (var rs = conn.prepareStatement(buildSelectDisjunctions.apply(ids)).executeQuery()) {
					this.resultSetToUsers(rs, users);
				} 
			}
		} catch (Exception e) {
			throw new SQLRuntimeException(e);
		}
		
		return users;
		
	}
	
	public List<User> findByDisjunctionsOfIds(List<List<Long>> idsList) {
		
		var users = new ArrayList<User>();
		
		try (var conn = this.getConnection()) {
			
			for (var ids : idsList) {
				
				try (var rs = conn.prepareStatement(buildSelectOfDisjunctions.apply(ids)).executeQuery()) {
					this.resultSetToUsers(rs, users);
				} 
			}
		} catch (Exception e) {
			throw new SQLRuntimeException(e);
		}
		
		return users;
	}
	
	public List<User> findByTemporaryTable(List<Long> ids) {
		
		var queryInsertTempTable = "INSERT INTO id (user_id) VALUES (?)";
		var querySelectUsers = """
				SELECT id, name, email, streetname, city, country 
				FROM user JOIN user_id ON id = user_id ORDER BY id
				""";
		
		try (var conn = this.getConnection()) {
			
			try (var pstmt = conn.prepareStatement(queryInsertTempTable)) {
				
				for (var id : ids) {
					pstmt.setLong(1, id);
					pstmt.addBatch();
				}
				
				pstmt.executeBatch();
			}
			
			var users = new ArrayList<User>();
			
			try (var rs = conn.prepareStatement(querySelectUsers).executeQuery()) {
				this.resultSetToUsers(rs, users);
			}
			
			return users;
			
		} catch (Exception e) {
			throw new SQLRuntimeException(e);
		}
	}
	
	public List<User> findByUnionAll(List<List<Long>> idsList) {
		
		var query = idsList
				.stream()
				.map(buildSimpleSelectIn)
				.map(q -> StringUtils.removeEnd(q, ORDER_BY_ID))
				.collect(Collectors.joining(" UNION ALL "))
				.concat(ORDER_BY_ID);
		
		try (var rs = this.getConnection().prepareStatement(query).executeQuery()) {
			var users = new ArrayList<User>();
			this.resultSetToUsers(rs, users);
			return users;
		} catch (Exception e) {
			throw new SQLRuntimeException(e);
		}
	}
	
	public List<User> findByTuples(List<List<Long>> idsList) {
		
		var users = new ArrayList<User>();
		
		try (var conn = this.getConnection()) {
			
			for (var ids : idsList) {
				
				try (var rs = conn.prepareStatement(buildSelectTuples.apply(ids)).executeQuery()) {
				
					this.resultSetToUsers(rs, users);
				} 
			}
		} catch (Exception e) {
			throw new SQLRuntimeException(e);
		}
		
		return users;
	}
	
	private void resultSetToUsers(ResultSet rs, List<User> users) throws SQLException {
		while (rs.next()) {
			users.add(new User(rs.getLong(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6)));
		}
	}
	
}
