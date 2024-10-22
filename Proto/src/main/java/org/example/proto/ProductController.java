package org.example.proto;


import javafx.scene.control.*;
import org.example.proto.ProductDAO;
import org.example.proto.Product;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.SQLException;
import java.util.Optional;

public class ProductController {
    @FXML private TableView<Product> productTable;
    @FXML private TableColumn<Product, String> nameColumn;
    @FXML private TableColumn<Product, String> categoryColumn;
    @FXML private TableColumn<Product, Double> priceColumn;
    @FXML private TableColumn<Product, Integer> stockColumn;

    @FXML private TextField nameField;
    @FXML private TextField categoryField;
    @FXML private TextField priceField;
    @FXML private TextField stockField;
    @FXML private Label capitalLabel;
    @FXML private Label incomeLabel;
    @FXML private Label costLabel;


    private ProductDAO productDAO = new ProductDAO();

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    private ObservableList<Product> productList = FXCollections.observableArrayList();

    private void updateFinancialSummary() {
        double totalCapital = 0.0;
        double totalIncome = 0.0;
        double totalCost = 0.0;

        // Loop through the product list and calculate financials
        for (Product product : productList) {
            totalCapital += product.getPrice() * product.getStock();
            totalIncome += product.getIncomes();
            totalCost += product.getCosts();
        }

        // Update the UI labels
        capitalLabel.setText("Capital: $" + String.format("%.2f", totalCapital));
        incomeLabel.setText("Income: $" + String.format("%.2f", totalIncome));
        costLabel.setText("Cost: $" + String.format("%.2f", totalCost));
    }


    @FXML
    public void initialize() {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        stockColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));

        loadProducts();
    }

    private boolean validateInput() {
        if (nameField.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Form Error!", "Please enter a product name.");
            return false;
        }
        if (categoryField.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Form Error!", "Please enter a product category.");
            return false;
        }
        if (priceField.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Form Error!", "Please enter a product price.");
            return false;
        }
        if (stockField.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Form Error!", "Please enter the stock quantity.");
            return false;
        }

        try {
            Double.parseDouble(priceField.getText());  // Validate price as a double
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Form Error!", "Invalid price format. Please enter a valid number.");
            return false;
        }

        try {
            Integer.parseInt(stockField.getText());  // Validate stock as an integer
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Form Error!", "Invalid stock format. Please enter a valid number.");
            return false;
        }

        return true;  // Return true if all validations pass
    }



    private void loadProducts() {
        try {
            productList.setAll(productDAO.getAllProducts());
            productTable.setItems(productList);
            updateFinancialSummary(); // Update financials whenever products are loaded
        } catch (SQLException e) {
            e.printStackTrace(); // Handle exceptions properly in production code
        }
    }

    @FXML
    public void handleAddProduct() {
        // Validate input before attempting to add a product
        if (validateInput()) {
            String name = nameField.getText();
            String category = categoryField.getText();
            double price = Double.parseDouble(priceField.getText());
            int stock = Integer.parseInt(stockField.getText());

            Product newProduct = new Product(name, category, price, stock);
            try {
                productDAO.addProduct(newProduct);  // Add product to database
                loadProducts();  // Refresh product list
            } catch (SQLException e) {
                e.printStackTrace();  // Handle SQL exceptions properly
            }
        }
    }

    @FXML
    public void handleSaveUpdatedProduct() {
        Product selectedProduct = productTable.getSelectionModel().getSelectedItem();
        if (selectedProduct != null) {
            // Validate input before updating the product
            if (validateInput()) {
                selectedProduct.setName(nameField.getText());
                selectedProduct.setCategory(categoryField.getText());
                selectedProduct.setPrice(Double.parseDouble(priceField.getText()));
                selectedProduct.setStock(Integer.parseInt(stockField.getText()));

                try {
                    productDAO.updateProduct(selectedProduct);  // Update product in the database
                    loadProducts();  // Refresh product list
                    clearFields();   // Clear input fields after saving
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Update Error", "No product selected for update.");
        }
    }

    @FXML
    public void handleDeleteProduct() {
        Product selectedProduct = productTable.getSelectionModel().getSelectedItem();
        if (selectedProduct != null) {
            try {
                productDAO.deleteProduct(selectedProduct.getId());
                loadProducts();
            } catch (SQLException e) {
                e.printStackTrace(); // Handle exceptions properly in production code
            }
        }
    }

    @FXML
    public void handleSellProduct() {
        Product selectedProduct = productTable.getSelectionModel().getSelectedItem();
        if (selectedProduct != null) {
            int quantity = getQuantityFromUser("Sell Product", "Enter quantity to sell:");
            if (quantity > 0 && quantity <= selectedProduct.getStock()) {
                try {
                    productDAO.sellProduct(selectedProduct.getId(), quantity);  // Update stock and income
                    loadProducts();  // Refresh product list and update financials
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else {
                showAlert(Alert.AlertType.ERROR, "Invalid Quantity", "Quantity must be between 1 and the current stock.");
            }
        }
    }

    @FXML
    public void handlePurchaseProduct() {
        Product selectedProduct = productTable.getSelectionModel().getSelectedItem();
        if (selectedProduct != null) {
            int quantity = getQuantityFromUser("Purchase Product", "Enter quantity to purchase:");
            if (quantity > 0) {
                try {
                    productDAO.purchaseProduct(selectedProduct.getId(), quantity);  // Update stock and cost
                    loadProducts();  // Refresh product list and update financials
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private int getQuantityFromUser(String title, String message) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle(title);
        dialog.setHeaderText(null);
        dialog.setContentText(message);
        Optional<String> result = dialog.showAndWait();

        if (result.isPresent()) {
            try {
                return Integer.parseInt(result.get());  // Parse the user input to an integer
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.ERROR, "Invalid Input", "Please enter a valid number.");
            }
        }
        return 0;  // Default value if input is invalid
    }

    @FXML
    public void handleApplyDiscount() {
        Product selectedProduct = productTable.getSelectionModel().getSelectedItem();
        if (selectedProduct != null) {
            try {
                productDAO.applyDiscount(selectedProduct.getId());
                loadProducts(); // Refresh product list
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void handleRemoveDiscount() {
        Product selectedProduct = productTable.getSelectionModel().getSelectedItem();
        if (selectedProduct != null) {
            try {
                productDAO.removeDiscount(selectedProduct.getId());
                loadProducts();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void clearFields() {
        nameField.clear();
        categoryField.clear();
        priceField.clear();
        stockField.clear();
    }

    // Additional methods for selling, purchasing, and updating products can be added similarly.
}
