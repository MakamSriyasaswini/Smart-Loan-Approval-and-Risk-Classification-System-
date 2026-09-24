package loan;

import java.util.ArrayList;
import java.util.List;

public class CreditAssessment {

    // Loan eligibility constants
    private static final int MIN_AGE = 21;
    private static final double MIN_MONTHLY_INCOME = 25000.0;
    private static final int MIN_CREDIT_SCORE = 650;
    private static final int MAX_CREDIT_SCORE = 850;

    // Risk and DTI constants
    private static final double MAX_DTI = 0.40;
    private static final double LOW_RISK_DTI = 0.30;
    private static final int LOW_RISK_CREDIT_SCORE = 750;

    // Loan calculation constants
    private static final int LOAN_INCOME_MULTIPLIER = 12;
    private static final int LOAN_TERM_MONTHS = 60;

    public String assessLoan(Customer customer,
                             LoanApplication application,
                             int creditScore)
            throws InvalidLoanException {

        validateInput(customer, application, creditScore);

        List<String> rejectionReasons = new ArrayList<>();

        // Basic eligibility checks
        if (customer.getAge() < MIN_AGE) {
            rejectionReasons.add(
                    "Customer must be at least 21 years old");
        }

        if (!isValidGovernmentId(customer.getGovernmentId())) {
            rejectionReasons.add(
                    "Government ID is invalid");
        }

        if (customer.getMonthlyIncome() < MIN_MONTHLY_INCOME) {
            rejectionReasons.add(
                    "Monthly income is below the minimum threshold");
        }

        if (creditScore < MIN_CREDIT_SCORE) {
            rejectionReasons.add(
                    "Credit score is below the minimum requirement");
        }

        // Calculate maximum permissible loan
        double maximumLoanAmount =
                calculateMaximumLoanAmount(customer);

        double requestedLoanAmount =
                application.getRequestedLoanAmount();

        if (requestedLoanAmount > maximumLoanAmount) {
            rejectionReasons.add(
                    "Requested loan amount exceeds maximum permissible amount");
        }

        // Calculate DTI
        double dti = calculateDTI(customer, application);

        if (dti > MAX_DTI) {
            rejectionReasons.add(
                    "Debt-to-income ratio exceeds maximum permissible DTI");
        }

        // Reject if any eligibility condition fails
        if (!rejectionReasons.isEmpty()) {
            return buildResult(
                    "High Risk / Rejected",
                    maximumLoanAmount,
                    dti,
                    rejectionReasons);
        }

        // Determine risk level
        if (creditScore >= LOW_RISK_CREDIT_SCORE
                && dti <= LOW_RISK_DTI) {

            return buildResult(
                    "Approved - Low Risk",
                    maximumLoanAmount,
                    dti,
                    rejectionReasons);
        }

        return buildResult(
                "Approved - Medium Risk",
                maximumLoanAmount,
                dti,
                rejectionReasons);
    }

    /**
     * Calculates the maximum loan amount based on:
     * 1. Annual-income-based limit
     * 2. Maximum permissible DTI
     */
    public double calculateMaximumLoanAmount(Customer customer)
            throws InvalidLoanException {

        validateCustomerFinancialData(customer);

        double monthlyIncome =
                customer.getMonthlyIncome();

        double existingObligations =
                customer.getExistingLoanObligations();

        // Maximum based on income
        double incomeBasedLimit =
                monthlyIncome * LOAN_INCOME_MULTIPLIER;

        // Maximum based on DTI
        double availableMonthlyCapacity =
                (monthlyIncome * MAX_DTI)
                        - existingObligations;

        double dtiBasedLimit =
                Math.max(0, availableMonthlyCapacity)
                        * LOAN_TERM_MONTHS;

        return Math.min(
                incomeBasedLimit,
                dtiBasedLimit);
    }

    /**
     * Calculates the debt-to-income ratio for the
     * customer's existing obligations plus the new loan.
     */
    public double calculateDTI(Customer customer,
                               LoanApplication application)
            throws InvalidLoanException {

        validateInput(
                customer,
                application,
                MIN_CREDIT_SCORE);

        double newLoanMonthlyObligation =
                application.getRequestedLoanAmount()
                        / LOAN_TERM_MONTHS;

        double totalMonthlyObligation =
                customer.getExistingLoanObligations()
                        + newLoanMonthlyObligation;

        return totalMonthlyObligation
                / customer.getMonthlyIncome();
    }

    /**
     * Valid government ID format:
     * GOV followed by 3 to 10 digits.
     */
    private boolean isValidGovernmentId(String governmentId) {

        return governmentId != null
                && governmentId.matches("GOV[0-9]{3,10}");
    }

    /**
     * Creates the final assessment result.
     */
    private String buildResult(String status,
                               double maximumLoanAmount,
                               double dti,
                               List<String> reasons) {

        StringBuilder result = new StringBuilder();

        result.append(status);

        result.append(" | Maximum Permissible Loan: ₹")
                .append(String.format(
                        "%.2f",
                        maximumLoanAmount));

        result.append(" | DTI: ")
                .append(String.format(
                        "%.2f%%",
                        dti * 100));

        if (!reasons.isEmpty()) {
            result.append(" | Reasons: ")
                    .append(String.join(
                            "; ",
                            reasons));
        }

        return result.toString();
    }

    /**
     * Validates common customer and application input.
     */
    private void validateInput(Customer customer,
                               LoanApplication application,
                               int creditScore)
            throws InvalidLoanException {

        if (customer == null) {
            throw new InvalidLoanException(
                    "Customer details cannot be null");
        }

        if (application == null) {
            throw new InvalidLoanException(
                    "Loan application cannot be null");
        }

        validateCustomerDetails(customer);
        validateApplicationDetails(application);
        validateCreditScore(creditScore);
    }

    /**
     * Validates customer information.
     */
    private void validateCustomerDetails(Customer customer)
            throws InvalidLoanException {

        if (isBlank(customer.getCustomerId())) {
            throw new InvalidLoanException(
                    "Customer ID cannot be empty");
        }

        if (isBlank(customer.getName())) {
            throw new InvalidLoanException(
                    "Customer name cannot be empty");
        }

        if (customer.getAge() < 0) {
            throw new InvalidLoanException(
                    "Age cannot be negative");
        }

        if (isBlank(customer.getGovernmentId())) {
            throw new InvalidLoanException(
                    "Government ID cannot be empty");
        }

        validateCustomerFinancialData(customer);
    }

    /**
     * Validates income and existing loan obligations.
     */
    private void validateCustomerFinancialData(Customer customer)
            throws InvalidLoanException {

        if (customer == null) {
            throw new InvalidLoanException(
                    "Customer details cannot be null");
        }

        if (customer.getMonthlyIncome() < 0) {
            throw new InvalidLoanException(
                    "Monthly income cannot be negative");
        }

        if (customer.getExistingLoanObligations() < 0) {
            throw new InvalidLoanException(
                    "Existing loan obligations cannot be negative");
        }

        // DTI calculation requires a positive income.
        if (customer.getMonthlyIncome() == 0) {
            throw new InvalidLoanException(
                    "Monthly income must be greater than zero");
        }
    }

    /**
     * Validates loan application information.
     */
    private void validateApplicationDetails(
            LoanApplication application)
            throws InvalidLoanException {

        if (isBlank(application.getApplicationId())) {
            throw new InvalidLoanException(
                    "Application ID cannot be empty");
        }

        if (application.getRequestedLoanAmount() <= 0) {
            throw new InvalidLoanException(
                    "Requested loan amount must be greater than zero");
        }
    }

    /**
     * Validates credit score range.
     */
    private void validateCreditScore(int creditScore)
            throws InvalidLoanException {

        if (creditScore < 0
                || creditScore > MAX_CREDIT_SCORE) {

            throw new InvalidLoanException(
                    "Credit score must be between 0 and 850");
        }
    }

    /**
     * Checks whether a string is null, empty,
     * or contains only whitespace.
     */
    private boolean isBlank(String value) {

        return value == null
                || value.trim().isEmpty();
    }
}
