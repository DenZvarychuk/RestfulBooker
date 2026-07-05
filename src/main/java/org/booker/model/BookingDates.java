package org.booker.model;

public record BookingDates(
        String checkin,
        String checkout
) {
    public String print() {
        return "\nCheckin: " + checkin + ",\nCheckout: " + checkout;
    }
}
