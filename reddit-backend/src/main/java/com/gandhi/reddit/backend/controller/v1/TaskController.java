package com.gandhi.reddit.backend.controller.v1;

import com.gandhi.reddit.backend.domain.Task;
import com.gandhi.reddit.backend.dto.TaskDto;
import com.gandhi.reddit.backend.domain.TaskStatus;
import com.gandhi.reddit.backend.repository.TaskRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    /**
     * NOTE : In the requirements, it is mentioned that if the task is not found, then HTTP Code 204 should be returned.
     *        Could this be a typo? I've used HTTP Code 204, however, I feel that 404 would be more appropriate.
     */


    private static final Map<String, TaskStatus> VALID_TASK_STATUES_MAP = Arrays.stream(TaskStatus.values())
                                                                            .collect(Collectors.toMap(TaskStatus::name, taskStatus -> taskStatus));
    private static final String INVALID_STATUS_ERROR_MESSAGE = String.format("Available statutes are: %s",
                                                                                String.join(", ",
                                                                                        Arrays.stream(TaskStatus.values())
                                                                                                .map(TaskStatus::name).collect(Collectors.toList())));

    private final TaskRepository repository;

    @Autowired
    public TaskController(TaskRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public ResponseEntity<List<TaskDto>> getAllTasks() {
        return ResponseEntity.ok().body(StreamSupport.stream(repository.findAll().spliterator(), false).map(Task::toDto).collect(Collectors.toList()));
    }

    @PostMapping
    public ResponseEntity<String> createTask(@RequestBody TaskDto taskDto) {

        if (taskDto.getTitle() == null || taskDto.getTitle().isEmpty()) {
            return ResponseEntity.badRequest().body("Task title is mandatory");
        }

        final Task task = new Task(taskDto.getTitle());
        task.setTaskStatus(TaskStatus.CREATED);
        task.setDescription(taskDto.getDescription());

        repository.save(task);

        return ResponseEntity.status(HttpStatus.CREATED).body(String.valueOf(task.getId()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskDto> getTask(@PathVariable("id") final Long id) {
        return repository.findById(id)
                .map(task -> ResponseEntity.ok(task.toDto()))
                .orElse(ResponseEntity.noContent().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity updateTask(@PathVariable("id") final Long id, @RequestBody final TaskDto taskDto) {

        if (!VALID_TASK_STATUES_MAP.containsKey(taskDto.getStatus())) {
            return ResponseEntity.ok().body(INVALID_STATUS_ERROR_MESSAGE);
        }

        final TaskStatus taskStatus = VALID_TASK_STATUES_MAP.get(taskDto.getStatus());

        return repository.findById(id)
                .map(task -> {

                    if (taskDto.getTitle() != null && !taskDto.getTitle().isEmpty()) {
                        task.setTitle(taskDto.getTitle());
                    }

                    if (taskDto.getDescription() != null && !taskDto.getDescription().isEmpty()) {
                        task.setDescription(taskDto.getDescription());
                    }

                    task.setTaskStatus(taskStatus);

                    return repository.save(task);
                })
                .map(updatedTask -> ResponseEntity.ok().body(updatedTask.toDto()))
                .orElse(ResponseEntity.noContent().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteTask(@PathVariable("id") final Long id, @RequestBody final TaskDto taskDto) {

        return repository.findById(id)
                .map(task -> {
                    repository.delete(task);
                    return ResponseEntity.ok().build();
                })
                .orElse(ResponseEntity.noContent().build());
    }

    @GetMapping("/describe/{id}")
    public ResponseEntity<String> getTaskDescription(@PathVariable("id") final Long id) {
        return repository.findById(id)
                .map(Task::toDto)
                .map(task -> ResponseEntity.ok(String.format("Description of Task [%s: %s] is: %s", task.getId(), task.getTitle(), task.getDescription())))
                .orElse(ResponseEntity.ok(String.format("Task with id = %s does not exist", id)));
    }

    @GetMapping("/describe")
    public ResponseEntity<List<String>> getAllTaskDescription() {
        final List<String> taskDescriptions = StreamSupport.stream(repository.findAll().spliterator(), false)
                .map(Task::toDto)
                .map(taskDto -> String.format("Description of Task [%s: %s] is: %s", taskDto.getId(), taskDto.getTitle(), taskDto.getDescription()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(taskDescriptions);
    }
}
