package es.tellier.recommander.api;

import es.tellier.recommander.api.beans.FriendshipRecommendation;
import es.tellier.recommander.api.beans.People;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class PeopleTest {

    private People people;
    private FriendshipRecommendation tataRecommendation;
    private FriendshipRecommendation suzanneRecommendation;
    private FriendshipRecommendation morganneRecommendation;
    private FriendshipRecommendation noraRecommendation;
    private FriendshipRecommendation bobRecommendation;
    private FriendshipRecommendation noraNewRecommendation;

    @Before
    public void setUp() {
        people = new People("Bob");
        bobRecommendation = new FriendshipRecommendation("bob", 0.40);
        noraRecommendation = new FriendshipRecommendation("nora", 0.20);
        morganneRecommendation = new FriendshipRecommendation("morganne", 0.30);
        suzanneRecommendation = new FriendshipRecommendation("suzanne", 0.10);
        tataRecommendation = new FriendshipRecommendation("tata", 0.25);
        noraNewRecommendation = new FriendshipRecommendation("nora", 0.60);
    }

    @Test
    public void getEmptyRecommendations() {
        assertThat(people.getFriendshipRecommendations()).isEmpty();
    }

    @Test
    public void getRecommendationsShouldBeSorted() {
        people.setFriendshipRecommendations(Arrays.asList(tataRecommendation, suzanneRecommendation, morganneRecommendation, noraRecommendation, bobRecommendation));
        assertThat(people.getFriendshipRecommendations()).containsExactly(bobRecommendation, morganneRecommendation, tataRecommendation, noraRecommendation, suzanneRecommendation);
    }

    @Test
    public void updatedRecommendationsShouldBeSorted() {
        people.setFriendshipRecommendations(Arrays.asList(tataRecommendation, suzanneRecommendation, morganneRecommendation, noraRecommendation, bobRecommendation));
        people.replaceFriendshipRecommandation(noraNewRecommendation);
        assertThat(people.getFriendshipRecommendations()).containsExactly(noraNewRecommendation, bobRecommendation, morganneRecommendation, tataRecommendation, suzanneRecommendation);
    }

}
