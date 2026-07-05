package org.booker.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AuthToken(
        @JsonProperty("token")
        String token) {
}
