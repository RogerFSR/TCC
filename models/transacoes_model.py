# models/produtos_model.py
from bson import ObjectId

class TransacaoModel:
    def __init__(self, db):
        self.collection = db['Transacoes']

    def create_transacao(self, transacao_data):
        result = self.collection.insert_one(transacao_data)
        return str(result.inserted_id)

    def get_all_transacoes(self):
        transacoes = self.collection.find()
        return [self._serialize(transacao) for transacao in transacoes]

    def get_transacao_by_id(self, transacao_id):
        transacao = self.collection.find_one({"_id": ObjectId(transacao_id)})
        return self._serialize(transacao) if transacao else None

    def update_transacao(self, transacao_id, transacao_data):
        result = self.collection.update_one({"_id": ObjectId(transacao_id)}, {"$set": transacao_data})
        return result.matched_count > 0

    def delete_transacao(self, transacao_id):
        result = self.collection.delete_one({"_id": ObjectId(transacao_id)})
        return result.deleted_count > 0

    def _serialize(self, transacao):
        if transacao is None:
            return None
        transacao['_id'] = str(transacao['_id'])  # Converter ObjectId para string
        return transacao
