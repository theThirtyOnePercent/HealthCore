from abc import ABC, abstractmethod
from enum import Enum
import equip


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


