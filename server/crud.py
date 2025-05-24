from sqlalchemy.orm import Session
import models, schemas
from auth import get_password_hash, verify_password

def get_user(db: Session, user_id: int):
    return db.query(models.User).filter(models.User.id == user_id).first()

def get_user_by_email(db: Session, email: str):
    return db.query(models.User).filter(models.User.email == email).first()

def create_user(db: Session, user: schemas.UserCreate, parent_id: int = None) -> models.User:
    hashed_password = get_password_hash(user.password)
    db_user = models.User(
        email=user.email,
        hashed_password=hashed_password,
        is_parent=user.is_parent,
        parent_id=parent_id 
    )
    db.add(db_user)
    db.commit()
    db.refresh(db_user)
    return db_user

def authenticate_user(db: Session, email: str, password: str):
    user = get_user_by_email(db, email)
    if not user:
        return False
    if not verify_password(password, user.hashed_password):
        return False
    return user

def update_user(db: Session, user_id: int, updates: dict):
    user = get_user(db, user_id)
    if not user:
        return None

    for key, value in updates.items():
        setattr(user, key, value)

    db.commit()
    db.refresh(user)
    return user



# Badges

def get_badges_for_child(db: Session, child_id: int):
    return db.query(models.Badge).filter(models.Badge.child_id == child_id).all()

def add_badge_to_child(db: Session, child_id: int, badge: schemas.BadgeCreate):
    badge_obj = models.Badge(name=badge.name, child_id=child_id)
    db.add(badge_obj)
    db.commit()
    db.refresh(badge_obj)
    return badge_obj
