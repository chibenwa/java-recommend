package es.tellier.recommander.inMemory;

import es.tellier.recommander.api.PeopleManipulator;
import es.tellier.recommander.api.PeopleManipulatorProvider;

public class InMemoryPeopleManipulatorProvider implements PeopleManipulatorProvider {

    @Override
    public PeopleManipulator provide() {
        return new InMemoryPeopleManipulator(new InMemoryEventEmmiter());
    }
}
