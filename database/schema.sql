CREATE TABLE TriageStatus (
    status_name VARCHAR(20) PRIMARY KEY
);

CREATE TABLE Network (
    network_id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY
);

CREATE TABLE Hospital (
    hospital_id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    network_id INT NOT NULL,
    name VARCHAR(255) NOT NULL,
    address VARCHAR(255),
    phoneNumber VARCHAR(50),
    CONSTRAINT fk_hospital_network FOREIGN KEY (network_id) REFERENCES Network(network_id)
);

CREATE TABLE Department (
    department_id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    hospital_id INT NOT NULL,
    name VARCHAR(255) NOT NULL,
    beds INT,
    totalStaffPositions INT,
    CONSTRAINT fk_department_hospital FOREIGN KEY (hospital_id) REFERENCES Hospital(hospital_id) ON DELETE CASCADE
);

CREATE TABLE InsurancePlan (
    insurancePlan_id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    network_id INT NOT NULL,
    name VARCHAR(255) NOT NULL,
    CONSTRAINT fk_insurance_network FOREIGN KEY (network_id) REFERENCES Network(network_id)
);

CREATE TABLE Patient (
    patient_id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    surname VARCHAR(50) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    network_id INT NOT NULL,
    healthcareCardNumber INT UNIQUE,
    insurancePlan_id INT,
    triageStatus VARCHAR(20) DEFAULT 'NotInTriage',
    CONSTRAINT fk_patient_network FOREIGN KEY (network_id) REFERENCES Network(network_id),
    CONSTRAINT fk_patient_insurance FOREIGN KEY (insurancePlan_id) REFERENCES InsurancePlan(insurancePlan_id) ON DELETE SET NULL,
    CONSTRAINT fk_patient_triage FOREIGN KEY (triageStatus) REFERENCES TriageStatus(status_name)
);

CREATE TABLE Doctor (
    doctor_id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    surname VARCHAR(100) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    network_id INT NOT NULL,
    specialization VARCHAR(100),
    appointmentPrice DOUBLE PRECISION, -- Changed from DOUBLE to DOUBLE PRECISION
    CONSTRAINT fk_doctor_network FOREIGN KEY (network_id) REFERENCES Network(network_id)
);

CREATE TABLE Administrator (
    admin_id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    surname VARCHAR(100) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    network_id INT NOT NULL,
    CONSTRAINT fk_admin_network FOREIGN KEY (network_id) REFERENCES Network(network_id)
);

CREATE TABLE Equipment (
    equipment_id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    department_id INT NOT NULL,
    equipmentType VARCHAR(100),
    quantity INT,
    CONSTRAINT fk_equipment_department FOREIGN KEY (department_id) REFERENCES Department(department_id) ON DELETE CASCADE
);

CREATE TABLE Shift (
    shift_id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    doctor_id INT NOT NULL,
    startTime TIMESTAMP, -- Changed from DATETIME to TIMESTAMP
    endTime TIMESTAMP,   -- Changed from DATETIME to TIMESTAMP
    CONSTRAINT fk_shift_doctor FOREIGN KEY (doctor_id) REFERENCES Doctor(doctor_id) ON DELETE CASCADE
);

CREATE TABLE Appointment (
    appointment_id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    patient_id INT NOT NULL,
    doctor_id INT NOT NULL,
    startDate TIMESTAMP, -- Changed from DATETIME to TIMESTAMP
    endDate TIMESTAMP,   -- Changed from DATETIME to TIMESTAMP
    CONSTRAINT fk_appointment_patient FOREIGN KEY (patient_id) REFERENCES Patient(patient_id),
    CONSTRAINT fk_appointment_doctor FOREIGN KEY (doctor_id) REFERENCES Doctor(doctor_id)
);

CREATE TABLE Note (
    note_id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    appointment_id INT UNIQUE NOT NULL,
    content TEXT,
    sentDate TIMESTAMP, -- Changed from DATETIME to TIMESTAMP
    CONSTRAINT fk_note_appointment FOREIGN KEY (appointment_id) REFERENCES Appointment(appointment_id) ON DELETE CASCADE
);

CREATE TABLE Diagnosis (
    diagnosis_id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    appointment_id INT NOT NULL,
    doctor_id INT NOT NULL,
    patient_id INT NOT NULL,
    dateRecord TIMESTAMP,         -- Changed from DATETIME to TIMESTAMP
    dateStartCondition TIMESTAMP,  -- Changed from DATETIME to TIMESTAMP
    dateEndCondition TIMESTAMP,    -- Changed from DATETIME to TIMESTAMP
    description TEXT,
    CONSTRAINT fk_diagnosis_appointment FOREIGN KEY (appointment_id) REFERENCES Appointment(appointment_id) ON DELETE CASCADE,
    CONSTRAINT fk_diagnosis_doctor FOREIGN KEY (doctor_id) REFERENCES Doctor(doctor_id),
    CONSTRAINT fk_diagnosis_patient FOREIGN KEY (patient_id) REFERENCES Patient(patient_id)
);

CREATE TABLE Doctor_Patient (
    doctor_id INT NOT NULL,
    patient_id INT NOT NULL,
    PRIMARY KEY (doctor_id, patient_id),
    CONSTRAINT fk_dp_doctor FOREIGN KEY (doctor_id) REFERENCES Doctor(doctor_id) ON DELETE CASCADE,
    CONSTRAINT fk_dp_patient FOREIGN KEY (patient_id) REFERENCES Patient(patient_id) ON DELETE CASCADE
);

CREATE TABLE Doctor_Department (
    doctor_id INT NOT NULL,
    department_id INT NOT NULL,
    PRIMARY KEY (doctor_id, department_id),
    CONSTRAINT fk_dd_doctor FOREIGN KEY (doctor_id) REFERENCES Doctor(doctor_id) ON DELETE CASCADE,
    CONSTRAINT fk_dd_department FOREIGN KEY (department_id) REFERENCES Department(department_id) ON DELETE CASCADE
);

CREATE TABLE Admin_Doctor (
    admin_id INT NOT NULL,
    doctor_id INT NOT NULL,
    PRIMARY KEY (admin_id, doctor_id),
    CONSTRAINT fk_ad_admin FOREIGN KEY (admin_id) REFERENCES Administrator(admin_id) ON DELETE CASCADE,
    CONSTRAINT fk_ad_doctor FOREIGN KEY (doctor_id) REFERENCES Doctor(doctor_id) ON DELETE CASCADE
);