package org.eclipse.cargotracker.interfaces.booking.web;

import org.junit.Test;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.io.Serializable;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ChangeArrivalDeadlineDateDialogTest {

    @Test
    public void launcherUsesTheRequiredManagedBeanContract() {
        ManagedBean managedBean = ChangeArrivalDeadlineDateDialog.class
                .getAnnotation(ManagedBean.class);

        assertEquals("changeArrivalDeadlineDateDialog", managedBean.name());
        assertTrue(ChangeArrivalDeadlineDateDialog.class.isAnnotationPresent(SessionScoped.class));
        assertTrue(Serializable.class.isAssignableFrom(ChangeArrivalDeadlineDateDialog.class));
    }

    @Test
    public void returnListenerAcceptsDialogReturnEvents() {
        new ChangeArrivalDeadlineDateDialog().handleReturn(null);
    }
}
