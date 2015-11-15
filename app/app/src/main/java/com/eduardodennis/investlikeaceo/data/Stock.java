package com.eduardodennis.investlikeaceo.data;

/**
 * Created by Eddie on 4/21/2015.
 */
public class Stock {

    private String symbol;
    private Long price;
    private Double weight;

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

}
