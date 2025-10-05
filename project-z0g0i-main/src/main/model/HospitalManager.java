package model;

import java.util.ArrayList;
import java.util.List;

/*
 * Represent a hospital manager that manages a list of hospitals.
 */
public class HospitalManager {
    private List<Hospital> hospitals;

    // EFFECTS: create a hospital manager with an empty list of hospitals
    public HospitalManager() {
        hospitals = new ArrayList<>();
        initializeHospitals();
    }
    
    /*
     * MODIFIES: this
     * EFFECTS: initializes the hospital list with seven predefined hospitals
     */
    private void initializeHospitals() {
        hospitals.add(new Hospital("UBC Hospital", "Surgery", "Within 3 days", 2000, true, 5));
        hospitals.add(new Hospital("Vancouver General Hospital", "Internal", "Within 2 days", 1500, true, 10));
        hospitals.add(new Hospital("St. Paul's Hospital", "Surgery", "Within 5 days", 2500, false, 8));
        hospitals.add(new Hospital("Mount Saint Joseph Hospital", "Internal", "Within 1 day", 1800, true, 12));
        hospitals.add(new Hospital("Burnaby Hospital", "Surgery", "Within 4 days", 2200, true, 6));
        hospitals.add(new Hospital("Richmond Hospital", "Internal", "Within 3 days", 1700, false, 15));
        hospitals.add(new Hospital("Lions Gate Hospital", "Surgery", "Within 7 days", 2400, true, 9));
        
        EventLog.getInstance().logEvent(new Event("Initialized hospital list with predefined hospitals."));
    }

    /*
     * REQUIRES: hospital is non-null
     * MODIFIES: this
     * EFFECTS: add the hospital to the list of hospitals
     */
    public void addHospital(Hospital hospital) {
        hospitals.add(hospital);
        EventLog.getInstance().logEvent(new Event("Hospital " + hospital.getName() + " added to the hospital manager."));
    }

    // EFFECTS: returns the list of all hospitals
    public List<Hospital> getHospitals() {
        return hospitals;
    }

    /**
     * REQUIRES: name is non-null and non-empty
     * EFFECTS: returns the hospital with the given name, or null if no hospital with that name exists
     */
    public Hospital findHospitalByName(String name) {
        for (Hospital hospital : hospitals) {
            if (hospital.getName().equals(name)) {
                return hospital;
            }
        }

        EventLog.getInstance().logEvent(new Event("Hospital " + name + " not found in hospital manager."));
        return null;
    }

    //MODIFIES: this
    //EFFECTS: set the hospitals
    public void setHospitals(List<Hospital> hospitals) {
        this.hospitals = hospitals;
        EventLog.getInstance().logEvent(new Event("Hospital list updated in the hospital manager."));
    }//stub

}

