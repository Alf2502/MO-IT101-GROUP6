/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package alfie.motorph;

/**
 * A utility class for calculating PhilHealth contributions based on salary.
 * The contribution is split equally between the employee and employer.
 */
public final class PhilHealth {

    private static final double DEFAULT_RATE = 0.03; // Default PhilHealth rate (3%)
    private static final double EMPLOYEE_SHARE = 0.5; // Employee's share of the contribution (50%)

    // Private constructor to prevent instantiation
    public PhilHealth() {
        throw new UnsupportedOperationException("Utility class should not be instantiated.");
    }

    /**
     * Calculates the employee's PhilHealth contribution based on their monthly salary.
     *
     * @param salary The employee's monthly salary.
     * @return The employee's PhilHealth contribution.
     * @throws IllegalArgumentException If the salary is negative.
     */
    public static double calculateContribution(double salary) {
    if (salary < 0) {
        throw new IllegalArgumentException("Salary cannot be negative.");
    }

    double totalContribution = salary * DEFAULT_RATE;
    return totalContribution * EMPLOYEE_SHARE; // Employee's share
}

    /**
     * Calculates the total PhilHealth contribution (employee + employer).
     *
     * @param salary The employee's monthly salary.
     * @return The total PhilHealth contribution.
     * @throws IllegalArgumentException If the salary is negative.
     */
    public static double calculateTotalContribution(double salary) {
        if (salary < 0) {
            throw new IllegalArgumentException("Salary cannot be negative.");
        }

        return salary * DEFAULT_RATE;
    }
}