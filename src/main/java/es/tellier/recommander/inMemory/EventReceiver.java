package es.tellier.recommander.inMemory;

import es.tellier.recommander.api.event.Event;

@FunctionalInterface
public interface EventReceiver {

    void manageEvent(Event event);

}
