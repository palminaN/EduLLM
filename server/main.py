from fastapi import FastAPI, Depends, HTTPException, Request
from fastapi.responses import RedirectResponse, JSONResponse
from sqlalchemy.orm import Session
import crud, models, schemas, database, groq_api
import re
from database import SessionLocal, engine
from groq_api import query_groq_llm

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

@app.post("/register/", response_model=schemas.UserAll)
def create_user(user: schemas.UserCreate, db: Session = Depends(get_db)):
    db_user = crud.get_user_by_email(db, email=user.email)
    if db_user:
        raise HTTPException(status_code=400, detail="Email already registered")
    
    user_dict = user.dict()
    user_dict["is_parent"] = True
    new_user = crud.create_user(db=db, user_data=user_dict)
    new_user.parent_id = -1  # convention si c’est un parent
    return new_user


@app.post("/login/", response_model=schemas.UserAll)
def login_user(user: schemas.UserLogin, db: Session = Depends(get_db)):
    db_user = crud.authenticate_user(db, user.email, user.password)
    if not db_user:
        raise HTTPException(status_code=401, detail="Invalid email or password")

    if db_user.is_parent:
        db_user.parent_id = -1  # même convention
    return db_user




# ======================== Enfant - Fonctions ========================

#TODO : erreur de redirection si id de parent
@app.get("/child/{child_id}/profile", response_model=schemas.User)
def get_child_profile(child_id: int, db: Session = Depends(get_db)):
    user = crud.get_user(db, user_id=child_id)
    if not user or user.is_parent:
        raise HTTPException(status_code=404, detail="Enfant non trouvé")
    return user

@app.get("/child/{child_id}/badges", response_model=list[schemas.Badge])
def get_child_badges(child_id: int, db: Session = Depends(get_db)):
    user = crud.get_user(db, user_id=child_id)
    if not user or user.is_parent:
        raise HTTPException(status_code=404, detail="Enfant non trouvé")
    return crud.get_badges_for_child(db, child_id=child_id)

@app.post("/child/{child_id}/badges", response_model=schemas.Badge)
def give_badge_to_child(
    child_id: int,
    badge: schemas.BadgeCreate,
    db: Session = Depends(get_db)
):
    user = crud.get_user(db, user_id=child_id)
    if not user or user.is_parent:
        raise HTTPException(status_code=404, detail="Enfant non trouvé")
    return crud.add_badge_to_child(db, child_id=child_id, badge=badge)



@app.get("/exercise/math")
def get_math_exercise():
    question = query_groq_llm("/exercise/math", "Génère une expression mathématique (addition, soustraction ou multiplication).")
    #print(f"Question générée : {question}")
    match = re.search(r"(\d+)\s*([+\-*x×])\s*(\d+)", question, re.IGNORECASE)
    if not match:
        raise HTTPException(status_code=400, detail="Question non valide")

    a, op, b = match.groups()
    op = '*' if op in ['x', '×'] else op

    try:
        result = eval(f"{a}{op}{b}")
    except Exception:
        raise HTTPException(status_code=500, detail="Erreur de calcul")

    return {"question": question, "answer": result}

"""
@app.post("/exercise/math")
def check_math_answer(user_answer: str, question: str):
    full_prompt = f"Question : {question}\nRéponse donnée : {user_answer}\nÉvalue si c'est correct."
    correction = query_groq_llm("/exercise/math", full_prompt)
    return {"correction": correction}


@app.get("/exercise/vocab")
def get_vocab_exercise():
    question = query_groq_llm("/exercise/vocab", "Donne un mot et demande un synonyme.")
    return {"instruction": question}

@app.post("/exercise/vocab")
def check_vocab_answer(answer: str, question: str):
    prompt = f"Mot demandé : {question}\nRéponse : {answer}\nDis si c'est bon."
    return {"feedback": query_groq_llm("/exercise/vocab", prompt)}

@app.get("/exercise/grammar")
def get_grammar_exercise():
    question = query_groq_llm("/exercise/grammar", "Fournis une phrase mal écrite à corriger.")
    return {"phrase": question}

@app.post("/exercise/grammar")
def check_grammar_answer(answer: str, phrase: str):
    prompt = f"Phrase initiale : {phrase}\nCorrection proposée : {answer}\nCorrige ou approuve."
    return {"correction": query_groq_llm("/exercise/grammar", prompt)}

"""

@app.get("/exercise/langue")
def get_language_exercise():
    question = query_groq_llm("/exercise/langue", f"Génère un exercice pour enfant.")
    return {"instruction": question}


@app.post("/exercise/langue")
def check_language_answer(answer: str, instruction: str):
    prompt = (
        f"Exercice : {instruction}\n"
        f"Réponse proposée : {answer}\n"
        f"Évalue si la réponse est correcte, claire ou à améliorer, pour un élève de primaire."
    )
    feedback = query_groq_llm("/exercise/langue", prompt)
    return {"feedback": feedback}

@app.get("/quiz")
def get_quiz_question():
    raw = query_groq_llm("/quiz", "Génère une question simple avec trois choix. Format strict.")
    
    lines = raw.splitlines()
    q_line = next((line for line in lines if line.startswith("Question :")), "")
    a_line = next((line for line in lines if line.startswith("A.")), "")
    b_line = next((line for line in lines if line.startswith("B.")), "")
    c_line = next((line for line in lines if line.startswith("C.")), "")
    r_line = next((line for line in lines if "Réponse correcte" in line), "")

    try:
        question = q_line.replace("Question :", "").strip()
        choices = {
            "A": a_line[2:].strip(),
            "B": b_line[2:].strip(),
            "C": c_line[2:].strip(),
        }
        correct = r_line.split(":")[1].strip()
    except Exception:
        raise HTTPException(status_code=500, detail="Impossible d'analyser la réponse du LLM")

    return {
        "question": question,
        "choices": choices,
        "correct_answer": correct  # "A", "B", ou "C"
    }


"""
@app.post("/quiz")
def check_quiz_answer(answer: str, question: str):
    prompt = f"Question : {question}\nRéponse : {answer}\nEst-ce correct ?"
    return {"result": query_groq_llm("/quiz", prompt)}
"""

@app.post("/story/start")
def generate_story_start(theme: str, character: str):
    prompt = f"Crée une histoire pour enfant de 7 à 11 ans avec le personnage {character} avec pour thème '{theme}'."
    story = query_groq_llm("/story/start", prompt)
    return {"story": story}

@app.post("/story/continue")
def generate_story_continue(previous: str, theme: str, character: str):
    prompt = (
        f"Tu racontes une histoire dans le thème '{theme}', "
        f"avec le personnage principal '{character}'. Voici le début de l'histoire :\n\n"
        f"{previous}\n\nContinue cette histoire avec le même style et personnage."
    )
    story = query_groq_llm("/story/continue", prompt)
    return {"story": story}




# ======================== Parent - Fonctions ========================

#TODO : erreur de redirection si id d'enfant
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

@app.put("/parent/settings")
def update_parent_settings(user_id: int, new_password: str, db: Session = Depends(get_db)):
    user = crud.get_user(db, user_id=user_id)
    if not user or not user.is_parent:
        raise HTTPException(status_code=404, detail="Parent non trouvé")
    from auth import get_password_hash
    user.hashed_password = get_password_hash(new_password)
    db.commit()
    return {"message": "Mot de passe mis à jour"}

@app.post("/parent/children", response_model=schemas.UserAll)
def create_child_account(
    child: schemas.UserCreate,
    parent_id: int,
    db: Session = Depends(get_db)
):
    db_user = crud.get_user_by_email(db, email=child.email)
    if db_user:
        raise HTTPException(status_code=400, detail="Email déjà utilisé")

    parent = crud.get_user(db, parent_id)
    if not parent or not parent.is_parent:
        raise HTTPException(status_code=403, detail="ID parent non valide")

    # Forcer le rôle enfant dans les données
    user_data = child.dict()
    user_data["is_parent"] = False

    new_child = crud.create_user(db=db, user_data=user_data, parent_id=parent.id)
    return new_child

@app.put("/parent/children/{child_id}", response_model=schemas.User)
def update_child(
    child_id: int,
    updates: schemas.UserUpdate,
    parent_id: int, 
    db: Session = Depends(get_db)
):
    user = crud.get_user(db, user_id=child_id)
    if not user or user.is_parent:
        raise HTTPException(status_code=404, detail="Enfant non trouvé")

    if user.parent_id != parent_id:
        raise HTTPException(status_code=403, detail="Vous ne pouvez pas modifier cet enfant.")

    update_data = {}

    if updates.email:
        existing = crud.get_user_by_email(db, email=updates.email)
        if existing and existing.id != child_id:
            raise HTTPException(status_code=400, detail="Cet email est déjà utilisé")
        update_data["email"] = updates.email

    if updates.password:
        from auth import get_password_hash
        update_data["hashed_password"] = get_password_hash(updates.password)

    user = crud.update_user(db, child_id, update_data)
    return user






@app.head("/ping")
def ping():
    return {"status": "ok"}

if __name__ == "__main__":
    import os
    import uvicorn

    port = int(os.environ.get("PORT", 8000))
    uvicorn.run("main:app", host="0.0.0.0", port=port)
