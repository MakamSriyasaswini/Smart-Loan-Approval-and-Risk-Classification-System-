package loan;

import java.util.ArrayList;
import java.util.List;

public class CreditAssessment {

    private static final double MIN_MONTHLY_INCOME = 25000.0;
    private static final int MIN_CREDIT_SCORE = 650;

    private static final double MAX_DTI = 0.40;
    private static final double LOW_RISK_DTI = 0.30;

    private static final int LOW_RISK_CREDIT_SCORE = 750;

    private static final int LOAN_INCOME_MULTIPLIER = 12;
    private static final int LOAN_TERM_MONTHS = 60;

    public String assessLoan(Customer customer,
                             LoanApplication application,
                             int creditScore)
            throws InvalidLoanException {

        validateInput(customer, application, creditScore);

        List<String> reasons = new ArrayList<>();

        if (customer.getAge() < 21) {
            reasons.add("Customer must be at least 21 years old");
        }

        if (!isValidGovernmentId(customer.getGovernmentId())) {
            reasons.add("Government ID is invalid");
        }

        if (customer.getMonthlyIncome() < MIN_MONTHLY_INCOME) {
            reasons.add("Monthly income is below the minimum threshold");
        }

        if (creditScore < MIN_CREDIT_SCORE) {
            reasons.add("Credit score is below the minimum requirement");
        }

        double maximumLoanAmount =
                calculateMaximumLoanAmount(customer);

        double requestedLoanAmount =
                application.getRequestedLoanAmount();

        if (requestedLoanAmount > maximumLoanAmount) {
            reasons.add(
                    "Requested loan amount exceeds maximum permissible amount");
        }

        double newLoanMonthlyObligation =
                requestedLoanAmount / LOAN_TERM_MONTHS;

        double totalMonthlyObligation =
                customer.getExistingLoanObligations()
                        + newLoanMonthlyObligation;

        double dti =
                totalMonthlyObligation
                        / customer.getMonthlyIncome();

        if (dti > MAX_DTI) {
            reasons.add(
                    "Debt-to-income ratio exceeds maximum permissible DTI");
        }

        if (!reasons.isEmpty()) {
            return buildResult(
                    "High Risk / Rejected",
                    maximumLoanAmount,
                    dti,
                    reasons);
        }

        if (creditScore >= LOW_RISK_CREDIT_SCORE
                && dti <= LOW_RISK_DTI) {

            return buildResult(
                    "Approved - Low Risk",
                    maximumLoanAmount,
                    dti,
                    reasons);
        }

        return buildResult(
                "Approved - Medium Risk",
                maximumLoanAmount,
                dti,
                reasons);
    }

    public double calculateMaximumLoanAmount(Customer customer)
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

        double incomeBasedLimit =
                customer.getMonthlyIncome()
                        * LOAN_INCOME_MULTIPLIER;

        double dtiBasedLimit =
                (customer.getMonthlyIncome() * MAX_DTI
                        - customer.getExistingLoanObligations())
                        * LOAN_TERM_MONTHS;

        if (dtiBasedLimit < 0) {
            dtiBasedLimit = 0;
        }

        return Math.min(
                incomeBasedLimit,
                dtiBasedLimit);
    }

    public double calculateDTI(Customer customer,
                               LoanApplication application)
            throws InvalidLoanException {

        validateInput(customer, application, MIN_CREDIT_SCORE);

        double newLoanMonthlyObligation =
                application.getRequestedLoanAmount()
                        / LOAN_TERM_MONTHS;

        double totalMonthlyObligation =
                customer.getExistingLoanObligations()
                        + newLoanMonthlyObligation;

        return totalMonthlyObligation
                / customer.getMonthlyIncome();
    }

    private boolean isValidGovernmentId(String governmentId) {

        return governmentId != null
                && governmentId.matches("GOV[0-9]{3,10}");
    }

    private String buildResult(String status,
                               double maximumLoanAmount,
                               double dti,
                               List<String> reasons) {

        StringBuilder result = new StringBuilder();

        result.append(status);

        result.append(" | Maximum Permissible Loan: ₹");
        result.append(
                String.format("%.2f", maximumLoanAmount));

        result.append(" | DTI: ");
        result.append(
                String.format("%.2f%%", dti * 100));

        if (!reasons.isEmpty()) {
            result.append(" | Reasons: ");
            result.append(
                    String.join("; ", reasons));
        }

        return result.toString();
    }

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

        if (customer.getCustomerId() == null
                || customer.getCustomerId().trim().isEmpty()) {

            throw new InvalidLoanException(
                    "Customer ID cannot be empty");
        }

        if (customer.getName() == null
                || customer.getName().trim().isEmpty()) {

            throw new InvalidLoanException(
                    "Customer name cannot be empty");
        }

        if (customer.getAge() < 0) {
            throw new InvalidLoanException(
                    "Age cannot be negative");
        }

        if (customer.getGovernmentId() == null
                || customer.getGovernmentId().trim().isEmpty()) {

            throw new InvalidLoanException(
                    "Government ID cannot be empty");
        }

        if (customer.getMonthlyIncome() < 0) {
            throw new InvalidLoanException(
                    "Monthly income cannot be negative");
        }

        if (customer.getExistingLoanObligations() < 0) {
            throw new InvalidLoanException(
                    "Existing loan obligations cannot be negative");
        }

        if (application.getApplicationId() == null
                || application.getApplicationId().trim().isEmpty()) {

            throw new InvalidLoanException(
                    "Application ID cannot be empty");
        }

        if (application.getRequestedLoanAmount() <= 0) {
            throw new InvalidLoanException(
                    "Requested loan amount must be greater than zero");
        }

        if (creditScore < 0 || creditScore > 850) {
            throw new InvalidLoanException(
                    "Credit score must be between 0 and 850");
        }
    }
}
