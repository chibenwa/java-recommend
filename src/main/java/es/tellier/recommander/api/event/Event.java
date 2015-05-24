package es.tellier.recommander.api.event;

import es.tellier.recommander.api.beans.People;

public class Event {

    public enum EventType {
        FriendshipChange,
    }

    private People concernedPeople;
    private EventType type;

    public Event(People concernedPeople, EventType type) {
        this.concernedPeople = concernedPeople;
        this.type = type;
    }

    public People getConcernedPeople() {
        return concernedPeople;
    }

    public EventType getType() {
        return type;
    }
}
