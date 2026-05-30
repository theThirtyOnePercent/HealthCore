INSERT INTO TriageStatus (status_name) VALUES 
('NonUrgent'), ('SemiUrgent'), ('Urgent'), ('Emergency'), ('Immediate'), ('NotInTriage')
ON CONFLICT DO NOTHING;

INSERT INTO Network DEFAULT VALUES;

INSERT INTO Hospital (network_id, name, address, phoneNumber) VALUES
(1, 'General Central Hospital', '100 Medical Plaza, Metro City', '555-0100'),
(1, 'St. Jude Community Clinic', '450 Suburban Boulevard, Westside', '555-0200');

INSERT INTO Department (hospital_id, name, beds, totalStaffPositions) VALUES
(1, 'Emergency Medicine', 5, 10),
(1, 'Cardiology Unit', 5, 8),
(2, 'Pediatrics Ward', 5, 6);

INSERT INTO Administrator (name, surname, email, password, network_id) VALUES
('Alice', 'Smith', 'alice.smith@network.com', '$2b$12$SecureHashForAlice123!', 1),
('Bob', 'Jones', 'bob.jones@network.com', '$2b$12$SecureHashForBob456!', 1);

INSERT INTO InsurancePlan (network_id, name) VALUES
(1, 'Blue Shield Premium'),
(1, 'Medicaid Standard Plan');

INSERT INTO Doctor (name, surname, email, password, network_id, specialization, appointmentPrice) VALUES
('John', 'Doe', 'john.doe@hospital.com', '$2b$12$hash1', 1, 'ER Specialist', 150.00),
('Jane', 'Smith', 'jane.smith@hospital.com', '$2b$12$hash2', 1, 'Trauma Surgeon', 200.00),
('Robert', 'Lee', 'robert.lee@hospital.com', '$2b$12$hash3', 1, 'ER Resident', 100.00),
('Emily', 'Davis', 'emily.davis@hospital.com', '$2b$12$hash4', 1, 'Acute Care Specialist', 160.00),
('Michael', 'Brown', 'michael.brown@hospital.com', '$2b$12$hash5', 1, 'Toxicologist', 180.00),
('William', 'Wilson', 'william.w@hospital.com', '$2b$12$hash6', 1, 'Cardiologist', 250.00),
('Olivia', 'Taylor', 'olivia.t@hospital.com', '$2b$12$hash7', 1, 'Cardiovascular Surgeon', 350.00),
('James', 'Miller', 'james.m@hospital.com', '$2b$12$hash8', 1, 'Electrophysiologist', 280.00),
('Sophia', 'Garcia', 'sophia.g@hospital.com', '$2b$12$hash9', 1, 'Pediatric Cardiologist', 260.00),
('Benjamin', 'Martinez', 'ben.m@hospital.com', '$2b$12$hash10', 1, 'Heart Failure Specialist', 240.00),
('Isabella', 'Anderson', 'isabella.a@hospital.com', '$2b$12$hash11', 1, 'Pediatrician', 120.00),
('Lucas', 'Thomas', 'lucas.t@hospital.com', '$2b$12$hash12', 1, 'Neonatologist', 210.00),
('Mia', 'White', 'mia.white@hospital.com', '$2b$12$hash13', 1, 'Pediatric ER Doc', 140.00),
('Mason', 'Harris', 'mason.h@hospital.com', '$2b$12$hash14', 1, 'Child Psychologist', 175.00),
('Charlotte', 'Clark', 'charlotte.c@hospital.com', '$2b$12$hash15', 1, 'Pediatric Allergist', 130.00);

INSERT INTO Doctor_Department (doctor_id, department_id) VALUES
(1, 1), (2, 1), (3, 1), (4, 1), (5, 1),
(6, 2), (7, 2), (8, 2), (9, 2), (10, 2),
(11, 3), (12, 3), (13, 3), (14, 3), (15, 3);

INSERT INTO Equipment (department_id, equipmentType, quantity) VALUES
(1, 'Defibrillator', 4),
(1, 'Ventilator', 6),
(1, 'Trauma Gurney', 10),
(1, 'Patient Monitor', 12),
(2, 'Echocardiogram Machine', 2),
(2, 'ECG Monitor', 8),
(2, 'Oxygen Concentrator', 5),
(3, 'Infant Incubator', 4),
(3, 'Pediatric Scale', 3),
(3, 'Phototherapy Lamp', 2);

INSERT INTO Patient (name, surname, email, password, network_id, healthcareCardNumber, insurancePlan_id, triageStatus)
SELECT 
    'PatientFirst_' || i as name,
    'PatientLast_' || i as surname,
    'patient' || i || '@example.com' as email,
    '$2b$12$DummyHashedPasswordForPatient' || i as password,
    1 as network_id,
    100000 + i as healthcareCardNumber,
    (CASE WHEN i % 2 = 0 THEN 1 ELSE 2 END) as insurancePlan_id, -- Alternate between insurance plans
    (CASE 
        WHEN i % 5 = 0 THEN 'NonUrgent'
        WHEN i % 5 = 1 THEN 'SemiUrgent'
        WHEN i % 5 = 2 THEN 'Urgent'
        WHEN i % 5 = 3 THEN 'Emergency'
        ELSE 'NotInTriage'
     END) as triageStatus
FROM generate_series(1, 50) AS i;

INSERT INTO Shift (doctor_id, startTime, endTime)
SELECT 
    d.doctor_id,
    (CASE WHEN i % 2 = 0 THEN '2026-06-01 08:00:00'::timestamp ELSE '2026-06-02 14:00:00'::timestamp END),
    (CASE WHEN i % 2 = 0 THEN '2026-06-01 16:00:00'::timestamp ELSE '2026-06-02 22:00:00'::timestamp END)
FROM Doctor d
CROSS JOIN generate_series(1, 2) AS i;


INSERT INTO Appointment (patient_id, doctor_id, startDate, endDate)
SELECT 
    p.patient_id,
    -- Safely maps doctor_id sequentially between the minimum and maximum generated IDs
    (SELECT MIN(doctor_id) FROM Doctor) + (p.patient_id % (SELECT COUNT(*) FROM Doctor)),
    '2026-06-03 09:00:00'::timestamp + (p.patient_id * INTERVAL '30 minutes'),
    '2026-06-03 09:30:00'::timestamp + (p.patient_id * INTERVAL '30 minutes')
FROM Patient p;

INSERT INTO Note (appointment_id, content, sentDate)
SELECT 
    appointment_id,
    'Patient presented routine symptoms. Checked vital statistics and recorded history for clinical evaluation.',
    startDate - INTERVAL '5 minutes'
FROM Appointment;

INSERT INTO Diagnosis (appointment_id, doctor_id, patient_id, dateRecord, dateStartCondition, dateEndCondition, description)
SELECT 
    appointment_id,
    doctor_id,
    patient_id,
    endDate,
    startDate - INTERVAL '2 days',
    (CASE WHEN appointment_id % 3 = 0 THEN endDate + INTERVAL '5 days' ELSE NULL END), -- Some conditions are resolved, some ongoing
    'Clinical observation indicates standard seasonal variation or mild localized inflammation. Advised rest and routine follow-up.'
FROM Appointment;

INSERT INTO Doctor_Patient (doctor_id, patient_id)
SELECT doctor_id, patient_id FROM Appointment
ON CONFLICT DO NOTHING;

INSERT INTO Admin_Doctor (admin_id, doctor_id)
SELECT a.admin_id, d.doctor_id 
FROM Administrator a 
CROSS JOIN Doctor d;