import werkzeug.security as ws  # For secure password hashing/checking
from app.persistence.authentication_p import UserPersistence

class AuthService:
    def __init__(self, persistence: UserPersistence):
        self.persistence = persistence

    def authenticate_credentials(self, email: str, password: str):
        """ Returns OTP code if valid, raises error if extension occurs """
        user = self.repository.get_by_email(email)
        
        # Wrong information
        if not user or not ws.check_password_hash(user.password_hash, password):
            raise ValueError("Invalid email or password.")
            
        # Generate code
        otp_code = user.generate_otp()
        self.repository.commit_changes()
        
        # Mock Email 
        print(f"DEBUG: Email sent to {email} with code: {otp_code}")
        return user.email

    def verify_mfa(self, email: str, input_code: str):
        """Clears code on success, raises error if extension. """
        user = self.repository.get_by_email(email)
        if not user:
            raise ValueError("Authentication session expired.")

        if not user.verify_otp(input_code):
            raise ValueError("Invalid or expired verification code.")

        user.verification_code = None
        self.repository.commit_changes()
        
        return user 