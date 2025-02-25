package alfie.motorph;

public class Employee {
    private final String id;
    private final String firstName;
    private final String lastName;
    private final String position;
    private final String dateOfBirth;
    private final String phoneNumber;
    private final String address;
    private final String sssNumber;
    private final String philhealthNumber;
    private final String tinNumber;
    private final String pagIbigNumber;
    private final String employmentStatus;
    private final String supervisor;
    private final double hourlyRate;
    
    // Constructor
    public Employee(String id, String firstName, String lastName, String position, String dateOfBirth, 
                    String phoneNumber, String address, String sssNumber, String philhealthNumber, 
                    String tinNumber, String pagIbigNumber, String employmentStatus, String supervisor, 
                    double hourlyRate) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.position = position;
        this.dateOfBirth = dateOfBirth;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.sssNumber = sssNumber;
        this.philhealthNumber = philhealthNumber;
        this.tinNumber = tinNumber;
        this.pagIbigNumber = pagIbigNumber;
        this.employmentStatus = employmentStatus;
        this.supervisor = supervisor;
        this.hourlyRate = hourlyRate;
    }
    
    // Getter methods for Employee details
    public String getId() {
        return id;
    }
    
    public String getlogInAs() {
        return firstName;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public String getPosition() {
        return position;
    }
    
    public String getDateOfBirth() {
        return dateOfBirth;
    }
    
    public String getPhoneNumber() {
        return phoneNumber;
    }
    
    public String getAddress() {
        return address;
    }
    
    public String getSupervisor() {
        return supervisor;
    }
    
    
    
    public double getHourlyRate() {
        return hourlyRate;
    }
    
}
