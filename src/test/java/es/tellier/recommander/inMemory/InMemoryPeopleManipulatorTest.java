package es.tellier.recommander.inMemory;

import es.tellier.recommander.api.PeopleManipulatorTest;

public class InMemoryPeopleManipulatorTest extends PeopleManipulatorTest {

    public InMemoryPeopleManipulatorTest() {
        super(new InMemoryPeopleManipulatorProvider());
    }

}
