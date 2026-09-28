package org.eclipse.cargotracker.interfaces.booking.facade.internal;

import org.eclipse.cargotracker.application.BookingService;
import org.eclipse.cargotracker.domain.model.cargo.Itinerary;
import org.eclipse.cargotracker.domain.model.cargo.TrackingId;
import org.eclipse.cargotracker.domain.model.location.UnLocode;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

public class DefaultBookingServiceFacadeTest {

    @Test
    public void changeDeadlineDelegatesOnceWithoutChangingDeadline() throws Exception {
        DefaultBookingServiceFacade facade = new DefaultBookingServiceFacade();
        BookingServiceSpy service = new BookingServiceSpy();
        Field field = DefaultBookingServiceFacade.class.getDeclaredField("bookingService");
        field.setAccessible(true);
        field.set(facade, service);

        Date deadline = new Date(123456789L);
        facade.changeDeadline("DEF789", deadline);

        assertEquals(1, service.calls);
        assertEquals(new TrackingId("DEF789"), service.trackingId);
        assertSame(deadline, service.deadline);
        assertEquals(123456789L, service.deadline.getTime());
    }

    private static class BookingServiceSpy implements BookingService {
        private int calls;
        private TrackingId trackingId;
        private Date deadline;

        @Override
        public void changeDeadline(TrackingId trackingId, Date deadline) {
            calls++;
            this.trackingId = trackingId;
            this.deadline = deadline;
        }

        @Override
        public TrackingId bookNewCargo(UnLocode origin, UnLocode destination, Date arrivalDeadline) {
            throw new AssertionError("Unexpected bookNewCargo call");
        }

        @Override
        public List<Itinerary> requestPossibleRoutesForCargo(TrackingId trackingId) {
            throw new AssertionError("Unexpected requestPossibleRoutesForCargo call");
        }

        @Override
        public void assignCargoToRoute(Itinerary itinerary, TrackingId trackingId) {
            throw new AssertionError("Unexpected assignCargoToRoute call");
        }

        @Override
        public void changeDestination(TrackingId trackingId, UnLocode unLocode) {
            throw new AssertionError("Unexpected changeDestination call");
        }
    }
}
