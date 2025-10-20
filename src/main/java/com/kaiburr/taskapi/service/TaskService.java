package com.kaiburr.taskapi.service;

import com.kaiburr.taskapi.model.Task;
import com.kaiburr.taskapi.model.TaskExecution;
import com.kaiburr.taskapi.repository.TaskRepository;
import com.kaiburr.taskapi.util.CommandValidator;
import org.springframework.stereotype.Service;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService {
    private final TaskRepository repo;

    // Name of the executor container that acts like a pod
    private final String executorContainer = "task-runner";

    public TaskService(TaskRepository repo) {
        this.repo = repo;
    }

    public List<Task> findAll() {
        return repo.findAll();
    }

    public Optional<Task> findById(String id) {
        return repo.findById(id);
    }

    public Task save(Task t) {
        return repo.save(t);
    }

    public void delete(String id) {
        repo.deleteById(id);
    }

    public List<Task> findByNameContains(String fragment) {
        return repo.findByNameContainingIgnoreCase(fragment);
    }

    public TaskExecution runTask(String taskId) throws Exception {
        Task task = repo.findById(taskId)
            .orElseThrow(() -> new IllegalArgumentException("Task not found"));

        String cmd = task.getCommand();
        if (!CommandValidator.isSafe(cmd)) {
            throw new IllegalArgumentException("Command failed validation");
        }

        TaskExecution exec = new TaskExecution();
        exec.setStartTime(Instant.now());

        // Build docker exec command -> runs inside container which simulates a pod
        ProcessBuilder pb = new ProcessBuilder("docker", "exec", "-i", executorContainer, "sh", "-c", cmd);
        pb.redirectErrorStream(true);

        Process p = pb.start();

        StringBuilder out = new StringBuilder();
        try (BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()))) {
            String line;
            while ((line = r.readLine()) != null) {
                out.append(line).append(System.lineSeparator());
            }
        }

        int exit = p.waitFor(); // wait for command to finish
        exec.setEndTime(Instant.now());
        exec.setOutput("exit=" + exit + "\n" + out.toString());

        // store execution
        task.getTaskExecutions().add(exec);
        repo.save(task);
        return exec;
    }
}
