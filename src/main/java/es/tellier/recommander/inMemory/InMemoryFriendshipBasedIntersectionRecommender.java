package es.tellier.recommander.inMemory;

import com.google.common.base.Throwables;
import es.tellier.recommander.api.FriendshipEnsembleCalculator;
import es.tellier.recommander.api.PeopleManipulator;
import es.tellier.recommander.api.beans.People;
import es.tellier.recommander.api.exceptions.PeopleNotFound;
import es.tellier.recommander.recommander.Recommender;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class InMemoryFriendshipBasedIntersectionRecommender implements Recommender {

    private FriendshipEnsembleCalculator intersectionCalculator;
    private PeopleManipulator peopleManipulator;

    public InMemoryFriendshipBasedIntersectionRecommender(PeopleManipulator peopleManipulator, FriendshipEnsembleCalculator intersectionCalculator) {
        this.intersectionCalculator = intersectionCalculator;
        this.peopleManipulator = peopleManipulator;
    }

    public List<InternalFriendshipRecommendation> buildRecommandedPeople(People people) {
        Set<People> friendsTwoDegree = new HashSet<>();
        List<People> friendsOneDegree = retrieveFriendList(people);
        friendsOneDegree.stream()
            .forEach(
                (friendOneDegree) -> friendsTwoDegree.addAll(retrieveFriendList(friendOneDegree))
            );
        return friendsTwoDegree.stream()
            .filter(isPeopleEligibleToFriendshipRecommendation(people, friendsOneDegree))
            .collect(Collectors.toList())
            .stream()
            .map((recommendedPeople) -> new InternalFriendshipRecommendation(recommendedPeople, calculateRecommendationScore(people, recommendedPeople)))
            .sorted((s1, s2) -> -Double.compare(s1.getScore(), s2.getScore()))
            .collect(Collectors.toList());
    }

    private List<People> retrieveFriendList(People people) {
        return people.getFriends()
            .stream()
            .map(this::retrievePeopleFromName)
            .collect(Collectors.toList());
    }

    private People retrievePeopleFromName(String peopleName) {
        try {
            return peopleManipulator.getPeople(peopleName);
        } catch (PeopleNotFound peopleNotFound) {
            throw Throwables.propagate(peopleNotFound);
        }
    }

    private Predicate<People> isPeopleEligibleToFriendshipRecommendation(People people, List<People> friendsOneDegree) {
        return (friendTwoDegree) -> people != friendTwoDegree
            && !friendsOneDegree.contains(friendTwoDegree);
    }

    private double calculateRecommendationScore(People people, People scoreRecommendation) {
        return ((double) intersectionCalculator.intersectFriends(people, scoreRecommendation).size())
            / ((double) intersectionCalculator.unionFriends(people, scoreRecommendation).size());
    }

}
