package alfie.motorph;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public final class SssContribution {
    private static final String SSS_CONTRIBUTION = "C:\\ZFiles\\SSS_Contribution_Schedule.csv";
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
        try (BufferedReader brSSS = new BufferedReader(new FileReader(SSS_CONTRIBUTION))){
            String line;
            while ((line = brSSS.readLine()) != null) {
                String[] parts  = line.split(",");
                
                if (parts.length <3 ) {
                    continue;
                }
                
                try {
                    double minSalary = Double.parseDouble(parts[0].trim());
                    double maxSalary = Double.parseDouble(parts[1].trim());
                    double contribution = Double.parseDouble(parts[2].trim());
                    
                    if (salary >= minSalary && salary <= maxSalary) {
                        return contribution;

                    }
                } catch (NumberFormatException e) {
                    System.err.println("Skipping invalid entry: " + line);
                }
                
            }
        
            
        } catch (IOException e) {
            System.err.println("Error reading SSS contribution file: " + e.getMessage());
        }
         return 0.0;
    }
}