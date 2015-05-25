package es.tellier.recommander.inMemory;

import es.tellier.recommander.api.FriendshipEnsembleCalculator;
import es.tellier.recommander.api.PeopleManipulator;
import es.tellier.recommander.api.beans.FriendshipRecommendation;
import es.tellier.recommander.api.exceptions.PeopleManipulationException;
import es.tellier.recommander.recommander.Recommender;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class InMemoryFriendshipIntegrationTests {

    private PeopleManipulator peopleManipulator;

    @Before
    public void setUp() {
        InMemoryEventEmmiter eventEmmiter = new InMemoryEventEmmiter();
        InMemoryPeopleManipulator inMemoryPeopleManipulator = new InMemoryPeopleManipulator(eventEmmiter);
        peopleManipulator = inMemoryPeopleManipulator;
        FriendshipEnsembleCalculator friendshipEnsembleCalculator = new InMemoryFriendshipEnsembleCalculator();
        Recommender recommender = new InMemoryFriendshipBasedIntersectionRecommender(peopleManipulator, friendshipEnsembleCalculator);
        InMemoryRecommendationUpdaterEventListener recommendationUpdaterEventListener = new InMemoryRecommendationUpdaterEventListener(recommender);
        inMemoryPeopleManipulator.register(recommendationUpdaterEventListener);
    }

    @Test
    public void noRecommendations() throws PeopleManipulationException {
        peopleManipulator.createPeople("bob");
        peopleManipulator.createPeople("annie");
        peopleManipulator.createPeople("boby");
        peopleManipulator.addFriendship(peopleManipulator.getPeople("bob"), peopleManipulator.getPeople("annie"));
        assertThat(peopleManipulator.getPeople("bob").getFriendshipRecommendations()).isEmpty();
        assertThat(peopleManipulator.getPeople("boby").getFriendshipRecommendations()).isEmpty();
        assertThat(peopleManipulator.getPeople("annie").getFriendshipRecommendations()).isEmpty();
    }

    @Test
    public void basicScenario() throws PeopleManipulationException {
        peopleManipulator.createPeople("bob");
        peopleManipulator.createPeople("annie");
        peopleManipulator.createPeople("boby");
        peopleManipulator.addFriendship(peopleManipulator.getPeople("bob"), peopleManipulator.getPeople("annie"));
        peopleManipulator.addFriendship(peopleManipulator.getPeople("boby"), peopleManipulator.getPeople("annie"));
        assertThat(peopleManipulator.getPeople("bob").getFriendshipRecommendations()).containsExactly(new FriendshipRecommendation("boby", 1.0));
        assertThat(peopleManipulator.getPeople("boby").getFriendshipRecommendations()).containsExactly(new FriendshipRecommendation("bob", 1.0));
        assertThat(peopleManipulator.getPeople("annie").getFriendshipRecommendations()).isEmpty();
    }

    @Test
    public void test() throws PeopleManipulationException {
        peopleManipulator.createPeople("Anaïs");
        peopleManipulator.createPeople("Benoit");
        peopleManipulator.createPeople("Claire");
        peopleManipulator.createPeople("Delphine");
        peopleManipulator.createPeople("Edward");
        peopleManipulator.addFriendship(peopleManipulator.getPeople("Anaïs"), peopleManipulator.getPeople("Benoit"));
        peopleManipulator.addFriendship(peopleManipulator.getPeople("Anaïs"), peopleManipulator.getPeople("Claire"));
        peopleManipulator.addFriendship(peopleManipulator.getPeople("Anaïs"), peopleManipulator.getPeople("Delphine"));
        peopleManipulator.addFriendship(peopleManipulator.getPeople("Claire"), peopleManipulator.getPeople("Benoit"));
        peopleManipulator.addFriendship(peopleManipulator.getPeople("Delphine"), peopleManipulator.getPeople("Benoit"));
        peopleManipulator.addFriendship(peopleManipulator.getPeople("Edward"), peopleManipulator.getPeople("Benoit"));
        peopleManipulator.addFriendship(peopleManipulator.getPeople("Edward"), peopleManipulator.getPeople("Delphine"));
        assertThat(peopleManipulator.getPeople("Edward").getFriendshipRecommendations()).containsExactly(new FriendshipRecommendation("Anaïs", 0.6666666666666666), new FriendshipRecommendation("Claire", 0.3333333333333333));
        assertThat(peopleManipulator.getPeople("Claire").getFriendshipRecommendations()).containsExactly(new FriendshipRecommendation("Delphine", 0.6666666666666666), new FriendshipRecommendation("Edward", 0.3333333333333333));
        assertThat(peopleManipulator.getPeople("Benoit").getFriendshipRecommendations()).isEmpty();
        assertThat(peopleManipulator.getPeople("Anaïs").getFriendshipRecommendations()).containsExactly(new FriendshipRecommendation("Edward", 0.6666666666666666));
        assertThat(peopleManipulator.getPeople("Delphine").getFriendshipRecommendations()).containsExactly(new FriendshipRecommendation("Claire", 0.6666666666666666));
    }
}
