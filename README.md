# DevPilot AI

AI knowledge-base assistant for practicing Java + Vue 3 full-stack development.

## Project Structure

```text
devpilot-ai/
  backend/   Spring Boot API service
  frontend/  Vue 3 + Vite web app
```

## Roadmap

- V1: Login, knowledge-base CRUD, document upload, text chunking, vector search, AI Q&A.
- V2: Streaming chat, source citations, chat history, knowledge-base switching.
- V3: AI project assistant tools, such as API design, Vue page scaffolding, and test-case generation.
- V4: Permissions, usage statistics, prompt templates, async document processing, and Docker deployment.

## Learning Notes

See [docs/learning-roadmap.md](docs/learning-roadmap.md) for the step-by-step learning plan and project notes.

## Local Development

### Backend

```bash
cd backend
./mvnw spring-boot:run
```

The API will start at `http://localhost:8080`.

### Frontend

```bash
cd frontend
npm install
npm run dev
```

The web app will start at `http://localhost:5173`.

## Environment Variables

Create `backend/.env` or configure your IDE with:

```bash
OPENAI_API_KEY=your_api_key
```

AI integration is intentionally left as a next implementation step.
