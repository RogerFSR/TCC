# app.py
from flask import Flask, render_template, jsonify, request
from controllers.produtos_controller import create_produto, get_produtos, get_produto, update_produto, delete_produto
from controllers.fornecedores_controller import create_fornecedor, get_fornecedor, get_fornecedores, update_fornecedor, delete_fornecedor
from controllers.transacoes_controller import create_transacao, get_transacao, get_transacoes, update_transacao, delete_transacao
from flask_cors import CORS

app = Flask(__name__)
CORS(app)  # Habilitar CORS para evitar problemas no navegador

#-------------------------------------------------------------------------------------------------------------------------
#RENDERIZANDO AS PAGINAS
#-------------------------------------------------------------------------------------------------------------------------

@app.route('/')
def index():
    return render_template('home.html')

#-------------------------------------------------------------------------------------------------------------------------
#ROTAS DOS PRODUTOS
#-------------------------------------------------------------------------------------------------------------------------

@app.route('/api/produtos', methods=['POST'])
def api_create_produto():
    try:
        return create_produto()
    except Exception as e:
        return jsonify({"error": f"Erro ao criar produto: {str(e)}"}), 500

@app.route('/api/produtos', methods=['GET'])
def api_get_produtos():
    try:
        return get_produtos()
    except Exception as e:
        return jsonify({"error": f"Erro ao buscar produtos: {str(e)}"}), 500

@app.route('/api/produtos/<produto_id>', methods=['GET'])
def api_get_produto(produto_id):
    try:
        return get_produto(produto_id)
    except Exception as e:
        return jsonify({"error": f"Erro ao buscar produto: {str(e)}"}), 500

@app.route('/api/produtos/<produto_id>', methods=['PUT'])
def api_update_produto(produto_id):
    try:
        return update_produto(produto_id)
    except Exception as e:
        return jsonify({"error": f"Erro ao atualizar produto: {str(e)}"}), 500

@app.route('/api/produtos/<produto_id>', methods=['DELETE'])
def api_delete_produto(produto_id):
    try:
        return delete_produto(produto_id)
    except Exception as e:
        return jsonify({"error": f"Erro ao deletar produto: {str(e)}"}), 500

#-------------------------------------------------------------------------------------------------------------------------
#ROTAS DOS FORNECEDORES
#-------------------------------------------------------------------------------------------------------------------------

@app.route('/api/fornecedores', methods=['POST'])
def api_create_fornecedor():
    try:
        return create_fornecedor()
    except Exception as e:
        return jsonify({"error": f"Erro ao criar fornecedor: {str(e)}"}), 500

@app.route('/api/fornecedores', methods=['GET'])
def api_get_fornecedores():
    try:
        return get_fornecedores()
    except Exception as e:
        return jsonify({"error": f"Erro ao buscar fornecedores: {str(e)}"}), 500

@app.route('/api/fornecedores/<fornecedor_id>', methods=['GET'])
def api_get_fornecedor(fornecedor_id):
    try:
        return get_fornecedor(fornecedor_id)
    except Exception as e:
        return jsonify({"error": f"Erro ao buscar fornecedor: {str(e)}"}), 500

@app.route('/api/fornecedores/<fornecedor_id>', methods=['PUT'])
def api_update_fornecedor(fornecedor_id):
    try:
        return update_fornecedor(fornecedor_id)
    except Exception as e:
        return jsonify({"error": f"Erro ao atualizar fornecedor: {str(e)}"}), 500

@app.route('/api/fornecedores/<fornecedor_id>', methods=['DELETE'])
def api_delete_fornecedor(fornecedor_id):
    try:
        return delete_fornecedor(fornecedor_id)
    except Exception as e:
        return jsonify({"error": f"Erro ao deletar fornecedor: {str(e)}"}), 500

#-------------------------------------------------------------------------------------------------------------------------
#ROTAS DAS TRANSICOES
#-------------------------------------------------------------------------------------------------------------------------

@app.route('/api/transacoes', methods=['POST'])
def api_create_transacao():
    try:
        return create_transacao()
    except Exception as e:
        return jsonify({"error": f"Erro ao criar transacao: {str(e)}"}), 500

@app.route('/api/transacoes', methods=['GET'])
def api_get_transacoes():
    try:
        return get_transacoes()
    except Exception as e:
        return jsonify({"error": f"Erro ao buscar transacoes: {str(e)}"}), 500

@app.route('/api/transacoes/<transacao_id>', methods=['GET'])
def api_get_transacao(transacao_id):
    try:
        return get_transacao(transacao_id)
    except Exception as e:
        return jsonify({"error": f"Erro ao buscar transacao: {str(e)}"}), 500

@app.route('/api/transacoes/<transacao_id>', methods=['PUT'])
def api_update_transacao(transacao_id):
    try:
        return update_transacao(transacao_id)
    except Exception as e:
        return jsonify({"error": f"Erro ao atualizar transacao: {str(e)}"}), 500

@app.route('/api/transacoes/<transacao_id>', methods=['DELETE'])
def api_delete_transacao(transacao_id):
    try:
        return delete_transacao(transacao_id)
    except Exception as e:
        return jsonify({"error": f"Erro ao deletar transacao: {str(e)}"}), 500

#-------------------------------------------------------------------------------------------------------------------------
#EXECUCAO DA MAIN
#-------------------------------------------------------------------------------------------------------------------------

if __name__ == '__main__':
   app.run(debug=True)
