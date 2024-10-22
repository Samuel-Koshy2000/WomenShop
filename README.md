what i changed: +added sell price and discount price columns in sql database instead of price one
in ProductDAO:
+ changed the addproduct to include the new parameters: purchase_price and sell_price
+ changed the purchaseproduct method to change costs based on purchase_price (not price) 
+ changed the sellproduct method to change incomes based on sell_price


TO DO:
change instances of price to purchase_price / sell_price depending on the case
code the applyDiscount & removeDiscount
add size and shoe_size for clothes & shoes category
