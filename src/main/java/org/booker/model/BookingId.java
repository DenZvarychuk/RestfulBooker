package org.booker.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record BookingId(
        @JsonProperty("bookingid")
        Integer bookingId) {
}
