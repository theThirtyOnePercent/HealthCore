from abc import ABC, abstractmethod
from enum import Enum
import hospital_hierarchy

class TriageStatus(Enum):
    NON_URGENT = "NonUrgent"
    SEMI_URGENT = "SemiUrgent"
    URGENT = "Urgent"
    EMERGENCY = "Emergency"
    IMMEDIATE = "Immediate"
    NOT_IN_TRIAGE = "NotInTriage"


class User(ABC):
    def __init__(self, name: str, surname: str, email: str, password: str):
        self._name = name
        self._surname = surname
        self._email = email
        self._password = password

    # ---- name ----
    @property
    def name(self):
        return self._name

    @name.setter
    def name(self, value):
        self._name = value

    # ---- surname ----
    @property
    def surname(self):
        return self._surname

    @surname.setter
    def surname(self, value):
        self._surname = value

    # ---- email ----
    @property
    def email(self):
        return self._email

    @email.setter
    def email(self, value):
        self._email = value

    # ---- password ----
    @property
    def password(self):
        return self._password

    @password.setter
    def password(self, value):
        self._password = value

class Patient(User):
    def __init__(
        self,
        patient_id: int,
        name: str,
        surname: str,
        email: str,
        password: str,
        healthcare_card_number: int,
        triage_status: TriageStatus
    ):
        super().__init__(name, surname, email, password)

        self.patient_id = patient_id
        self.healthcare_card_number = healthcare_card_number
        self.triage_status = triage_status

class Doctor(User):
    def __init__(
        self,
        doctor_id: int,
        name: str,
        surname: str,
        email: str,
        password: str,
        specialization: str,

    ):
        super().__init__(name, surname, email, password)

        self.doctor_id = doctor_id
        self.specialization = specialization
        self.appointment_price = None
        self.equipment_list = []

    def add_equipment(self, equipment: Equipment):
        self.equipment_list.append(equipment)


class Administrator(User):
    def __init__(self, admin_id: int, name: str, surname: str, email: str, password: str):
        super().__init__(name, surname, email, password)
        self.admin_id = admin_id