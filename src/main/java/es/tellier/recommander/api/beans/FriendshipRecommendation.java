package es.tellier.recommander.api.beans;

import java.util.Objects;

public class FriendshipRecommendation {

    private String people;
    private double score;

    public FriendshipRecommendation(String people, double score) {
        this.people = people;
        this.score = score;
    }

    public String getPeople() {
        return people;
    }

    public double getScore() {
        return score;
    }

    @Override
    public int hashCode() {
        return Objects.hash(people, score);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof FriendshipRecommendation) {
            FriendshipRecommendation other = (FriendshipRecommendation) o;
            return Objects.equals(other.people, people)
                && Objects.equals(other.score, score);
        }
        return false;
    }

}
