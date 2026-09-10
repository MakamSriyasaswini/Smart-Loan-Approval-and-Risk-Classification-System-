package loan;

public class Customer {

    private String customerId;
    private String name;
    private int age;
    private String governmentId;
    private double monthlyIncome;
    private double existingLoanObligations;

    public Customer(String customerId, String name, int age,
                    String governmentId, double monthlyIncome,
                    double existingLoanObligations) {

        this.customerId = customerId;
        this.name = name;
        this.age = age;
        this.governmentId = governmentId;
        this.monthlyIncome = monthlyIncome;
        this.existingLoanObligations = existingLoanObligations;
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
}
