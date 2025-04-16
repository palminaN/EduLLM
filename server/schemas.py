# app/schemas.py
from pydantic import BaseModel, EmailStr

class UserCreate(BaseModel):
    email: EmailStr
    password: str
    is_parent: bool

class User(BaseModel):
    id: int
    email: EmailStr
    is_parent: bool

    class Config:
        orm_mode = True

class UserLogin(BaseModel):
    email: EmailStr
    password: str