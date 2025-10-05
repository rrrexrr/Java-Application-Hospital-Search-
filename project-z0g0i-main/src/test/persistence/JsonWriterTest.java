package persistence;

import model.Hospital;
import model.Patient;

import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class JsonWriterTest {

    @Test
    void testWriterInvalidFile() {
        try {
            persistence.JsonWriter writer = new JsonWriter("./data/myIllegalFileName\0.json");
            writer.open();
            fail("IOException was expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testWriterEmptyHospitalList() {
        try {
            List<Hospital> hospitals = new ArrayList<>();
            persistence.JsonWriter writer = new persistence.JsonWriter("./data/testWriterEmptyHospital.json");
            writer.open();
            writer.write(hospitals);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterEmptyHospital.json");
            hospitals = reader.read();
            assertEquals(0, hospitals.size());
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testWriterGeneralHospitalList() {
        try {
            List<Hospital> hospitals = new ArrayList<>();
            Hospital hospital = new Hospital("UBC Hospital", "Surgery", "Within 3 days", 2000, true, 5);
            Patient patient = new Patient("John Doe", "Broken Arm", "Minor fracture", true, "Within 3 days", 1000);
            hospital.addPatient(patient);
            hospitals.add(hospital);

            JsonWriter writer = new JsonWriter("./data/testWriterGeneralHospital.json");
            writer.open();
            writer.write(hospitals);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterGeneralHospital.json");
            hospitals = reader.read();
            assertEquals(1, hospitals.size());

            Hospital readHospital = hospitals.get(0);
            assertEquals("UBC Hospital", readHospital.getName());
            assertEquals("Surgery", readHospital.getTreatingSymptom());
            assertEquals(3, readHospital.getAvailableSeats());

            List<Patient> waitingList = readHospital.getWaitingList();
            assertEquals(1, waitingList.size());
            assertEquals("John Doe", waitingList.get(0).getName());
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }
}
