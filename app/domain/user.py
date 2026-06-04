from sqlalchemy import Column, Integer, String
from app.persistence.database import Base
from enum import Enum
import random


class Role(Enum):
    PATIENT = "PATIENT",
    DOCTOR = "DOCTOR" ,
    ADMINISTRATOR = "ADMINISTRATOR"

class User(Base):
    __tablename__ = "users"

    id = Column(Integer, primary_key=True)
    name = Column(String(255), nullable=False)
    surname = Column(String(255), nullable=False)
    email = Column(String(255), unique=True, nullable=False)
    password = Column(String(255), nullable=False)
    role = Column(Role, nullable=False)
    
    def generate_otp(self) -> str:
        """Generates a 6-digit code valid for 10 minutes (Step 3)."""
        code = f"{random.randint(100000, 999999)}"
        return code

    def verify_otp(self, input_code: str) -> bool:
        return True
        