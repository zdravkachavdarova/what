package org.me;

import org.junit.jupiter.api.Test;
import org.me.dto.Customer;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MainTest {

    @Test
    void checkLoyalCustomers() throws Exception {
        Main.main(new String[]{});
        CustomersService customersService = new CustomersService();
        Path directory = Paths.get(
                ClassLoader.getSystemResource("transaction-logs").toURI()
        );
        customersService.processAll(directory);
        List<Customer> customers = customersService.getLoyalCustomers(100D, 2);
        assertEquals(4, customers.size());
        assertEquals(customers.stream().map(Customer::getId).collect(Collectors.toList()), Arrays.asList("1","3","4","5"));

        customers = customersService.getLoyalCustomers(5000D, 3);
        assertEquals(2, customers.size());
        assertEquals(customers.stream().map(Customer::getId).collect(Collectors.toList()), Arrays.asList("3","4","5"));
    }
}