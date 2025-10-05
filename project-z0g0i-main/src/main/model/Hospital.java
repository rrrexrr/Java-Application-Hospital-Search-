package model;

import java.util.ArrayList;
import java.util.List;

import model.Event;
import model.EventLog;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

// Represents a hospital with specific details and a waitlist of patients.

public class Hospital implements Writable {
    private String name;
    private String treatingSymptom;
    private String treatingPeriod;
    private double averageCost;
    private boolean hasAmbulance;
    private int availableSeats;
    private List<Patient> waitingList;

    /* Constructor
     * REQUIRES: averageCost >= 0 and availableSeats >= 0, all strings must be non-null and non-empty
     * EFFECTS: creates a new hospital with the given information
     */

    public Hospital(String name, String treatingSymptom, String treatingPeriod, double averageCost, boolean hasAmbulance, int availableSeats) {
        this.name = name;
        this.treatingSymptom = treatingSymptom;
        this.treatingPeriod = treatingPeriod;
        this.averageCost = averageCost;
        this.hasAmbulance = hasAmbulance;
        this.availableSeats = availableSeats;
        this.waitingList = new ArrayList<>();

    }

     /* MODIFIES: this
      * EFFECTS: add the patient to the waiting list if there are available seats, 
      * decreases availableSeats by 1
      */ 
    public void addPatient(Patient patient) {
        if (availableSeats > 0) {
            waitingList.add(patient);
            availableSeats--;

            EventLog.getInstance().logEvent(new Event("Patient " + patient.getName() + " added to " + this.name + " hospital."));
        } else {
            // If no available seats, maybe log this as well
            EventLog.getInstance().logEvent(new Event("Failed to add patient " + patient.getName() + " to " + this.name + ". No available seats."));
        }
    }

    // EFFECTS: returns the name of the hospital
    public String getName() {
        return name;
    }

    // EFFECTS: returns the treating symptom of the hospital
    public String getTreatingSymptom() {
        return treatingSymptom;
    }

    // EFFECTS: returns the treating period of the hospital
    public String getTreatingPeriod() {
        return treatingPeriod;
    }

    // EFFECTS: returns the average cost of treatment at the hospital
    public double getAverageCost() {
        return averageCost;
    }

    // EFFECTS: returns true if the hospital has an ambulance, false otherwise
    public boolean hasAmbulance() {
        return hasAmbulance;
    }

    // EFFECTS: returns the number of available seats for patients
    public int getAvailableSeats() {
        return availableSeats;
    }

    // EFFECTS: returns the waiting list of patients
    public List<Patient> getWaitingList() {
        return waitingList;
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("name", name);
        json.put("treatingSymptom", treatingSymptom);
        json.put("treatingPeriod", treatingPeriod);
        json.put("averageCost", averageCost);
        json.put("hasAmbulance", hasAmbulance);
        json.put("availableSeats", availableSeats);

        JSONArray patientsArray = new JSONArray();
        for (Patient patient : waitingList) {
            patientsArray.put(patient.toJson());
        }
        json.put("waitingList", patientsArray);

        return json;
    }

}
