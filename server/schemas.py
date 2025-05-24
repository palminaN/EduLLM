from pydantic import BaseModel, EmailStr
from typing import Optional, List

# Base commune

class UserBase(BaseModel):
    email: EmailStr
    is_parent: bool = False

# Entrées 

class UserCreate(BaseModel):
    email: EmailStr
    password: str

class UserLogin(BaseModel):
    email: EmailStr
    password: str

class UserUpdate(BaseModel):
    email: Optional[EmailStr] = None
    password: Optional[str] = None

# Sorties 

class User(UserBase):
    id: int
    parent_id: Optional[int] = None

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

class Child(User):
    badges: Optional[List[Badge]] = []
