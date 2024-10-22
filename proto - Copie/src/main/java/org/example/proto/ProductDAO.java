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
        String query = "UPDATE products SET name=?, category=?, price=?, stock=?, discount=? WHERE id=?";
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
        String query = "UPDATE products SET stock = stock - ?, incomes = incomes + (price * ?) WHERE id=?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, quantity);
            statement.setInt(2, quantity);
            statement.setInt(3, productId);
            statement.executeUpdate();
        }
    }

    public void purchaseProduct(int productId, int quantity) throws SQLException {
        String query = "UPDATE products SET stock = stock + ?, costs = costs + (price * ?) WHERE id=?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, quantity);
            statement.setInt(2, quantity);
            statement.setInt(3, productId);
            statement.executeUpdate();
        }
    }
    public void applyDiscount(int productId) throws SQLException {
        // Step 1: Check if the product already has a discount applied
        String selectQuery = "SELECT category, sell_price, discount FROM products WHERE id = ?";
        String updateQuery = "UPDATE products SET discount = TRUE, sell_price = ? WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement selectStatement = connection.prepareStatement(selectQuery);
             PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {

            // Fetch product information
            selectStatement.setInt(1, productId);
            ResultSet resultSet = selectStatement.executeQuery();

            if (resultSet.next()) {
                String category = resultSet.getString("category");
                double sellPrice = resultSet.getDouble("sell_price");
                boolean discount = resultSet.getBoolean("discount");

                // If the product already has a discount, do not apply it again
                if (discount) {
                    System.out.println("Discount has already been applied to this product.");
                    return;
                }

                // Step 2: Calculate the discount based on the category
                double discountPercentage = 0.0;
                if ("clothes".equalsIgnoreCase(category)) {
                    discountPercentage = 0.10;  // 10% discount
                } else if ("accessories".equalsIgnoreCase(category)) {
                    discountPercentage = 0.05;  // 5% discount
                } else if ("shoes".equalsIgnoreCase(category)) {
                    discountPercentage = 0.15;  // 15% discount
                }

                // Step 3: Apply the discount and update the sell_price
                double newSellPrice = sellPrice - (sellPrice * discountPercentage);

                // Step 4: Update the database with the new price and set discount to TRUE
                updateStatement.setDouble(1, newSellPrice);  // New price after discount
                updateStatement.setInt(2, productId);        // Product ID
                updateStatement.executeUpdate();

                System.out.println("Discount applied successfully to product with ID: " + productId);
            } else {
                System.out.println("Product with ID: " + productId + " not found.");
            }
        }
    }


    public void removeDiscount(int productId) throws SQLException {
        String query = "UPDATE products SET discount = FALSE WHERE id=?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, productId);
            statement.executeUpdate();
        }
    }
}

