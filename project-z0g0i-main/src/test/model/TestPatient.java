package model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TestPatient {
    private Patient patient;

    @BeforeEach
    public void runBefore() {
        patient = new Patient("Ben", "Surgery", "Broken Arm", false, "Within 7 days", 10000);
    }

    @Test
    public void testPatientConstructor() {
        assertEquals("Ben", patient.getName());
        assertEquals("Surgery", patient.getSymptom());
        assertEquals("Broken Arm", patient.getDescription());
        assertFalse(patient.isUrgent());
        assertEquals("Within 7 days", patient.getTreatingPeriod());
        assertEquals(10000, patient.getExpectedFee());
    }

    @Test
    public void testToJson() {
        assertEquals("Ben", patient.toJson().getString("name"));
        assertEquals("Surgery", patient.toJson().getString("symptom"));
        assertEquals("Broken Arm", patient.toJson().getString("description"));
        assertFalse(patient.toJson().getBoolean("isUrgent"));
        assertEquals("Within 7 days", patient.toJson().getString("treatingPeriod"));
        assertEquals(10000, patient.toJson().getDouble("expectedFee"));
    }
}
