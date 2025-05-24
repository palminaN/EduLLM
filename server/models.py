# app/models.py
from sqlalchemy import Column, Integer, String, Boolean, ForeignKey
from sqlalchemy.orm import relationship
from database import Base

class User(Base):
    __tablename__ = "users"

    id = Column(Integer, primary_key=True, index=True)
    email = Column(String, unique=True, index=True)
    hashed_password = Column(String)
    is_parent = Column(Boolean, default=False)
    
    parent_id = Column(Integer, ForeignKey("users.id"), nullable=True)

    # Facultatif : relation ORM bidirectionnelle
    children = relationship("User", remote_side=[id])
    badges = relationship("Badge", back_populates="owner")

class Badge(Base):
    __tablename__ = "badges"

    id = Column(Integer, primary_key=True, index=True)
    name = Column(String)
    child_id = Column(Integer, ForeignKey("users.id"))

    owner = relationship("User", back_populates="badges")