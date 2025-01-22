# controllers/produtos_controller.py
from flask import jsonify, request
from models.fornecedores_model import FornecedorModel
from models.db_model import db_connection

# Conectar ao banco de dados usando o db_model
db = db_connection.connect()
fornecedor_model = FornecedorModel(db)

# Função auxiliar para validar dados de entrada
def validar_fornecedor_data(fornecedor_data):
   required_fields = ['nome', 'contato', 'produtosFornecidos']
   for field in required_fields:
       if field not in fornecedor_data or not fornecedor_data[field]:
           return False, f"O campo {field} é obrigatório"
   return True, None

# Criar um produto
def create_fornecedor():
   fornecedor_data = request.get_json()
   valid, message = validar_fornecedor_data(fornecedor_data)
   if not valid:
       return jsonify({"error": message}), 400
   fornecedor_id = fornecedor_model.create_fornecedor(fornecedor_data)
   return jsonify({"message": "Fornecedor criado", "id": fornecedor_id}), 201

# Ler todos os produtos
def get_fornecedores():
    fornecedores = fornecedor_model.get_all_fornecedores()
    if isinstance(fornecedores, list):
        return jsonify(fornecedores), 200
    else:
        return jsonify({"error": "Nenhum fornecedor encontrado"}), 404

# Ler um produto por ID
def get_fornecedor(fornecedor_id):
    fornecedor = fornecedor_model.get_fornecedor_by_id(fornecedor_id)
    if fornecedor:
        return jsonify(fornecedor), 200
    return jsonify({"error": "Fornecedor não encontrado"}), 404

# Atualizar um produto
def update_fornecedor(fornecedor_id):
    fornecedor_data = request.get_json()
    valid, message = validar_fornecedor_data(fornecedor_data)
    if not valid:
        return jsonify({"error": message}), 400
    atualizado = fornecedor_model.update_fornecedor(fornecedor_id, fornecedor_data)
    if atualizado:
        return jsonify({"message": "Fornecedor atualizado"}), 200
    return jsonify({"error": "Fornecedor não encontrado"}), 404

# Deletar um produto
def delete_fornecedor(fornecedor_id):
    deletado = fornecedor_model.delete_fornecedor(fornecedor_id)
    if deletado:
        return jsonify({"message": "Fornecedor deletado"}), 200
    return jsonify({"error": "Fornecedor não encontrado"}), 404
