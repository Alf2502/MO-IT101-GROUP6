/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package alfie.motorph;

import java.io.*;
import java.util.*;

public class PagIbig {
    private static final double EMPLOYEE_RATE_1 = 0.01;
    private static final double EMPLOYEE_RATE_2 = 0.02;
    private static final double MAX_CONTRIBUTION = 100.00;

    public static double calculateContribution(double salary, List<PagIbig> pagIbig) {
        double contribution = (salary <= 1500) ? salary * EMPLOYEE_RATE_1 : salary * EMPLOYEE_RATE_2;
        return Math.min(contribution, MAX_CONTRIBUTION);
    }

    public static List<PagIbig> loadContributions(String filePath) {
        List<PagIbig> pagIbigList = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            br.readLine(); // Skip header
            while ((line = br.readLine()) != null) {
                pagIbigList.add(new PagIbig());
            }
        } catch (IOException e) {
            System.out.println("Error loading Pag-IBIG Contributions: " + e.getMessage());
        }
        return pagIbigList;
    }
}