package com.caveofprogramming.db.mysql;

import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.After;
import org.junit.Before;

public class UserDaoImplTest {

	private Connection conn;
	private List<User> users;

	private static final int NUM_TEST_USERS = 1000;

	private List<User> loadUsers() throws IOException {

		// @formatter:off
		return Files
			.lines(Paths.get("../greatexpectations.txt"))
			.map(line -> line.split("[^A-Za-z]"))
			.map(Arrays::asList)
			.flatMap(list -> list.stream())
			.filter(word -> word.length() > 3 && word.length() < 20)
			.map(word -> new User(word))
			.limit(NUM_TEST_USERS)
			.collect(Collectors.toList());
			
		// @formatter:on
	}

	@Before
	public void setUp() throws SQLException, IOException {

		users = loadUsers();

		var props = Profile.getProperties("db");

		var db = Database.instance();

		db.connect(props);

		conn = db.getConnection();
		conn.setAutoCommit(false);
	}

	@After
	public void tearDown() throws SQLException {
		Database.instance().close();
	}
	
	private int getMaxId() throws SQLException {
		
		var stmt = conn.createStatement();
		
		var rs = stmt.executeQuery("SELECT MAX(id) AS id FROM user");
		
		rs.next();
		
		var id = rs.getInt("id");
	
		stmt.close();
		
		return id;
	}
	
	@Test
	public void testSaveMultiple() throws SQLException {
		UserDao userDao = new UserDaoImpl();
		
		for(var u: users) {
			userDao.save(u);
		}
		
		var maxId = getMaxId();
		
		System.out.println(maxId);
	}

	@Test
	public void testSave() throws SQLException {
		User user = new User("TestUserDaoImpl");

		UserDao userDao = new UserDaoImpl();

		userDao.save(user);
		var stmt = conn.createStatement();

		var rs = stmt.executeQuery("SELECT id, name FROM user ORDER BY id DESC");

		var result = rs.next();

		assertTrue("cannot retrieve inserted user", result);

		String name = rs.getString("name");

		assertEquals("user name does not match retrieved", user.getName(), name);

		stmt.close();
	}
}
