package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class TestHospitalManager {
    private HospitalManager hospitalManager;

    @BeforeEach
    public void runBefore() {
        hospitalManager = new HospitalManager();
    }

    @Test
    public void testHospitalManagerConstructor() {
        List<Hospital> hospitals = hospitalManager.getHospitals();
        assertEquals(7, hospitals.size());
        assertEquals("UBC Hospital", hospitals.get(0).getName());
        assertEquals("Vancouver General Hospital", hospitals.get(1).getName());
        assertEquals("St. Paul's Hospital", hospitals.get(2).getName());
        assertEquals("Mount Saint Joseph Hospital", hospitals.get(3).getName());
        assertEquals("Burnaby Hospital", hospitals.get(4).getName());
        assertEquals("Richmond Hospital", hospitals.get(5).getName());
        assertEquals("Lions Gate Hospital", hospitals.get(6).getName());
    }

    @Test
    public void testAddHospital() {
        Hospital newHospital = new Hospital("New Hospital", "Surgery", "Within 2 days", 3000, true, 5);
        hospitalManager.addHospital(newHospital);
        List<Hospital> hospitals = hospitalManager.getHospitals();
        assertEquals(8, hospitals.size());
        assertEquals(newHospital, hospitals.get(7));
    }

    @Test
    public void testFindHospitalByName() {
        Hospital foundHospital = hospitalManager.findHospitalByName("UBC Hospital");
        assertNotNull(foundHospital);
        assertEquals("UBC Hospital", foundHospital.getName());

        Hospital notFoundHospital = hospitalManager.findHospitalByName("Nonexistent Hospital");
        assertNull(notFoundHospital);
    }

    @Test
    public void testSetHospitals() {
        List<Hospital> hospitals = new ArrayList<>();
        hospitals.add(new Hospital("New Hospital", "Surgery", "Within 3 days", 5000, true, 10));
        hospitalManager.setHospitals(hospitals);
        assertEquals(1, hospitalManager.getHospitals().size());
        assertEquals("New Hospital", hospitalManager.getHospitals().get(0).getName());
    }
}
