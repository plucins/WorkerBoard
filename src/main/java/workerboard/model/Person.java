package workerboard.model;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    @OneToOne
    @JoinColumn(name = "adress_id")
    private Adress adress;
    private LocalDate dayOfBirth;
    private String pesel;
    private LocalDate drivingLicenseIssueDate;
    @OneToOne
    @JoinColumn(name = "insuranceHistory_id")
    private InsuranceHistory insuranceHistory;

    public Person() {
    }

    public Person(String firstName, String lastName, Adress adress, LocalDate dayOfBirth, String pesel, LocalDate drivingLicenseIssueDate, InsuranceHistory insuranceHistory) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.adress = adress;
        this.dayOfBirth = dayOfBirth;
        this.pesel = pesel;
        this.drivingLicenseIssueDate = drivingLicenseIssueDate;
        this.insuranceHistory = insuranceHistory;
    }

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Adress getAdress() {
        return adress;
    }

    public LocalDate getDayOfBirth() {
        return dayOfBirth;
    }

    public String getPesel() {
        return pesel;
    }

    public LocalDate getDrivingLicenseIssueDate() {
        return drivingLicenseIssueDate;
    }

    public InsuranceHistory getInsuranceHistory() {
        return insuranceHistory;
    }
}