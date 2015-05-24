package es.tellier.recommander.inMemory;

import es.tellier.recommander.api.FriendshipEnsembleCalculator;
import es.tellier.recommander.api.beans.People;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class InMemoryFriendshipEnsembleCalculator implements FriendshipEnsembleCalculator {

    @Override
    public List<String> intersectFriends(People people1, People people2) {
        List<String> people1FriendsName = people1.getFriends();
        return people2.getFriends()
            .stream()
            .filter(people1FriendsName::contains)
            .collect(Collectors.toList());
    }

    @Override
    public List<String> unionFriends(People people1, People people2) {
        List<String> people1FriendsName = new ArrayList<>(people1.getFriends());
        if (people1FriendsName.contains(people2.getName())) {
            people1FriendsName.remove(people2.getName());
        }
        people2.getFriends()
            .forEach((peopleName -> {
                if (!people1.getFriends().contains(peopleName) && ! peopleName.equals(people1.getName()) ) {
                    people1FriendsName.add(peopleName);
                }
            }));
        return people1FriendsName;
    }

}
