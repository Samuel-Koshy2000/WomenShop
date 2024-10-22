what i changed: +added sell price and discount price columns in sql database instead of price one
in ProductDAO:
+ changed the addproduct to include the new parameters: purchase_price and sell_price
+ changed the purchaseproduct method to change costs based on purchase_price (not price) 
+ changed the sellproduct method to change incomes based on sell_price


TO DO:
code the applyDiscount & removeDiscount
add size and shoe_size for clothes & shoes category
Add size & shoe_size column too. 

SQL query for product database (need to add size & shoe_size)
CREATE TABLE `products` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `category` varchar(100) DEFAULT NULL,
  `purchase_price` decimal(10,2) DEFAULT NULL,
  `sell_price` decimal(10,2) DEFAULT NULL,
  `stock` int DEFAULT NULL,
  `discount` tinyint(1) DEFAULT '0',
  `incomes` decimal(10,2) DEFAULT '0.00',
  `costs` decimal(10,2) DEFAULT '0.00',
  PRIMARY KEY (`id`),
  KEY `idx_name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

SQL query for discount database
CREATE TABLE `discount` (
  `id` varchar(45) NOT NULL,
  `discount` tinyint DEFAULT NULL,
  `product_name` varchar(45) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_product_name` (`product_name`),
  CONSTRAINT `fk_product_name` FOREIGN KEY (`product_name`) REFERENCES `products` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
