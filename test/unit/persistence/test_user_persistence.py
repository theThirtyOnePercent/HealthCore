from app.domain.user import User
from app.persistence.authentication_p import UserPersistence


def test_get_user_by_email(db_session):
    # Arrange: Create a test user and add to the database
    test_user = User(name="Test User", email="ulzii@gmail.com", password="password123", role="Patient")
    db_session.add(test_user)
    db_session.commit() 