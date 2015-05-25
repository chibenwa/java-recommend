package es.tellier.recommander.recommander;

import es.tellier.recommander.api.beans.People;
import es.tellier.recommander.api.exceptions.PeopleAlreadyFriend;
import es.tellier.recommander.api.exceptions.PeopleManipulationException;
import es.tellier.recommander.inMemory.InMemoryEventEmmiter;
import es.tellier.recommander.inMemory.InMemoryFriendshipBasedIntersectionRecommender;
import es.tellier.recommander.inMemory.InMemoryFriendshipEnsembleCalculator;
import es.tellier.recommander.inMemory.InMemoryPeopleManipulator;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class InMemoryRecommenderTest {

    private InMemoryPeopleManipulator peopleManipulator;
    private InMemoryFriendshipBasedIntersectionRecommender recommander;
    private People me;
    private People f1;
    private People f2;
    private People f3;
    private People f4;
    private People f5;
    private People pB;
    private People pC;
    private People pD;
    private People pE;
    private Recommender.InternalFriendshipRecommendation rpB;
    private Recommender.InternalFriendshipRecommendation rpC;
    private Recommender.InternalFriendshipRecommendation rpD;
    private Recommender.InternalFriendshipRecommendation rpE;

    @Before
    public void setUp() throws PeopleManipulationException {
        peopleManipulator = new InMemoryPeopleManipulator(new InMemoryEventEmmiter());
        me = peopleManipulator.createPeople("me");
        f1 = peopleManipulator.createPeople("f1");
        f2 = peopleManipulator.createPeople("f2");
        f3 = peopleManipulator.createPeople("f3");
        f4 = peopleManipulator.createPeople("f4");
        f5 = peopleManipulator.createPeople("f5");
        pB = peopleManipulator.createPeople("pB");
        pC = peopleManipulator.createPeople("pC");
        pD = peopleManipulator.createPeople("pD");
        pE = peopleManipulator.createPeople("pE");
        rpB = new Recommender.InternalFriendshipRecommendation(pB, 0);
        rpC = new Recommender.InternalFriendshipRecommendation(pC, 0);
        rpD = new Recommender.InternalFriendshipRecommendation(pD, 0);
        rpE = new Recommender.InternalFriendshipRecommendation(pE, 0);
        recommander = new InMemoryFriendshipBasedIntersectionRecommender(peopleManipulator, new InMemoryFriendshipEnsembleCalculator());
    }

    @Test
    public void noFriendship() throws PeopleManipulationException {
        assertThat(recommander.buildRecommandedPeople(me)).isEmpty();
    }

    @Test
    public void friendshipLevel1() throws PeopleManipulationException {
        buildStep1();
        assertThat(recommander.buildRecommandedPeople(me)).containsExactly(rpC, rpB);
    }

    @Test
    public void friendshipLevel2() throws PeopleManipulationException {
        buildStep2();
        assertThat(recommander.buildRecommandedPeople(me)).containsExactly(rpC, rpB);
    }

    @Test
    public void friendshipLevel3() throws PeopleManipulationException {
        buildStep3();
        assertThat(recommander.buildRecommandedPeople(me)).containsExactly(rpC, rpB, rpD);
    }

    @Test
    public void friendshipLevel4() throws PeopleManipulationException {
        buildStep4();
        assertThat(recommander.buildRecommandedPeople(me)).containsExactly(rpE, rpC, rpB, rpD);
    }

    private void buildStep1() throws PeopleAlreadyFriend {
        peopleManipulator.addFriendship(me, f1);
        peopleManipulator.addFriendship(me, f2);
        peopleManipulator.addFriendship(f1, pC);
        peopleManipulator.addFriendship(f2, pC);
        peopleManipulator.addFriendship(f2, pB);
    }

    private void buildStep2() throws PeopleAlreadyFriend {
        buildStep1();
        peopleManipulator.addFriendship(me, f5);
        peopleManipulator.addFriendship(f5, pC);
    }

    private void buildStep3() throws PeopleAlreadyFriend {
        buildStep2();
        peopleManipulator.addFriendship(me, f3);
        peopleManipulator.addFriendship(pB, f3);
        peopleManipulator.addFriendship(pD, f3);
    }

    private void buildStep4() throws PeopleAlreadyFriend {
        buildStep3();
        peopleManipulator.addFriendship(pE, f3);
        peopleManipulator.addFriendship(pE, f4);
        peopleManipulator.addFriendship(pE, f5);
        peopleManipulator.addFriendship(pE, f1);
        peopleManipulator.addFriendship(me, f4);
    }

}
