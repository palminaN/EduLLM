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
