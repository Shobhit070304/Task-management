package com.taskapi.task_management.service;

import com.taskapi.task_management.exception.TaskNotFoundException;
import com.taskapi.task_management.models.Task;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskService {
    private final List<Task> tasks = new ArrayList<>();
    private Long nextId = 1L;

    public TaskService() {
        tasks.add(new Task(nextId++, "Learn Spring Boot Basics", "Complete Chapter 1", "COMPLETED", "HIGH", LocalDate.now().minusDays(5)));
        tasks.add(new Task(nextId++, "Build REST API", "Implement CRUD operations", "IN_PROGRESS", "HIGH", LocalDate.now().plusDays(3)));
        tasks.add(new Task(nextId++, "Add Database Integration", "Learn JPA and Hibernate", "PENDING", "MEDIUM", LocalDate.now().plusDays(7)));
    }


    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks);
    }

    public Task getTaskById(Long id) {
        return tasks.stream().filter(task -> task.getId().equals(id)).findFirst().orElseThrow(() -> new TaskNotFoundException(id));
    }

    public Task createTask(String title, String description, String status, String priority, LocalDate dueDate) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be empty");
        }

        String finalStatus = (status != null) ? status.toUpperCase() : "PENDING";
        String finalPriority = (priority != null) ? priority.toUpperCase() : "MEDIUM";

        // Validate status
        if (!isValidStatus(finalStatus)) {
            throw new IllegalArgumentException("Invalid status. Allowed: PENDING, IN_PROGRESS, COMPLETED");
        }

        // Validate priority
        if (!isValidPriority(finalPriority)) {
            throw new IllegalArgumentException("Invalid priority. Allowed: LOW, MEDIUM, HIGH");
        }

        if (dueDate.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Due date cannot be in the past");
        }

        Task newTask = new Task(nextId++, title, description, finalStatus, finalPriority, dueDate);
        tasks.add(newTask);
        return newTask;
    }

    public Task updateTask(Long id, String title, String description, String status, String priority, LocalDate dueDate) {
        Task task = getTaskById(id); // This will throw exception if not found

        // Update fields (only if provided)
        if (title != null && !title.trim().isEmpty()) {
            task.setTitle(title);
        }

        if (description != null) {
            task.setDescription(description);
        }

        if (status != null) {
            String upperStatus = status.toUpperCase();
            if (!isValidStatus(upperStatus)) {
                throw new IllegalArgumentException("Invalid status. Allowed: PENDING, IN_PROGRESS, COMPLETED");
            }
            task.setStatus(upperStatus);
        }

        if (priority != null) {
            String upperPriority = priority.toUpperCase();
            if (!isValidPriority(upperPriority)) {
                throw new IllegalArgumentException("Invalid priority. Allowed: LOW, MEDIUM, HIGH");
            }
            task.setPriority(upperPriority);
        }

        if(dueDate != null){
            if (dueDate.isBefore(LocalDate.now())) {
                throw new IllegalArgumentException("Due date cannot be in the past");
            }
            task.setDueDate(dueDate);
        }

        return task;
    }

    public void deleteTask(Long id) {
        Task task = getTaskById(id); // Check if exists
        tasks.remove(task);
    }

    public List<Task> getTasksByStatus(String status) {
        String upperStatus = status.toUpperCase();
        if (!isValidStatus(upperStatus)) {
            throw new IllegalArgumentException("Invalid status. Allowed: PENDING, IN_PROGRESS, COMPLETED");
        }

        return tasks.stream()
                .filter(task -> task.getStatus().equals(upperStatus))
                .collect(Collectors.toList());
    }

    public List<Task> getTasksByPriority(String priority) {
        String upperPriority = priority.toUpperCase();
        if (!isValidPriority(upperPriority)) {
            throw new IllegalArgumentException("Invalid priority. Allowed: LOW, MEDIUM, HIGH");
        }

        return tasks.stream()
                .filter(task -> task.getPriority().equals(upperPriority))
                .collect(Collectors.toList());
    }

    public List<Task> getTasksByTitle(String title) {
        if (title == null || title.isBlank()) {
            return List.of(); // or return all tasks (your choice)
        }

        String search = title.toLowerCase();

        return tasks.stream()
                .filter(task -> task.getTitle().toLowerCase().contains(search))
                .collect(Collectors.toList());
    }

    public long getTaskCount() {
        return tasks.size();
    }

    public long getTaskCountByStatus(String status) {
        return getTasksByStatus(status).size();
    }

    // Helper methods for validation
    private boolean isValidStatus(String status) {
        return status.equals("PENDING") || status.equals("IN_PROGRESS") || status.equals("COMPLETED");
    }

    private boolean isValidPriority(String priority) {
        return priority.equals("LOW") || priority.equals("MEDIUM") || priority.equals("HIGH");
    }
}
