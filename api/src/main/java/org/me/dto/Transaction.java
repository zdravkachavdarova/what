package org.me.dto;

import lombok.Getter;

public class Transaction {
    @Getter
    private String CustomerID ;
    @Getter
    private String Timestamp;
    @Getter
    private double TransactionAmount;
    @Getter
    private String StoreId;

    public Transaction(String[] args) throws Exception {
        if (args.length != 4) {
            throw new Exception("Failed to create a transaction");
        }
        CustomerID = args[0].trim();
        Timestamp = args[1].trim();
        TransactionAmount = Double.parseDouble(args[2].trim());
        StoreId = args[3].trim();
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "CustomerID='" + CustomerID + '\'' +
                ", Timestamp='" + Timestamp + '\'' +
                ", TransactionAmount=" + TransactionAmount +
                ", StoreId='" + StoreId + '\'' +
                '}';
    }
}
