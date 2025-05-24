# app/schemas.py
from pydantic import BaseModel, EmailStr
from typing import Optional, List

# Base commune

class UserBase(BaseModel):
    email: EmailStr
    is_parent: bool = False



# Entrées 

class UserCreate(UserBase):
    password: str

class UserLogin(BaseModel):
    email: EmailStr
    password: str

class UserUpdate(BaseModel):
    email: Optional[EmailStr] = None
    password: Optional[str] = None
    is_parent: Optional[bool] = None



# Sorties 

class User(UserBase):
    id: int
    parent_id: Optional[int] = None  # <- ici

    model_config = {
        "from_attributes": True
    }



# Badges

class Badge(BaseModel):
    id: int
    name: str

    model_config = {
        "from_attributes": True
    }

class BadgeCreate(BaseModel):
    name: str
    


# Groupes

class Child(User):  # hérite déjà de parent_id via User
    badges: Optional[List[Badge]] = []




