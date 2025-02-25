/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package alfie.motorph;

/**
 *
 * @author Alfie
 */
public class PhilHealth {
     private static final double RATE = 0.03;

    public static double calculateContribution(double salary) {
        return (salary * RATE) / 2; // Split between employee and employer
    }
    
}
