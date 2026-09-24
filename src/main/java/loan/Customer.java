package loan;

/**
 * Represents a customer applying for a loan.
 */
public class Customer {

    private final String customerId;
    private final String name;
    private final int age;
    private final String governmentId;
    private final double monthlyIncome;
    private final double existingLoanObligations;

    /**
     * Creates a new customer.
     *
     * @param customerId unique customer identifier
     * @param name customer name
     * @param age customer age
     * @param governmentId government-issued ID
     * @param monthlyIncome customer's monthly income
     * @param existingLoanObligations existing monthly loan obligations
     */
    public Customer(String customerId,
                    String name,
                    int age,
                    String governmentId,
                    double monthlyIncome,
                    double existingLoanObligations) {

        if (customerId == null || customerId.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Customer ID cannot be empty");
        }

        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Customer name cannot be empty");
        }

        if (age < 0) {
            throw new IllegalArgumentException(
                    "Age cannot be negative");
        }

        if (governmentId == null
                || governmentId.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Government ID cannot be empty");
        }

        if (monthlyIncome < 0) {
            throw new IllegalArgumentException(
                    "Monthly income cannot be negative");
        }

        if (existingLoanObligations < 0) {
            throw new IllegalArgumentException(
                    "Existing loan obligations cannot be negative");
        }

        this.customerId = customerId;
        this.name = name;
        this.age = age;
        this.governmentId = governmentId;
        this.monthlyIncome = monthlyIncome;
        this.existingLoanObligations =
                existingLoanObligations;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public String getGovernmentId() {
        return governmentId;
    }

    public double getMonthlyIncome() {
        return monthlyIncome;
    }

    public double getExistingLoanObligations() {
        return existingLoanObligations;
    }

    /**
     * Returns customer information in readable form.
     */
    @Override
    public String toString() {

        return "Customer{" +
                "customerId='" + customerId + '\'' +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", governmentId='" + governmentId + '\'' +
                ", monthlyIncome=" + monthlyIncome +
                ", existingLoanObligations=" +
                existingLoanObligations +
                '}';
    }
}
