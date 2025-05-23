# app/schemas.py
from pydantic import BaseModel, EmailStr
from typing import Optional, List

# ======================== Base commun ========================

class UserBase(BaseModel):
    email: EmailStr
    is_parent: bool = False


# ======================== Entrées ========================

class UserCreate(UserBase):
    password: str

class UserLogin(BaseModel):
    email: EmailStr
    password: str

class UserUpdate(BaseModel):
    email: Optional[EmailStr] = None
    password: Optional[str] = None
    is_parent: Optional[bool] = None


# ======================== Sorties ========================

class User(UserBase):
    id: int

    class Config:
        orm_mode = True


# ======================== Groupes ========================

class Child(User):  # héritage du schéma User
    badges: Optional[List[str]] = []
