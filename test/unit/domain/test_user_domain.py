from app.domain.user import User


def test_user_email_valid():
    TestUser = User(name="Ulzii", email="ulzii123@gmail.com", password="password123", role="Patient")
    assert TestUser.has_valid_email() is True


def test_user_email_invalid():
    TestUser = User(name=" Ulzii", email="mail.com", password="password123", role="Patient")
    assert TestUser.has_valid_email() is False