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

-- Insert start values
INSERT INTO Ingredient(IngredientName, storedAmount, Unit) VALUES
('Bread Crumbs', 500000, 'g' ),
('Butter', 500000, 'g'),
('Chocolate', 500000, 'g'),
('Chopped almonds', 500000, 'g'),
('Cinnamon', 500000, 'g'),
('Egg whites', 500000,'ml'),
('Eggs', 500000,'g'),
('Fine-ground nuts', 500000,'g'),
('Flour', 500000,'g'),
('Ground, roasted nuts', 500000,'g'),
('Icing sugar', 500000,'g'),
('Marzipan', 500000,'g'),
('Potato starch',500000,'g'),
('Roasted, chopped nuts', 500000,'g'),
('Sodium bicarbonate', 500000,'g'),
('Sugar', 500000,'g'),
('Vanilla sugar', 500000, 'g'),
('Vanilla', 500000, 'g'),
('Wheat flour', 500000, 'g');


INSERT INTO Quantity VALUES 
('Almond delight', 'Butter', 400),
('Almond delight', 'Chopped almonds', 279),
('Almond delight', 'Cinnamon', 10),
('Almond delight', 'Flour', 400),
('Almond delight', 'Sugar', 270),
('Amneris', 'Butter', 250),
('Amneris', 'Eggs', 250),
('Amneris', 'Marzipan', 750),
('Amneris', 'Potato starch', 25),
('Amneris', 'Wheat flour', 25),
('Nut Cookie', 'Fine-ground nuts', 750),
('Nut Cookie', 'Ground, roasted nuts', 625),
('Nut Cookie', 'Bread Crumbs', 125),
('Nut Cookie', 'Sugar', 375),
('Nut Cookie', 'Egg whites', 350),
('Nut Cookie', 'Chocolate', 50),
('Berliner', 'Butter', 250),
('Berliner','Chocolate', 50),
('Berliner','Eggs', 50),
('Berliner','Flour', 350),
('Berliner','Icing sugar', 100),
('Berliner','Vanilla sugar', 5),
('Nut ring','Butter',450),
('Nut ring','Flour',450),
('Nut ring','Icing sugar',190),
('Nut ring','Roasted, chopped nuts',225),
('Tango' ,'Butter', 200),
('Tango', 'Flour', 300),
('Tango', 'Sodium bicarbonate', 4),
('Tango', 'Sugar', 250),
('Tango', 'Vanilla', 2);


INSERT INTO Cookie(cookieName) VALUES 
('Almond delight'),
('Amneris'),
('Berliner'),
('Nut cookie'),
('Nut ring'),
('Tango');

INSERT INTO Company(companyName,address) VALUES
('Bjudkakor AB', 'Ystad'),
('Finkakor AB', 'Helsingborg'),
('G?stkakor AB', 'H?ssleholm'),
('Kaffebr?d AB', 'Landskrona'),
('Kalaskakor AB', 'Trelleborg'),
('Partykakor AB', 'Kristianstad'),
('Sk?nekakor AB', 'Perstorp'),
('Sm?br?d AB', 'Malm?');

SET FOREIGN_KEY_CHECKS = 1;