# EduLLM – Backend FastAPI

## Installation et lancement

1. Installer les dépendances :
   ```
   pip install -r requirements.txt
   ```

2. Ajouter une clé API dans un fichier `.env` :
   ```
   GROQ_API_KEY=<your_api_key_here>
   ```

3. Démarrer le serveur local :
   ```
   uvicorn main:app --reload
   ```

4. Accéder à la documentation interactive :
   [http://localhost:8000/docs](http://localhost:8000/docs)

---

## API EduLLM – Documentation des routes

## Authentification

### POST `/register/`
Crée un nouvel utilisateur, **parent uniquement**.

**Body (JSON)** :
```json
{
  "email": "user@example.com",
  "password": "motdepasse"
}
```

### POST `/login/`
Authentifie un utilisateur existant (parent ou enfant).

**Body (JSON)** :
```json
{
  "email": "user@example.com",
  "password": "motdepasse"
}
```

---

## Enfant

### GET `/child/{child_id}/profile`
Récupère les informations d’un enfant.

### GET `/child/{child_id}/badges`
Renvoie les badges obtenus par l’enfant.

### POST `/child/{child_id}/badges`
Attribue un nouveau badge à un enfant.

**Body (JSON)** :
```json
{
  "name": "Super Lecteur"
}
```

---

## Exercices interactifs (LLM)

### GET `/exercise/math`
Génère une question de mathématiques simple (addition, soustraction, multiplication).

**Réponse :**
```json
{
  "question": "345 x 7",
  "answer": 2415
}
```

### GET `/exercise/langue`
Génère un exercice de français (vocabulaire ou grammaire) adapté à un enfant.

### POST `/exercise/langue`
Vérifie la réponse proposée à un exercice de français.

**Body (JSON)** :
```json
{
  "instruction": "Corrige la phrase : Le chien mangeais des croquettes.",
  "answer": "Le chien mangeait des croquettes."
}
```

---

## Quiz

### GET `/quiz`
Génère une question de culture générale à choix multiple.

**Réponse :**
```json
{
  "question": "Quelle est la capitale de la France ?",
  "choices": {
    "A": "Paris",
    "B": "Lyon",
    "C": "Marseille"
  },
  "correct_answer": "A"
}
```

---

## Histoires (LLM)

### POST `/story/start`
Génère le début d’une histoire.

**Body (JSON)** :
```json
{
  "theme": "espace",
  "character": "Léo"
}
```

### POST `/story/continue`
Génère la suite d’une histoire.

**Body (JSON)** :
```json
{
  "theme": "espace",
  "character": "Léo",
  "previous": "Léo entra dans la fusée..."
}
```

---

## Parent

### GET `/parent/{parent_id}/profile`
Récupère les informations d’un parent.

### GET `/parent/children`
Renvoie la liste de tous les enfants (non filtrée par parent).

### POST `/parent/children/?parent_id=...`
Crée un enfant associé à un parent donné.

**Body (JSON)** :
```json
{
  "email": "enfant@edu.fr",
  "password": "1234"
}
```

### PUT `/parent/children/{child_id}?parent_id=...`
Met à jour le compte d’un enfant (email, mot de passe).

**Body (JSON)** :
```json
{
  "email": "nouveau@mail.com",
  "password": "newpass"
}
```

### PUT `/parent/settings?user_id=...`
Modifie le mot de passe d’un parent.

**Body (JSON)** :
```json
{
  "new_password": "motdepasse123"
}
```

---

## Autres

### HEAD `/ping`
Permet de vérifier que le serveur est actif (utile pour Render ou du monitoring).
