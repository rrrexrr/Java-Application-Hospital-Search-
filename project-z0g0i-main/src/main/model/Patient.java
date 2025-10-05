package model;

import org.json.JSONObject;
import persistence.Writable;


//Represents a patient with their information name, symptoms, descrptions, urgency, treating period
// expected treating fee.

public class Patient implements Writable {
    private String name;
    private String symptom;
    private String description;
    private boolean isUrgent;
    private String treatingPeriod;
    private double expectedFee;

    //Constructor
    //* REQUIRES: all strings must be non-null and non-empty
    //* EFFECTS: creates a new patient with the given information
    //*
    public Patient(String name, String symptom, String description, boolean isUrgent, String treatingPeriod, double expectedFee) {
        this.name = name;
        this.symptom = symptom;
        this.description = description;
        this.isUrgent = isUrgent;
        this.treatingPeriod = treatingPeriod;
        this.expectedFee = expectedFee;

        EventLog.getInstance().logEvent(new Event("Patient " + name + " created with symptom " + symptom));
    }

     // EFFECTS: returns the name of the patient
    public String getName() {
        return name;
    }

    // EFFECTS: returns the symptom of the patient
    public String getSymptom() {
        return symptom;
    }

    // EFFECTS: returns the description of the patient's condition
    public String getDescription() {
        return description;
    }

    // EFFECTS: returns whether the patient's condition is urgent
    public boolean isUrgent() {
        return isUrgent;
    }

    // EFFECTS: returns the expected treating period for the patient
    public String getTreatingPeriod() {
        return treatingPeriod;
    }

    // EFFECTS: returns the expected fee for treatment
    public double getExpectedFee() {
        return expectedFee;
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("name", name);
        json.put("symptom", symptom);
        json.put("description", description);
        json.put("isUrgent", isUrgent);
        json.put("treatingPeriod", treatingPeriod);
        json.put("expectedFee", expectedFee);
        return json;
    }
    
}
