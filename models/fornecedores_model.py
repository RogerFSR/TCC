# models/fornecedores_model.py
from bson import ObjectId

class FornecedorModel:
    def __init__(self, db):
        self.collection = db['Fornecedores']

    def create_fornecedor(self, fornecedor_data):
        result = self.collection.insert_one(fornecedor_data)
        return str(result.inserted_id)

    def get_all_fornecedores(self):
        fornecedores = self.collection.find()
        return [self._serialize(fornecedor) for fornecedor in fornecedores]

    def get_fornecedor_by_id(self, fornecedor_id):
        fornecedor = self.collection.find_one({"_id": ObjectId(fornecedor_id)})
        return self._serialize(fornecedor) if fornecedor else None

    def update_fornecedor(self, fornecedor_id, fornecedor_data):
        result = self.collection.update_one({"_id": ObjectId(fornecedor_id)}, {"$set": fornecedor_data})
        return result.matched_count > 0

    def delete_fornecedor(self, fornecedor_id):
        result = self.collection.delete_one({"_id": ObjectId(fornecedor_id)})
        return result.deleted_count > 0

    def _serialize(self, fornecedor):
        if fornecedor is None:
            return None
        fornecedor['_id'] = str(fornecedor['_id'])  # Converter ObjectId para string
        return fornecedor
