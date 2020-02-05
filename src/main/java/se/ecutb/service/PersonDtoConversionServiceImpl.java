package se.ecutb.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.ecutb.data.TodoRepository;
import se.ecutb.dto.PersonDto;
import se.ecutb.dto.PersonDtoWithTodo;
import se.ecutb.dto.TodoDto;
import se.ecutb.model.Person;
import se.ecutb.model.Todo;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Component
public class PersonDtoConversionServiceImpl implements PersonDtoConversionService {


    private TodoRepository todoRepository;
    private TodoDtoConversionService todoDtoConversionService;

    @Autowired
    public PersonDtoConversionServiceImpl(TodoRepository todoRepository, TodoDtoConversionService todoDtoConversionService) {
        this.todoRepository = todoRepository;
        this.todoDtoConversionService = todoDtoConversionService;
    }

    @Override
    public PersonDto convertToPersonDto(Person person) {
        return new PersonDto(person.getPersonId(),person.getFirstName(),person.getLastName());
    }

    @Override
    public PersonDtoWithTodo convertToPersonDtoWithTodo(Person person, List<Todo> assignedTodos) {
        List<Todo> todoList = assignedTodos.stream()
                .filter(todo -> todo.getAssignee() != null &&
                        todo.getAssignee().equals(person))
                .collect(Collectors.toList());
        List<TodoDto> todoDtoList = todoList.stream()
                .map(todo -> todoDtoConversionService.convertToDto(todo))
                .collect(Collectors.toList());
        return new PersonDtoWithTodo(person.getPersonId(),person.getFirstName(),person.getLastName(),todoDtoList);
        }

}
