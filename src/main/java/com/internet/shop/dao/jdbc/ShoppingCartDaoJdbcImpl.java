package com.internet.shop.dao.jdbc;

import com.internet.shop.dao.ShoppingCartDao;
import com.internet.shop.exception.DataProcessingException;
import com.internet.shop.lib.Dao;
import com.internet.shop.model.Product;
import com.internet.shop.model.ShoppingCart;
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
public class ShoppingCartDaoJdbcImpl implements ShoppingCartDao {
    @Override
    public ShoppingCart create(ShoppingCart shoppingCart) {
        try (Connection connection = ConnectionUtil.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO shopping_carts(user_id) VALUES (?)",
                    Statement.RETURN_GENERATED_KEYS);
            statement.setLong(1, shoppingCart.getUserId());
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                shoppingCart.setId(resultSet.getLong("id"));
            }
            addProductsToDB(shoppingCart, connection);
            return shoppingCart;
        } catch (SQLException e) {
            throw new DataProcessingException("Creation shopping cart "
                                              + shoppingCart + " failed", e);
        }
    }

    @Override
    public Optional<ShoppingCart> getByUserId(Long userId) {
        try (Connection connection = ConnectionUtil.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM shopping_carts c "
                    + "LEFT JOIN shopping_carts_products cp ON c.id = cp.cart_id "
                    + "LEFT JOIN products p on cp.product_id = p.id "
                    + "WHERE c.user_id = ? AND c.deleted = FALSE");
            statement.setLong(1, userId);
            return getShoppingCartFromStatement(statement);
        } catch (SQLException e) {
            throw new DataProcessingException("Getting shopping cart for user with id "
                                              + userId + " failed", e);
        }
    }

    @Override
    public Optional<ShoppingCart> getById(Long id) {
        try (Connection connection = ConnectionUtil.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM shopping_carts c "
                    + "JOIN shopping_carts_products cp ON c.id = cp.cart_id "
                    + "JOIN products p on cp.product_id = p.id "
                    + "WHERE c.id = ? AND c.deleted = FALSE");
            statement.setLong(1, id);
            return getShoppingCartFromStatement(statement);
        } catch (SQLException e) {
            throw new DataProcessingException("Getting shopping cart with id "
                                              + id + " failed", e);
        }
    }

    @Override
    public List<ShoppingCart> getAll() {
        try (Connection connection = ConnectionUtil.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM shopping_carts c "
                    + "JOIN shopping_carts_products cp ON c.id = cp.cart_id "
                    + "JOIN products p on cp.product_id = p.id "
                    + "WHERE c.deleted = FALSE",
                    ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet resultSet = statement.executeQuery();
            List<ShoppingCart> carts = new ArrayList<>();
            while (resultSet.next()) {
                carts.add(getShoppingCartFromResultSet(resultSet));
                resultSet.previous();
            }
            return carts;
        } catch (SQLException e) {
            throw new DataProcessingException("Can not get items from DB", e);
        }
    }

    @Override
    public ShoppingCart update(ShoppingCart shoppingCart) {
        try (Connection connection = ConnectionUtil.getConnection()) {
            deleteProductsFromDB(shoppingCart.getId(), connection);
            addProductsToDB(shoppingCart, connection);
            PreparedStatement statement = connection.prepareStatement(
                    "UPDATE shopping_carts SET user_id = ? "
                    + "WHERE id = ? AND deleted = FALSE");
            statement.setLong(1, shoppingCart.getUserId());
            statement.setLong(2, shoppingCart.getId());
            statement.executeUpdate();
            return shoppingCart;
        } catch (SQLException e) {
            throw new DataProcessingException("Updating shopping cart "
                                              + shoppingCart + " failed", e);
        }
    }

    @Override
    public boolean deleteById(Long id) {
        try (Connection connection = ConnectionUtil.getConnection()) {
            deleteProductsFromDB(id, connection);
            PreparedStatement statement = connection.prepareStatement(
                    "UPDATE shopping_carts SET deleted = TRUE WHERE id = ?");
            statement.setLong(1, id);
            return statement.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new DataProcessingException("Deleting shopping cart with id "
                                              + id + " failed", e);
        }
    }

    private Optional<ShoppingCart> getShoppingCartFromStatement(PreparedStatement statement)
            throws SQLException {
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            return Optional.of(getShoppingCartFromResultSet(resultSet));
        }
        return Optional.empty();
    }

    private ShoppingCart getShoppingCartFromResultSet(ResultSet resultSet) throws SQLException {
        long id = resultSet.getLong("id");
        long userId = resultSet.getLong("user_id");
        List<Product> products = new ArrayList<>();
        long productId = resultSet.getLong("product_id");
        if (productId != 0) {
            do {
                products.add(new Product(resultSet.getLong("product_id"),
                        resultSet.getString("name"), resultSet.getDouble("price")));
            } while (resultSet.next() && id == resultSet.getLong("id"));
        }
        return new ShoppingCart(id, products, userId);
    }

    private void deleteProductsFromDB(Long id, Connection connection) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(
                "DELETE FROM shopping_carts_products WHERE cart_id = ?");
        statement.setLong(1, id);
        statement.executeUpdate();
    }

    private void addProductsToDB(ShoppingCart shoppingCart, Connection connection)
            throws SQLException {
        PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO shopping_carts_products VALUES (?, ?)");
        statement.setLong(1, shoppingCart.getId());
        for (Product product : shoppingCart.getProducts()) {
            statement.setLong(2, product.getId());
            statement.executeUpdate();
        }
    }
}
