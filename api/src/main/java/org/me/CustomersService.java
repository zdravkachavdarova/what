package org.me;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import org.me.dto.Customer;
import org.me.dto.Transaction;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CustomersService {

    private final Map<String, Customer> customers;

    private final ExecutorService executorService;

    public CustomersService() {
        this.customers = new ConcurrentHashMap<>();
        this.executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    }

    public List<Customer> getLoyalCustomers(double amount, int numberOfStores) {
        return customers.values().stream().filter(c -> c.isLoyal(amount, numberOfStores)).collect(Collectors.toList());
    }

    public Runnable processFile(Path path) {
        return () -> {
            System.out.println(path);
            try (Reader reader = Files.newBufferedReader(path)) {
                CSVParser parser = new CSVParserBuilder().withSeparator(',').withIgnoreQuotations(true).build();
                try (CSVReader csvReader = new CSVReaderBuilder(reader).withSkipLines(1).withCSVParser(parser).build()) {
                    String[] line;
                    while ((line = csvReader.readNext()) != null) {
                        Transaction newTransaction = new Transaction(line);
                        customers.putIfAbsent(newTransaction.getCustomerID(), new Customer(newTransaction.getCustomerID()));
                        customers.get(newTransaction.getCustomerID()).addTransaction(newTransaction);
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
    }

    public void processAll(Path directory) throws IOException {
        List<Future> futures = new ArrayList<>();
        try (Stream<Path> stream = Files.walk(directory)) {
            stream.forEach(path -> {
                        Future<?> future = executorService.submit(this.processFile(path));
                        futures.add(future);
                    }
            );
        }
        for (Future<?> future : futures) {
            try {
                future.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
    }

    public void shutdown() throws InterruptedException {
        executorService.shutdown();
        if (!executorService.awaitTermination(30, TimeUnit.SECONDS)) {
            executorService.shutdownNow();
        }
    }
}
