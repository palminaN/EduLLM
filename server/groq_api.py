# app/groq_api.py

import os
import requests
import yaml
import openai
from dotenv import load_dotenv

# Chargement des variables d'environnement
load_dotenv()
API_KEY = os.getenv("GROQ_API_KEY")

# Configuration de l'API Groq via openai
openai.api_key = API_KEY
GROQ_API_URL = "https://api.groq.com/openai/v1"  #TODO attention à l'URL exacte selon Groq

# Chargement des prompts
def load_prompts(file_name="prompts.yaml"):
    execution_dir = os.path.dirname(os.path.abspath(__file__))
    file_path = os.path.join(execution_dir, file_name)
    with open(file_path, "r", encoding="utf-8") as file:
        return yaml.safe_load(file)

PROMPTS = load_prompts()



def query_groq_llm(route: str, user_text: str, model="gemma2-9b-it"):
    prompt_text = PROMPTS.get(route, "")
    messages = [
        {"role": "system", "content": prompt_text},
        {"role": "user", "content": user_text}
    ]

    try:
        response = openai.ChatCompletion.create(
            model=model,
            messages=messages
        )
        ai_response = response["choices"][0]["message"]["content"]
        return ai_response
    except Exception as e:
        print(f"Erreur LLM : {e}")
        return "Erreur lors de la génération de la réponse."
