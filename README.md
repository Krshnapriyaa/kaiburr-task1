#  Kaiburr Assessment – Task 1 (Java Backend + REST API)

**Name:** Krishna Priya K P  
**Date:** 18 October 2025  

---

##  Overview
A Spring Boot (Java 17) backend exposing REST endpoints to create, view, delete and execute **Task** objects.  
Each task represents a shell command executed inside a Docker container (`task-runner`).  
All task and execution details are stored in **MongoDB**.

---

##  Tech Stack
| Layer | Technology |
|:------|:------------|
| Backend | Spring Boot 3.3 |
| Language | Java 17 |
| Database | MongoDB 6.0 |
| Execution | Alpine (task-runner container) |
| Containerization | Docker / Compose |
| Testing | PowerShell + cURL |

---

##  API Endpoints
| Method | Endpoint | Purpose |
|:--------|:----------|:--------|
| GET | `/api/tasks` | List all tasks |
| GET | `/api/tasks?id={id}` | Fetch task by ID |
| GET | `/api/tasks/search?name={text}` | Search by name |
| PUT | `/api/tasks` | Create / update task |
| PUT | `/api/tasks/{id}/executions` | Run task command |
| DELETE | `/api/tasks/{id}` | Delete task |

---

##  Execution Steps & Screenshots

| Step | Command / Action | Expected Result | Screenshot |
|:-----|:-----------------|:----------------|:------------|
| 1 | `docker rm -f task-mongo task-runner 2>$null`<br>`docker run -d --name task-mongo -p 27017:27017 mongo:6.0`<br>`docker run -d --name task-runner alpine:3.19 sh -c "while true; do sleep 1000; done"`<br>`docker ps` | Both Mongo and runner containers are **Up** | ![Docker Containers](./Screenshot%20(196).png) |
| 2 | `mvn clean package`<br>`mvn spring-boot:run` | Spring Boot application starts on port 8080 | ![Spring Boot Startup](./Screenshot%20(194).png) |
| 3 | `curl.exe http://localhost:8080/api/tasks` | `[]` (empty list – no tasks yet) | ![GET Empty](./Screenshot%20(195).png) |
| 4 | ```powershell
$body = '{
  "id":"123",
  "name":"Print Hello",
  "owner":"John Smith",
  "command":"echo Hello World!"
}'
curl.exe -X PUT http://localhost:8080/api/tasks -H "Content-Type: application/json" -d $body
``` | Task created – JSON object returned | ![PUT Create Task](./Screenshot%20(197).png) |
| 5 | `curl.exe http://localhost:8080/api/tasks` | Shows saved task array | *(same as 197)* |
| 6 | `curl.exe -X PUT http://localhost:8080/api/tasks/123/executions` | Runs command → stores output `"Hello World!"` | *(add your execution screenshot)* |
| 7 | `curl.exe "http://localhost:8080/api/tasks?id=123"` | Displays task with `taskExecutions` list | *(add your screenshot)* |
| 8 | `curl.exe -X DELETE http://localhost:8080/api/tasks/123` | “Task deleted successfully” | *(optional)* |
| 9 | `curl.exe http://localhost:8080/api/tasks` | `[]` – deletion verified | *(optional)* |

---

##  Sample Responses
**Create Task**
```json
{
  "id":"123",
  "name":"Print Hello",
  "owner":"John Smith",
  "command":"echo Hello World!",
  "taskExecutions":[]
}
