package se.ecutb.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.ecutb.data.IdSequencers;
import se.ecutb.data.PersonRepository;
import se.ecutb.model.AbstractPersonFactory;
import se.ecutb.model.Address;
import se.ecutb.model.Person;

import java.util.List;

@Component
public class CreatePersonServiceImpl extends AbstractPersonFactory implements CreatePersonService{

    private PersonRepository personRepository;
    private IdSequencers idSequencers;

    @Autowired
    public CreatePersonServiceImpl(PersonRepository personRepository, IdSequencers idSequencers) {
        this.personRepository = personRepository;
        this.idSequencers = idSequencers;
    }

    @Override
    public Person create(String firstName, String lastName, String email) throws IllegalArgumentException {
        List<Person> personList = personRepository.findAll();
        for (Person person:personList) {
            if (person.getEmail().equalsIgnoreCase(email)){
                throw new IllegalArgumentException("user with that email already exists");
            }
        }
        return createNewPerson(idSequencers.nextPersonId(),firstName,lastName,email,null);
    }

    @Override
    public Person create(String firstName, String lastName, String email, Address address) throws IllegalArgumentException {
        List<Person> personList = personRepository.findAll();
        for (Person person:personList) {
            if (person.getEmail().equalsIgnoreCase(email)){
                throw new IllegalArgumentException("user with that email already exists");
            }
        }
        return createNewPerson(idSequencers.nextPersonId(),firstName,lastName,email,address);
    }
}
