package com.kaiburr.taskapi.controller;

import com.kaiburr.taskapi.model.Task;
import com.kaiburr.taskapi.model.TaskExecution;
import com.kaiburr.taskapi.service.TaskService;
import com.kaiburr.taskapi.util.CommandValidator;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
public class TaskController {

    private final TaskService svc;

    public TaskController(TaskService svc) {
        this.svc = svc;
    }

    // GET /api/tasks or /api/tasks?id=123
    @GetMapping("/tasks")
    public ResponseEntity<?> getTasks(@RequestParam(required = false) String id) {
        if (id == null) {
            return ResponseEntity.ok(svc.findAll());
        }
        return svc.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // PUT /api/tasks (create or update)
    @PutMapping("/tasks")
    public ResponseEntity<?> putTask(@RequestBody Task t) {
        if (t.getCommand() == null || !CommandValidator.isSafe(t.getCommand())) {
            return ResponseEntity.badRequest().body("Invalid or unsafe command");
        }
        Task saved = svc.save(t);
        return ResponseEntity.ok(saved);
    }

    // DELETE /api/tasks/{id}
    @DeleteMapping("/tasks/{id}")
    public ResponseEntity<?> deleteTask(@PathVariable String id) {
        if (svc.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        svc.delete(id);
        return ResponseEntity.ok().build();
    }

    // GET /api/tasks/search?name=foo
    @GetMapping("/tasks/search")
    public ResponseEntity<?> search(@RequestParam String name) {
        List<Task> list = svc.findByNameContains(name);
        if (list == null || list.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(list);
    }

    // PUT /api/tasks/{id}/executions -> run command and record execution
    @PutMapping("/tasks/{id}/executions")
    public ResponseEntity<?> runExecution(@PathVariable String id) {
        try {
            TaskExecution exec = svc.runTask(id);
            return ResponseEntity.ok(exec);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Execution failed: " + e.getMessage());
        }
    }
}
