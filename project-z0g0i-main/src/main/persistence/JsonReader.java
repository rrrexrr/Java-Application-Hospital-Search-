package persistence;

import model.Hospital;
import model.Patient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;
import java.util.List;
import java.util.ArrayList;

// Represents a reader that reads hospital waiting list data from JSON stored in a file
public class JsonReader {
    private String source;

    // EFFECTS: constructs reader to read from source file
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads list of hospitals from file and returns it;
    // throws IOException if an error occurs reading data from file
    public List<Hospital> read() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseHospitals(jsonObject);
    }

    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }

        return contentBuilder.toString();
    }

    // EFFECTS: parses hospitals from JSON object and returns list of hospitals
    private List<Hospital> parseHospitals(JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("hospitals");
        List<Hospital> hospitals = new ArrayList<>();
        for (Object json : jsonArray) {
            JSONObject nextHospital = (JSONObject) json;
            hospitals.add(parseHospital(nextHospital));
        }
        return hospitals;
    }

    // EFFECTS: parses hospital from JSON object and returns it
    private Hospital parseHospital(JSONObject jsonObject) {
        String name = jsonObject.getString("name");
        String treatingSymptom = jsonObject.getString("treatingSymptom");
        String treatingPeriod = jsonObject.getString("treatingPeriod");
        double averageCost = jsonObject.getDouble("averageCost");
        boolean hasAmbulance = jsonObject.getBoolean("hasAmbulance");
        int availableSeats = jsonObject.getInt("availableSeats");

        Hospital hospital = new Hospital(name, treatingSymptom, treatingPeriod, averageCost, hasAmbulance, availableSeats);
        addPatients(hospital, jsonObject);
        return hospital;
    }

    // MODIFIES: hospital
    // EFFECTS: parses patients from JSON object and adds them to the hospital's waiting list
    private void addPatients(Hospital hospital, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("waitingList");
        for (Object json : jsonArray) {
            JSONObject nextPatient = (JSONObject) json;
            hospital.addPatient(parsePatient(nextPatient));
        }
    }

    
    // EFFECTS: parses patient from JSON object and returns it
    private Patient parsePatient(JSONObject jsonObject) {
        String name = jsonObject.getString("name");
        String symptom = jsonObject.getString("symptom");
        String description = jsonObject.getString("description");
        boolean isUrgent = jsonObject.getBoolean("isUrgent");
        String treatingPeriod = jsonObject.getString("treatingPeriod");
        double expectedFee = jsonObject.getDouble("expectedFee");

        return new Patient(name, symptom, description, isUrgent, treatingPeriod, expectedFee);
    }
}
