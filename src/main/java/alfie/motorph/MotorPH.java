package alfie.motorph;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class MotorPH {
    
    public static void main(String[] args) {
        String employeeDetailsFile = "C:\\ZFiles\\MotorPH_Employee_Details.csv";
        String workHoursFile = "C:\\ZFiles\\MotorPH_Attendance_Record.csv";
        String sssFilePath = "C:\\ZFiles\\SSS_Contribution_Schedule.csv";
        String pagIbigFilePath = "C:\\ZFiles\\PagIbig_Contribution.csv";
        String taxFilePath = "C:\\ZFiles\\Withholding_Tax.csv";

        String designerNumSign = "##########################################################################################";
        String designerNegSign = "------------------------------------------------------------------------------------------";
        String designerWelcome =  "#####                               Welcome to MotorPH                               #####";
       
        try (Scanner scanner = new Scanner(System.in)) {
            boolean exit = false;
            System.out.println(designerNumSign);
            System.out.println(designerWelcome);
            System.out.println(designerNumSign);
            System.out.println("Please provide your employee ID to log-in.");
            System.out.print("Enter your Employee ID: ");
            String searchValue = scanner.nextLine();  

            EmployeeDetails employeeDetails = new EmployeeDetails();
            Employee employee = employeeDetails.getEmployeeDetails(employeeDetailsFile, searchValue);
            
            if (employee == null) {
                System.out.println("No employee found with this ID: " + searchValue);
                return;
            }

            WorkHours workHours = new WorkHours(); 
            List<SssContribution> sssBrackets = SssContribution.loadSssBrackets();
            List<PagIbig> pagIbigContributions = PagIbig.loadContributions(pagIbigFilePath);
            List<WithholdingTax> taxBrackets = WithholdingTax.loadTaxBrackets();

            while (!exit) {
                System.out.println(designerNegSign);
                System.out.println("[1] View Employee Details");
                System.out.println("[2] Calculate Work Hours and Salary");
                System.out.println("[0] Exit");
                System.out.println("");
                System.out.print("Please select next action: ");
                String choice = scanner.nextLine();
                
                switch (choice) {
                    case "1" -> displayEmployeeDetails(employee);
                    case "2" -> calculateWorkHours(workHours, workHoursFile, employee, scanner, sssBrackets, pagIbigContributions, taxBrackets);
                    case "0" -> { 
                        exit = true;
                        System.out.println("Exiting the program. Goodbye!");
                    }
                    default -> System.out.println("Invalid option. Please select again.");
                }
            }
        }
    }
    
    private static void displayEmployeeDetails(Employee employee) {
        System.out.println(" ");
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

    private static void calculateWorkHours(WorkHours workHours, String workHoursFile, Employee employee, Scanner scanner, 
                                            List<SssContribution> sssContributions, List<PagIbig> pagIbigContributions, List<WithholdingTax> taxBrackets) { 
        
        System.out.print("Enter start date (MM/dd/yyyy): ");
        String startDateStr = scanner.nextLine().trim();
        System.out.print("Enter end date (MM/dd/yyyy): ");
        String endDateStr = scanner.nextLine().trim();

        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        sdf.setLenient(false);
        DecimalFormat df = new DecimalFormat("#,##0.00");

        try {
            Date startDate = sdf.parse(startDateStr);
            Date endDate = sdf.parse(endDateStr);

            Map<Date, Double> workRecords = workHours.getWorkRecords(workHoursFile, employee.getId(), startDate, endDate);
            double totalHours = 0;
            double totalRegularHours = 0;
            double totalOvertimeHours = 0;
            double overtimeThreshold = 8.0;
            double hourlyRate = employee.getHourlyRate();
            double overtimeRate = hourlyRate * 1.5;

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(startDate);
            
            System.out.println(" ");
            System.out.println("[  Work Hours Summary:  ]");
            while (!calendar.getTime().after(endDate)) {
                Date currentDate = calendar.getTime();
                Double hoursWorked = workRecords.getOrDefault(currentDate, null);
                
                if (hoursWorked != null) {
                    totalHours += hoursWorked;
                    double regularHours = Math.min(hoursWorked, overtimeThreshold);
                    double overtimeHours = Math.max(0, hoursWorked - overtimeThreshold);
                    totalRegularHours += regularHours;
                    totalOvertimeHours += overtimeHours;
                    System.out.printf("%s -> %s hours\n", sdf.format(currentDate), df.format(hoursWorked));
                } else {
                    System.out.printf("%s -> N/A hours\n", sdf.format(currentDate));
                }
                calendar.add(Calendar.DATE, 1);
            }
            System.out.printf("[  Total Hours Worked: %s hours  ]\n", df.format(totalHours));
            
            System.out.println("------------------------------------------------------------------------------------------");
            System.out.print("Would you like to calculate your salary? (yes/no): ");
            String response = scanner.nextLine().trim().toLowerCase();
            
            if (response.equals("yes")) {
                List<SssContribution> sssBrackets = SssContribution.loadSssBrackets();
                
                double regularPay = totalRegularHours * hourlyRate;
                double overtimePay = totalOvertimeHours * overtimeRate;
                double totalSalary = regularPay + overtimePay;
                double grossSalary = regularPay + overtimePay; // Define this before using it!
                
                double philHealthContribution = PhilHealth.calculateContribution(totalSalary);
                double sssContribution = SssContribution.calculateContribution(grossSalary, sssBrackets);
                double pagIbigContribution = PagIbig.calculateContribution(totalSalary, pagIbigContributions);
                double tax = WithholdingTax.calculateTax(totalSalary, taxBrackets);
                
                double totalDeductions = philHealthContribution + sssContribution + pagIbigContribution + tax;
                double netSalary = totalSalary - totalDeductions;
                
                System.out.println("");
                System.out.println("[  Salary Breakdown  ]");
                System.out.printf("Regular Hours: %s x %s = %s\n", df.format(totalRegularHours), df.format(hourlyRate), df.format(regularPay));
                System.out.printf("Overtime Hours: %s x %s = %s\n", df.format(totalOvertimeHours), df.format(overtimeRate), df.format(overtimePay));
                System.out.printf("Gross Salary: %s\n", df.format(totalSalary));
                System.out.printf("Total Deductions: %s\n", df.format(totalDeductions));
                System.out.printf("[ Net Salary: %s ]\n", df.format(netSalary));
                System.out.println(" ");

                System.out.print("Would you like to view the breakdown of deductions? (yes/no): ");
                String deductionResponse = scanner.nextLine().trim().toLowerCase();
                if (deductionResponse.equals("yes")) {
                    System.out.println("");
                    System.out.println("[  Deductions breakdown  ]");
                    System.out.printf("PhilHealth Contribution: %s\n", df.format(philHealthContribution));
                    System.out.printf("SSS Contribution: %s\n", df.format(sssContribution));
                    System.out.printf("Pag-IBIG Contribution: %s\n", df.format(pagIbigContribution));
                    System.out.printf("Withholding Tax: %s\n", df.format(tax));
                    System.out.printf("[ Total Deductions: %s ]\n", df.format(totalDeductions));
                    System.out.println("  ");
                }
            }
        } catch (ParseException e) {
            System.out.println("\u274C Invalid date format. Please use MM/dd/yyyy.");
        }
    }
}