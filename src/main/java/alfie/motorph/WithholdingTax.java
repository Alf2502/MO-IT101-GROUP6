package alfie.motorph;

public final class WithholdingTax {

    // Private constructor to prevent instantiation
    private WithholdingTax() {
        throw new UnsupportedOperationException("Utility class should not be instantiated.");
    }

    /**
     * Calculates the withholding tax based on the employee's monthly salary.
     *
     * @param monthlySalary The employee's monthly salary.
     * @return The withholding tax amount.
     */
    public static double calculateTax(double monthlySalary) {
        if (monthlySalary < 0) {
            throw new IllegalArgumentException("Salary cannot be negative.");
        }

        // Example withholding tax calculation logic
        if (monthlySalary <= 20832) {
            return 0;
        } else if (monthlySalary <= 33332) {
            return 0 + ((monthlySalary - 20833) * 0.20);
        } else if (monthlySalary <= 66666) {
            return 2500 + ((monthlySalary - 33333) * 0.25);
        }
        // Add more brackets as needed...
        return 0; // Default if no bracket matches
    }
}