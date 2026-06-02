from flask import jsonify
from flask import Blueprint

auth_bp = Blueprint("auth", __name__)


@auth_bp.route("/login", methods=["POST"])
def login():
     return jsonify({"message": "Logged on"})


@auth_bp.route("/logout", methods=["POST"])
def logout():
    return jsonify({"message": "Logged out"})