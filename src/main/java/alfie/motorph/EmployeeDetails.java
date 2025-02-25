package alfie.motorph;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class EmployeeDetails {
    
    public Employee getEmployeeDetails(String employeeDetailsFile, String employeeId) {
        String line;
        String delimiter = ",";
        
        try (BufferedReader br = new BufferedReader(new FileReader(employeeDetailsFile))) {
            while ((line = br.readLine()) != null) {
                String[] values = line.split(delimiter);
                if (values[0].equalsIgnoreCase(employeeId)) {
                    // Create an Employee object and return it
                    return new Employee(values[0], values[1], values[2], values[11], values[3], values[5], 
                                        values[4], values[6], values[7], values[8], values[9], values[10], 
                                        values[12], Double.parseDouble(values[18]));
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading employee details: " + e.getMessage());
        }
        
        return null;  // Employee not found
    }
}