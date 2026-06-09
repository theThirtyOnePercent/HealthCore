
INSERT INTO Users (name, surname, email, password, role)
SELECT 'Ulzii', 'ulzii', 'ulzii@test.com', 'testpassword', 'Patient'
WHERE NOT EXISTS (SELECT 1 FROM Users WHERE email = 'ulzii@test.com');