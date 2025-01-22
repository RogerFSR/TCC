# models/db_model.py
from pymongo.mongo_client import MongoClient
from pymongo.server_api import ServerApi

class MongoDBConnection:
    def __init__(self):
        # URI do MongoDB Atlas
        self.uri = "mongodb+srv://sakailuizeduardo:Ff6hDwh252k5GoTx@cluster0.45oi1.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0"
        self.client = None
        self.db = None

    def connect(self):
        # Criar a conexão com o MongoDB forçando TLS
        self.client = MongoClient(
            self.uri, 
            server_api=ServerApi('1'),
            tls=True,  # Forçar uso de TLS
            tlsAllowInvalidCertificates=True  # Permitir certificados inválidos
        )
        self.db = self.client['db1']
        return self.db

# Instância global para conexão com o banco de dados
db_connection = MongoDBConnection()
