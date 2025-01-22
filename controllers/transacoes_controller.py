# controllers/produtos_controller.py
from flask import jsonify, request
from models.transacoes_model import TransacaoModel
from models.db_model import db_connection

# Conectar ao banco de dados usando o db_model
db = db_connection.connect()
transacao_model = TransacaoModel(db)

# Função auxiliar para validar dados de entrada
def validar_transacao_data(transacao_data):
   required_fields = ['entrada', 'saida']
   for field in required_fields:
       if field not in transacao_data or not transacao_data[field]:
           return False, f"O campo {field} é obrigatório"
   return True, None

# Criar um produto
def create_transacao():
   transacao_data = request.get_json()
   valid, message = validar_transacao_data(transacao_data)
   if not valid:
       return jsonify({"error": message}), 400
   transacao_id = transacao_model.create_transacao(transacao_data)
   return jsonify({"message": "Transacao criada", "id": transacao_id}), 201

# Ler todos os produtos
def get_transacoes():
    transacoes = transacao_model.get_all_transacoes()
    if isinstance(transacoes, list):
        return jsonify(transacoes), 200
    else:
        return jsonify({"error": "Nenhum transacao encontrada"}), 404

# Ler um produto por ID
def get_transacao(transacao_id):
    transacao = transacao_model.get_transacao_by_id(transacao_id)
    if transacao:
        return jsonify(transacao), 200
    return jsonify({"error": "Transacao não encontrada"}), 404

# Atualizar um produto
def update_transacao (transacao_id):
    transacao_data = request.get_json()
    valid, message = validar_transacao_data(transacao_data)
    if not valid:
        return jsonify({"error": message}), 400
    atualizado = transacao_model.update_transacao(transacao_id, transacao_data)
    if atualizado:
        return jsonify({"message": "Transacao atualizada"}), 200
    return jsonify({"error": "Transacao não encontrada"}), 404

# Deletar um produto
def delete_transacao(transacao_id):
    deletado = transacao_model.delete_transacao(transacao_id)
    if deletado:
        return jsonify({"message": "Transacao deletada"}), 200
    return jsonify({"error": "Transacao não encontrada"}), 404
