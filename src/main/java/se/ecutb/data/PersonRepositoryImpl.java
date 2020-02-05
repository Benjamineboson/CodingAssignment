package se.ecutb.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.ecutb.model.Address;
import se.ecutb.model.Person;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Component
public class PersonRepositoryImpl implements PersonRepository {


    private List<Person> personList = new ArrayList<>();

    @Override
    public Optional<Person> findById(int personId) {
        return personList.stream()
                .filter(person -> person.getPersonId() == personId)
                .findFirst();
    }

    @Override
    public Person persist(Person person) throws IllegalArgumentException {
        String newEmail = person.getEmail();
        for (Person person1:personList) {
            if (person1.getEmail().equalsIgnoreCase(newEmail)){
                throw new IllegalArgumentException("Person with that email already exists");
            }
        }
        personList.add(person);
        return person;
    }

    @Override
    public Optional<Person> findByEmail(String email) {
        return personList.stream()
                .filter(person -> person.getEmail().equalsIgnoreCase(email))
                .findFirst();
    }

    @Override
    public List<Person> findByAddress(Address address) {
        if (address == null){
            return personList.stream()
                    .filter(person -> person.getAddress() == null)
                    .collect(Collectors.toList());
        }else{
            return personList.stream()
                    .filter(person -> person.getAddress() != null && person.getAddress().equals(address))
                    .collect(Collectors.toList());
        }
    }

    @Override
    public List<Person> findByCity(String city) {
        List<Person> newList = new ArrayList<>();
        for (Person person:personList) {
            if (person.getAddress() != null && person.getAddress().getCity().equalsIgnoreCase(city)){
                newList.add(person);
            }
        }
            return newList;
    }

    @Override
    public List<Person> findByLastName(String lastName) {
        return personList.stream()
                .filter(person -> person.getLastName().equalsIgnoreCase(lastName))
                .collect(Collectors.toList());
    }

    @Override
    public List<Person> findByFullName(String fullName) {
        return personList.stream()
                .filter(person -> (person.getFirstName()+" "+person.getLastName()).equalsIgnoreCase(fullName))
                .collect(Collectors.toList());
    }

    @Override
    public List<Person> findAll() {
        return personList;
    }

    @Override
    public boolean delete(int personId) throws IllegalArgumentException {
        Person temp = findById(personId).get();
        if (personList.contains(temp)){
            personList.remove(temp);
            return true;
        }else{
            return false;
        }
    }

    @Override
    public void clear() {
        personList.clear();
    }
}
