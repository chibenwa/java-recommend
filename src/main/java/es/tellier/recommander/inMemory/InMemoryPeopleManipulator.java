package es.tellier.recommander.inMemory;

import com.google.common.base.Preconditions;
import com.google.common.base.Throwables;
import es.tellier.recommander.api.PeopleManipulator;
import es.tellier.recommander.api.beans.People;
import es.tellier.recommander.api.exceptions.PeopleAlreadyExists;
import es.tellier.recommander.api.exceptions.PeopleAlreadyFriend;
import es.tellier.recommander.api.exceptions.PeopleNotFound;
import es.tellier.recommander.api.exceptions.PeopleNotFriend;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InMemoryPeopleManipulator implements PeopleManipulator{

    private Map<String, People> peopleMap;
    private InMemoryEventEmmiter eventEmmiter;

    public InMemoryPeopleManipulator(InMemoryEventEmmiter eventEmmiter) {
        peopleMap = new HashMap<>();
        this.eventEmmiter = eventEmmiter;
    }

    @Override
    public People createPeople(String name) throws PeopleAlreadyExists{
        People previousPeople = peopleMap.get(name);
        if (previousPeople != null) {
            throw new PeopleAlreadyExists(previousPeople);
        }
        People people = new People(name);
        peopleMap.put(name, people);
        return people;
    }

    @Override
    public People getPeople(String name) throws PeopleNotFound {
        return Optional.ofNullable(peopleMap.get(name))
            .orElseThrow(()->new PeopleNotFound(name));
    }

    @Override
    public People getOrCreatePeople(String name) {
        People previousPeople = peopleMap.get(name);
        if (previousPeople != null) {
            return previousPeople;
        }
        People people = new People(name);
        peopleMap.put(name, people);
        return people;
    }

    @Override
    public void deletePeople(String name) throws PeopleNotFound {
        People previousPeople = peopleMap.get(name);
        if (previousPeople == null) {
            throw new PeopleNotFound(name);
        }
        previousPeople.getFriends().stream().map((peopleName) -> {
            try {
                return getPeople(peopleName);
            } catch (PeopleNotFound peopleNotFound) {
                throw Throwables.propagate(peopleNotFound);
            }
        }).forEach((people) -> {
            try {
                removeFriendship(previousPeople, people);
            } catch (PeopleNotFriend peopleNotFriend) {
                throw Throwables.propagate(peopleNotFriend);
            }
        });
        peopleMap.remove(name);
    }

    @Override
    public void removeFriendship(People friend1, People friend2) throws PeopleNotFriend {
        Preconditions.checkArgument(!friend1.equals(friend2));
        if (!arePeopleFriends(friend1, friend2)) {
            throw new PeopleNotFriend(friend1, friend2);
        }
        friend1.removeFriend(friend2);
        friend2.removeFriend(friend1);
        eventEmmiter.emmitFriendshipChange(friend1);
        eventEmmiter.emmitFriendshipChange(friend2);
    }

    @Override
    public void addFriendship(People friend1, People friend2) throws PeopleAlreadyFriend {
        Preconditions.checkArgument(!friend1.equals(friend2));
        if (arePeopleFriends(friend1, friend2)) {
            throw new PeopleAlreadyFriend(friend1, friend2);
        }
        friend1.addFriend(friend2);
        friend2.addFriend(friend1);
        eventEmmiter.emmitFriendshipChange(friend1);
        eventEmmiter.emmitFriendshipChange(friend2);
    }

    @Override
    public boolean peopleExists(String name) {
        return peopleMap.get(name) != null;
    }

    private boolean arePeopleFriends(People friend1, People friend2) {
        Preconditions.checkArgument(!friend1.equals(friend2));
        return friend1.getFriends().contains(friend2.getName()) || friend2.getFriends().contains(friend1.getName());
    }

}
