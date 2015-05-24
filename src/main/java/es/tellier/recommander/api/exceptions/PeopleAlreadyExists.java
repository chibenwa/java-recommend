package es.tellier.recommander.api.exceptions;

import es.tellier.recommander.api.beans.People;

public class PeopleAlreadyExists extends PeopleManipulationException {

    public PeopleAlreadyExists(People people) {
        super(constructMessage(people));
    }

    private static String constructMessage(People people) {
        return people.getName() + " already exists";
    }
}
