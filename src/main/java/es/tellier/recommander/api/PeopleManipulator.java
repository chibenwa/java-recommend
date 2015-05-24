package es.tellier.recommander.api;

import es.tellier.recommander.api.beans.People;
import es.tellier.recommander.api.exceptions.PeopleAlreadyExists;
import es.tellier.recommander.api.exceptions.PeopleAlreadyFriend;
import es.tellier.recommander.api.exceptions.PeopleNotFound;
import es.tellier.recommander.api.exceptions.PeopleNotFriend;

import java.util.List;

public interface PeopleManipulator {

    /*
     * Basic operation on Peoples
     */
    People createPeople(String name) throws PeopleAlreadyExists;

    People getPeople(String name) throws PeopleNotFound;

    People getOrCreatePeople(String name);

    boolean peopleExists(String name);

    void deletePeople(String name) throws PeopleNotFound;

    /*
     * Friendships operations
     */
    void removeFriendship(People friend1, People friend2) throws PeopleNotFriend;

    void addFriendship(People friend1, People friend2) throws PeopleAlreadyFriend;

}
