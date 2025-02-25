/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package alfie.motorph;

import java.util.*;

public class SssContribution {
    private final double salaryMin;
    private final double salaryMax;
    private final double contribution;

    public SssContribution(double salaryMin, double salaryMax, double contribution) {
        this.salaryMin = salaryMin;
        this.salaryMax = salaryMax;
        this.contribution = contribution;
    }

    public static List<SssContribution> loadSssBrackets() {
        List<SssContribution> sssBrackets = new ArrayList<>();

        // SSS Brackets based on provided data
        sssBrackets.add(new SssContribution(0, 3250, 135.00));
        sssBrackets.add(new SssContribution(3250, 3750, 157.50));
        sssBrackets.add(new SssContribution(3750, 4250, 180.00));
        sssBrackets.add(new SssContribution(4250, 4750, 202.50));
        sssBrackets.add(new SssContribution(4750, 5250, 225.00));
        sssBrackets.add(new SssContribution(5250, 5750, 247.50));
        sssBrackets.add(new SssContribution(5750, 6250, 270.00));
        sssBrackets.add(new SssContribution(6250, 6750, 292.50));
        sssBrackets.add(new SssContribution(6750, 7250, 315.00));
        sssBrackets.add(new SssContribution(7250, 7750, 337.50));
        sssBrackets.add(new SssContribution(7750, 8250, 360.00));
        sssBrackets.add(new SssContribution(8250, 8750, 382.50));
        sssBrackets.add(new SssContribution(8750, 9250, 405.00));
        sssBrackets.add(new SssContribution(9250, 9750, 427.50));
        sssBrackets.add(new SssContribution(9750, 10250, 450.00));
        sssBrackets.add(new SssContribution(10250, 10750, 472.50));
        sssBrackets.add(new SssContribution(10750, 11250, 495.00));
        sssBrackets.add(new SssContribution(11250, 11750, 517.50));
        sssBrackets.add(new SssContribution(11750, 12250, 540.00));
        sssBrackets.add(new SssContribution(12250, 12750, 562.50));
        sssBrackets.add(new SssContribution(12750, 13250, 585.00));
        sssBrackets.add(new SssContribution(13250, 13750, 607.50));
        sssBrackets.add(new SssContribution(13750, 14250, 630.00));
        sssBrackets.add(new SssContribution(14250, 14750, 652.50));
        sssBrackets.add(new SssContribution(14750, 15250, 675.00));
        sssBrackets.add(new SssContribution(15250, 15750, 697.50));
        sssBrackets.add(new SssContribution(15750, 16250, 720.00));
        sssBrackets.add(new SssContribution(16250, 16750, 742.50));
        sssBrackets.add(new SssContribution(16750, 17250, 765.00));
        sssBrackets.add(new SssContribution(17250, 17750, 787.50));
        sssBrackets.add(new SssContribution(17750, 18250, 810.00));
        sssBrackets.add(new SssContribution(18250, 18750, 832.50));
        sssBrackets.add(new SssContribution(18750, 19250, 855.00));
        sssBrackets.add(new SssContribution(19250, 19750, 877.50));
        sssBrackets.add(new SssContribution(19750, 20250, 900.00));
        sssBrackets.add(new SssContribution(20250, 20750, 922.50));
        sssBrackets.add(new SssContribution(20750, 21250, 945.00));
        sssBrackets.add(new SssContribution(21250, 21750, 967.50));
        sssBrackets.add(new SssContribution(21750, 22250, 990.00));
        sssBrackets.add(new SssContribution(22250, 22750, 1012.50));
        sssBrackets.add(new SssContribution(22750, 23250, 1035.00));
        sssBrackets.add(new SssContribution(23250, 23750, 1057.50));
        sssBrackets.add(new SssContribution(23750, 24250, 1080.00));
        sssBrackets.add(new SssContribution(24250, 24750, 1102.50));
        sssBrackets.add(new SssContribution(24750, 10000000, 1125.00)); // Max contribution

        return sssBrackets;
    }

    public static double calculateContribution(double salary, List<SssContribution> sssBrackets) {
        for (SssContribution bracket : sssBrackets) {
            if (salary >= bracket.salaryMin && salary <= bracket.salaryMax) {
                return bracket.contribution;
            }
        }
        return 0; // Default if no bracket matches
    }
}