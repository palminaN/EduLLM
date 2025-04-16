# app/groq_api.py
import requests

GROQ_API_URL = "https://api.groq.com/v1/llm"
API_KEY = "your_api_key_here"

def query_groq_llm(prompt: str):
    headers = {
        "Authorization": f"Bearer {API_KEY}",
        "Content-Type": "application/json"
    }
    data = {
        "prompt": prompt
    }
    response = requests.post(GROQ_API_URL, headers=headers, json=data)
    return response.json()
