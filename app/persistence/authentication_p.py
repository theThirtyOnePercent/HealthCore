from app.domain.user import User;
from sqlalchemy.orm import Session

class UserPersistence:
    """
    @brief Repository for User database operations.
    This class provides methods to interact with the User table in the database.
    """
    def __init__(self, db_session: Session):
        """
        @brief Repository constructor.
        @param db_session: SQLAlchemy Session object for database operations.
        """
        self.db = db_session

    def get_by_email(self, email: str) -> User:
        """
        @brief Retrieve a user by their email address.
        @return User object if found, else None.
        """
        return self.db.query(User).filter(User.email == email).first()

   
    def commit_changes(self):
        """
        @brief Commit pending database changes.
        @return None
        """
        self.db.commit()