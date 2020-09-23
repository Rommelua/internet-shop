package com.internet.shop.dao.jdbc;

import com.internet.shop.dao.OrderDao;
import com.internet.shop.exception.DataProcessingException;
import com.internet.shop.lib.Dao;
import com.internet.shop.model.Order;
import com.internet.shop.model.Product;
import com.internet.shop.util.ConnectionUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Dao
public class OrderDaoJdbcImpl implements OrderDao {
    @Override
    public Order create(Order order) {
        try (Connection connection = ConnectionUtil.getConnection()) {
            String sql = "INSERT INTO orders(user_id) VALUES (?)";
            try (PreparedStatement statement = connection.prepareStatement(sql,
                    Statement.RETURN_GENERATED_KEYS)) {
                statement.setLong(1, order.getUserId());
                statement.executeUpdate();
                ResultSet resultSet = statement.getGeneratedKeys();
                if (resultSet.next()) {
                    order.setId(resultSet.getLong("id"));
                }
            }
            addProductsToDB(order, connection);
            return order;
        } catch (SQLException e) {
            throw new DataProcessingException("Creation order " + order + " failed", e);
        }
    }

    @Override
    public Optional<Order> getById(Long id) {
        try (Connection connection = ConnectionUtil.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM orders o "
                    + "JOIN orders_products op ON o.id = op.order_id "
                    + "JOIN products p on op.product_id = p.id "
                    + "WHERE o.id = ? AND o.deleted = FALSE");
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(getOrderFromResultSet(resultSet));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new DataProcessingException("Getting order with id " + id + " failed", e);
        }
    }

    @Override
    public List<Order> getUserOrders(Long userId) {
        try (Connection connection = ConnectionUtil.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM orders o "
                    + "JOIN orders_products op ON o.id = op.order_id "
                    + "JOIN products p on op.product_id = p.id "
                    + "WHERE o.user_id = ? AND o.deleted = FALSE",
                    ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            statement.setLong(1, userId);
            return getOrdersListFromStatement(statement);
        } catch (SQLException e) {
            throw new DataProcessingException("Getting orders for user with id "
                                              + userId + " failed", e);
        }
    }

    @Override
    public List<Order> getAll() {
        try (Connection connection = ConnectionUtil.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM orders o "
                    + "JOIN orders_products op ON o.id = op.order_id "
                    + "JOIN products p on op.product_id = p.id "
                    + "WHERE o.deleted = FALSE ORDER BY o.id",
                    ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            return getOrdersListFromStatement(statement);
        } catch (SQLException e) {
            throw new DataProcessingException("Can not get items from DB", e);
        }
    }

    @Override
    public Order update(Order order) {
        try (Connection connection = ConnectionUtil.getConnection()) {
            deleteProductsFromDB(order.getId(), connection);
            addProductsToDB(order, connection);
            PreparedStatement statement = connection.prepareStatement(
                    "UPDATE orders SET user_id = ? "
                    + "WHERE id = ? AND deleted = FALSE");
            statement.setLong(1, order.getUserId());
            statement.setLong(2, order.getId());
            statement.executeUpdate();
            return order;
        } catch (SQLException e) {
            throw new DataProcessingException("Updating order " + order + " failed", e);
        }
    }

    @Override
    public boolean deleteById(Long id) {
        try (Connection connection = ConnectionUtil.getConnection()) {
            deleteProductsFromDB(id, connection);
            PreparedStatement statement = connection.prepareStatement(
                    "UPDATE orders SET deleted = TRUE WHERE id = ?");
            statement.setLong(1, id);
            return statement.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new DataProcessingException("Deleting order with id " + id + " failed", e);
        }
    }

    private Order getOrderFromResultSet(ResultSet resultSet) throws SQLException {
        long id = resultSet.getLong("id");
        long userId = resultSet.getLong("user_id");
        List<Product> products = new ArrayList<>();
        do {
            products.add(new Product(resultSet.getLong("product_id"),
                    resultSet.getString("name"), resultSet.getDouble("price")));
        } while (resultSet.next() && id == resultSet.getLong("id"));
        return new Order(id, products, userId);
    }

    private List<Order> getOrdersListFromStatement(PreparedStatement statement)
            throws SQLException {
        ResultSet resultSet = statement.executeQuery();
        List<Order> orders = new ArrayList<>();
        while (resultSet.next()) {
            orders.add(getOrderFromResultSet(resultSet));
            resultSet.previous();
        }
        return orders;
    }

    private void deleteProductsFromDB(Long orderId, Connection connection) throws SQLException {
        String sql = "DELETE FROM orders_products WHERE order_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, orderId);
            statement.executeUpdate();
        }
    }

    private void addProductsToDB(Order order, Connection connection) throws SQLException {
        String sql = "INSERT INTO orders_products VALUES (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, order.getId());
            for (Product product : order.getProducts()) {
                statement.setLong(2, product.getId());
                statement.executeUpdate();
            }
        }
    }
}
