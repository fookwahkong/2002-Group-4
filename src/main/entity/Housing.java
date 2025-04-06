package main.entity;

public class Housing {
    private String type;
    private float sellingPrice;
    private int numberOfUnits;

    public Housing(String type) {
        this.type = type;
    }

    public String getTypeName() {
        return this.type;
    }

    public int getNumberOfUnits() {
        return this.numberOfUnits;
    }

    public void setNumberOfUnits(int numberOfUnits) {
        this.numberOfUnits = numberOfUnits;
    }

    public float getSellingPrice() {
        return this.sellingPrice;
    }

    public void setSellingPrice(float price) {
        this.sellingPrice = price;
    }

}
