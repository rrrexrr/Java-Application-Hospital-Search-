package persistence;

import model.Hospital;
import model.Patient;

import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JsonReaderTest {

    @Test
    void testReaderNonExistentFile() {
        persistence.JsonReader reader = new JsonReader("./data/noSuchFile.json");
        try {
            reader.read();
            fail("IOException expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testReaderEmptyHospitalList() {
        JsonReader reader = new JsonReader("./data/testReaderEmptyHospital.json");
        try {
            assertEquals(0, reader.read().size());
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    void testReaderGeneralHospitalList() {
        JsonReader reader = new JsonReader("./data/testReaderGeneralHospital.json");
        try {
            List<Hospital> hospitals = reader.read();
            assertEquals(2, hospitals.size());

            Hospital hospital1 = hospitals.get(0);
            assertEquals("UBC Hospital", hospital1.getName());
            assertEquals("Surgery", hospital1.getTreatingSymptom());
            assertEquals(4, hospital1.getAvailableSeats());

            List<Patient> waitingList = hospital1.getWaitingList();
            assertEquals(1, waitingList.size());
            assertEquals("John Doe", waitingList.get(0).getName());
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }
}