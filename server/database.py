import os
from sqlalchemy import create_engine
from sqlalchemy.ext.declarative import declarative_base
from sqlalchemy.orm import sessionmaker

# === Suppression conditionnelle si RESET_DB est true ===
DATABASE_FILE = "./test.db"
RESET_DB = os.getenv("RESET_DB", "false").lower() == "true"

if RESET_DB and os.path.exists(DATABASE_FILE):
    os.remove(DATABASE_FILE)

DATABASE_URL = f"sqlite:///{DATABASE_FILE}"

engine = create_engine(DATABASE_URL, connect_args={"check_same_thread": False})
SessionLocal = sessionmaker(autocommit=False, autoflush=False, bind=engine)
Base = declarative_base()