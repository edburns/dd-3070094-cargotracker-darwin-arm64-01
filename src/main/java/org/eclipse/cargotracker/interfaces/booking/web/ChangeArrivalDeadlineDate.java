package org.eclipse.cargotracker.interfaces.booking.web;

import org.eclipse.cargotracker.interfaces.booking.facade.BookingServiceFacade;
import org.eclipse.cargotracker.interfaces.booking.facade.dto.CargoRoute;
import org.primefaces.PrimeFaces;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.text.ParseException;
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
            arrivalDeadlineDate = new SimpleDateFormat(FORMAT)
                    .parse(cargo.getArrivalDeadlineDate());
        } catch (ParseException e) {
            throw new RuntimeException("Error parsing date", e);
        }
    }

    public void changeArrivalDeadline() {
        if (arrivalDeadlineDate == null) {
            throw new IllegalStateException("An arrival deadline date is required.");
        }

        bookingServiceFacade.changeDeadline(trackingId, arrivalDeadlineDate);
        PrimeFaces.current().dialog().closeDynamic("DONE");
    }
}
