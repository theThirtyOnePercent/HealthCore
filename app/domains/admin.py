import user

class Administrator(User):
    def __init__(self, admin_id: int, name: str, surname: str, email: str, password: str):
        super().__init__(name, surname, email, password)
        self.admin_id = admin_id