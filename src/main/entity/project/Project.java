package main.entity.project;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import main.controller.user.Registration;
import main.entity.Enquiry;
import main.entity.Housing;
import main.entity.user.HDBManager;
import main.entity.user.HDBOfficer;
import main.entity.user.User;

public class Project
{

  private String name;
  private boolean visible;
  private String neighborhood;
  private LocalDate openingDate;
  private LocalDate closingDate;
  private HDBManager manager;
  private List<HDBOfficer> officers;
  private int officerSlot = 10;
  // private List<Application> applications;
  private List<Enquiry> enquiries;
  private List<Registration> registrations;
  private Housing housingTypeOne;
  private Housing housingTypeTwo;

  public Project(String name, String neighborhood, boolean visible)
  {
    this.name = name;
    this.neighborhood = neighborhood;
    this.visible = visible;
    this.officers = new ArrayList<>();
    //  this.applications = new ArrayList<>();
    this.enquiries = new ArrayList<>();
    this.registrations = new ArrayList<>();
  }

  public String getName()
  {
    return this.name;
  }

  public void setDate(LocalDate openingDate, LocalDate closingDate)
  {
    this.openingDate = openingDate;
    this.closingDate = closingDate;
  }

  public void setManagerInCharge(HDBManager manager)
  {
    this.manager = manager;
  }

  public void addOfficersIncharge(HDBOfficer officer)
  {
    if (this.officerSlot == 0)
    {
      System.out.println("No more slot.");
      return;
    }
    this.officerSlot -= 1;
    this.officers.add(officer);
  }

  public void setOfficerSlot(int slot)
  {
    if (this.officers.size() > slot)
    {
      System.out.println("Illegal size.");
      return;
    }
    this.officerSlot = slot;
  }

  public void addEnquiry(Enquiry enquiry)
  {
    this.enquiries.add(enquiry);
  }

  public void deleteEnquiry(Enquiry enquiry) {
      this.enquiries.remove(enquiry);
  }

  public List<Enquiry> getEnquiries() {
        return this.enquiries;
    }

  public void addRegistration(Registration registration)
  {
    this.registrations.add(registration);
  }

  public void setHousingTypeOne(float sellingPrice, int numberofUnits)
  {
    this.housingTypeOne = new Housing("2-Room");
    this.housingTypeOne.setSellingPrice(sellingPrice);
    this.housingTypeOne.setNumberOfUnits(numberofUnits);
  }

  public void setHousingTypeTwo(float sellingPrice, int numberOfUnits)
  {
    this.housingTypeTwo = new Housing("3-Room");
    this.housingTypeTwo.setSellingPrice(sellingPrice);
    this.housingTypeTwo.setNumberOfUnits(numberOfUnits);
  }

  public void setPriceTypeOne(float sellingPrice) {
    this.housingTypeOne.setSellingPrice(sellingPrice);
  }

  public void setPriceTypeTwo(float sellingPrice) {
    this.housingTypeTwo.setSellingPrice(sellingPrice);
  }

  public void setNoOfUnitsTypeOne(int noOfUnits) {
    this.housingTypeOne.setNumberOfUnits(noOfUnits);
  }

  public void setNoOfUnitsTypeTwo(int noOfUnits) {
    this.housingTypeTwo.setNumberOfUnits(noOfUnits);
  }

  public void setOpeningDate(LocalDate openingDate) {
    this.openingDate = openingDate;
  }

  public void setClosingDate(LocalDate closingDate) {
    this.closingDate = closingDate;
  }


  public List<Registration> getRegistrationList(User user)
  {
    return registrations;
  }

  public HDBManager getManager() {
    return this.manager;
  }

  public List<HDBOfficer> getAssignedOfficers() {
    return this.officers;
  }

  public boolean getVisibility() {
    return this.visible;
  }

  public void setVisiblity(boolean visible) {
    this.visible = visible;
  }

  public String getNeighbourhood()
  {
    return this.neighborhood;
  }

  public int getNoOfUnitsTypeOne()
  {
    return this.housingTypeOne.getNumberOfUnits();
  }

  public float getPriceTypeOne()
  {
    return this.housingTypeOne.getSellingPrice();
  }

  public int getNoOfUnitsTypeTwo()
  {
    return this.housingTypeTwo.getNumberOfUnits();
  }

  public float getPriceTypeTwo()
  {
    return this.housingTypeTwo.getSellingPrice();
  }

  public LocalDate getOpeningDate()
  {
    return this.openingDate;
  }

  public LocalDate getClosingDate()
  {
    return this.closingDate;
  }

  public int getSlots()
  {
    return this.officerSlot;
  }

  public void setName(String name)
  {
    this.name = name;
  }

  public void setNeighbourhood(String neighborhood) {
    this.neighborhood = neighborhood;
  }
}
