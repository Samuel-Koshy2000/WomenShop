package org.example.proto;

import DatabaseConnection.*;
import org.example.proto.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {
    public List<Product> getAllProducts() throws SQLException {
        List<Product> products = new ArrayList<>();
        String query = "SELECT * FROM products";
        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                Product product = new Product(
                        resultSet.getString("name"),
                        resultSet.getString("category"),
                        resultSet.getDouble("purchase_price"),
                        resultSet.getDouble("sell_price"),
                        resultSet.getInt("stock")

                );
                product.setId(resultSet.getInt("id"));
                product.setDiscount(resultSet.getBoolean("discount"));
                product.setIncomes(resultSet.getDouble("incomes"));
                product.setCosts(resultSet.getDouble("costs"));
                products.add(product);
            }
        }
        return products;
    }

    public void addProduct(Product product) throws SQLException {
        String query = "INSERT INTO products (name, category, purchase_price, sell_price, stock, discount) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, product.getName());
            statement.setString(2, product.getCategory());
            statement.setDouble(3, product.getPurchase_price());
            statement.setDouble(4, product.getSell_price());
            statement.setInt(5, product.getStock());
            statement.setBoolean(6, product.isDiscount());
            statement.executeUpdate();
        }
    }

    public void updateProduct(Product product) throws SQLException {
        String query = "UPDATE products SET name=?, category=?, purchase_price=?, sell_price=?, stock=?, discount=? WHERE id=?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, product.getName());
            statement.setString(2, product.getCategory());
            statement.setDouble(3, product.getPurchase_price());
            statement.setDouble(4, product.getSell_price());
            statement.setInt(5, product.getStock());
            statement.setBoolean(6, product.isDiscount());
            statement.setInt(7, product.getId());
            statement.executeUpdate();
        }
    }

    public void deleteProduct(int productId) throws SQLException {
        String query = "DELETE FROM products WHERE id=?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, productId);
            statement.executeUpdate();
        }
    }

    public void sellProduct(int productId, int quantity) throws SQLException {
        String query = "UPDATE products "
                + "SET stock = stock - ?, "
                + "incomes = incomes + (CASE WHEN discount = 0 THEN sell_price * ? ELSE discount * ? END) "
                + "WHERE id=?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, quantity);
            statement.setInt(2, quantity);
            statement.setInt(3, quantity);
            statement.setInt(4, productId);
            statement.executeUpdate();
        }
    }

    public void purchaseProduct(int productId, int quantity) throws SQLException {
        String query = "UPDATE products SET stock = stock + ?, costs = costs + (purchase_price * ?) WHERE id=?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, quantity);
            statement.setInt(2, quantity);
            statement.setInt(3, productId);
            statement.executeUpdate();
        }
    }
    public void applyDiscount(int productId) throws SQLException {
        String query = "UPDATE products (CASE WHEN category = clothes SET discount = sell_price * 0.9 ELSE WHEN category = accessories SET discount = sell_price * 0.8 ELSE category = shoes SET discount = sell_price * 0.85";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, productId);
            statement.executeUpdate();
        }
    }


    public void removeDiscount(int productId) throws SQLException {
        String query = "UPDATE products SET discount = 0 WHERE id=?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, productId);
            statement.executeUpdate();
        }
    }
}

