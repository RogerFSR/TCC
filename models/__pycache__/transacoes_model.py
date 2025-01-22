# models/fornecedores_model.py
from bson import ObjectId

class TransacoesModel:
    def __init__(self, db):
        self.collection = db['Transacoes']

    def create_transacao(self,transacao_data):
        result = self.collection.insert_one(transacao_data)
        return str(result.inserted_id)

    def get_all_transacoes(self):
        transacoes = self.collection.find()
        return [self._serialize(transacao) for transacao in transacoes]

    def get_transacoes_by_id(self, transacoes_id):
        transacoes = self.collection.find_one({"_id": ObjectId(transacoes_id)})
        return self._serialize(transacoes) if transacoes else None

    def update_transacoes(self,transacoes_id, transacoes_data):
        result = self.collection.update_one({"_id": ObjectId(transacoes_id)}, {"$set": transacoes_data})
        return result.matched_count > 0

    def delete_transacoes(self, transacoes_id):
        result = self.collection.delete_one({"_id": ObjectId(transacoes_id)})
        return result.deleted_count > 0

    def _serialize(self, transacoes):
        if transacoes is None:
            return None
        transacoes['_id'] = str(transacoes['_id'])  # Converter ObjectId para string
        return transacoes
