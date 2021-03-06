package workerboard.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import workerboard.config.LocalDateDeserializer;
import workerboard.config.LocalDateSerializer;
import workerboard.model.dto.RiskDto;
import workerboard.model.enums.InsuranceApplicationState;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
public class InsuranceApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    @JoinColumn(name = "vehicle_id")
    private Vehicle vehicle;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "person_id")
    private List<Person> persons = new ArrayList<>();
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "risk_id")
    private List<Risk> risks;
    private String number;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "message_id")
    private List<ServiceMessage> messages = new ArrayList<>();
    @Enumerated(EnumType.STRING)
    private InsuranceApplicationState state;
    @Transient
    private List<RiskDto> riskVariants = new ArrayList<>();
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate registerDate = LocalDate.now();
    private Integer installmentAmount = 1;
    private Integer totalPolicyValue;
    @OneToOne
    @JoinColumn(name = "seller_id")
    private ApplicationUser seller;

    public InsuranceApplication() {
    }

    public InsuranceApplication(Vehicle vehicle, List<Person> persons, List<Risk> risks, String number, List<ServiceMessage> messages, InsuranceApplicationState state, Integer installmentAmount, Integer totalPolicyValue, ApplicationUser seller) {
        this.vehicle = vehicle;
        this.persons = persons;
        this.risks = risks;
        this.number = number;
        this.messages = messages;
        this.state = state;
        this.installmentAmount = installmentAmount;
        this.totalPolicyValue = totalPolicyValue;
        this.seller = seller;
    }

    public void setSeller(ApplicationUser seller) {
        this.seller = seller;
    }

    public Long getId() {
        return id;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public List<Person> getPersons() {
        return persons;
    }

    public List<Risk> getRisks() {
        return risks;
    }

    public String getNumber() {
        return number;
    }

    public List<ServiceMessage> getMessages() {
        return messages;
    }

    public void setRisks(List<Risk> risks) {
        this.risks = risks;
    }

    public List<RiskDto> getRiskVariants() {
        return riskVariants;
    }

    public void setRiskVariants(List<RiskDto> riskVariants) {
        this.riskVariants = riskVariants;
    }

    public void addMessage(ServiceMessage serviceMessage) {
        messages.add(serviceMessage);
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public LocalDate getRegisterDate() {
        return registerDate;
    }

    public InsuranceApplicationState getState() {
        return state;
    }

    public void setState(InsuranceApplicationState state) {
        this.state = state;
    }

    public Integer getInstallmentAmount() {
        return installmentAmount;
    }

    public Integer getTotalPolicyValue() {
        return totalPolicyValue;
    }

    public ApplicationUser getSeller() {
        return seller;
    }

    @Override
    public String toString() {
        return "InsuranceApplication{" +
                "id=" + id +
                ", vehicle=" + vehicle +
                ", persons=" + persons +
                ", risks=" + risks +
                ", number='" + number + '\'' +
                ", messages=" + messages +
                ", state=" + state +
                ", riskVariants=" + riskVariants +
                ", registerDate=" + registerDate +
                ", installmentAmount=" + installmentAmount +
                ", totalPolicyValue=" + totalPolicyValue +
                ", seller=" + seller +
                '}';
    }
}
