package se.ecutb.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.ecutb.data.IdSequencers;
import se.ecutb.data.PersonRepository;
import se.ecutb.data.TodoRepository;
import se.ecutb.dto.PersonDto;
import se.ecutb.dto.PersonDtoWithTodo;
import se.ecutb.model.Address;
import se.ecutb.model.Person;
import se.ecutb.model.Todo;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class PersonServiceImpl implements PersonService {

    private TodoRepository todoRepository;
    private PersonDtoConversionService personDtoConversionService;
    private CreatePersonService createPersonService;
    private PersonRepository personRepository;
    private IdSequencers idSequencers;

    @Autowired
    public PersonServiceImpl(CreatePersonService createPersonService, PersonRepository personRepository,
                             IdSequencers idSequencers,PersonDtoConversionService personDtoConversionService, TodoRepository todoRepository) {
        this.createPersonService = createPersonService;
        this.personRepository = personRepository;
        this.idSequencers = idSequencers;
        this.personDtoConversionService = personDtoConversionService;
        this.todoRepository = todoRepository;
    }

    @Override
    public Person createPerson(String firstName, String lastName, String email, Address address) {
        return personRepository.persist(createPersonService.create(firstName,lastName,email,address));
    }

    @Override
    public List<PersonDto> findAll() {
        List<PersonDto> personDtoList = personRepository.findAll().stream()
                .map(person -> personDtoConversionService.convertToPersonDto(person))
                .collect(Collectors.toList());
        return personDtoList;
    }

    @Override
    public PersonDto findById(int personId) throws IllegalArgumentException {
        return personRepository.findAll().stream()
                .filter(person -> person.getPersonId() == personId)
                .map(person -> personDtoConversionService.convertToPersonDto(person))
                .findFirst().orElseThrow(IllegalArgumentException::new);
    }

    @Override
    public Person findByEmail(String email) throws IllegalArgumentException {
        if (personRepository.findAll().contains(findByEmail(email))){
            return null;
        }else{
            return personRepository.findAll().stream()
                    .filter(person -> person.getEmail().equalsIgnoreCase(email))
                    .findFirst().get();
        }

    }

    @Override
    public List<PersonDtoWithTodo> findPeopleWithAssignedTodos() {
        List<Todo> todoList = todoRepository.findAll().stream()
                .collect(Collectors.toList());
        List<PersonDtoWithTodo> personDtoWithTodoList = personRepository.findAll().stream()
                .map(person -> personDtoConversionService.convertToPersonDtoWithTodo(person,todoList))
                .collect(Collectors.toList());

        return personDtoWithTodoList.stream()
                .filter(personDtoWithTodo -> personDtoWithTodo.getAssignedTodo().size() != 0)
                .collect(Collectors.toList());
    }

    @Override
    public List<PersonDto> findAllPeopleWithNoTodos() {
        List<Todo> todoList = todoRepository.findAll().stream()
                .collect(Collectors.toList());
        List<PersonDtoWithTodo> personDtoWithTodoList = personRepository.findAll().stream()
                .map(person -> personDtoConversionService.convertToPersonDtoWithTodo(person,todoList))
                .collect(Collectors.toList());

        return personDtoWithTodoList.stream()
                .filter(personDtoWithTodo -> personDtoWithTodo.getAssignedTodo().size() == 0)
                .map(personDtoWithTodo -> personDtoConversionService.convertToPersonDto(personRepository.findById(personDtoWithTodo.getPersonId()).get()))
                .collect(Collectors.toList());
    }

    @Override
    public List<PersonDto> findPeopleByAddress(Address address) {
        if (address != null){
            return personRepository.findAll().stream()
                    .filter(person -> person.getAddress() != null &&
                            person.getAddress().equals(address))
                    .map(person -> personDtoConversionService.convertToPersonDto(person))
                    .collect(Collectors.toList());
        }else{
            return personRepository.findAll().stream()
                    .filter(person -> person.getAddress() == null)
                    .map(person -> personDtoConversionService.convertToPersonDto(person))
                    .collect(Collectors.toList());
        }

    }

    @Override
    public List<PersonDto> findPeopleByCity(String city) {
        return personRepository.findAll().stream()
                .filter(person -> person.getAddress() != null
                        && person.getAddress().getCity().equalsIgnoreCase(city))
                .map(person -> personDtoConversionService.convertToPersonDto(person))
                .collect(Collectors.toList());
    }

    @Override
    public List<PersonDto> findByFullName(String fullName) {
        return personRepository.findAll().stream()
                .filter(person -> (person.getFirstName()+" "+person.getLastName()).equalsIgnoreCase(fullName))
                .map(person -> personDtoConversionService.convertToPersonDto(person))
                .collect(Collectors.toList());
    }

    @Override
    public List<PersonDto> findByLastName(String lastName) {
        return personRepository.findAll().stream()
                .filter(person -> person.getLastName().equalsIgnoreCase(lastName))
                .map(person -> personDtoConversionService.convertToPersonDto(person))
                .collect(Collectors.toList());
    }

    @Override
    public boolean deletePerson(int personId) throws IllegalArgumentException {
        return personRepository.delete(findById(personId).getPersonId());
    }
}
