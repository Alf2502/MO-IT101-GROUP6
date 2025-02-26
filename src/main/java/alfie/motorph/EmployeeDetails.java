package alfie.motorph;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class EmployeeDetails {

    /**
     * Fetches employee details from a CSV file based on the employee ID.
     *
     * @param filePath   Path to the employee details CSV file.
     * @param employeeId ID of the employee to search for.
     * @return An Employee object if found, otherwise null.
     */
    public Employee getEmployeeDetails(String filePath, String employeeId) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean isFirstLine = true; // For skipping headers

            while ((line = br.readLine()) != null) {
                // Skip empty lines and headers
                if (isFirstLine) {
                    isFirstLine = false;
                    if (line.startsWith("EmployeeID")) continue; // Skip header
                }
                if (line.trim().isEmpty()) continue;

                String[] parts = line.split(",");
                if (parts.length < 9) { // Ensure there are enough columns
                    System.err.println("⚠ Skipping invalid record: " + line);
                    continue;
                }

                String empId = parts[0].trim();
                if (empId.equals(employeeId)) {
                    // Create and return an Employee object
                    return new Employee(
                        empId,
                        parts[1].trim(), // Last Name
                        parts[2].trim(), // First Name
                        parts[11].trim(), // Position
                        parts[3].trim(), // Date of Birth
                        parts[4].trim(), // Phone Number
                        parts[5].trim(), // Address
                        parts[12].trim(), // Supervisor
                        Double.parseDouble(parts[18].trim()) // Hourly Rate
                    );
                }
            }
        } catch (IOException e) {
            System.err.println("❌ Error reading employee details file: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("❌ Invalid hourly rate format in employee details file.");
        }

        return null; // Employee not found
    }
}