package se.ecutb.service;

import org.springframework.beans.factory.annotation.Autowired;
import se.ecutb.data.IdSequencers;
import se.ecutb.data.TodoRepository;
import se.ecutb.dto.TodoDto;
import se.ecutb.model.Person;
import se.ecutb.model.Todo;

import java.time.LocalDate;
import java.util.List;

public class TodoServiceImpl implements TodoService {



    private CreateTodoService createTodoService;
    private IdSequencers idSequencers;
    private CreatePersonService createPersonService;
    private TodoDtoConversionService todoDtoConversionService;
    private TodoRepository todoRepository;

    @Autowired
    public TodoServiceImpl(CreateTodoService createTodoService, IdSequencers idSequencers, CreatePersonService createPersonService,
                           TodoDtoConversionService todoDtoConversionService, TodoRepository todoRepository) {
        this.createTodoService = createTodoService;
        this.idSequencers = idSequencers;
        this.createPersonService = createPersonService;
        this.todoDtoConversionService = todoDtoConversionService;
        this.todoRepository = todoRepository;
    }


    @Override
    public Todo createTodo(String taskDescription, LocalDate deadLine, Person assignee) {
        return todoRepository.persist(createTodoService.createTodo(taskDescription,deadLine,assignee));
    }

    @Override
    public TodoDto findById(int todoId) throws IllegalArgumentException {
        return todoRepository.findById(todoId).;
    }

    @Override
    public List<TodoDto> findByTaskDescription(String taskDescription) {
        return null;
    }

    @Override
    public List<TodoDto> findByDeadLineBefore(LocalDate endDate) {
        return null;
    }

    @Override
    public List<TodoDto> findByDeadLineAfter(LocalDate startDate) {
        return null;
    }

    @Override
    public List<TodoDto> findByDeadLineBetween(LocalDate startDate, LocalDate endDate) {
        return null;
    }

    @Override
    public List<TodoDto> findAssignedTasksByPersonId(int personId) {
        return null;
    }

    @Override
    public List<TodoDto> findUnassignedTasks() {
        return null;
    }

    @Override
    public List<TodoDto> findAssignedTasks() {
        return null;
    }

    @Override
    public List<TodoDto> findByDoneStatus(boolean done) {
        return null;
    }

    @Override
    public List<TodoDto> findAll() {
        return null;
    }

    @Override
    public boolean delete(int todoId) throws IllegalArgumentException {
        return false;
    }
}
