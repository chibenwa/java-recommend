package es.tellier.recommander.inMemory;

import es.tellier.recommander.api.EventEmmiter;
import es.tellier.recommander.api.beans.People;
import es.tellier.recommander.api.event.Event;

import java.util.ArrayList;
import java.util.List;

public class InMemoryEventEmmiter implements EventEmmiter {

    private List<EventReceiver> receivers;

    public InMemoryEventEmmiter() {
        receivers = new ArrayList<>();
    }

    public void registerReceiver(EventReceiver receiver) {
        receivers.add(receiver);
    }

    public void unregisterReceiver(EventReceiver receiver) {
        receivers.remove(receiver);
    }

    @Override
    public void emmitFriendshipChange(People people) {
        Event event = new Event(people, Event.EventType.FriendshipChange);
        emmitEvent(event);
    }

    private void emmitEvent(Event event) {
        receivers.forEach((receiver) -> receiver.manageEvent(event));
    }

}
