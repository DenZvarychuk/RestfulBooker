package org.booker.model;

public record BookingResponse(
        String firstname,
        String lastname,
        Integer totalprice,
        Boolean depositpaid,
        BookingDates bookingdates,
        String additionalneeds
) {
    public void print() {
        System.out.println("Booking Response:");
        System.out.println("    Firstname: " + firstname);
        System.out.println("    Lastname: " + lastname);
        System.out.println("    Total Price: " + totalprice);
        System.out.println("    Deposit Paid: " + depositpaid);
        System.out.println("    Booking Dates: " + bookingdates.print());
        System.out.println("    Additional Needs: " + additionalneeds);
    }
}
