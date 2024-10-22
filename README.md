what i changed: +added sell price and discount price columns in sql database instead of price one
in ProductDAO:
+ changed the addproduct to include the new parameters: purchase_price and sell_price
+ changed the purchaseproduct method to change costs based on purchase_price (not price) 
+ changed the sellproduct method to change incomes based on sell_price


TO DO:
code the applyDiscount & removeDiscount -> changing the discount column to discount column (decimal), first the discount = 0, then, when applying a discount, discount is updated from 0 to x price thanks to the sell_price and a set amount of discount based on the category. to stop a discount, we just set discount = 0.
To make this work, we need to modify the sellProduct method to check if discount = 0 or not. if discount = 0, we use sell_price, else, we use discount.
fix capital/incomes/costs
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
  `discount` decimal(10,2) DEFAULT '0.00',
  `incomes` decimal(10,2) DEFAULT '0.00',
  `costs` decimal(10,2) DEFAULT '0.00',
  PRIMARY KEY (`id`),
  KEY `idx_name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
