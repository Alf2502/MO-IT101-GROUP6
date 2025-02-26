package alfie.motorph;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.*;

public class WorkHours {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MM/dd/yyyy");
    private static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm");

    public WorkHours() {
        // Initialize formatters (optional)
    }

    /**
     * Retrieves work records with time-in and time-out for an employee within a specified date range.
     *
     * @param workHoursFile Path to the work hours CSV file.
     * @param employeeId    ID of the employee to filter records.
     * @param startDate     Start of the date range (inclusive).
     * @param endDate       End of the date range (inclusive).
     * @return Map of dates to time-in and time-out records.
     */
    public Map<Date, String[]> getWorkRecordsWithTime(String workHoursFile, String employeeId, Date startDate, Date endDate) {
        Map<Date, String[]> workRecords = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(workHoursFile))) {
            String line;
            boolean isFirstLine = true; // For skipping headers (if present)

            while ((line = br.readLine()) != null) {
                // Skip empty lines and headers
                if (isFirstLine) {
                    isFirstLine = false;
                    if (line.startsWith("EmployeeID")) continue; // Skip header
                }
                if (line.trim().isEmpty()) continue;

                String[] parts = line.split(",");
                if (parts.length < 6) {
                    System.err.println("⚠ Skipping invalid record (missing fields): " + line);
                    continue;
                }

                String empId = parts[0].trim();
                if (!empId.equals(employeeId)) continue;

                try {
                    Date workDate = DATE_FORMAT.parse(parts[3].trim());
                    // Check if workDate is within range
                    if (workDate.before(startDate) || workDate.after(endDate)) continue;

                    String timeIn = parts[4].trim();
                    String timeOut = parts[5].trim();

                    workRecords.put(workDate, new String[]{timeIn, timeOut});

                } catch (ParseException e) {
                    System.err.println("⚠ Skipping invalid date/time format: " + line);
                }
            }
        } catch (IOException e) {
            System.err.println("❌ Error reading work hours file: " + e.getMessage());
        }

        return workRecords;
    }

    /**
     * Retrieves work records for a specific employee within a given date range.
     *
     * @param workHoursFile Path to the work hours CSV file.
     * @param employeeId    ID of the employee.
     * @param startDate     Start of the date range.
     * @param endDate       End of the date range.
     * @return A map of dates to hours worked.
     */
    public Map<Date, Double> getWorkRecords(String workHoursFile, String employeeId, Date startDate, Date endDate) {
        Map<Date, Double> workRecords = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(workHoursFile))) {
            String line;
            boolean isFirstLine = true; // For skipping headers (if present)

            while ((line = br.readLine()) != null) {
                // Skip empty lines and headers
                if (isFirstLine) {
                    isFirstLine = false;
                    if (line.startsWith("EmployeeID")) continue; // Skip header
                }
                if (line.trim().isEmpty()) continue;

                String[] parts = line.split(",");
                if (parts.length < 6) {
                    System.err.println("⚠ Skipping invalid record (missing fields): " + line);
                    continue;
                }

                String empId = parts[0].trim();
                if (!empId.equals(employeeId)) continue;

                try {
                    Date workDate = DATE_FORMAT.parse(parts[3].trim());
                    // Check if workDate is within range
                    if (workDate.before(startDate) || workDate.after(endDate)) continue;

                    Date timeIn = TIME_FORMAT.parse(parts[4].trim());
                    Date timeOut = TIME_FORMAT.parse(parts[5].trim());

                    // Ensure timeOut is after timeIn
                    if (timeOut.before(timeIn)) {
                        System.err.println("⚠ Skipping invalid time range (timeOut < timeIn): " + line);
                        continue;
                    }

                    double hoursWorked = calculateHours(timeIn, timeOut);
                    workRecords.put(workDate, hoursWorked);

                } catch (ParseException e) {
                    System.err.println("⚠ Skipping invalid date/time format: " + line);
                }
            }
        } catch (IOException e) {
            System.err.println("❌ Error reading work hours file: " + e.getMessage());
        }

        return workRecords;
    }

    /**
     * Calculates hours worked between two timestamps.
     *
     * @param timeIn  The time the employee clocked in.
     * @param timeOut The time the employee clocked out.
     * @return The total hours worked as a double.
     */
    private double calculateHours(Date timeIn, Date timeOut) {
        long milliseconds = timeOut.getTime() - timeIn.getTime();
        return milliseconds / (1000.0 * 60 * 60); // Convert ms to hours
    }
}