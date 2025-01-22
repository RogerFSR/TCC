# controllers/user_controller.py
from flask import jsonify, request
from models.user_model import UserModel

user_model = UserModel()

def get_users():
    users = user_model.get_all_users()
    return jsonify(users)

def add_user():
    user_data = request.get_json()  # Recebe dados do front-end
    if "name" in user_data:
        user_model.add_user({"name": user_data["name"]})
        return jsonify({"message": "Usuário adicionado com sucesso"}), 201
    return jsonify({"error": "Dados inválidos"}), 400
