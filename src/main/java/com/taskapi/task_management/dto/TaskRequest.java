package com.taskapi.task_management.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class TaskRequest {
    private String title;
    private String description;
    private String status;    // Optional (default: PENDING)
    private String priority;  // Optional (default: MEDIUM)
    private LocalDate dueDate;
}
