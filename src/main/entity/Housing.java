package main.entity;

public class Housing {
    private String type;
	private float sellingPrice;
    private int numberOfUnits;

    public Housing(String type) {
        this.type = type;
    }

    public void setSellingPrice(float price) {
        this.sellingPrice = sellingPrice;
    }

    public int getNumberOfUnits() {
        return this.numberOfUnits;
    }

    public void setNumberOfUnits(int numberOfUnits) {
        this.numberOfUnits = numberOfUnits;
    }

}
