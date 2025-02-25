/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package alfie.motorph;

import java.util.*;

public class WithholdingTax {
    private final double salaryMin;
    private final double salaryMax;
    private final double baseTax;
    private final double excessRate;

    public WithholdingTax(double salaryMin, double salaryMax, double baseTax, double excessRate) {
        this.salaryMin = salaryMin;
        this.salaryMax = salaryMax;
        this.baseTax = baseTax;
        this.excessRate = excessRate;
    }

    public static List<WithholdingTax> loadTaxBrackets() {
        List<WithholdingTax> taxBrackets = new ArrayList<>();

        // ✅ Fixed: Removed extra parameter & corrected class name
        taxBrackets.add(new WithholdingTax(0, 20832, 0, 0));  // No tax for 20,832 and below
        taxBrackets.add(new WithholdingTax(20833, 33332, 0, 0.20));  // 20% in excess of 20833
        taxBrackets.add(new WithholdingTax(33333, 66666, 2500, 0.25));  // 2500 + 25% excess of 33333
        taxBrackets.add(new WithholdingTax(66667, 166666, 10833, 0.30));  // 10833 + 30% excess of 66667
        taxBrackets.add(new WithholdingTax(166667, 666666, 40833.33, 0.32));  // 40833.33 + 32% excess of 166667
        taxBrackets.add(new WithholdingTax(666667, Double.MAX_VALUE, 200833.33, 0.35));  // 200833.33 + 35% excess of 666667

        return taxBrackets;
    }

    public static double calculateTax(double monthlySalary, List<WithholdingTax> taxBrackets) {
        for (WithholdingTax bracket : taxBrackets) {
            if (monthlySalary >= bracket.salaryMin && monthlySalary <= bracket.salaryMax) {
                double tax = bracket.baseTax + ((monthlySalary - bracket.salaryMin) * bracket.excessRate);
                //System.out.println("Applied Tax Bracket: " + bracket.salaryMin + " - " + bracket.salaryMax);
                //System.out.println("Tax Deducted: " + tax);
                return tax;
            }
        }
        System.out.println("⚠ No tax bracket found for salary: " + monthlySalary);
        return 0; // No tax applied
    }
}