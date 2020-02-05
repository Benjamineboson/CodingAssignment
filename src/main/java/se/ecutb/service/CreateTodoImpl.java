package se.ecutb.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.ecutb.data.IdSequencers;
import se.ecutb.data.TodoRepository;
import se.ecutb.model.AbstractTodoFactory;
import se.ecutb.model.Person;
import se.ecutb.model.Todo;

import java.time.LocalDate;
import java.util.List;

@Component
public class CreateTodoImpl extends AbstractTodoFactory implements CreateTodoService {


    private IdSequencers idSequencers;
    private TodoRepository todoRepository;

    @Autowired
    public CreateTodoImpl(IdSequencers idSequencers, TodoRepository todoRepository) {
        this.idSequencers = idSequencers;
        this.todoRepository = todoRepository;
    }

    @Override
    public Todo createTodo(String taskDescription, LocalDate deadLine, Person assignee) throws IllegalArgumentException {
        List<Todo> todoList = todoRepository.findAll();
        for (Todo todo:todoList) {
            if (todo.getTaskDescription().equalsIgnoreCase(taskDescription)){
                throw new IllegalArgumentException("Task already exists");
            }
        }
        return createTodoItem(idSequencers.nextTodoId(),taskDescription,deadLine,assignee);
    }

    @Override
    public Todo createTodo(String taskDescription, LocalDate deadLine) throws IllegalArgumentException {
        List<Todo> todoList = todoRepository.findAll();
        for (Todo todo:todoList) {
            if (todo.getTaskDescription().equalsIgnoreCase(taskDescription)){
                throw new IllegalArgumentException("Task already exists");
            }
        }
        return createTodoItem(idSequencers.nextTodoId(),taskDescription,deadLine,null);
    }
}
