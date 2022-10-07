package com.caveofprogramming.db.mysql;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserDaoImpl implements UserDao {

	@Override
	public void save(User u) {

		var conn = Database.instance().getConnection();

		try {
			var stmt = conn.prepareStatement("INSERT INTO user (name) VALUES (?)");

			stmt.setString(1, u.getName());

			stmt.executeUpdate();

			stmt.close();
		} catch (SQLException e) {
			throw new DaoException(e);
		}
	}

	@Override
	public Optional<User> findById(int id) {
		var conn = Database.instance().getConnection();

		try {
			var stmt = conn.prepareStatement("SELECT name FROM user where id=?");

			stmt.setInt(1, id);
			var rs = stmt.executeQuery();

			while (rs.next()) {
				var name = rs.getString("name");

				User user = new User(id, name);
				return Optional.of(user);
			}

			stmt.close();

		} catch (SQLException e) {
			throw new DaoException(e);
		}

		return Optional.empty();
	}

	@Override
	public void update(User u) {
		var conn = Database.instance().getConnection();

		try {
			var stmt = conn.prepareStatement("UPDATE user SET name=? WHERE id=?");
			stmt.setString(1, u.getName());
			stmt.setInt(2, u.getId());
			stmt.executeUpdate();
			
		} catch (SQLException e) {
			throw new DaoException(e);
		}

	}

	@Override
	public void delete(User u) {
		var conn = Database.instance().getConnection();

		try {

			var stmt = conn.prepareStatement("DELETE FROM user WHERE id=?");

			stmt.setInt(1, u.getId());

			stmt.executeUpdate();

			stmt.close();

		}

		catch (SQLException e) {
			throw new DaoException(e);
		}
	}

	@Override
	public List<User> getAll() {

		List<User> users = new ArrayList<>();

		var conn = Database.instance().getConnection();

		try {
			var stmt = conn.createStatement();

			var rs = stmt.executeQuery("SELECT id, name FROM user ORDER BY id");

			while (rs.next()) {
				var id = rs.getInt("id");
				var name = rs.getString("name");

				users.add(new User(id, name));
			}

			stmt.close();

		} catch (SQLException e) {
			throw new DaoException(e);
		}

		return users;
	}

}
