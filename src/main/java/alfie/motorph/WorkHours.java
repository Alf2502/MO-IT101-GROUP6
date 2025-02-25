package alfie.motorph;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.*;

public class WorkHours {

    private final SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
    private final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

    public WorkHours() {
        // Default constructor
    }

    public Map<Date, Double> getWorkRecords(String workHoursFile, String employeeId, Date startDate, Date endDate) {
        Map<Date, Double> workRecords = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(workHoursFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");

                if (parts.length < 6) {
                    System.out.println("Skipping invalid record: " + line);
                    continue;
                }

                String empId = parts[0].trim();
                String dateStr = parts[3].trim();
                String timeInStr = parts[4].trim();
                String timeOutStr = parts[5].trim();

                if (!empId.equals(employeeId)) continue;

                try {
                    Date workDate = sdf.parse(dateStr);
                    if (!workDate.before(startDate) && !workDate.after(endDate)) {
                        Date timeIn = timeFormat.parse(timeInStr);
                        Date timeOut = timeFormat.parse(timeOutStr);

                        double hoursWorked = (timeOut.getTime() - timeIn.getTime()) / (1000.0 * 60 * 60);
                        workRecords.put(workDate, hoursWorked);
                    }
                } catch (ParseException | NumberFormatException e) {
                    System.out.println("Skipping invalid record: " + line);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading work hours file: " + e.getMessage());
        }

        return workRecords;
    }
}