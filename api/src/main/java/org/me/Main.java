package org.me;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;

public class Main {
    public static CustomersService customersService = new CustomersService();

    public static void main(String[] args) throws Exception {
        long start = Instant.now().toEpochMilli();
        Path directory = Paths.get(
                ClassLoader.getSystemResource("transaction-logs").toURI()
        );
        customersService.processAll(directory);
        customersService.shutdown();
        // Print all loyal customers
        customersService.getLoyalCustomers(5000D, 2).forEach(c -> System.out.println(c.toString()));

        System.out.println("time: " + Instant.now().minusMillis(start).toEpochMilli());
    }
}
