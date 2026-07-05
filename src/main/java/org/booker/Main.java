package org.booker;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class Main {

    private static final Logger Log = LogManager.getLogger(Main.class);

    public static void main(String[] args) throws IOException, InterruptedException {
        Log.info("RestfullBooker started");

        new Application().run();

        Log.info("RestfullBooker closed");

    }
}
