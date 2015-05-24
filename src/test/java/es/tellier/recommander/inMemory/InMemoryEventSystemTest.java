package es.tellier.recommander.inMemory;

import es.tellier.recommander.api.beans.People;
import es.tellier.recommander.api.event.Event;
import es.tellier.recommander.api.exceptions.PeopleManipulationException;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class InMemoryEventSystemTest {

    class MockEventReceiver implements EventReceiver {

        private List<Event> events;

        public MockEventReceiver() {
            events = new ArrayList<>();
        }

        @Override
        public void manageEvent(Event event) {
            events.add(event);
        }

        public List<Event> getEvents() {
            return events;
        }
    }

    private MockEventReceiver mockEventReceiver;
    private InMemoryEventEmmiter eventEmmiter;

    @Before
    public void setUp() {
        mockEventReceiver = new MockEventReceiver();
        eventEmmiter = new InMemoryEventEmmiter();
    }

    @Test
    public void registeredReceiverShouldGetTriggered() {
        People marc = new People("marc");
        eventEmmiter.registerReceiver(mockEventReceiver);
        eventEmmiter.emmitFriendshipChange(marc);
        List<Event> events = mockEventReceiver.getEvents();
        assertThat(events).hasSize(1);
        assertThat(events.get(0).getConcernedPeople()).isEqualTo(marc);
        assertThat(events.get(0).getType()).isEqualTo(Event.EventType.FriendshipChange);
    }

    @Test
    public void registeredReceiverShouldGetTriggeredUponFriendshipChange() throws PeopleManipulationException {
        People marc = new People("marc");
        People steph = new People("steph");
        eventEmmiter.registerReceiver(mockEventReceiver);
        InMemoryPeopleManipulator peopleManipulator = new InMemoryPeopleManipulator(eventEmmiter);
        peopleManipulator.addFriendship(marc, steph);
        List<Event> events = mockEventReceiver.getEvents();
        assertThat(events).hasSize(2);
        List<People> peoples = events.stream()
            .map(Event::getConcernedPeople)
            .collect(Collectors.toList());
        assertThat(peoples).containsOnly(marc, steph);
    }

    @Test
    public void notRegisteredReceiverShouldNotGetTriggered() {
        People marc = new People("marc");
        eventEmmiter.emmitFriendshipChange(marc);
        assertThat( mockEventReceiver.getEvents()).isEmpty();
    }

    @Test
    public void unregisteredReceiverShouldGetTriggered() {
        People marc = new People("marc");
        eventEmmiter.registerReceiver(mockEventReceiver);
        eventEmmiter.unregisterReceiver(mockEventReceiver);
        eventEmmiter.emmitFriendshipChange(marc);
        assertThat(mockEventReceiver.getEvents()).isEmpty();
    }

}
