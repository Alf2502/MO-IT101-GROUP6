package alfie.motorph;

public class Employee {

    private final String id;
    private final String lastName;
    private final String firstName;
    private final String position;
    private final String dateOfBirth;
    private final String phoneNumber;
    private final String address;
    private final String supervisor;
    private final double hourlyRate;

    public Employee(
        String id,
        String lastName,
        String firstName,
        String position,
        String dateOfBirth,
        String phoneNumber,
        String address,
        String supervisor,
        double hourlyRate
    ) {
        this.id = id;
        this.lastName = lastName;
        this.firstName = firstName;
        this.position = position;
        this.dateOfBirth = dateOfBirth;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.supervisor = supervisor;
        this.hourlyRate = hourlyRate;
    }

    // Getters for all fields
    public String getId() {
        return id;
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

    public String getlogInAs() {
        return firstName; // Or any other logic for login name
    }
}