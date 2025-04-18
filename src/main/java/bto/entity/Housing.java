package bto.entity;

/**
 * A class representing a housing.
 */
public class Housing {
    private String type;
    private float sellingPrice;
    private int numberOfUnits;

    /**
     * Constructor for Housing
     * @param type The type of housing
     */
    public Housing(String type) {
        this.type = type;
    }

    /**
     * Gets the type of housing
     * @return The type of housing
     */
    public String getTypeName() {
        return this.type;
    }

    /**
     * Gets the number of units
     * @return The number of units
     */
    public int getNumberOfUnits() {
        return this.numberOfUnits;
    }

    /**
     * Sets the number of units
     * @param numberOfUnits The number of units
     */
    public void setNumberOfUnits(int numberOfUnits) {
        this.numberOfUnits = numberOfUnits;
    }

    /**
     * Gets the selling price
     * @return The selling price
     */
    public float getSellingPrice() {
        return this.sellingPrice;
    }

    /**
     * Sets the selling price
     * @param price The selling price
     */
    public void setSellingPrice(float price) {
        this.sellingPrice = price;
    }

}
