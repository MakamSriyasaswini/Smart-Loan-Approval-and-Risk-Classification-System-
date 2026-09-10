package loan;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        CreditAssessment assessment = new CreditAssessment();

        try {

            System.out.print("Enter number of customers: ");
            int numberOfCustomers = scanner.nextInt();
            scanner.nextLine();

            if (numberOfCustomers <= 0) {
                System.out.println(
                        "Number of customers must be greater than zero.");
                return;
            }

            for (int i = 1; i <= numberOfCustomers; i++) {

                System.out.println("\n========== Customer " + i + " ==========");

                System.out.print("Customer ID: ");
                String customerId = scanner.nextLine();

                System.out.print("Name: ");
                String name = scanner.nextLine();

                System.out.print("Age: ");
                int age = scanner.nextInt();
                scanner.nextLine();

                System.out.print("Government ID (GOV123): ");
                String governmentId = scanner.nextLine();

                System.out.print("Monthly Income: ");
                double monthlyIncome = scanner.nextDouble();

                System.out.print("Existing Monthly Loan Obligations: ");
                double existingObligations = scanner.nextDouble();

                System.out.print("Credit Score: ");
                int creditScore = scanner.nextInt();
                scanner.nextLine();

                System.out.print("Application ID: ");
                String applicationId = scanner.nextLine();

                System.out.print("Requested Loan Amount: ");
                double requestedLoanAmount = scanner.nextDouble();
                scanner.nextLine();

                Customer customer = new Customer(
                        customerId,
                        name,
                        age,
                        governmentId,
                        monthlyIncome,
                        existingObligations
                );

                LoanApplication application =
                        new LoanApplication(
                                applicationId,
                                requestedLoanAmount
                        );

                try {

                    double maximumLoan =
                            assessment.calculateMaximumLoanAmount(customer);

                    System.out.printf(
                            "Maximum Permissible Loan: ₹%.2f%n",
                            maximumLoan);

                    double dti =
                            assessment.calculateDTI(
                                    customer,
                                    application);

                    System.out.printf(
                            "Calculated DTI: %.2f%%%n",
                            dti * 100);

                    String result =
                            assessment.assessLoan(
                                    customer,
                                    application,
                                    creditScore);

                    System.out.println(
                            "Loan Decision: " + result);

                } catch (InvalidLoanException e) {

                    System.out.println(
                            "Input Error: " + e.getMessage());
                }
            }

        } catch (InputMismatchException e) {

            System.out.println(
                    "Input Error: Please enter the correct data type.");

        } finally {

            scanner.close();
        }
    }
}
