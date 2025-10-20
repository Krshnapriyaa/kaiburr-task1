# 🚀 Kaiburr Assessment – Task 1 (Java Backend + REST API)

### 👩‍💻 Developer       : Krishna Priya K P  
### 📅 Date              : 18 October 2025  

---

## 📘 Project Overview
A Spring Boot ( Java 17 ) backend providing a REST API for managing and executing **Task** objects.  
Each task represents a shell command that can be executed inside a Docker container (`task-runner`).  
All task and execution details are persisted in **MongoDB**.

---

## 🧰 Technologies Used

| Component | Technology |
|------------|-------------|
| Language | Java 17 |
| Framework | Spring Boot 3.3 |
| Database | MongoDB 6.0 (Docker container) |
| Runner | Alpine Linux container |
| Containerization | Docker / Docker Compose |
| Tools | PowerShell / cURL |

---

## 🧩 API Endpoints

| Method | Endpoint | Description |
|--------|-----------|-------------|
| `GET` | `/api/tasks` | Get all tasks |
| `GET` | `/api/tasks?id={id}` | Get task by ID |
| `GET` | `/api/tasks/search?name={string}` | Search task by name |
| `PUT` | `/api/tasks` | Create / update a task |
| `PUT` | `/api/tasks/{id}/executions` | Execute the task command |
| `DELETE` | `/api/tasks/{id}` | Delete a task |

---

## ⚙️ Setup & Execution

### **1️⃣ Start MongoDB and Runner Containers**

```powershell
docker rm -f task-mongo task-runner 2>$null
docker run -d --name task-mongo -p 27017:27017 mongo:6.0
docker run -d --name task-runner alpine:3.19 sh -c "while true; do sleep 1000; done"
docker ps
