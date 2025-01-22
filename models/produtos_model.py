# models/produtos_model.py
from bson import ObjectId

class ProdutoModel:
    def __init__(self, db):
        self.collection = db['Produtos']

    def create_produto(self, produto_data):
        result = self.collection.insert_one(produto_data)
        return str(result.inserted_id)

    def get_all_produtos(self):
        produtos = self.collection.find()
        return [self._serialize(produto) for produto in produtos]

    def get_produto_by_id(self, produto_id):
        produto = self.collection.find_one({"_id": ObjectId(produto_id)})
        return self._serialize(produto) if produto else None

    def update_produto(self, produto_id, produto_data):
        result = self.collection.update_one({"_id": ObjectId(produto_id)}, {"$set": produto_data})
        return result.matched_count > 0

    def delete_produto(self, produto_id):
        result = self.collection.delete_one({"_id": ObjectId(produto_id)})
        return result.deleted_count > 0

    def _serialize(self, produto):
        if produto is None:
            return None
        produto['_id'] = str(produto['_id'])  # Converter ObjectId para string
        return produto
