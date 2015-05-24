package es.tellier.recommander.api;

import static org.assertj.core.api.Assertions.assertThat;

import com.google.common.base.Throwables;
import es.tellier.recommander.api.beans.People;
import es.tellier.recommander.api.exceptions.PeopleAlreadyExists;
import es.tellier.recommander.api.exceptions.PeopleAlreadyFriend;
import es.tellier.recommander.api.exceptions.PeopleManipulationException;
import es.tellier.recommander.api.exceptions.PeopleNotFound;
import es.tellier.recommander.api.exceptions.PeopleNotFriend;
import org.junit.Before;
import org.junit.Test;

public abstract class PeopleManipulatorTest {

    public static final String MARTIN = "martin";
    public static final String SUZANNE = "suzanne";

    private  PeopleManipulatorProvider peopleManipulatorProvider;
    private PeopleManipulator peopleManipulator;

    public PeopleManipulatorTest(PeopleManipulatorProvider peopleManipulatorProvider) {
        this.peopleManipulatorProvider = peopleManipulatorProvider;
    }

    @Before
    public void setUp() {
        peopleManipulator = peopleManipulatorProvider.provide();
    }

    @Test
    public void createPeopleShouldSucceedWhenEmpty() throws PeopleManipulationException {
        peopleManipulator.createPeople(MARTIN);
        assertThat(peopleManipulator.getPeople(MARTIN)).isEqualTo(new People(MARTIN));
    }

    @Test(expected = PeopleAlreadyExists.class)
    public void createPeopleShouldFailtWhenAlreadyPresent() throws PeopleManipulationException {
        peopleManipulator.createPeople(MARTIN);
        peopleManipulator.createPeople(MARTIN);
    }

    @Test
    public void createPeopleShouldSucceedWhenCreatingDifferentPeople() throws PeopleManipulationException {
        peopleManipulator.createPeople(MARTIN);
        peopleManipulator.createPeople(SUZANNE);
        assertThat(peopleManipulator.getPeople(SUZANNE)).isEqualTo(new People(SUZANNE));
    }

    @Test(expected = PeopleNotFound.class)
    public void deletePeopleShouldFailWhenThereIsNoPeople() throws PeopleManipulationException {
        peopleManipulator.deletePeople(MARTIN);
    }

    @Test(expected = PeopleNotFound.class)
    public void getPeopleShouldFailWhenThereIsNoPeople() throws PeopleManipulationException {
        peopleManipulator.getPeople(MARTIN);
    }

    @Test(expected = PeopleNotFound.class)
    public void deletePeopleShouldDeletePresentPeople() throws PeopleManipulationException {
        try {
            peopleManipulator.createPeople(MARTIN);
            peopleManipulator.deletePeople(MARTIN);
        } catch (Exception exception) {
            Throwables.propagate(exception);
        }
        peopleManipulator.getPeople(MARTIN);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addFriendshipShouldFailIfAppliedOnTheSamePeople() throws PeopleManipulationException {
        People martin = peopleManipulator.createPeople(MARTIN);
        peopleManipulator.addFriendship(martin, martin);
    }

    @Test
    public void addFriendshipShouldLinkTwoDifferentPeople() throws PeopleManipulationException {
        People martin = peopleManipulator.createPeople(MARTIN);
        People suzanne = peopleManipulator.createPeople(SUZANNE);
        peopleManipulator.addFriendship(martin, suzanne);
        assertThat(suzanne.getFriends()).containsOnly(martin.getName());
        assertThat(martin.getFriends()).containsOnly(suzanne.getName());
    }

    @Test(expected = PeopleAlreadyFriend.class)
    public void addFriendshipShouldFailIfPeopleAreAlreadyFriends() throws PeopleManipulationException {
        People martin;
        People suzanne;
        try {
            martin = peopleManipulator.createPeople(MARTIN);
            suzanne = peopleManipulator.createPeople(SUZANNE);
            peopleManipulator.addFriendship(martin, suzanne);
        } catch (Exception exception) {
            throw Throwables.propagate(exception);
        }
        peopleManipulator.addFriendship(martin, suzanne);
    }

    @Test(expected = IllegalArgumentException.class)
    public void removeFriendshipShouldFailIfAppliedOnTheSamePeople() throws PeopleManipulationException {
        People martin = peopleManipulator.createPeople(MARTIN);
        peopleManipulator.removeFriendship(martin, martin);
    }

    @Test(expected = PeopleNotFriend.class)
    public void removeFriendshipShouldFailIfPeopleAreNotFriends() throws PeopleManipulationException {
        People martin;
        People suzanne;
        try {
            martin = peopleManipulator.createPeople(MARTIN);
            suzanne = peopleManipulator.createPeople(SUZANNE);
        } catch (Exception exception) {
            throw Throwables.propagate(exception);
        }
        peopleManipulator.removeFriendship(martin, suzanne);
    }

    @Test
    public void removeFriendshipShouldUndoFriendship() throws PeopleManipulationException {
        People martin = peopleManipulator.createPeople(MARTIN);
        People suzanne = peopleManipulator.createPeople(SUZANNE);
        peopleManipulator.addFriendship(martin, suzanne);
        peopleManipulator.removeFriendship(martin, suzanne);
        assertThat(martin.getFriends()).isEmpty();
        assertThat(suzanne.getFriends()).isEmpty();
    }

    @Test
    public void getOrCreatePeopleShouldCreateNePeopleIfNotPresent() throws PeopleManipulationException {
        peopleManipulator.getOrCreatePeople(MARTIN);
        assertThat(peopleManipulator.getPeople(MARTIN)).isEqualTo(new People(MARTIN));
    }

    @Test
    public void getOrCreatePeopleShouldRetrievePeopleIfPresent() throws PeopleManipulationException {
        peopleManipulator.getOrCreatePeople(MARTIN);
        peopleManipulator.getOrCreatePeople(MARTIN);
        assertThat(peopleManipulator.getPeople(MARTIN)).isEqualTo(new People(MARTIN));
    }

    @Test
    public void peopleExistsShouldReturnFalseWhenThereIsNoPeople() throws PeopleManipulationException {
        assertThat(peopleManipulator.peopleExists(MARTIN)).isFalse();
    }

    @Test
    public void peopleExistsShouldReturnTrueWhenThereIsPeople() throws PeopleManipulationException {
        peopleManipulator.createPeople(MARTIN);
        assertThat(peopleManipulator.peopleExists(MARTIN)).isTrue();
    }

}
