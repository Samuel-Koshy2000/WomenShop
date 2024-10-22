package org.example.proto;

public class Product {
    private int id;
    private String name;
    private String category;

    private double purchase_price;



    private double sell_price;
    private int stock;
    private boolean discount;
    private double incomes;
    private double costs;

    public Product(String name, String category, double purchase_price, double sell_price, int stock) {
        this.name = name;
        this.category = category;
        this.purchase_price = purchase_price;
        this.sell_price = sell_price;
        this.stock = stock;
        this.discount = false;
        this.incomes = 0.00;
        this.costs = 0.00;
    }

    // Getters and setters for each field
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getPurchase_price() {
        return purchase_price;
    }

    public void setPurchase_price(double purchase_price) {
        this.purchase_price = purchase_price;
    }

    public double getSell_price() {
        return sell_price;
    }

    public void setSell_price(double sell_price) {
        this.sell_price = sell_price;
    }

    /*
    public double getPurchasePrice() {
        return purchase_price;
    }

    public void setPurchasePrice(double purchase_price) {
        this.purchase_price = purchase_price;
    }
    public double getSellPrice() {
        return sell_price;
    }

    public void setSellPrice(double price) {
        this.sell_price = price;
    }


     */

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public boolean isDiscount() {
        return discount;
    }

    public void setDiscount(boolean discount) {
        this.discount = discount;
    }

    public double getIncomes() {
        return incomes;
    }

    public void setIncomes(double incomes) {
        this.incomes = incomes;
    }

    public double getCosts() {
        return costs;
    }

    public void setCosts(double costs) {
        this.costs = costs;
    }
}
