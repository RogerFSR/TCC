# controllers/produtos_controller.py
from flask import jsonify, request
from models.produtos_model import ProdutoModel
from models.db_model import db_connection

# Conectar ao banco de dados usando o db_model
db = db_connection.connect()
produto_model = ProdutoModel(db)

# Função auxiliar para validar dados de entrada
def validar_produto_data(produto_data):
   required_fields = ['nome', 'codigo', 'quantidade', 'categoria']
   for field in required_fields:
       if field not in produto_data or not produto_data[field]:
           return False, f"O campo {field} é obrigatório"
   return True, None

# Criar um produto
def create_produto():
   produto_data = request.get_json()
   valid, message = validar_produto_data(produto_data)
   if not valid:
       return jsonify({"error": message}), 400
   produto_id = produto_model.create_produto(produto_data)
   return jsonify({"message": "Produto criado", "id": produto_id}), 201

# Ler todos os produtos
def get_produtos():
    produtos = produto_model.get_all_produtos()
    if isinstance(produtos, list):
        return jsonify(produtos), 200
    else:
        return jsonify({"error": "Nenhum produto encontrado"}), 404

# Ler um produto por ID
def get_produto(produto_id):
   produto = produto_model.get_produto_by_id(produto_id)
   if produto:
       return jsonify(produto), 200
   return jsonify({"error": "Produto não encontrado"}), 404

# Atualizar um produto
def update_produto(produto_id):
   produto_data = request.get_json()
   valid, message = validar_produto_data(produto_data)
   if not valid:
       return jsonify({"error": message}), 400
   atualizado = produto_model.update_produto(produto_id, produto_data)
   if atualizado:
       return jsonify({"message": "Produto atualizado"}), 200
   return jsonify({"error": "Produto não encontrado"}), 404

# Deletar um produto
def delete_produto(produto_id):
   deletado = produto_model.delete_produto(produto_id)
   if deletado:
       return jsonify({"message": "Produto deletado"}), 200
   return jsonify({"error": "Produto não encontrado"}), 404
