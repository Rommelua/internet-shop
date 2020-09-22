package com.internet.shop.dao.jdbc;

import com.internet.shop.dao.UserDao;
import com.internet.shop.exception.DataProcessingException;
import com.internet.shop.lib.Dao;
import com.internet.shop.model.Role;
import com.internet.shop.model.User;
import com.internet.shop.util.ConnectionUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Dao
public class UserDaoJdbcImpl implements UserDao {
    @Override
    public User create(User user) {
        try (Connection connection = ConnectionUtil.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO users(name, login, password) VALUES (?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, user.getName());
            statement.setString(2, user.getLogin());
            statement.setString(3, user.getPassword());
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                user.setId(resultSet.getLong("id"));
            }
            addRolesToDB(user, connection);
            return user;
        } catch (SQLException e) {
            throw new DataProcessingException("Creation user " + user + " failed", e);
        }
    }

    @Override
    public Optional<User> getById(Long id) {
        try (Connection connection = ConnectionUtil.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM users u "
                    + "JOIN users_roles ur ON u.id = ur.user_id "
                    + "JOIN roles r on ur.role_id = r.id "
                    + "WHERE u.id = ? AND deleted = FALSE");
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(getUserFromResultSet(resultSet));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new DataProcessingException("Getting user with id " + id + " failed", e);
        }
    }

    @Override
    public Optional<User> getByLogin(String login) {
        try (Connection connection = ConnectionUtil.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM users u "
                    + "JOIN users_roles ur ON u.id = ur.user_id "
                    + "JOIN roles r on ur.role_id = r.id "
                    + "WHERE u.login = ? AND deleted = FALSE");
            statement.setString(1, login);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(getUserFromResultSet(resultSet));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new DataProcessingException("Getting user with login " + login + " failed", e);
        }
    }

    @Override
    public List<User> getAll() {
        try (Connection connection = ConnectionUtil.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM users u "
                    + "JOIN users_roles ur ON u.id = ur.user_id "
                    + "JOIN roles r on ur.role_id = r.id "
                    + "WHERE deleted = FALSE ORDER BY u.id",
                    ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet resultSet = statement.executeQuery();
            List<User> allUsers = new ArrayList<>();
            while (resultSet.next()) {
                allUsers.add(getUserFromResultSet(resultSet));
                resultSet.previous();
            }
            return allUsers;
        } catch (SQLException e) {
            throw new DataProcessingException("Can not get items from DB", e);
        }
    }

    @Override
    public User update(User user) {
        try (Connection connection = ConnectionUtil.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(
                    "UPDATE users SET name = ?, login = ?, password = ? "
                    + "WHERE id = ? AND deleted = FALSE");
            statement.setString(1, user.getName());
            statement.setString(2, user.getLogin());
            statement.setString(3, user.getPassword());
            statement.setLong(4, user.getId());
            statement.executeUpdate();
            deleteRolesFromDB(user.getId(), connection);
            addRolesToDB(user, connection);
            return user;
        } catch (SQLException e) {
            throw new DataProcessingException("Updating user " + user + " failed", e);
        }
    }

    @Override
    public boolean deleteById(Long id) {
        try (Connection connection = ConnectionUtil.getConnection()) {
            deleteRolesFromDB(id, connection);
            PreparedStatement statement = connection.prepareStatement(
                    "UPDATE users SET deleted = TRUE WHERE id = ?");
            statement.setLong(1, id);
            return statement.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new DataProcessingException("Deleting user with id " + id + " failed", e);
        }
    }

    private void addRolesToDB(User user, Connection connection) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO users_roles VALUES (?, ?)");
        statement.setLong(1, user.getId());
        for (Role role : user.getRoles()) {
            statement.setLong(2, role.getRoleName().ordinal() + 1);
            statement.executeUpdate();
        }
    }

    private void deleteRolesFromDB(Long userId, Connection connection) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(
                "DELETE FROM users_roles WHERE user_id = ?");
        statement.setLong(1, userId);
        statement.executeUpdate();
    }

    private User getUserFromResultSet(ResultSet resultSet) throws SQLException {
        long id = resultSet.getLong("id");
        String name = resultSet.getString("name");
        String login = resultSet.getString("login");
        String password = resultSet.getString("password");
        Set<Role> roles = new HashSet<>();
        do {
            roles.add(Role.of(resultSet.getString("role_name")));
        } while (resultSet.next() && id == resultSet.getLong("id"));
        return new User(id, name, login, password, roles);
    }
}
