package bto.interfaces;

import bto.enums.MaritalStatus;

/**
 * Interface representing a personal profile with basic personal information.
 */ 
public interface PersonalProfile {
    /**
     * Gets the name of the person.
     *
     * @return the name of the person
     */
    String getName();
    /**
     * Gets the age of the person.
     *
     * @return the age of the person
     */
    int getAge();
    /**
     * Gets the marital status of the person.
     *
     * @return the marital status
     */
    MaritalStatus getMaritalStatus();
}
