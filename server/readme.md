# EduLLM – Backend FastAPI

## Installation et lancement

1. Installe les dépendances :
   ```
   pip install -r requirements.txt
   ```

2. Ajoute ta clé API dans un fichier `.env` situé dans le dossier `server/` :
   ```
   GROQ_API_KEY=ta_clé_groq_ici
   ```

3. Démarre le serveur local :
   ```
   uvicorn main:app --reload
   ```

4. Accède à la documentation interactive :
   [http://localhost:8000/docs](http://localhost:8000/docs)

---

## API EduLLM – Documentation des routes

### Authentification

#### POST `/register/`
Crée un nouvel utilisateur (parent ou enfant).

**Body (JSON)** :
```json
{
  "email": "user@example.com",
  "password": "motdepasse",
  "is_parent": true
}
```

#### POST `/login/`
Authentifie un utilisateur existant.

**Body (JSON)** :
```json
{
  "email": "user@example.com",
  "password": "motdepasse"
}
```

---

### Enfant

#### GET `/child/{child_id}/profile`
Récupère les informations d’un enfant.

#### GET `/child/{child_id}/badges`
Renvoie les badges obtenus par l’enfant.

#### POST `/child/{child_id}/badges?parent_id=...`
Attribue un nouveau badge à un enfant.

**Body (JSON)** :
```json
{
  "name": "Super Lecteur"
}
```

---

### Exercices interactifs (LLM)

#### GET `/exercise/math`
Génère une question de mathématiques simple.

#### GET `/exercise/vocab`
Génère une question de vocabulaire.

#### POST `/exercise/vocab`
Vérifie la réponse donnée par l’enfant.

**Body (JSON)** :
```json
{
  "question": "Quel est un synonyme de 'joie' ?",
  "answer": "bonheur"
}
```

#### GET `/exercise/grammar`
Fournit une phrase à corriger.

#### POST `/exercise/grammar`
Vérifie la correction proposée.

**Body (JSON)** :
```json
{
  "phrase": "Le chien a manger de la soupe",
  "answer": "Le chien a mangé de la soupe"
}
```

#### GET `/quiz`
Génère une question de culture générale à choix multiple.

---

### Histoires (LLM)

#### POST `/story/start`
Génère le début d’une histoire.

**Body (JSON)** :
```json
{
  "theme": "espace",
  "character": "Léo"
}
```

#### POST `/story/continue`
Génère la suite d’une histoire.

**Body (JSON)** :
```json
{
  "previous": "Léo entra dans la fusée..."
}
```

---

### Parent

#### GET `/parent/{parent_id}/profile`
Récupère les informations d’un parent.

#### GET `/parent/children`
Renvoie la liste de tous les enfants (non filtrée par parent).

#### POST `/parent/children/?parent_id=...`
Crée un enfant associé à un parent donné.

**Body (JSON)** :
```json
{
  "email": "enfant@edu.fr",
  "password": "1234",
  "is_parent": false
}
```

#### PUT `/parent/children/{child_id}?parent_id=...`
Met à jour le compte d’un enfant (email, mot de passe).

**Body (JSON)** :
```json
{
  "email": "nouveau@mail.com",
  "password": "newpass"
}
```

#### PUT `/parent/settings?user_id=...`
Modifie le mot de passe d’un parent.

**Body (JSON)** :
```json
{
  "new_password": "motdepasse123"
}
```

---

### Utilitaire

#### HEAD `/ping`
Permet de vérifier que l’API est en ligne (utile pour Render ou outils de monitoring).
