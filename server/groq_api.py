import os
import requests
import yaml
from datetime import datetime
from openai import OpenAI
from dotenv import load_dotenv

# Chargement des variables d'environnement
load_dotenv()
API_KEY = os.getenv("GROQ_API_KEY")

# Configuration de l'API Groq via openai
GROQ_API_URL = "https://api.groq.com/openai/v1"  #TODO attention à l'URL exacte selon Groq

# Initialiser le client OpenAI compatible Groq
client = OpenAI(
    api_key=API_KEY,
    base_url=GROQ_API_URL
)

# Chargement des prompts
def load_prompts(file_name="prompts.yaml"):
    execution_dir = os.path.dirname(os.path.abspath(__file__))
    file_path = os.path.join(execution_dir, file_name)
    with open(file_path, "r", encoding="utf-8") as file:
        return yaml.safe_load(file)

PROMPTS = load_prompts()



def query_groq_llm(route: str, user_text: str, model="gemma2-9b-it"):
    prompt_text = f"{PROMPTS.get(route)}\nÉtiquette : {datetime.now().isoformat()}"
    messages = [
        {"role": "system", "content": prompt_text},
        {"role": "user", "content": user_text}
    ]

    try:
        response = client.chat.completions.create(
            model=model,
            messages=messages,
            temperature=0.8
        )

        answer = response.choices[0].message.content
        print(answer)
        return answer
    except Exception as e:
        print(f"Erreur LLM :\n{e}")
        return "Erreur lors de l'appel à l'IA."
