package ui;

import model.EventLog;
import model.Hospital;
import model.Patient;

import model.Event;

import javax.swing.*;
import java.awt.*;
import java.util.List;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class EmergencyHelpCenterGUI extends JFrame {
    private EmergencyHelpCenterApp app;

    public EmergencyHelpCenterGUI() {
        app = new EmergencyHelpCenterApp();
        initializeGUI();
    }

    private void initializeGUI() {
        setTitle("Emergency Help Center");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);

        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Menu buttons
        JPanel buttonPanel = new JPanel(new GridLayout(0, 1));
        JButton addPatientButton = new JButton("Add Patient");
        JButton viewHospitalsButton = new JButton("View Hospitals");
        JButton viewWaitingListButton = new JButton("View Hospital Waiting List");
        JButton saveButton = new JButton("Save Data");
        JButton loadButton = new JButton("Load Data");

        buttonPanel.add(addPatientButton);
        buttonPanel.add(viewHospitalsButton);
        buttonPanel.add(viewWaitingListButton);
        buttonPanel.add(saveButton);
        buttonPanel.add(loadButton);

        mainPanel.add(buttonPanel, BorderLayout.CENTER);
        add(mainPanel);

        // Action listeners
        addPatientButton.addActionListener(e -> showAddPatientDialog());
        viewHospitalsButton.addActionListener(e -> displayHospitals());
        viewWaitingListButton.addActionListener(e -> displayWaitingList());
        saveButton.addActionListener(e -> app.saveData());
        // loadButton.addActionListener(e -> app.loadData());
        loadButton.addActionListener(e -> {
            try {
                app.loadData(); // Attempt to load data
                // Display loaded hospital details
                StringBuilder loadedData = new StringBuilder("Loaded Hospitals:\n");
                for (Hospital hospital : app.getHospitalManager().getHospitals()) {
                    loadedData.append(hospital.getName()).append(" - Available Seats: ")
                            .append(hospital.getAvailableSeats()).append("\n");
                }
                JOptionPane.showMessageDialog(this, loadedData.toString(), "Load Successful", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                // Display error message if loading fails
                JOptionPane.showMessageDialog(this, "Failed to load data: " + ex.getMessage(),
                        "Load Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Add Window Listener to print EventLog when closing
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                printEventLog();
            }
        });

        setVisible(true);
    }

    private void showAddPatientDialog() {
        JTextField nameField = new JTextField();
        JTextField symptomField = new JTextField();
        JTextField descriptionField = new JTextField();
        JCheckBox urgentCheckBox = new JCheckBox("Urgent");
        JTextField periodField = new JTextField();
        JTextField feeField = new JTextField();

        Object[] message = {
                "Name:", nameField,
                "Symptom: Surgery/Internal", symptomField,
                "Description:", descriptionField,
                "Urgency:", urgentCheckBox,
                "Treating Period:", periodField,
                "Expected Fee:", feeField,
        };

        int option = JOptionPane.showConfirmDialog(null, message, "Add Patient", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                // Collect input from the dialog
                String name = nameField.getText();
                String symptom = symptomField.getText();
                String description = descriptionField.getText();
                boolean isUrgent = urgentCheckBox.isSelected();
                String period = periodField.getText();
                double fee = Double.parseDouble(feeField.getText());

                // Create a new patient
                Patient p = new Patient(name, symptom, description, isUrgent, period, fee);

                // Let the user select a hospital
                List<Hospital> hospitals = app.getHospitalManager().getHospitals();
                String[] hospitalNames = hospitals.stream().map(Hospital::getName).toArray(String[]::new);
                String selectedHospital = (String) JOptionPane.showInputDialog(
                        this,
                        "Select a hospital to add the patient:",
                        "Hospital Selection",
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        hospitalNames,
                        hospitalNames[0]
                );

                if (selectedHospital != null) {
                    // Find the selected hospital
                    Hospital hospital = app.getHospitalManager().findHospitalByName(selectedHospital);
                    if (hospital != null) {
                        // Add the patient to the hospital
                        if (hospital.getAvailableSeats() > 0) {
                            hospital.addPatient(p);
                            JOptionPane.showMessageDialog(this, "Patient added to " + hospital.getName() + " successfully!");
                        } else {
                            JOptionPane.showMessageDialog(this, "The selected hospital has no available seats.");
                        }
                    } else {
                        JOptionPane.showMessageDialog(this, "Hospital not found.");
                    }
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Invalid input for the fee. Please enter a valid number.");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "An error occurred: " + e.getMessage());
            }
        }
    }

    private void displayHospitals() {
        StringBuilder hospitalsInfo = new StringBuilder("Hospitals:\n");
        for (Hospital hospital : app.getHospitalManager().getHospitals()) {
            hospitalsInfo.append(hospital.getName()).append("\n");
        }
        JOptionPane.showMessageDialog(this, hospitalsInfo.toString());
    }

    private void displayWaitingList() {
        StringBuilder waitingListInfo = new StringBuilder("Waiting List:\n");
        for (Hospital hospital : app.getHospitalManager().getHospitals()) {
            waitingListInfo.append(hospital.getName()).append(":\n");
            for (Patient patient : hospital.getWaitingList()) {
                waitingListInfo.append(patient.getName()).append("\n");
            }
        }
        JOptionPane.showMessageDialog(this, waitingListInfo.toString());
    }

    // EFFECTS: prints all the events logged to the console
    private void printEventLog() {
        EventLog eventLog = EventLog.getInstance();
        for (Event event : eventLog) {
            System.out.println(event.getDate() + ": " + event.getDescription());
        }
    }

    public static void main(String[] args) {
        new EmergencyHelpCenterGUI();
    }
}
