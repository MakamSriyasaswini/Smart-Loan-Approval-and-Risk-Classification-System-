package loan;

public class LoanApplication {

    private String applicationId;
    private double requestedLoanAmount;

    public LoanApplication(String applicationId,
                           double requestedLoanAmount) {

        this.applicationId = applicationId;
        this.requestedLoanAmount = requestedLoanAmount;
    }

    public String getApplicationId() {
        return applicationId;
    }

    public double getRequestedLoanAmount() {
        return requestedLoanAmount;
    }
}
