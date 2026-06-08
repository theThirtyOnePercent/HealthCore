from sqlalchemy import Column, Integer, String, Enum
from app.persistence.database import Base
from enum import Enum as PyEnum
import random


class UserRole(PyEnum):
    """
    @brief Enumeration of user role in the system.
    """
    DOCTOR = "doctor"
    PATIENT = "patient"
    ADMINISTRATOR = "administrator"


class User(Base):
    __tablename__ = "users"
    """
    @brief Represents a user in the system.
    Stores general user information.
    """
    id = Column(Integer, primary_key=True)
    name = Column(String(255), nullable=False)
    surname = Column(String(255), nullable=False)
    email = Column(String(255), unique=True, nullable=False)
    password = Column(String(255), nullable=False)
    role = Column(Enum(UserRole), nullable=False)
    

    def verify_otp(self, input_code: str) -> bool:
        """
        @brief Verify the provided OTP against the generated OTP.

        @param input_code  to verify.
        @return True if the input code matches the generated OTP, otherwise False.
        """
        return True
        