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
  private List<HDBOfficer> pendingOfficers = new ArrayList<>();


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

  public List<HDBOfficer> getPendingOfficers() {
    return ArrayList<>(pendingOfficers);
  }
}
