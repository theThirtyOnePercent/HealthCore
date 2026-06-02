INSERT INTO Hospitals (name, address, phone_number) VALUES
('Stark General Hospital', 'New York, USA', '+1-555-IRON-MAN'),
('Wayne Memorial Hospital', 'Gotham City', '+1-555-BAT-CARE'),
('Hogwarts Health & Spellcare Hospital', 'Scottish Highlands', '+44-000-HP-CARE');

INSERT INTO InsurancePlans (name) VALUES
('Galactic Health Plan'),
('Gotham Care Premium'),
('MuggleCare Basic'),
('Stark Industries Coverage');

INSERT INTO Departments (hospital_id, name, beds, total_staff_positions) VALUES
(1, 'Cardiology', 40, 10),
(1, 'Robotics Surgery', 25, 8),
(2, 'Trauma Unit', 60, 15),
(2, 'Psychiatry', 30, 6),
(3, 'Spell Injuries', 20, 7),
(3, 'Potions Ward', 15, 5);

INSERT INTO Users (name, surname, email, password, role) VALUES
('Gregory', 'House', 'house@wayne-med.com', 'pwd1', 'Doctor'),
('Stephen', 'Strange', 'strange@hogwarts-med.com', 'pwd2', 'Doctor'),
('Leonard', 'McCoy', 'mccoy@stark-med.com', 'pwd3', 'Doctor'),
('Meredith', 'Grey', 'grey@wayne-med.com', 'pwd4', 'Doctor'),

('Luke', 'Skywalker', 'luke@galaxy.com', 'pwd5', 'Patient'),
('Harry', 'Potter', 'harry@hogwarts.edu', 'pwd6', 'Patient'),
('Bruce', 'Wayne', 'bruce@wayneenterprises.com', 'pwd7', 'Patient'),

('Nick', 'Fury', 'fury@shield.gov', 'pwd8', 'Administrator');

INSERT INTO Patients (patient_id, healthcare_card_number, insurance_plan_id, triage_status) VALUES
(5, 100001, 1, 'SemiUrgent'),
(6, 100002, 3, 'Urgent'),
(7, 100003, 2, 'NonUrgent');

INSERT INTO Doctors (doctor_id, department_id, specialization, appointment_price) VALUES
(1, 4, 'Diagnostic Psychiatry', 250.00),
(2, 5, 'Arcane Surgery', 500.00),
(3, 1, 'Interstellar Cardiology', 300.00),
(4, 3, 'Trauma Medicine', 220.00);

INSERT INTO Equipments (department_id, equipment_type, quantity) VALUES
(1, 'ECG Machine', 5),
(2, 'Surgical Robot', 2),
(3, 'Defibrillator', 10),
(4, 'EEG Scanner', 4),
(5, 'Wand Stabilizer', 6),
(6, 'Alchemy Kit', 3);

INSERT INTO Equipments_Doctors (equipment_id, quantity, doctor_id) VALUES
(4, 2, 1),
(5, 1, 2),
(1, 1, 3),
(3, 1, 4);

INSERT INTO Shifts (doctor_id, start_time, end_time) VALUES
(1, '2026-06-02 08:00:00', '2026-06-02 16:00:00'),
(2, '2026-06-02 10:00:00', '2026-06-02 18:00:00'),
(3, '2026-06-02 09:00:00', '2026-06-02 17:00:00'),
(4, '2026-06-02 12:00:00', '2026-06-02 20:00:00');

INSERT INTO Appointments (patient_id, doctor_id, start_date, end_date) VALUES
(5, 3, '2026-06-02 10:00:00', '2026-06-02 10:30:00'),
(6, 2, '2026-06-02 11:00:00', '2026-06-02 11:45:00'),
(7, 1, '2026-06-02 14:00:00', '2026-06-02 14:30:00');

INSERT INTO Notes (appointment_id, content, sent_date) VALUES
(1, 'Routine cardiac scan after hyperspace stress.', '2026-06-02 10:40:00'),
(2, 'Minor magical instability detected. Prescribed stabilization charms.', '2026-06-02 12:00:00'),
(3, 'Patient insists he is "the night".', '2026-06-02 15:00:00');

INSERT INTO Diagnosis (appointment_id, date_record, date_start_condition, date_end_condition, description) VALUES
(1, '2026-06-02 10:35:00', '2026-06-02 09:50:00', '2026-06-02 10:30:00', 'Mild hyperspace arrhythmia'),
(2, '2026-06-02 11:50:00', '2026-06-02 11:00:00', '2026-06-02 11:45:00', 'Chaotic spell residue imbalance'),
(3, '2026-06-02 14:40:00', '2026-06-02 14:00:00', '2026-06-02 14:30:00', 'Severe nocturnal identity fixation syndrome');