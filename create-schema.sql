SET FOREIGN_KEY_CHECKS = 0;

-- Drop old tables
DROP TABLE IF EXISTS Ingredient;
DROP TABLE IF EXISTS Quantity;
DROP TABLE IF EXISTS Cookie;
DROP TABLE IF EXISTS StoredIn;
DROP TABLE IF EXISTS ShippedIn;
DROP TABLE IF EXISTS Pallet;
DROP TABLE IF EXISTS Ordered;
DROP TABLE IF EXISTS Company;
DROP TABLE IF EXISTS Orders;
-- SET FOREIGN_KEY_CHECKS = 1;



-- Create tables
CREATE TABLE Ingredient ( 
    IngredientName VARCHAR(200), 
    StoredAmount INT, 
    LastDelivery DATETIME, 
    QuantityOrdered INT, 
    Unit VARCHAR(2), 
    PRIMARY KEY (IngredientName), 
    CHECK(StoredAmount >=0) );

CREATE TABLE Cookie (
    CookieName VARCHAR(200),
    PRIMARY KEY (CookieName)
);


CREATE TABLE Quantity (
    CookieName VARCHAR(200),
    IngredientName VARCHAR(200),
    IngAmount INT,
    PRIMARY KEY (IngredientName, CookieName), 
    FOREIGN KEY (CookieName) REFERENCES Cookie(CookieName),
    FOREIGN KEY (IngredientName) REFERENCES Ingredient(IngredientName) 
);

CREATE TABLE StoredIn (
    CookieName VARCHAR(200),
    PalletNumber INT,
    PRIMARY KEY (CookieName, PalletNumber ),
    FOREIGN KEY (CookieName) REFERENCES  Cookie(CookieName),
    FOREIGN KEY (PalletNumber) REFERENCES  Pallet(PalletNumber)
);

CREATE TABLE ShippedIn(
    OrderNumber INT,
    PalletNumber INT,
    PRIMARY KEY (OrderNumber, PalletNumber ),
    FOREIGN KEY (OrderNumber) REFERENCES  Orders(OrderNumber),
    FOREIGN KEY (PalletNumber) REFERENCES  Pallet(PalletNumber)
);

CREATE TABLE Pallet(
    PalletNumber int NOT NULL AUTO_INCREMENT,
    ProductName VARCHAR(200),
    TimeOfProduction DATETIME,
    PalletLocation VARCHAR(200),
    Blocked TINYINT(1),
    PRIMARY KEY (PalletNumber)
);

CREATE TABLE Ordered(
    OrderNumber INT,
    CookieName VARCHAR(30),
    PRIMARY KEY (CookieName, OrderNumber),
    FOREIGN KEY (OrderNumber) REFERENCES Orders(OrderNumber),
    FOREIGN KEY (CookieName) REFERENCES Cookie(CookieName)
);

create table Company(
    CompanyName VARCHAR(200),
    address VARCHAR(200),
    phoneNbr INT(10),
    PRIMARY KEY(CompanyName) 
);

create table Orders(
    OrderNumber INT NOT NULL AUTO_INCREMENT,
    CompanyName VARCHAR(200),
    shippedDate DATETIME,
    PRIMARY KEY(OrderNumber),
    FOREIGN KEY(CompanyName) references Company(CompanyName)
);

SET FOREIGN_KEY_CHECKS = 1;