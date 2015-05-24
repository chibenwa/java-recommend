package es.tellier.recommander.api;

import es.tellier.recommander.api.beans.People;

import java.util.List;

public interface FriendshipEnsembleCalculator {

    List<String> intersectFriends(People people1, People people2);

    List<String> unionFriends(People people1, People people2);

}
