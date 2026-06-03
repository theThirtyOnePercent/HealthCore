import user
import equip

class Doctor(User):
    def __init__(
        self,
        doctor_id: int,
        name: str,
        surname: str,
        email: str,
        password: str,
        specialization: str,
    ):

        super().__init__(name, surname, email, password)
        
        self.doctor_id = doctor_id
        self.specialization = specialization
        self.appointment_price = None
        self.equipment_list = []

    def add_equipment(self, equipment: Equipment):
        self.equipment_list.append(equipment)
