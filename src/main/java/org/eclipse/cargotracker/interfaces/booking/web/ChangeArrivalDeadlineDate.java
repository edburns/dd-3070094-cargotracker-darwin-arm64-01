package org.eclipse.cargotracker.interfaces.booking.web;

import org.eclipse.cargotracker.interfaces.booking.facade.BookingServiceFacade;
import org.eclipse.cargotracker.interfaces.booking.facade.dto.CargoRoute;
import org.primefaces.PrimeFaces;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Handles changing the cargo arrival deadline. Like the destination editor,
 * this bean operates strictly against the booking service facade and its DTOs,
 * so the domain model stays shielded from user interface considerations.
 */
@Named
@ViewScoped
public class ChangeArrivalDeadlineDate implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final String FORMAT = "MM/dd/yyyy";

    private String trackingId;
    private CargoRoute cargo;
    private Date arrivalDeadlineDate;
    @Inject
    private BookingServiceFacade bookingServiceFacade;

    public String getTrackingId() {
        return trackingId;
    }

    public void setTrackingId(String trackingId) {
        this.trackingId = trackingId;
    }

    public CargoRoute getCargo() {
        return cargo;
    }

    public Date getArrivalDeadlineDate() {
        return arrivalDeadlineDate;
    }

    public void setArrivalDeadlineDate(Date arrivalDeadlineDate) {
        this.arrivalDeadlineDate = arrivalDeadlineDate;
    }

    public void load() {
        cargo = bookingServiceFacade.loadCargoForRouting(trackingId);

        try {
            arrivalDeadlineDate = parseArrivalDeadline(cargo.getArrivalDeadlineDate());
        } catch (ParseException e) {
            throw new RuntimeException("Error parsing date", e);
        }
    }

    private Date parseArrivalDeadline(String value) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat(FORMAT);
        formatter.setLenient(false);
        ParsePosition position = new ParsePosition(0);
        Date parsed = formatter.parse(value, position);
        if (parsed == null || position.getIndex() != value.length()) {
            int errorOffset = position.getErrorIndex() >= 0
                    ? position.getErrorIndex() : position.getIndex();
            throw new ParseException("Unparseable date: \"" + value + "\"", errorOffset);
        }
        return parsed;
    }

    public void changeArrivalDeadline() {
        if (arrivalDeadlineDate == null) {
            throw new IllegalStateException("An arrival deadline date is required.");
        }

        bookingServiceFacade.changeDeadline(trackingId, arrivalDeadlineDate);
        PrimeFaces.current().dialog().closeDynamic("DONE");
    }
}
