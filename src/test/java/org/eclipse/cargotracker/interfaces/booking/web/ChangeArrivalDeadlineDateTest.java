package org.eclipse.cargotracker.interfaces.booking.web;

import org.eclipse.cargotracker.interfaces.booking.facade.BookingServiceFacade;
import org.eclipse.cargotracker.interfaces.booking.facade.dto.CargoRoute;
import org.eclipse.cargotracker.interfaces.booking.facade.dto.Location;
import org.eclipse.cargotracker.interfaces.booking.facade.dto.RouteCandidate;
import org.junit.Test;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;

public class ChangeArrivalDeadlineDateTest {

    private static final String TRACKING_ID = "ABC123";

    @Test
    public void loadUsesTrackingIdAndConvertsTheDtoDeadline() throws Exception {
        BookingServiceFacadeFake facade = new BookingServiceFacadeFake(
                cargoWithDeadline(new SimpleDateFormat("MM/dd/yyyy").parse("03/15/2014")));
        ChangeArrivalDeadlineDate controller = controllerFor(facade);
        controller.setTrackingId(TRACKING_ID);

        controller.load();

        assertEquals(TRACKING_ID, facade.loadedTrackingId);
        assertSame(facade.cargo, controller.getCargo());
        assertEquals(new SimpleDateFormat("MM/dd/yyyy").parse("03/15/2014"),
                controller.getArrivalDeadlineDate());
    }

    @Test
    public void loadSurfacesMalformedDtoData() throws Exception {
        BookingServiceFacadeFake facade = new BookingServiceFacadeFake(new MalformedCargoRoute());
        ChangeArrivalDeadlineDate controller = controllerFor(facade);
        controller.setTrackingId(TRACKING_ID);

        try {
            controller.load();
            fail("Expected malformed source data to be surfaced");
        } catch (RuntimeException expected) {
            assertNotNull(expected.getCause());
        }

        assertNull(controller.getArrivalDeadlineDate());
    }

    @Test
    public void changeArrivalDeadlineDelegatesSelectedDate() throws Exception {
        BookingServiceFacadeFake facade = new BookingServiceFacadeFake(
                cargoWithDeadline(new Date()));
        ChangeArrivalDeadlineDate controller = controllerFor(facade);
        controller.setTrackingId(TRACKING_ID);
        Date selected = new SimpleDateFormat("MM/dd/yyyy").parse("12/24/2014");
        controller.setArrivalDeadlineDate(selected);

        try {
            controller.changeArrivalDeadline();
        } catch (RuntimeException outsideFaces) {
            // Closing the dynamic dialog needs a Faces context, which is
            // unavailable in a container-free test. The facade delegation
            // asserted below has already happened at this point.
        }

        assertEquals(1, facade.changeDeadlineCalls);
        assertEquals(TRACKING_ID, facade.changedTrackingId);
        assertSame(selected, facade.changedDeadline);
    }

    @Test
    public void changeArrivalDeadlineRejectsNullSelection() throws Exception {
        BookingServiceFacadeFake facade = new BookingServiceFacadeFake(
                cargoWithDeadline(new Date()));
        ChangeArrivalDeadlineDate controller = controllerFor(facade);
        controller.setTrackingId(TRACKING_ID);

        try {
            controller.changeArrivalDeadline();
            fail("Expected a null deadline to be rejected");
        } catch (IllegalStateException expected) {
            // Expected.
        }

        assertEquals(0, facade.changeDeadlineCalls);
    }

    @Test
    public void facadeFailurePropagates() throws Exception {
        BookingServiceFacadeFake facade = new BookingServiceFacadeFake(
                cargoWithDeadline(new Date()));
        facade.changeDeadlineFailure = new IllegalArgumentException("Rejected by the facade");
        ChangeArrivalDeadlineDate controller = controllerFor(facade);
        controller.setTrackingId(TRACKING_ID);
        controller.setArrivalDeadlineDate(new Date());

        try {
            controller.changeArrivalDeadline();
            fail("Expected the facade failure to propagate");
        } catch (IllegalArgumentException expected) {
            assertEquals("Rejected by the facade", expected.getMessage());
        }

        assertEquals(1, facade.changeDeadlineCalls);
    }

    private ChangeArrivalDeadlineDate controllerFor(BookingServiceFacade facade) throws Exception {
        ChangeArrivalDeadlineDate controller = new ChangeArrivalDeadlineDate();
        Field field = ChangeArrivalDeadlineDate.class.getDeclaredField("bookingServiceFacade");
        field.setAccessible(true);
        field.set(controller, facade);
        return controller;
    }

    private CargoRoute cargoWithDeadline(Date arrivalDeadline) {
        return new CargoRoute(TRACKING_ID, "USNYC", "JNTKO", arrivalDeadline, false, false,
                "USNYC", "IN_PORT");
    }

    private static class MalformedCargoRoute extends CargoRoute {
        private static final long serialVersionUID = 1L;

        MalformedCargoRoute() {
            super(TRACKING_ID, "USNYC", "JNTKO", new Date(), false, false, "USNYC", "IN_PORT");
        }

        @Override
        public String getArrivalDeadlineDate() {
            return "not-a-date";
        }
    }

    private static class BookingServiceFacadeFake implements BookingServiceFacade {
        private final CargoRoute cargo;
        private String loadedTrackingId;
        private int changeDeadlineCalls;
        private String changedTrackingId;
        private Date changedDeadline;
        private RuntimeException changeDeadlineFailure;

        BookingServiceFacadeFake(CargoRoute cargo) {
            this.cargo = cargo;
        }

        @Override
        public CargoRoute loadCargoForRouting(String trackingId) {
            this.loadedTrackingId = trackingId;
            return cargo;
        }

        @Override
        public void changeDeadline(String trackingId, Date arrivalDeadline) {
            changeDeadlineCalls++;
            this.changedTrackingId = trackingId;
            this.changedDeadline = arrivalDeadline;

            if (changeDeadlineFailure != null) {
                throw changeDeadlineFailure;
            }
        }

        @Override
        public String bookNewCargo(String origin, String destination, Date arrivalDeadline) {
            throw new AssertionError("Unexpected bookNewCargo call");
        }

        @Override
        public void assignCargoToRoute(String trackingId, RouteCandidate route) {
            throw new AssertionError("Unexpected assignCargoToRoute call");
        }

        @Override
        public void changeDestination(String trackingId, String destinationUnLocode) {
            throw new AssertionError("Unexpected changeDestination call");
        }

        @Override
        public List<RouteCandidate> requestPossibleRoutesForCargo(String trackingId) {
            throw new AssertionError("Unexpected requestPossibleRoutesForCargo call");
        }

        @Override
        public List<Location> listShippingLocations() {
            throw new AssertionError("Unexpected listShippingLocations call");
        }

        @Override
        public List<CargoRoute> listAllCargos() {
            throw new AssertionError("Unexpected listAllCargos call");
        }
    }
}
