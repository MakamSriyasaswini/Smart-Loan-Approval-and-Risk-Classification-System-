package loan;

import static org.junit.Assert.*;
import org.junit.Test;

public class CreditAssessmentTest {

    CreditAssessment assessment = new CreditAssessment();

    @Test
    public void testLowRiskApproval()
            throws InvalidLoanException {

        Customer customer = new Customer(
                "C101",
                "Ravi",
                30,
                "GOV123",
                100000,
                10000
        );

        LoanApplication application =
                new LoanApplication(
                        "L101",
                        300000
                );

        String result =
                assessment.assessLoan(
                        customer,
                        application,
                        800
                );

        assertTrue(
                result.contains("Approved - Low Risk"));
    }

    @Test
    public void testMediumRiskApproval()
            throws InvalidLoanException {

        Customer customer = new Customer(
                "C102",
                "Arun",
                30,
                "GOV124",
                60000,
                10000
        );

        LoanApplication application =
                new LoanApplication(
                        "L102",
                        300000
                );

        String result =
                assessment.assessLoan(
                        customer,
                        application,
                        680
                );

        assertTrue(
                result.contains("Approved - Medium Risk"));
    }

    @Test
    public void testBoundaryAge21()
            throws InvalidLoanException {

        Customer customer = new Customer(
                "C103",
                "Kiran",
                21,
                "GOV125",
                60000,
                5000
        );

        LoanApplication application =
                new LoanApplication(
                        "L103",
                        200000
                );

        String result =
                assessment.assessLoan(
                        customer,
                        application,
                        700
                );

        assertTrue(
                result.contains("Approved"));
    }

    @Test
    public void testMinimumCreditScore650()
            throws InvalidLoanException {

        Customer customer = new Customer(
                "C104",
                "Suresh",
                30,
                "GOV126",
                60000,
                5000
        );

        LoanApplication application =
                new LoanApplication(
                        "L104",
                        200000
                );

        String result =
                assessment.assessLoan(
                        customer,
                        application,
                        650
                );

        assertTrue(
                result.contains("Approved"));
    }

    @Test
    public void testMaximumDTIBoundary()
            throws InvalidLoanException {

        Customer customer = new Customer(
                "C105",
                "Rahul",
                30,
                "GOV127",
                50000,
                10000
        );

        LoanApplication application =
                new LoanApplication(
                        "L105",
                        600000
                );

        String result =
                assessment.assessLoan(
                        customer,
                        application,
                        700
                );

        assertTrue(
                result.contains("Approved"));

        assertTrue(
                result.contains("DTI: 40.00%"));
    }

    @Test
    public void testMinimumIncomeBoundary()
            throws InvalidLoanException {

        Customer customer = new Customer(
                "C106",
                "Vijay",
                30,
                "GOV128",
                25000,
                0
        );

        LoanApplication application =
                new LoanApplication(
                        "L106",
                        100000
                );

        String result =
                assessment.assessLoan(
                        customer,
                        application,
                        700
                );

        assertTrue(
                result.contains("Approved"));
    }

    @Test
    public void testAgeBelow21()
            throws InvalidLoanException {

        Customer customer = new Customer(
                "C107",
                "Ajay",
                20,
                "GOV129",
                60000,
                5000
        );

        LoanApplication application =
                new LoanApplication(
                        "L107",
                        200000
                );

        String result =
                assessment.assessLoan(
                        customer,
                        application,
                        700
                );

        assertTrue(
                result.contains(
                        "Customer must be at least 21 years old"));
    }

    @Test
    public void testInvalidGovernmentId()
            throws InvalidLoanException {

        Customer customer = new Customer(
                "C108",
                "Manoj",
                30,
                "ABC999",
                60000,
                5000
        );

        LoanApplication application =
                new LoanApplication(
                        "L108",
                        200000
                );

        String result =
                assessment.assessLoan(
                        customer,
                        application,
                        700
                );

        assertTrue(
                result.contains(
                        "Government ID is invalid"));
    }

    @Test
    public void testLowIncome()
            throws InvalidLoanException {

        Customer customer = new Customer(
                "C109",
                "Karthik",
                30,
                "GOV130",
                20000,
                2000
        );

        LoanApplication application =
                new LoanApplication(
                        "L109",
                        100000
                );

        String result =
                assessment.assessLoan(
                        customer,
                        application,
                        700
                );

        assertTrue(
                result.contains(
                        "Monthly income is below the minimum threshold"));
    }

    @Test
    public void testLowCreditScore()
            throws InvalidLoanException {

        Customer customer = new Customer(
                "C110",
                "Ramesh",
                30,
                "GOV131",
                60000,
                5000
        );

        LoanApplication application =
                new LoanApplication(
                        "L110",
                        200000
                );

        String result =
                assessment.assessLoan(
                        customer,
                        application,
                        600
                );

        assertTrue(
                result.contains(
                        "Credit score is below the minimum requirement"));
    }

    @Test
    public void testLoanAmountExceedsLimit()
            throws InvalidLoanException {

        Customer customer = new Customer(
                "C111",
                "Deepak",
                30,
                "GOV132",
                50000,
                5000
        );

        LoanApplication application =
                new LoanApplication(
                        "L111",
                        700000
                );

        String result =
                assessment.assessLoan(
                        customer,
                        application,
                        750
                );

        assertTrue(
                result.contains(
                        "Requested loan amount exceeds maximum permissible amount"));
    }

    @Test
    public void testHighDTI()
            throws InvalidLoanException {

        Customer customer = new Customer(
                "C112",
                "Sanjay",
                30,
                "GOV133",
                50000,
                20000
        );

        LoanApplication application =
                new LoanApplication(
                        "L112",
                        100000
                );

        String result =
                assessment.assessLoan(
                        customer,
                        application,
                        750
                );

        assertTrue(
                result.contains(
                        "Debt-to-income ratio exceeds maximum permissible DTI"));
    }

    @Test
    public void testMultipleRejectionReasons()
            throws InvalidLoanException {

        Customer customer = new Customer(
                "C113",
                "Prakash",
                18,
                "INVALID123",
                20000,
                20000
        );

        LoanApplication application =
                new LoanApplication(
                        "L113",
                        500000
                );

        String result =
                assessment.assessLoan(
                        customer,
                        application,
                        600
                );

        assertTrue(
                result.contains(
                        "Customer must be at least 21 years old"));

        assertTrue(
                result.contains(
                        "Government ID is invalid"));

        assertTrue(
                result.contains(
                        "Monthly income is below the minimum threshold"));

        assertTrue(
                result.contains(
                        "Credit score is below the minimum requirement"));

        assertTrue(
                result.contains(
                        "Requested loan amount exceeds maximum permissible amount"));

        assertTrue(
                result.contains(
                        "Debt-to-income ratio exceeds maximum permissible DTI"));
    }

    @Test
    public void testMaximumPermissibleLoan()
            throws InvalidLoanException {

        Customer customer = new Customer(
                "C114",
                "Varun",
                30,
                "GOV134",
                50000,
                5000
        );

        double maximum =
                assessment.calculateMaximumLoanAmount(
                        customer);

        assertEquals(
                270000.0,
                maximum,
                0.01);
    }

    @Test
    public void testDTICalculation()
            throws InvalidLoanException {

        Customer customer = new Customer(
                "C115",
                "Rohit",
                30,
                "GOV135",
                50000,
                5000
        );

        LoanApplication application =
                new LoanApplication(
                        "L115",
                        250000
                );

        double dti =
                assessment.calculateDTI(
                        customer,
                        application);

        assertEquals(
                0.183333,
                dti,
                0.0001);
    }

    @Test
    public void testInvalidCustomerId() {

        Customer customer = new Customer(
                "",
                "Test",
                30,
                "GOV136",
                50000,
                5000
        );

        LoanApplication application =
                new LoanApplication(
                        "L116",
                        100000
                );

        try {

            assessment.assessLoan(
                    customer,
                    application,
                    700);

            fail(
                    "Expected InvalidLoanException");

        } catch (InvalidLoanException e) {

            assertEquals(
                    "Customer ID cannot be empty",
                    e.getMessage());
        }
    }

    @Test
    public void testNegativeAge() {

        Customer customer = new Customer(
                "C117",
                "Test",
                -5,
                "GOV137",
                50000,
                5000
        );

        LoanApplication application =
                new LoanApplication(
                        "L117",
                        100000
                );

        try {

            assessment.assessLoan(
                    customer,
                    application,
                    700);

            fail(
                    "Expected InvalidLoanException");

        } catch (InvalidLoanException e) {

            assertEquals(
                    "Age cannot be negative",
                    e.getMessage());
        }
    }

    @Test
    public void testNegativeIncome() {

        Customer customer = new Customer(
                "C118",
                "Test",
                30,
                "GOV138",
                -50000,
                5000
        );

        LoanApplication application =
                new LoanApplication(
                        "L118",
                        100000
                );

        try {

            assessment.assessLoan(
                    customer,
                    application,
                    700);

            fail(
                    "Expected InvalidLoanException");

        } catch (InvalidLoanException e) {

            assertEquals(
                    "Monthly income cannot be negative",
                    e.getMessage());
        }
    }

    @Test
    public void testInvalidCreditScore() {

        Customer customer = new Customer(
                "C119",
                "Test",
                30,
                "GOV139",
                50000,
                5000
        );

        LoanApplication application =
                new LoanApplication(
                        "L119",
                        100000
                );

        try {

            assessment.assessLoan(
                    customer,
                    application,
                    900);

            fail(
                    "Expected InvalidLoanException");

        } catch (InvalidLoanException e) {

            assertEquals(
                    "Credit score must be between 0 and 850",
                    e.getMessage());
        }
    }

    @Test
    public void testInvalidLoanAmount() {

        Customer customer = new Customer(
                "C120",
                "Test",
                30,
                "GOV140",
                50000,
                5000
        );

        LoanApplication application =
                new LoanApplication(
                        "L120",
                        0
                );

        try {

            assessment.assessLoan(
                    customer,
                    application,
                    700);

            fail(
                    "Expected InvalidLoanException");

        } catch (InvalidLoanException e) {

            assertEquals(
                    "Requested loan amount must be greater than zero",
                    e.getMessage());
        }
    }
}
