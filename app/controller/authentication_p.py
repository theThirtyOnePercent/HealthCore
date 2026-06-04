from flask import request, jsonify, session
from app.persistence.database import SessionLocal
from app.persistence.authentication_p import UserPersistence
from app.business.authentication_b import AuthService

def login_step_1_controller():
    """Submitting Email & Password."""
    data = request.get_json() or {}
    email = data.get("email")
    password = data.get("password")

    db = SessionLocal()
    try:
        repo = UserPersistence(db)
        auth_service = AuthService(repo)
        
        session_email = auth_service.authenticate_credentials(email, password)
        
    
        session["mfa_pending_email"] = session_email
        
        return jsonify({"message": "User successfully entered", "next_step": "verify_mfa"}), 200
    except ValueError as e:
        # Extension 2a.1: Display warning message
        return jsonify({"error": str(e)}), 401
    finally:
        db.close()

def login_step_2_controller():
    """Endpoint for Step 4: Submitting the Email OTP"""
    data = request.get_json() or {}
    input_code = data.get("code")
    email = session.get("mfa_pending_email")

    if not email:
        return jsonify({"error": "No login session active. Please restart process."}), 400

    db = SessionLocal()
    try:
        repo = UserPersistence(db)
        auth_service = AuthService(repo)
        

        user = auth_service.verify_mfa(email, input_code)
        session.pop("mfa_pending_email", None) # Clear temp track
        session["user_id"] = user.id
        session["user_role"] = user.role # Dynamic configuration based on actor mapping
        
        return jsonify({
            "message": "Authentication successful.",
            "role": user.role,
        }), 200
    except ValueError as e:
        return jsonify({"error": str(e)}), 401
    finally:
        db.close()