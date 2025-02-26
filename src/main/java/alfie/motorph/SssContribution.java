package alfie.motorph;

public final class SssContribution {

    // Private constructor to prevent instantiation
    private SssContribution() {
        throw new UnsupportedOperationException("Utility class should not be instantiated.");
    }

    /**
     * Calculates the SSS contribution based on the employee's salary.
     *
     * @param salary The employee's monthly salary.
     * @return The SSS contribution amount.
     */
    public static double calculateContribution(double salary) {
        if (salary < 0) {
            throw new IllegalArgumentException("Salary cannot be negative.");
        }

        // Example SSS contribution calculation logic
        if (salary <= 3250) {
            return 135.00;
        } else if (salary <= 3750) {
            return 157.50;
        } else if (salary <= 4250) {
            return 180.00;
        }
        // Add more brackets as needed...
        return 0; // Default if no bracket matches
    }
}