package es.tellier.recommander.recommander;

import es.tellier.recommander.api.beans.People;

import java.util.List;
import java.util.Objects;

public interface Recommender {

    List<InternalFriendshipRecommendation> buildRecommandedPeople(People people);

    class InternalFriendshipRecommendation {

        private People people;
        private double score;

        public InternalFriendshipRecommendation(People people, double score) {
            this.people = people;
            this.score = score;
        }

        public People getPeople() {
            return people;
        }

        public double getScore() {
            return score;
        }

        @Override
        public int hashCode() {
            return Objects.hash(people.getName());
        }

        @Override
        public boolean equals(Object o) {
            return o instanceof InternalFriendshipRecommendation
                && Objects.equals(((InternalFriendshipRecommendation) o).getPeople().getName(), people.getName());
        }
    }

}
