package es.tellier.recommander.inMemory;

import es.tellier.recommander.api.beans.FriendshipRecommendation;
import es.tellier.recommander.api.beans.People;
import es.tellier.recommander.api.event.Event;
import es.tellier.recommander.recommander.Recommender;

import java.util.List;
import java.util.stream.Collectors;

public class InMemoryRecommendationUpdaterEventListener implements EventReceiver {

    private Recommender recommender;

    public InMemoryRecommendationUpdaterEventListener(Recommender recommender) {
        this.recommender = recommender;
    }

    @Override
    public void manageEvent(Event event) {
        if (event.getType() == Event.EventType.FriendshipChange) {
            People concernedPeople = event.getConcernedPeople();
            List<Recommender.InternalFriendshipRecommendation> internalFriendshipRecommendations = recommender.buildRecommandedPeople(concernedPeople);
            resetPeopleRecommendations(concernedPeople, internalFriendshipRecommendations);
            // Permitted because the calculation used for recommendation score is symetric
            // We need to do heavy calculations only for updated People.
            updateLinkedPeopleRecommendations(concernedPeople, internalFriendshipRecommendations);
        }
    }

    private void resetPeopleRecommendations(People concernedPeople, List<Recommender.InternalFriendshipRecommendation> internalFriendshipRecommendations) {
        concernedPeople.setFriendshipRecommendations(internalFriendshipRecommendations.stream()
            .map((iFriendshipRecommendation) -> new FriendshipRecommendation(iFriendshipRecommendation.getPeople().getName(), iFriendshipRecommendation.getScore()))
            .collect(Collectors.toList()));
    }

    private void updateLinkedPeopleRecommendations(People concernedPeople, List<Recommender.InternalFriendshipRecommendation> internalFriendshipRecommendations) {
        internalFriendshipRecommendations.forEach(
            (iFriendshipRecommendation -> iFriendshipRecommendation.getPeople()
                .replaceFriendshipRecommandation(new FriendshipRecommendation(concernedPeople.getName(), iFriendshipRecommendation.getScore()))));
    }

}
