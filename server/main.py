# app/main.py
from fastapi import FastAPI, Depends, HTTPException, Request
from fastapi.responses import RedirectResponse, JSONResponse
from sqlalchemy.orm import Session
import crud, models, schemas, database, groq_api
from database import SessionLocal, engine

models.Base.metadata.create_all(bind=engine)

app = FastAPI(title="EduLLM")


@app.exception_handler(404)
async def handle_404(req : Request, exc : HTTPException):
    return RedirectResponse("/docs")

@app.exception_handler(500)
async def handle_500(req : Request, exc : HTTPException):
    return JSONResponse(status_code=500,content={"error":"Nous n'avous pas reussi a traiter votre requete"})

def get_db():
    db = SessionLocal()
    try:
        yield db
    finally:
        db.close()

@app.post("/register/", response_model=schemas.User)
def create_user(user: schemas.UserCreate, db: Session = Depends(get_db)):
    db_user = crud.get_user_by_email(db, email=user.email)
    if db_user:
        raise HTTPException(status_code=400, detail="Email already registered")
    return crud.create_user(db=db, user=user)

@app.post("/login/", response_model=schemas.User)
def login_user(user: schemas.UserLogin, db: Session = Depends(get_db)):
    db_user = crud.authenticate_user(db, user.email, user.password)
    if not db_user:
        raise HTTPException(status_code=401, detail="Invalid email or password")
    return db_user

@app.get("/ping")
def ping():
    return {"status": "ok"}



# ======================== Enfant - Fonctions ========================

@app.get("/child/{child_id}/profile", response_model=schemas.User)
def get_child_profile(child_id: int, db: Session = Depends(get_db)):
    user = crud.get_user(db, user_id=child_id)
    if not user or user.is_parent:
        raise HTTPException(status_code=404, detail="Enfant non trouvé")
    return user


#TODO faire les logiques de ces routes 
@app.get("/child/{child_id}/badges")
def get_child_badges(child_id: int):
    return {"badges": ["Lecteur", "Math Master", "Explorateur"]}

@app.get("/exercise/math")
def get_math_exercise():
    return {"question": "Combien font 245 + 387 ?"}

@app.post("/exercise/math")
def check_math_answer(answer: int):
    return {"correct": answer == 632}

@app.get("/exercise/vocab")
def get_vocab_exercise():
    return {"word": "joie", "instruction": "Donne un synonyme"}

@app.post("/exercise/vocab")
def check_vocab_answer(answer: str):
    return {"correct": answer.strip().lower() == "bonheur"}

@app.get("/exercise/grammar")
def get_grammar_exercise():
    return {"phrase": "Le chien a mangé le os"}

@app.post("/exercise/grammar")
def check_grammar_answer(answer: str):
    return {"correction": answer}

@app.get("/quiz")
def get_quiz_question():
    return {
        "question": "Quelle planète est la plus proche du Soleil ?",
        "choices": ["Terre", "Mars", "Mercure"]
    }

@app.post("/quiz")
def check_quiz_answer(answer: str):
    return {"correct": answer.strip().lower() == "mercure"}

@app.post("/story/start")
def generate_story_start(theme: str, character: str):
    intro = f"Il était une fois {character}, passionné par {theme}..."
    return {"story": intro}

@app.post("/story/continue")
def generate_story_continue(previous: str):
    return {"story": previous + " Et soudain, une nouvelle aventure commence..."}


# ======================== Parent - Fonctions ========================

@app.get("/parent/{parent_id}/profile", response_model=schemas.User)
def get_parent_profile(parent_id: int, db: Session = Depends(get_db)):
    user = crud.get_user(db, user_id=parent_id)
    if not user or not user.is_parent:
        raise HTTPException(status_code=404, detail="Parent non trouvé")
    return user

@app.get("/parent/children", response_model=list[schemas.User])
def get_all_children(db: Session = Depends(get_db)):
    users = db.query(models.User).filter(models.User.is_parent == False).all()
    return users

@app.put("/parent/children/{child_id}")
def update_child(child_id: int, new_email: str, db: Session = Depends(get_db)):
    user = crud.get_user(db, user_id=child_id)
    if not user or user.is_parent:
        raise HTTPException(status_code=404, detail="Enfant non trouvé")
    user.email = new_email
    db.commit()
    return {"message": "Compte enfant mis à jour", "email": user.email}

@app.put("/parent/settings")
def update_parent_settings(user_id: int, new_password: str, db: Session = Depends(get_db)):
    user = crud.get_user(db, user_id=user_id)
    if not user or not user.is_parent:
        raise HTTPException(status_code=404, detail="Parent non trouvé")
    from auth import get_password_hash
    user.hashed_password = get_password_hash(new_password)
    db.commit()
    return {"message": "Mot de passe mis à jour"}

@app.post("/parent/children/", response_model=schemas.User)
def create_child_account(child: schemas.UserCreate, db: Session = Depends(get_db)):
    if child.is_parent:
        raise HTTPException(status_code=400, detail="Impossible de créer un parent via cette route.")
    
    db_user = crud.get_user_by_email(db, email=child.email)
    if db_user:
        raise HTTPException(status_code=400, detail="Email déjà utilisé")

    new_user = crud.create_user(db=db, user=child)
    return new_user

@app.put("/parent/children/{child_id}", response_model=schemas.User)
def update_child_account(child_id: int, updates: schemas.UserUpdate, db: Session = Depends(get_db)):
    user = crud.get_user(db, user_id=child_id)
    if not user or user.is_parent:
        raise HTTPException(status_code=404, detail="Enfant non trouvé")

    if updates.email:
        existing_user = crud.get_user_by_email(db, email=updates.email)
        if existing_user and existing_user.id != child_id:
            raise HTTPException(status_code=400, detail="Cet email est déjà utilisé")
        user.email = updates.email

    if updates.password:
        from auth import get_password_hash
        user.hashed_password = get_password_hash(updates.password)

    db.commit()
    db.refresh(user)
    return user
