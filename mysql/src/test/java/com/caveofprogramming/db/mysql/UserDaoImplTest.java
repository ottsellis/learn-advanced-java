package com.caveofprogramming.db.mysql;

import org.junit.Test;
import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.After;
import org.junit.Before;

public class UserDaoImplTest {
	
	private Connection conn;

	@Before
	public void setUp() throws SQLException {
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
		
		assertEquals("user name does not match retrieved",user.getName(),name);
		
		stmt.close();
	}
}
