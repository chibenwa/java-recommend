package es.tellier.recommander.api;


import es.tellier.recommander.api.beans.People;

public interface EventEmmiter {

    void emmitFriendshipChange(People people);

}
