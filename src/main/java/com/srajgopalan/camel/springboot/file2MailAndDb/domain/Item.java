package com.srajgopalan.camel.springboot.file2MailAndDb.domain;

import org.apache.camel.dataformat.bindy.annotation.CsvRecord;
import org.apache.camel.dataformat.bindy.annotation.DataField;

import java.math.BigInteger;

@CsvRecord(separator = ",", skipFirstLine = true)
public class Item {

    @DataField(pos = 1)
    private String operation;

    @DataField(pos = 2)
    private String sku;

    @DataField(pos=3)
    private String description;

    @DataField(pos=4, precision = 2)
    private BigInteger price;

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigInteger getPrice() {
        return price;
    }

    public void setPrice(BigInteger price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "description{" +
                "operation='" + operation + '\'' +
                ", sku='" + sku + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                '}';
    }
}
