package es.tellier.recommander.api.exceptions;

import es.tellier.recommander.api.beans.People;

public class PeopleNotFriend extends PeopleManipulationException {

    public PeopleNotFriend(People people1, People people2) {
        super(constructExceptionMessage(people1, people2));
    }

    private static String constructExceptionMessage(People people1, People people2) {
        return people1.getName() + " is not friend with " + people2;
    }

}
