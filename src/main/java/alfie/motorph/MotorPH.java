package alfie.motorph;

import java.text.SimpleDateFormat;
import java.util.*;

public class MotorPH {

    // File paths (can be moved to a configuration file)
    private static final String EMPLOYEE_DETAILS_FILE = "C:\\ZFiles\\MotorPH_Employee_Details.csv";
    private static final String WORK_HOURS_FILE = "C:\\ZFiles\\MotorPH_Attendance_Record.csv";

    // Design elements
    private static final String DESIGNER_NUM_SIGN = "##########################################################################################";
    private static final String DESIGNER_NEG_SIGN = "------------------------------------------------------------------------------------------";
    private static final String DESIGNER_WELCOME = "#####                               Welcome to MotorPH                               #####";

    // Date formatter
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MM/dd/yyyy");

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            boolean exit = false;

            // Display welcome message
            System.out.println(DESIGNER_NUM_SIGN);
            System.out.println(DESIGNER_WELCOME);
            System.out.println(DESIGNER_NUM_SIGN);

            // Prompt for employee ID
            System.out.println("Please provide your employee ID to log-in.");
            System.out.print("Enter your Employee ID: ");
            String searchValue = scanner.nextLine().trim();

            // Validate employee ID
            if (searchValue.isEmpty()) {
                System.out.println("❌ Employee ID cannot be empty.");
                return;
            }

            // Fetch employee details
            EmployeeDetails employeeDetails = new EmployeeDetails();
            Employee employee = employeeDetails.getEmployeeDetails(EMPLOYEE_DETAILS_FILE, searchValue);

            if (employee == null) {
                System.out.println("❌ No employee found with this ID: " + searchValue);
                return;
            }

            // Initialize services
            WorkHours workHours = new WorkHours();
            WorkHoursCalculator workHoursCalculator = new WorkHoursCalculator(workHours);

            // Main menu loop
            while (!exit) {
                System.out.println(DESIGNER_NEG_SIGN);
                System.out.println("[1] View Employee Details");
                System.out.println("[2] Calculate Work Hours and Salary");
                System.out.println("[3] View Weekly Time-In and Time-Out");
                System.out.println("[0] Exit");
                System.out.println();
                System.out.print("Please select next action: ");
                String choice = scanner.nextLine().trim();

                switch (choice) {
                    case "1" -> displayEmployeeDetails(employee);
                    case "2" -> calculateSalary(employee, workHoursCalculator, scanner);
                    case "3" -> viewWeeklyTimeInOut(employee, workHours, scanner);
                    case "0" -> {
                        exit = true;
                        System.out.println("Exiting the program. Goodbye!");
                    }
                    default -> System.out.println("❌ Invalid option. Please select again.");
                }
            }
        }
    }

    /**
     * Displays the details of the given employee.
     *
     * @param employee The employee whose details will be displayed.
     */
    private static void displayEmployeeDetails(Employee employee) {
        System.out.println();
        System.out.println("Good day Mr./Ms.: " + employee.getlogInAs());
        System.out.println("Employee Details");
        System.out.println("ID Number:          " + employee.getId());
        System.out.println("Name:               " + employee.getFullName());
        System.out.println("Position:           " + employee.getPosition());
        System.out.println("Date of birth:      " + employee.getDateOfBirth());
        System.out.println("Phone number:       " + employee.getPhoneNumber());
        System.out.println("Address:            " + employee.getAddress());
        System.out.println("Supervisor:         " + employee.getSupervisor());
        System.out.println("Hourly Rate:        " + employee.getHourlyRate());
    }

    /**
     * Calculates and displays the employee's salary.
     *
     * @param employee              The employee for whom to calculate the salary.
     * @param workHoursCalculator   The work hours calculator service.
     * @param scanner               The scanner for user input.
     */
    private static void calculateSalary(Employee employee, WorkHoursCalculator workHoursCalculator, Scanner scanner) {
        // Prompt for year
        System.out.print("Select year (yyyy): ");
        int year = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        // Prompt for month
        System.out.println("Select Month (01-12):");
        System.out.println("[1] January   [2] February  [3] March      [4] April");
        System.out.println("[5] May       [6] June      [7] July       [8] August");
        System.out.println("[9] September [10] October  [11] November  [12] December");
        System.out.print("Enter your choice (1-12): ");
        int monthChoice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        if (monthChoice < 1 || monthChoice > 12) {
            System.out.println("❌ Invalid month selection.");
            return;
        }

        // Calculate start and end dates for the selected month
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, monthChoice - 1); // Month is 0-based in Calendar
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        Date startDate = calendar.getTime();

        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        Date endDate = calendar.getTime();

        // Fetch work records for the selected month
        Map<Date, Double> workRecords = workHoursCalculator.getWorkRecords(WORK_HOURS_FILE, employee.getId(), startDate, endDate);

        if (workRecords.isEmpty()) {
            System.out.println("❌ No work records found for the selected month.");
            return;
        }

        // Display options for viewing salary or deductions
        boolean backToMain = false;
        while (!backToMain) {
            System.out.println("\n[  Options for " + new SimpleDateFormat("MMMM yyyy").format(startDate) + "  ]");
            System.out.println("[1] View Salary");
            System.out.println("[2] View Deductions");
            System.out.println("[3] View Net Salary");
            System.out.println("[9] Back to Main Menu");
            System.out.println("[0] Exit");
            System.out.print("Please select an option: ");
            String option = scanner.nextLine().trim();

            switch (option) {
                case "1" -> viewSalary(employee, workHoursCalculator, workRecords);
                case "2" -> viewDeductions(employee, workHoursCalculator, workRecords);
                case "3" -> viewNetSalaries(employee, workHoursCalculator, workRecords);
                case "9" -> backToMain = true;
                case "0" -> {
                    System.out.println("Exiting the program. Goodbye!");
                    System.exit(0);
                }
                default -> System.out.println("❌ Invalid option. Please select again.");
            }
        }
    }

    /**
     * Displays the salary details for the selected month.
     */
    private static void viewSalary(Employee employee, WorkHoursCalculator workHoursCalculator, Map<Date, Double> workRecords) {
    double totalWorkHours = workHoursCalculator.calculateTotalWorkHours(workRecords);
    double hourlyRate = employee.getHourlyRate();
    double grossSalary = totalWorkHours * hourlyRate;

    System.out.println("\n[  Salary Details  ]");
    System.out.printf("Total Work Hours: %.2f hours\n", totalWorkHours);
    System.out.printf("Hourly Rate:      PHP %.2f\n", hourlyRate);
    System.out.printf("Gross Salary:     PHP %.2f\n", grossSalary);
}

    /**
     * Displays the deductions for the selected month.
     */
    private static double viewDeductions(Employee employee, WorkHoursCalculator workHoursCalculator, Map<Date, Double> workRecords) {
        double totalWorkHours = workHoursCalculator.calculateTotalWorkHours(workRecords);
        double hourlyRate = employee.getHourlyRate();
        double grossSalary = totalWorkHours * hourlyRate;

        // Calculate deductions
        double pagIbigDeduction = PagIbig.calculateContribution(grossSalary);
        double philHealthDeduction = PhilHealth.calculateContribution(grossSalary);
        double sssDeduction = SssContribution.calculateContribution(grossSalary);
        double withholdingTaxDeduction = WithholdingTax.calculateTax(grossSalary);
        double totalDeductions = pagIbigDeduction + philHealthDeduction + sssDeduction + withholdingTaxDeduction;

        System.out.println("\n[  Deductions  ]");
        System.out.printf("Pag-IBIG:         PHP %.2f\n", pagIbigDeduction);
        System.out.printf("PhilHealth:       PHP %.2f\n", philHealthDeduction);
        System.out.printf("SSS:              PHP %.2f\n", sssDeduction);
        System.out.printf("Withholding Tax:  PHP %.2f\n", withholdingTaxDeduction);
        System.out.printf("Total Deductions: PHP %.2f\n", totalDeductions);
        
        return totalDeductions;
    }
    
    /**
     * Displays the Net Salaries for the selected month.
     */
    private static void viewNetSalaries(Employee employee, WorkHoursCalculator workHoursCalculator, Map<Date, Double> workRecords){
        double totalWorkHours = workHoursCalculator.calculateTotalWorkHours(workRecords);
        double hourlyRate = employee.getHourlyRate();
        double grossSalary = totalWorkHours * hourlyRate;
        
        // Calculate deductions
        double pagIbigDeduction = PagIbig.calculateContribution(grossSalary);
        double philHealthDeduction = PhilHealth.calculateContribution(grossSalary);
        double sssDeduction = SssContribution.calculateContribution(grossSalary);
        double withholdingTaxDeduction = WithholdingTax.calculateTax(grossSalary);
        double totalDeductions = pagIbigDeduction + philHealthDeduction + sssDeduction + withholdingTaxDeduction;
        
        double netSalary = grossSalary - totalDeductions;
        
        System.out.println("\n[ Net Salary  ]");
        System.out.printf("Total Deduction:    PHP %.2f\n", totalDeductions);
        System.out.printf("Gross Salary:   PHP %.2f\n", grossSalary);
        System.out.printf("Net Salaray: PHP %.2f", netSalary);
        System.out.println("");
    }

    /**
     * Displays the weekly time-in and time-out records for the employee.
     *
     * @param employee  The employee for whom to view records.
     * @param workHours The work hours service.
     * @param scanner   The scanner for user input.
     */
    private static void viewWeeklyTimeInOut(Employee employee, WorkHours workHours, Scanner scanner) {
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

        // Fetch work records for the selected week
        Map<Date, String[]> workRecords = workHours.getWorkRecordsWithTime(WORK_HOURS_FILE, employee.getId(), startDate, endDate);

        if (workRecords.isEmpty()) {
            System.out.println("❌ No work records found for the specified week.");
            return;
        }

        // Display time-in and time-out records
        System.out.println("\n[  Weekly Time-In and Time-Out Records  ]");
        System.out.printf("Week %d of %d (%s to %s)\n", week, year, new SimpleDateFormat("MM/dd/yyyy").format(startDate), new SimpleDateFormat("MM/dd/yyyy").format(endDate));

        calendar.setTime(startDate);
        while (!calendar.getTime().after(endDate)) {
            Date currentDate = calendar.getTime();
            String[] timeRecord = workRecords.getOrDefault(currentDate, null);
            String formattedDate = new SimpleDateFormat("MM/dd/yyyy - EEEE").format(currentDate);

            if (timeRecord != null) {
                System.out.printf("%s -> Time-In: %s, Time-Out: %s\n",
                    formattedDate,
                    timeRecord[0], // Time-In
                    timeRecord[1]); // Time-Out
            } else {
                System.out.printf("%s -> No record\n", formattedDate);
            }

            calendar.add(Calendar.DATE, 1); // Move to the next day
        }
    }
}