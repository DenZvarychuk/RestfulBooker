package org.booker.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.booker.Main;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class BookingFilter {
    private static final Logger Log = LogManager.getLogger(BookingFilter.class);

    private static final String FirstNameParam = "firstname";
    private static final String LastNameParam = "lastname";
    private static final String CheckinParam = "checkin";
    private static final String CheckoutParam = "checkout";

    private String firstname;
    private String lastname;
    private LocalDate checkin;
    private LocalDate checkout;

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public void setCheckin(LocalDate checkin) {
        this.checkin = checkin;
    }

    public void setCheckout(LocalDate checkout) {
        this.checkout = checkout;
    }

    public String toQueryString() {
        StringBuilder sb = new StringBuilder();

        addParam(sb, FirstNameParam, firstname);
        addParam(sb, LastNameParam, lastname);
        addParam(sb, CheckinParam, checkin != null ? checkin.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) : null);
        addParam(sb, CheckoutParam, checkout != null ? checkout.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) : null);

        Log.info("Generated query string: {}", sb.toString());
        return sb.toString();
    }

    private void addParam(StringBuilder sb, String key, String value) {
        if (value == null || value.isEmpty()) {
            return;
        }
        if (sb.length() > 0) {
            sb.append("&");
        }
        sb.append(key)
                .append("=")
                .append(URLEncoder.encode(value, StandardCharsets.UTF_8));
    }

}
