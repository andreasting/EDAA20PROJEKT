SET FOREIGN_KEY_CHECKS = 0;

-- Clearing tables
TRUNCATE TABLE Ingredient;
TRUNCATE TABLE Quantity;
TRUNCATE TABLE Cookie;
TRUNCATE TABLE StoredIn;
TRUNCATE TABLE ShippedIn;
TRUNCATE TABLE Pallet;
TRUNCATE TABLE Ordered;
TRUNCATE TABLE Company;
TRUNCATE TABLE Orders;

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

SET FOREIGN_KEY_CHECKS = 1;