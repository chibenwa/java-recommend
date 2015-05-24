package es.tellier.recommander.inMemory;

import es.tellier.recommander.api.PeopleManipulator;
import es.tellier.recommander.api.beans.People;
import es.tellier.recommander.api.exceptions.PeopleManipulationException;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class InMemoryFriendshipIntersectionCalculatorTest {

    private PeopleManipulator peopleManipulator;
    private People bob;
    private People annie;
    private People mia;
    private People marchal;
    private InMemoryFriendshipEnsembleCalculator friendshipIntersectionCalculator;

    @Before
    public void setUp() throws PeopleManipulationException {
        peopleManipulator = new InMemoryPeopleManipulator(new InMemoryEventEmmiter());
        bob = peopleManipulator.createPeople("bob");
        annie = peopleManipulator.createPeople("annie");
        mia = peopleManipulator.createPeople("mia");
        marchal = peopleManipulator.createPeople("marchal");
        friendshipIntersectionCalculator = new InMemoryFriendshipEnsembleCalculator();
    }

    @Test
    public void emptyFriendshipIntersection() {
        assertThat(friendshipIntersectionCalculator.intersectFriends(mia, bob)).isEmpty();
    }

    @Test
    public void noCommonFriendsIntersection() throws PeopleManipulationException {
        peopleManipulator.addFriendship(bob, mia);
        peopleManipulator.addFriendship(bob, annie);
        peopleManipulator.addFriendship(mia, marchal);
        assertThat(friendshipIntersectionCalculator.intersectFriends(mia, bob)).isEmpty();
    }

    @Test
    public void commonFriendsIntersection() throws PeopleManipulationException {
        peopleManipulator.addFriendship(bob, mia);
        peopleManipulator.addFriendship(bob, annie);
        peopleManipulator.addFriendship(bob, marchal);
        peopleManipulator.addFriendship(mia, marchal);
        assertThat(friendshipIntersectionCalculator.intersectFriends(mia, bob)).containsOnly(marchal.getName());
    }

    @Test
    public void emptyFriendshipUnion() {
        assertThat(friendshipIntersectionCalculator.unionFriends(mia, bob)).isEmpty();
    }

    @Test
    public void noCommonFriendsUnion() throws PeopleManipulationException {
        peopleManipulator.addFriendship(bob, mia);
        peopleManipulator.addFriendship(bob, annie);
        peopleManipulator.addFriendship(mia, marchal);
        assertThat(friendshipIntersectionCalculator.unionFriends(mia, bob)).containsOnly(marchal.getName(), annie.getName());
    }

    @Test
    public void commonFriendsUnion() throws PeopleManipulationException {
        peopleManipulator.addFriendship(bob, mia);
        peopleManipulator.addFriendship(bob, annie);
        peopleManipulator.addFriendship(bob, marchal);
        peopleManipulator.addFriendship(mia, marchal);
        assertThat(friendshipIntersectionCalculator.unionFriends(mia, bob)).containsOnly(marchal.getName(), annie.getName());
    }


}
