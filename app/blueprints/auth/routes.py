from flask import jsonify, request
from models import User
from . import auth_bp

@auth_bp.route("/login", methods=["POST"])
def login():
    data = request.json
    user = User(id=1, username=data["username"], email="test@test.com")
    return jsonify(user.to_dict())

@auth_bp.route("/logout", methods=["POST"])
def logout():
    return jsonify({"message": "Logged out"})