package ui;

import model.EventLog;
import model.Hospital;
import model.HospitalManager;
import model.Patient;

import model.EventLog;
import model.Event;

import persistence.JsonReader;
import persistence.JsonWriter;

import java.io.FileNotFoundException;
import java.io.IOException;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// The Emergency Help Center application.
public class EmergencyHelpCenterApp {
    private static final String JSON_STORE = "./data/hospitalData.json";
    private HospitalManager hospitalManager;
    private Scanner scanner;
    private List<Patient> patients;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;

    // EFFECTS: initializes the application
    public EmergencyHelpCenterApp() {
        patients = new ArrayList<>();
        hospitalManager = new HospitalManager();
        scanner = new Scanner(System.in);

        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);
    }

    // MODIFIES: this
    // EFFECTS: computes user input and respond to given actions
    public void runApp() {
        boolean keepRunning = true;

        while (keepRunning) {
            displayMenu();
            String command = scanner.next().toLowerCase();

            switch (command) {
                case "1":
                    addPatient();
                    break;
                case "2":
                    viewAllHospitals();
                    break;
                case "3":
                    viewHospitalWaitingList();
                    break;
                case "4":
                    saveData();
                    break;
                case "5":
                    loadData();
                    break;
                case "q":
                    keepRunning = false;
                    saveOnExit();
                    break;
                default:
                    System.out.println("Invalid selection, please try again.");
                    break;
            }
        }
    }

    // EFFECTS: displays the main menu to the user
    private void displayMenu() {
        System.out.println("\nSelect from:");
        System.out.println("1 -> Add patient information");
        System.out.println("2 -> View all hospitals");
        System.out.println("3 -> View a hospital's waiting list");
        System.out.println("4 -> Save data");
        System.out.println("5 -> Load data");
        System.out.println("q -> Quit");
    }


    // MODIFIES: this
    // EFFECTS: let user to enter patient information and stores it, then let the user to add the patient to a hospital
    private void addPatient() {
        System.out.println("Enter patient name: ");
        String name = scanner.next();
        System.out.println("Enter symptom (Surgery/Internal): ");
        String symptom = scanner.next();
        System.out.println("Enter description of current situation: ");
        System.out.println("Note: Do not enter spaces in the input.");
        String description = scanner.next();
        System.out.println("Is it urgent? (true/false): ");
        boolean isUrgent;
        while (true) {
            String input = scanner.next();
            if (input.equalsIgnoreCase("true")) {
                isUrgent = true;
                break;
            } else if (input.equalsIgnoreCase("false")) {
                isUrgent = false;
                break;
            } else {
                System.out.println("Invalid input. Please enter true or false:");
            }
        }
        System.out.println("Enter expected treating period: ");
        System.out.println("Note: Do not enter spaces in the input.");
        String treatingPeriod = scanner.next();
        System.out.println("Enter expected treating fee: ");
        double expectedFee;
        System.out.println("Note: Do not enter spaces in the input.");
        expectedFee = scanner.nextDouble();

        Patient newPatient = new Patient(name, symptom, description, isUrgent, treatingPeriod, expectedFee);
        patients.add(newPatient);
        System.out.println("Patient information saved.");

        System.out.println("Would you like to add more patients? (yes/no)");
        String response = scanner.next().toLowerCase();
        if (response.equals("no")) {
            addPatientsToHospital();
        } else if (patients.size() == 3) {
            addPatientsToHospital();
        } else {
            addPatient();
        }
    }

    // MODIFIES: this
    // EFFECTS: let user to select a hospital and add patients to that hospital's waiting list
    private void addPatientsToHospital() {
        if (patients.isEmpty()) {
            System.out.println("No patient information available. Please add patient information first.");
            return;
        }

        System.out.println("Select a hospital by number:");
        List<Hospital> hospitals = hospitalManager.getHospitals();
        for (int i = 0; i < hospitals.size(); i++) {
            System.out.println((i + 1) + " -> " + hospitals.get(i).getName());
        }

        int hospitalIndex;
        System.out.println("Note: Do not enter spaces in the input.");
        hospitalIndex = scanner.nextInt() - 1;
        if (hospitalIndex < 0 || hospitalIndex >= hospitals.size()) {
            System.out.println("Invalid hospital selection.");
            return;
        }

        Hospital selectedHospital = hospitals.get(hospitalIndex);
        for (Patient patient : patients) {
            selectedHospital.addPatient(patient);
        }
        System.out.println("All patients added to the hospital waiting list.");
        patients.clear(); // Clear patients after adding to hospital
    }



    
    // EFFECTS: displays information about all hospitals
    private void viewAllHospitals() {
        for (Hospital hospital : hospitalManager.getHospitals()) {
            System.out.println("\nHospital Name: " + hospital.getName());
            System.out.println("Treating Symptom: " + hospital.getTreatingSymptom());
            System.out.println("Treating Period: " + hospital.getTreatingPeriod());
            System.out.println("Average Cost: " + hospital.getAverageCost());
            System.out.println("Has Ambulance: " + hospital.hasAmbulance());
            System.out.println("Available Seats: " + hospital.getAvailableSeats());
        }
    }

    // EFFECTS: let user to select a hospital and show the waiting list of that hospital
    private void viewHospitalWaitingList() {
        System.out.println("Select a hospital by number:");
        List<Hospital> hospitals = hospitalManager.getHospitals();
        for (int i = 0; i < hospitals.size(); i++) {
            System.out.println((i + 1) + " -> " + hospitals.get(i).getName());
        }

        int hospitalIndex = scanner.nextInt() - 1;
        if (hospitalIndex < 0 || hospitalIndex >= hospitals.size()) {
            System.out.println("Invalid hospital selection.");
            return;
        }

        Hospital selectedHospital = hospitals.get(hospitalIndex);

        System.out.println("\nWaiting List for " + selectedHospital.getName() + ":");
        for (Patient patient : selectedHospital.getWaitingList()) {
            System.out.println("Patient Name: " + patient.getName());
            System.out.println("Symptom: " + patient.getSymptom());
            System.out.println("Description: " + patient.getDescription());
            System.out.println("Urgent: " + patient.isUrgent());
            System.out.println("Treating Period: " + patient.getTreatingPeriod());
            System.out.println("Expected Fee: " + patient.getExpectedFee());
            System.out.println();
        }
    }

    // EFFECTS: saves data to file
    public void saveData() {
        System.out.println("Saving data...");
        try {
            jsonWriter.open();
            jsonWriter.write(hospitalManager.getHospitals());
            jsonWriter.close();
            System.out.println("Data saved.");
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file: " + JSON_STORE);
        }
    }

    // EFFECTS: loads data from file
    public void loadData() {
        System.out.println("Loading data...");
        try {
            List<Hospital> hospitals = jsonReader.read();
            hospitalManager.setHospitals(hospitals);
            System.out.println("Data loaded.");
        } catch (IOException e) {
            System.out.println("Unable to read from file: " + JSON_STORE);
        }
    }

    // EFFECTS: asks user if they want to save data on exit
    private void saveOnExit() {
        System.out.println("Would you like to save the current data before exiting? (yes/no)");
        String response = scanner.next().toLowerCase();
        if (response.equals("yes")) {
            saveData();
        }
        System.out.println("Exiting application. Goodbye!");
    }

    // EFFECTS: get the hospitalManager class
    public HospitalManager getHospitalManager() {
        return hospitalManager;
    }
}

