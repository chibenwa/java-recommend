package es.tellier.recommander.api.beans;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Bean that represents a People
 *
 * People can get friends.
 */
public class People {
    private String name;
    private List<String> friends;
    private Map<String, Double> friendshipRecommendationMap;

    public People(String name) {
        this.name = name;
        this.friends = new ArrayList<>();
        this.friendshipRecommendationMap = new HashMap<>();
    }

    public String getName() {
        return name;
    }

    public List<String> getFriends() {
        return friends;
    }

    public List<FriendshipRecommendation> getFriendshipRecommendations() {
        return friendshipRecommendationMap.entrySet()
            .stream()
            .map((entry) -> new FriendshipRecommendation(entry.getKey(), entry.getValue()))
            .sorted((s1, s2) -> -Double.compare(s1.getScore(), s2.getScore()))
            .collect(Collectors.toList());
    }

    public void setFriendshipRecommendations(List<FriendshipRecommendation> friendshipRecommendation) {
        this.friendshipRecommendationMap = friendshipRecommendation
            .stream()
            .collect(Collectors.toMap(FriendshipRecommendation::getPeople, FriendshipRecommendation::getScore));
    }

    public void replaceFriendshipRecommandation(FriendshipRecommendation friendshipRecommendation) {
        friendshipRecommendationMap.put(friendshipRecommendation.getPeople(), friendshipRecommendation.getScore());
    }

    public void addFriend(People newFriend) {
        friends.add(newFriend.getName());
    }

    public void removeFriend(People oldFriend) {
        friends.remove(oldFriend.getName());
    }

    @Override
    public boolean equals (Object object) {
        return object instanceof People
            && Objects.equals(((People) object).getName(), name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }

    @Override
    public String toString() {
        return "People " + name;
    }
}
