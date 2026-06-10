INSERT INTO Hospitals (name, address, phone_number) VALUES
('St. Mary Hospital', '123 Main St, New York', '+1-212-555-1001'),
('Central Medical Center', '456 Oak Ave, Chicago', '+1-312-555-1002'),
('Green Valley Hospital', '789 Pine Rd, Denver', '+1-303-555-1003'),
('Riverside General', '321 River St, Seattle', '+1-206-555-1004'),
('Sunrise Health Center', '654 Sunset Blvd, Los Angeles', '+1-310-555-1005');

INSERT INTO Departments (hospital_id, name, beds, total_staff_positions) VALUES
(1, 'Cardiology', 50, 30),
(2, 'Emergency', 80, 45),
(3, 'Neurology', 40, 25),
(4, 'Orthopedics', 35, 20),
(5, 'Pediatrics', 60, 35);

INSERT INTO InsurancePlans (name) VALUES
('Basic Care'),
('Silver Health'),
('Gold Health'),
('Premium Plus'),
('Family Coverage');

INSERT INTO Insurances_Hospitals (insurance_plan_id, hospital_id) VALUES
(1, 1),
(2, 2),
(3, 3),
(4, 4),
(5, 5);

INSERT INTO Insurances_Hospitals (insurance_plan_id, hospital_id) VALUES
(1, 2),
(2, 3),
(3, 4),
(4, 5),
(5, 1);

INSERT INTO Equipments (department_id, equipment_type, quantity) VALUES
(1, 'ECG Machine', 10),
(2, 'Ventilator', 25),
(3, 'MRI Scanner', 3),
(4, 'X-Ray Machine', 5),
(5, 'Incubator', 12);