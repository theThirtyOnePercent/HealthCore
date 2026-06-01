import user

class TriageStatus(Enum):
    NON_URGENT = "NonUrgent"
    SEMI_URGENT = "SemiUrgent"
    URGENT = "Urgent"
    EMERGENCY = "Emergency"
    IMMEDIATE = "Immediate"
    NOT_IN_TRIAGE = "NotInTriage"


class Patient(User):
    def __init__(
        self,
        patient_id: int,
        name: str,
        surname: str,
        email: str,
        password: str,
        healthcare_card_number: int,
        triage_status: TriageStatus
    ):
        super().__init__(name, surname, email, password)

        self.patient_id = patient_id
        self.healthcare_card_number = healthcare_card_number
        self.triage_status = triage_status