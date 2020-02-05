package se.ecutb.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.ecutb.data.IdSequencers;
import se.ecutb.data.TodoRepository;
import se.ecutb.dto.TodoDto;
import se.ecutb.model.Person;
import se.ecutb.model.Todo;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Component
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
        if (!todoRepository.findById(todoId).isPresent()){
            throw new IllegalArgumentException("No such id");
        }
        return todoRepository.findAll().stream()
                .filter(todo -> todo.getTodoId() == todoId)
                .map(todo -> todoDtoConversionService.convertToDto(todo))
                .findFirst().get();
    }

    @Override
    public List<TodoDto> findByTaskDescription(String taskDescription) {
        return todoRepository.findAll().stream()
                .filter(todo -> todo.getTaskDescription().toLowerCase().contains(taskDescription))
                .map(todo -> todoDtoConversionService.convertToDto(todo))
                .collect(Collectors.toList());
    }

    @Override
    public List<TodoDto> findByDeadLineBefore(LocalDate endDate) {
        return todoRepository.findAll().stream()
                .filter(todo -> todo.getDeadLine().isBefore(endDate))
                .map(todo -> todoDtoConversionService.convertToDto(todo))
                .collect(Collectors.toList());
    }

    @Override
    public List<TodoDto> findByDeadLineAfter(LocalDate startDate) {
        return todoRepository.findAll().stream()
                .filter(todo -> todo.getDeadLine().isAfter(startDate))
                .map(todo -> todoDtoConversionService.convertToDto(todo))
                .collect(Collectors.toList());
    }

    @Override
    public List<TodoDto> findByDeadLineBetween(LocalDate startDate, LocalDate endDate) {
        return todoRepository.findAll().stream()
                .filter(todo -> todo.getDeadLine().isBefore(endDate)
                && todo.getDeadLine().isAfter(startDate))
                .map(todo -> todoDtoConversionService.convertToDto(todo))
                .collect(Collectors.toList());
    }

    @Override
    public List<TodoDto> findAssignedTasksByPersonId(int personId) {
        return todoRepository.findAll().stream()
                .filter(todo -> todo.getAssignee() != null
                        && todo.getAssignee().getPersonId() == personId)
                .map(todo -> todoDtoConversionService.convertToDto(todo))
                .collect(Collectors.toList());
    }

    @Override
    public List<TodoDto> findUnassignedTasks() {
        return todoRepository.findAll().stream()
                .filter(todo -> todo.getAssignee() == null)
                .map(todo -> todoDtoConversionService.convertToDto(todo))
                .collect(Collectors.toList());
    }

    @Override
    public List<TodoDto> findAssignedTasks() {
        return todoRepository.findAll().stream()
                .filter(todo -> todo.getAssignee() != null)
                .map(todo -> todoDtoConversionService.convertToDto(todo))
                .collect(Collectors.toList());
    }

    @Override
    public List<TodoDto> findByDoneStatus(boolean done) {
        return todoRepository.findAll().stream()
                .filter(todo -> todo.isDone() == done)
                .map(todo -> todoDtoConversionService.convertToDto(todo))
                .collect(Collectors.toList());
    }

    @Override
    public List<TodoDto> findAll() {
        return todoRepository.findAll().stream()
                .map(todo -> todoDtoConversionService.convertToDto(todo))
                .collect(Collectors.toList());
    }

    @Override
    public boolean delete(int todoId) throws IllegalArgumentException {
        return todoRepository.delete(todoId);
    }
}
