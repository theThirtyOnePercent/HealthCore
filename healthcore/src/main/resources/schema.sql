CREATE TABLE Hospitals (
    hospital_id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    address VARCHAR(255),
    phone_number VARCHAR(50)
);

CREATE TABLE Departments (
    department_id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    hospital_id INT NOT NULL,
    name VARCHAR(255) NOT NULL,
    beds INT,
    total_staff_positions INT,
    CONSTRAINT fk_department_hospital FOREIGN KEY (hospital_id) REFERENCES Hospitals(hospital_id) ON DELETE CASCADE
);

CREATE TABLE InsurancePlans (
    insurance_plan_id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE Insurances_Hospitals (
    insurance_plan_id INT,
    hospital_id INT,
    CONSTRAINT fk_insurance_hospital FOREIGN KEY (insurance_plan_id) REFERENCES InsurancePlans(insurance_plan_id) ON DELETE CASCADE,
    CONSTRAINT fk_hospital_insurance FOREIGN KEY (hospital_id) REFERENCES Hospitals(hospital_id) ON DELETE CASCADE
);

CREATE TABLE Users (
    id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    surname VARCHAR(50) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) CHECK (role IN ('Patient','Doctor', 'Administrator')) NOT NULL
);

CREATE TABLE Patients (
    id INT PRIMARY KEY,
    healthcare_card_number INT UNIQUE,
    insurance_plan_id INT,
    triage_status VARCHAR(20) CHECK (triage_status IN ('NonUrgent','SemiUrgent','Urgent','Emergency','Immediate','NotInTriage')) NOT NULL,
    CONSTRAINT fk_patient_user FOREIGN KEY (id) REFERENCES Users(id) ON DELETE CASCADE,
    CONSTRAINT fk_patient_insurance FOREIGN KEY (insurance_plan_id) REFERENCES InsurancePlans(insurance_plan_id) ON DELETE SET NULL
);

CREATE TABLE Doctors (
    id INT PRIMARY KEY,
    department_id INT NOT NULL,
    specialization VARCHAR(100),
    appointment_price DOUBLE PRECISION, -- Changed from DOUBLE to DOUBLE PRECISION
    CONSTRAINT fk_doctor_department FOREIGN KEY (department_id) REFERENCES Departments(department_id) ON DELETE CASCADE,
    CONSTRAINT fk_doctor_user FOREIGN KEY (id) REFERENCES Users(id) ON DELETE CASCADE
);

CREATE TABLE Administrators (
    id INT PRIMARY KEY,
    CONSTRAINT fk_admin_user
        FOREIGN KEY (id) REFERENCES Users(id)
        ON DELETE CASCADE
);

CREATE TABLE Equipments (
    equipment_id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    department_id INT NOT NULL,
    equipment_type VARCHAR(100),
    quantity INT,
    CONSTRAINT fk_equipment_department FOREIGN KEY (department_id) REFERENCES Departments(department_id) ON DELETE CASCADE
);


CREATE TABLE Equipments_Doctors(
    equipment_id INT,
    quantity INT,
    doctor_id INT,
    PRIMARY KEY (equipment_id, doctor_id),
    CONSTRAINT fk_equipment_doctor FOREIGN KEY (equipment_id) REFERENCES Equipments(equipment_id),
    CONSTRAINT fk_doctor_equipment FOREIGN KEY (doctor_id) REFERENCES Doctors(id)
);

CREATE TABLE Shifts (
    shift_id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    doctor_id INT NOT NULL,
    start_time TIMESTAMP, -- Changed from DATETIME to TIMESTAMP
    end_time TIMESTAMP,   -- Changed from DATETIME to TIMESTAMP
    CONSTRAINT fk_shift_doctor FOREIGN KEY (doctor_id) REFERENCES Doctors(id) ON DELETE CASCADE
);

CREATE TABLE Appointments (
    appointment_id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    patient_id INT NOT NULL,
    doctor_id INT NOT NULL,
    start_date TIMESTAMP, -- Changed from DATETIME to TIMESTAMP
    end_date TIMESTAMP,   -- Changed from DATETIME to TIMESTAMP
    CONSTRAINT fk_appointment_patient FOREIGN KEY (patient_id) REFERENCES Patients(id),
    CONSTRAINT fk_appointment_doctor FOREIGN KEY (doctor_id) REFERENCES Doctors(id)
);

CREATE TABLE Notes (
    note_id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    appointment_id INT UNIQUE NOT NULL,
    content TEXT,
    sent_date TIMESTAMP, -- Changed from DATETIME to TIMESTAMP
    CONSTRAINT fk_note_appointment FOREIGN KEY (appointment_id) REFERENCES Appointments(appointment_id) ON DELETE CASCADE
);

CREATE TABLE Diagnosis (
    diagnosis_id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    appointment_id INT NOT NULL,
    date_record TIMESTAMP,         -- Changed from DATETIME to TIMESTAMP
    date_start_condition TIMESTAMP,  -- Changed from DATETIME to TIMESTAMP
    date_end_condition TIMESTAMP,    -- Changed from DATETIME to TIMESTAMP
    description TEXT,
    CONSTRAINT fk_diagnosis_appointment FOREIGN KEY (appointment_id) REFERENCES Appointments(appointment_id) ON DELETE CASCADE
);