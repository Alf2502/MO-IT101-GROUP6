package alfie.motorph;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class WorkHoursCalculator {

    private final WorkHours workHours;
    private static final int REGULAR_WORK_HOURS = 8; // Regular working hours per day
    private static final double OVERTIME_RATE = 1.25; // Overtime rate (25% more than regular pay)
    private static final SimpleDateFormat DATE_FORMAT_WITH_DAY = new SimpleDateFormat("MM/dd/yyyy - EEEE");
    private static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm"); // Time formatter

    public WorkHoursCalculator(WorkHours workHours) {
        this.workHours = workHours;
    }

    /**
     * Allows the user to select a year and week number to calculate work hours and salary for that period.
     *
     * @param workHoursFile Path to the work hours file.
     * @param employee      The employee for whom to calculate work hours and salary.
     * @param scanner       The scanner for user input.
     */
    public void calculateWorkHours(String workHoursFile, Employee employee, Scanner scanner) {
        // Prompt for year
        System.out.print("Enter year (YYYY): ");
        int year = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        // Prompt for week number
        System.out.print("Enter week number (1-52): ");
        int week = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        // Validate week number
        if (week < 1 || week > 52) {
            System.out.println("❌ Invalid week number. Please enter a value between 1 and 52.");
            return;
        }

        // Calculate start and end dates for the selected week
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.WEEK_OF_YEAR, week);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        Date startDate = calendar.getTime();

        calendar.add(Calendar.DATE, 6); // Move to Sunday of the same week
        Date endDate = calendar.getTime();

        // Process work records for the selected week
        processWorkRecords(workHoursFile, employee, startDate, endDate);
    }

    /**
     * Processes and displays work records and salary calculations for the selected week.
     *
     * @param workHoursFile Path to the work hours file.
     * @param employee      The employee for whom to calculate work hours and salary.
     * @param startDate     Start of the date range (Monday of the selected week).
     * @param endDate       End of the date range (Sunday of the selected week).
     */
    private void processWorkRecords(String workHoursFile, Employee employee, Date startDate, Date endDate) {
        // Fetch work records for the selected week
        Map<Date, Double> workRecords = getWorkRecords(workHoursFile, employee.getId(), startDate, endDate);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);

        double totalWorkHours = 0;
        double totalLateHours = 0;
        double totalOvertimeHours = 0;
        double hourlyRate = employee.getHourlyRate();

        System.out.println("\n[  Work Hours Summary:  ]");
        while (!calendar.getTime().after(endDate)) {
            Date currentDate = calendar.getTime();
            Double hoursWorked = workRecords.getOrDefault(currentDate, null);
            String formattedDate = DATE_FORMAT_WITH_DAY.format(currentDate);
            String dayOfWeek = new SimpleDateFormat("EEEE").format(currentDate);

            if (hoursWorked != null) {
                // Calculate overtime and late hours
                double overtimeHours = Math.max(0, hoursWorked - REGULAR_WORK_HOURS);
                double lateHours = Math.max(0, REGULAR_WORK_HOURS - hoursWorked);
                totalWorkHours += hoursWorked;
                totalLateHours += lateHours;
                totalOvertimeHours += overtimeHours;

                // Display work details for the day
                System.out.printf("%s -> Present, Worked: %.0f Hrs %.0f Mins", formattedDate, Math.floor(hoursWorked), (hoursWorked % 1) * 60);
                if (overtimeHours > 0) {
                    System.out.printf(", Overtime: %.0f Hrs %.0f Mins", Math.floor(overtimeHours), (overtimeHours % 1) * 60);
                }
                if (lateHours > 0) {
                    System.out.printf(", Late: %.0f Hrs %.0f Mins", Math.floor(lateHours), (lateHours % 1) * 60);
                }
                System.out.println();
            } else {
                // Display absence or rest day
                System.out.printf("%s -> %s\n", formattedDate, isWeekend(dayOfWeek) ? "Rest day" : "Absent");
            }

            // Move to the next day
            calendar.add(Calendar.DATE, 1);
        }

        // Calculate salary and deductions
        double regularPay = totalWorkHours * hourlyRate;
        double overtimePay = totalOvertimeHours * hourlyRate * OVERTIME_RATE;
        double lateDeduction = totalLateHours * hourlyRate;
        double totalGrossSalary = regularPay + overtimePay - lateDeduction;

        // Government Deductions (Example Percentages)
        double sssDeduction = SssContribution.calculateContribution(totalGrossSalary);
        double philhealthDeduction = PhilHealth.calculateContribution(totalGrossSalary);
        double pagibigDeduction = PagIbig.calculateContribution(totalGrossSalary);
        double totalDeductions = sssDeduction + philhealthDeduction + pagibigDeduction;
        double netSalary = totalGrossSalary - totalDeductions;

        // Display totals and salary details
        System.out.println("\n[  Totals  ]");
        System.out.printf("Total Work Hours: %.0f Hrs %.0f Mins\n", Math.floor(totalWorkHours), (totalWorkHours % 1) * 60);
        System.out.printf("Total Late Hours: %.0f Hrs %.0f Mins\n", Math.floor(totalLateHours), (totalLateHours % 1) * 60);
        System.out.printf("Total Overtime Hours: %.0f Hrs %.0f Mins\n", Math.floor(totalOvertimeHours), (totalOvertimeHours % 1) * 60);

        System.out.println("\n[  Salary Calculation  ]");
        System.out.printf("Regular Pay: PHP %.2f\n", regularPay);
        System.out.printf("Overtime Pay: PHP %.2f\n", overtimePay);
        System.out.printf("Late Deduction: -PHP %.2f\n", lateDeduction);
        System.out.printf("SSS Deduction: -PHP %.2f\n", sssDeduction);
        System.out.printf("PhilHealth Deduction: -PHP %.2f\n", philhealthDeduction);
        System.out.printf("Pag-IBIG Deduction: -PHP %.2f\n", pagibigDeduction);
        System.out.printf("Total Deductions: -PHP %.2f\n", totalDeductions);
        System.out.printf("Net Salary: PHP %.2f\n", netSalary);
    }

    /**
     * Checks if the given day of the week is a weekend (Saturday or Sunday).
     *
     * @param dayOfWeek The day of the week (e.g., "Monday", "Tuesday").
     * @return True if the day is a weekend, otherwise false.
     */
    private boolean isWeekend(String dayOfWeek) {
        return dayOfWeek.equalsIgnoreCase("Saturday") || dayOfWeek.equalsIgnoreCase("Sunday");
    }

    /**
     * Fetches work records for a specific employee within a given date range.
     *
     * @param workHoursFile Path to the work hours file.
     * @param employeeId    ID of the employee.
     * @param startDate     Start of the date range.
     * @param endDate       End of the date range.
     * @return A map of dates to hours worked.
     */
    public Map<Date, Double> getWorkRecords(String workHoursFile, String employeeId, Date startDate, Date endDate) {
        // Fetch time-in and time-out records
        Map<Date, String[]> timeRecords = workHours.getWorkRecordsWithTime(workHoursFile, employeeId, startDate, endDate);
        Map<Date, Double> workRecords = new HashMap<>();

        // Calculate hours worked for each day
        for (Map.Entry<Date, String[]> entry : timeRecords.entrySet()) {
            Date workDate = entry.getKey();
            String[] timeRecord = entry.getValue();
            String timeInStr = timeRecord[0]; // Time-In
            String timeOutStr = timeRecord[1]; // Time-Out

            try {
                // Parse time-in and time-out
                Date timeIn = TIME_FORMAT.parse(timeInStr);
                Date timeOut = TIME_FORMAT.parse(timeOutStr);

                // Calculate hours worked
                double hoursWorked = calculateHours(timeIn, timeOut);
                workRecords.put(workDate, hoursWorked);
            } catch (ParseException e) {
                System.err.println("⚠ Skipping invalid time format for date: " + workDate);
            }
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

    /**
     * Calculates the total work hours from a map of work records.
     *
     * @param workRecords A map of dates to hours worked.
     * @return The total work hours as a double.
     */
    public double calculateTotalWorkHours(Map<Date, Double> workRecords) {
        return workRecords.values().stream().mapToDouble(Double::doubleValue).sum();
    }
}