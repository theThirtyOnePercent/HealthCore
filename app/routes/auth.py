from flask import Blueprint
from app.controller.authentication_c import login_step_1_controller, login_step_2_controller


auth_bp = Blueprint('auth', __name__)

auth_bp.add_url_rule('/login', view_func=login_step_1_controller, methods=['POST'])

auth_bp.add_url_rule('/login/step-2', view_func=login_step_2_controller, methods=['POST'])
