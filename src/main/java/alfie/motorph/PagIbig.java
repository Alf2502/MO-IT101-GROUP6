package alfie.motorph;

/**
 * A utility class for calculating Pag-IBIG contributions based on salary.
 */
public final class PagIbig {

    private static final double EMPLOYEE_RATE_1 = 0.01; // Rate for salaries <= 1500
    private static final double EMPLOYEE_RATE_2 = 0.02; // Rate for salaries > 1500
    private static final double MAX_CONTRIBUTION = 100.00; // Maximum contribution limit

    // Private constructor to prevent instantiation
    public PagIbig() {
        throw new UnsupportedOperationException("Utility class should not be instantiated.");
    }

    /**
     * Calculates the Pag-IBIG contribution based on the employee's salary.
     *
     * @param salary The employee's monthly salary.
     * @return The calculated Pag-IBIG contribution.
     */
    public static double calculateContribution(double salary) {
    if (salary < 0) {
        throw new IllegalArgumentException("Salary cannot be negative.");
    }

    double rate = (salary <= 1500) ? EMPLOYEE_RATE_1 : EMPLOYEE_RATE_2;
    double contribution = salary * rate;
    return Math.min(contribution, MAX_CONTRIBUTION);
}


}