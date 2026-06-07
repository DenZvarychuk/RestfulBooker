package org.booker.client;

public class BookingHTTPClientConfig {

    private final String host;
    private final long timeout;
    private final int retries;

    public BookingHTTPClientConfig(String host) {
        this.host = host;
        this.timeout = 5000L;
        this.retries = 3;
    }

    public BookingHTTPClientConfig(String host, long timeout, int retries) {
        this.host = host;
        this.timeout = timeout;
        this.retries = retries;
    }

    public String getHost() {
        return host;
    }

    public long getTimeout() {
        return timeout;
    }

    public int getRetries() {
        return retries;
    }
}
