package com.example.greeniqadmin;

public class CarbonDataAdmin {
    private String transportTotal;
    private String foodTotal;
    private String electricityTotal;

    public CarbonDataAdmin() {
    }

    public CarbonDataAdmin(String transportTotal, String foodTotal, String electricityTotal) {
        this.transportTotal = transportTotal;
        this.foodTotal = foodTotal;
        this.electricityTotal = electricityTotal;
    }

    public String getTransportTotal() {
        return transportTotal;
    }

    public void setTransportTotal(String transportTotal) {
        this.transportTotal = transportTotal;
    }

    public String getFoodTotal() {
        return foodTotal;
    }

    public void setFoodTotal(String foodTotal) {
        this.foodTotal = foodTotal;
    }

    public String getElectricityTotal() {
        return electricityTotal;
    }

    public void setElectricityTotal(String electricityTotal) {
        this.electricityTotal = electricityTotal;
    }
}

