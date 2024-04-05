package org.me.dto;

import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.atomic.DoubleAdder;

public class Customer {
    private String id;
    private DoubleAdder ref = new DoubleAdder();

    public Set<String> stores = new ConcurrentSkipListSet<>();

    public Customer(String id) {
        this.id = id;
    }

    public void addTransaction(Transaction transaction) {
        ref.add(transaction.getTransactionAmount());
        stores.add(transaction.getStoreId());
    }

    public boolean isLoyal(double amount, int numberOfStores) {
        return ref.sum() >= amount && stores.size() >= numberOfStores;
    }

    public String getId() {
        return this.id;
    }

    @Override
    public String toString() {
        return "========= CUSTOMER ID - " + this.id + " =========" +
                "amountSpent = " + this.ref.sum() + "\n" +
                "stores = " + this.stores;
    }
}
