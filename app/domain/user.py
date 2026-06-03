from sqlalchemy import Column, Integer, String
from app.persistence.database import Base
import random

class User(Base):
    __tablename__ = "users"

    id = Column(Integer, primary_key=True)
    email = Column(String(255), unique=True, nullable=False)
    password_hash = Column(String(255), nullable=False)
    role = Column(String(50), nullable=False)  # Patient, Doctor, Administrator
    
    # OTP fields
    verification_code = Column(String(6), nullable=True)

    def generate_otp(self) -> str:
        """Generates a 6-digit code valid for 10 minutes (Step 3)."""
        code = f"{random.randint(100000, 999999)}"
        return code

    def verify_otp(self, input_code: str) -> bool:
        """Verifies if the code matches and isn't expired (Step 5)."""
        if not self.verification_code or not self.code_expires_at:
            return False
        return self.verification_code == input_code