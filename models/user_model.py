# models/user_model.py
class UserModel:
    def __init__(self):
        # Simulação de um banco de dados
        self.users = [{"id": 1, "name": "Alice"}, {"id": 2, "name": "Bob"}]

    def get_all_users(self):
        return self.users

    def add_user(self, user):
        new_id = len(self.users) + 1
        user['id'] = new_id
        self.users.append(user)
