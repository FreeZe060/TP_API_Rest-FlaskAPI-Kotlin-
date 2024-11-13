import os

class Config:
    """Configuration de base pour l'application Flask."""
    
    SQLALCHEMY_DATABASE_URI = os.getenv('DATABASE_URL', 'sqlite:///ma_db.db')
    SQLALCHEMY_TRACK_MODIFICATIONS = False

    SECRET_KEY = os.getenv('SECRET_KEY', 'Xy0a7kM0D')

    DEBUG = os.getenv('DEBUG', 'False').lower() in ('true', '1', 't')
