from app.domain.user import User;
from sqlalchemy.orm import Session

class UserPersistence:
    def __init__(self, db_session: Session):
        self.db = db_session

    def get_by_email(self, email: str) -> User:
        return self.db.query(User).filter(User.email == email).first()

    def commit_changes(self):
        self.db.commit()