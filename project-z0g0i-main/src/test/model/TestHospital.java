package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestHospital {
    private Hospital hospital;
    private Patient patient;

    @BeforeEach
    public void runBefore() {
        patient = new Patient("Ben", "Surgery", "Broken Arm", false, "Within 7 days", 10000);
        hospital = new Hospital("UBC Hospital", "Surgery", "Within 3 days", 2000.0, true, 2);
    }

    @Test
    public void testHospitalConstructor() {
        assertEquals("UBC Hospital", hospital.getName());
        assertEquals("Surgery", hospital.getTreatingSymptom());
        assertEquals("Within 3 days", hospital.getTreatingPeriod());
        assertEquals(2000.0, hospital.getAverageCost(), 0.001);
        assertTrue(hospital.hasAmbulance());
        assertEquals(2, hospital.getAvailableSeats());
    }

    @Test
    public void testAddPatientToHospital() {
        hospital.addPatient(patient);
        List<Patient> waitingList = hospital.getWaitingList();
        assertEquals(1, waitingList.size());
        assertEquals(patient, waitingList.get(0));
        assertEquals(1, hospital.getAvailableSeats());
    }

    @Test
    public void testAddPatientToFullHospital() {
        hospital.addPatient(patient);
        hospital.addPatient(new Patient("Alice", "Internal", "High fever", true, "Immediate", 500));
        assertEquals(0, hospital.getAvailableSeats());

        hospital.addPatient(new Patient("Charlie", "Surgery", "Fractured leg", false, "Within 5 days", 3000));
        assertEquals(2, hospital.getWaitingList().size());
    }

    @Test
    public void testToJson() {
        hospital.addPatient(patient);
        assertEquals("UBC Hospital", hospital.toJson().getString("name"));
        assertEquals("Surgery", hospital.toJson().getString("treatingSymptom"));
        assertEquals("Within 3 days", hospital.toJson().getString("treatingPeriod"));
        assertEquals(2000, hospital.toJson().getDouble("averageCost"));
        assertTrue(hospital.toJson().getBoolean("hasAmbulance"));
        assertEquals(1, hospital.toJson().getInt("availableSeats"));
        assertEquals(1, hospital.toJson().getJSONArray("waitingList").length());
    }

}
