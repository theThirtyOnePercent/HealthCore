import werkzeug.security as ws  # For secure password hashing/checking
from app.persistence.authentication_p import UserPersistence


class AuthenticationBusiness:
    def __init__(self, persistence: UserPersistence):
        self.persistence = persistence


    def authenticate_credentials(self, email: str, password: str):
        """
        @brief Authenticate user credentials.
        @param email User email address.
        @param password User password.
        @return User email if authentication is successful, otherwise raises ValueError.
        """
        user = self.persistence.get_by_email(email)
        if not user:
            raise ValueError("Invalid email or password.")
        else:
            if ws.check_password_hash(user.password, password):
                return user.email
            else:
                raise ValueError("Invalid email or password.")