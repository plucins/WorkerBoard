package workerboard.serivce;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import workerboard.evens.EventProducer;
import workerboard.evens.TypeNotification;
import workerboard.exception.NotFound;
import workerboard.model.ApplicationUser;
import workerboard.model.ChartData;
import workerboard.model.InsuranceApplication;
import workerboard.model.ServiceMessage;
import workerboard.model.enums.InsuranceApplicationState;
import workerboard.model.enums.ServiceMessageType;
import workerboard.repository.ApplicationUserCustomRepository;
import workerboard.repository.InsuranceHistoryRepository;
import workerboard.repository.InsuranceRepository;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class InsuranceService extends BasicAbstractService<InsuranceApplication> {


    private InsuranceRepository insuranceRepository;
    private InsuranceHistoryRepository insuranceHistoryRepository;
    private RisksService risksService;
    private ApplicationUserCustomRepository applicationUserRepository;
    private EventProducer eventProducer;

    @Autowired
    public InsuranceService(InsuranceRepository insuranceRepository, InsuranceHistoryRepository insuranceHistoryRepository,
                            RisksService risksService, ApplicationUserCustomRepository applicationUserRepository, EventProducer eventProducer) {
        super.setBasicRepository(insuranceRepository);
        this.insuranceRepository = insuranceRepository;
        this.insuranceHistoryRepository = insuranceHistoryRepository;
        this.risksService = risksService;
        this.applicationUserRepository = applicationUserRepository;
        this.eventProducer = eventProducer;
    }

    public InsuranceApplication registerInsuranceApplication(InsuranceApplication insuranceApplication) {
        if (!isApplicationValid(insuranceApplication)) {
            insuranceApplication.addMessage(new ServiceMessage(ServiceMessageType.ERROR, "Tariffication attribute is missing"));
            return insuranceApplication;
        }

        insuranceApplication.setRiskVariants(risksService.getAvailableRisks(insuranceApplication));
        insuranceApplication.setNumber(getCalculationNumber());
        insuranceApplication.setState(InsuranceApplicationState.SIMULATION);

        insuranceHistoryRepository.save(insuranceApplication.getPersons().get(0).getInsuranceHistory());
        insuranceRepository.save(insuranceApplication);
        return insuranceApplication;

    }

    private String getCalculationNumber() {
        return "" + LocalDate.now().getYear() + LocalDate.now().getMonthValue() +
                LocalDate.now().getDayOfMonth() + "00" + (insuranceRepository.countByRegisterDate(LocalDate.now()) + 1);
    }

    private boolean isApplicationValid(InsuranceApplication insuranceApplication) {
        if (insuranceApplication.getVehicle() == null) return false;
        if (Integer.valueOf(insuranceApplication.getVehicle().getVehicleValue()) == 0 ||
                insuranceApplication.getVehicle().getVehicleValue() == null) return false;
        if (insuranceApplication.getPersons().isEmpty() || insuranceApplication.getPersons() == null) return false;
        if (insuranceApplication.getPersons().get(0).getDayOfBirth() == null) return false;

        return insuranceApplication.getPersons().get(0).getDrivingLicenseIssueDate() != null;

    }

    public InsuranceApplication getInsuranceApplicationById(Long id) throws NotFound {

        Optional<InsuranceApplication> applicationOptional = insuranceRepository.findById(id);
        if (applicationOptional.isPresent()) {
            InsuranceApplication insuranceApplication = applicationOptional.get();
            insuranceApplication.setRiskVariants(risksService.getAvailableRisks(insuranceApplication));
            return insuranceApplication;
        }

        throw new NotFound("Application with ID " + id + " not found");
    }

    public InsuranceApplication updateInsuranceApplication(Long id, InsuranceApplication insuranceApplication) throws NotFound {
        Optional<InsuranceApplication> optionalApplication = insuranceRepository.findById(id);
        if (optionalApplication.isPresent()) {
            InsuranceApplication applicationFromDB = optionalApplication.get();
            applicationFromDB = insuranceApplication;

            if (!(insuranceApplication.getState() == InsuranceApplicationState.CANCELED)) {
                applicationFromDB.setState(InsuranceApplicationState.APPLICATION);
            }

            return insuranceRepository.save(applicationFromDB);
        }

        throw new NotFound("Application with ID: " + id + "not found");
    }

    public InsuranceApplication acceptInsuranceApplication(Long id, InsuranceApplication insuranceApplication) throws NotFound {
        Optional<InsuranceApplication> optionalApplication = insuranceRepository.findById(id);
        if (optionalApplication.isPresent()) {
            InsuranceApplication applicationFromDb = optionalApplication.get();
            applicationFromDb = insuranceApplication;
            applicationFromDb.setState(InsuranceApplicationState.POLICY);
            applicationFromDb = insuranceRepository.save(applicationFromDb);

            this.eventProducer.createInsuranceNewEvent(applicationFromDb);
            eventProducer.createNotificationEvent(TypeNotification.POINT_FOR_POLICY, applicationFromDb.getSeller().getId());
            return applicationFromDb;

        }
        throw new NotFound("Application with ID: " + id + "not found");
    }

    public List<InsuranceApplication> getInsuranceApplicationByUser(Long id) throws NotFound {
        Optional<ApplicationUser> applicationUserOptional = applicationUserRepository.findById(id);
        if (applicationUserOptional.isPresent()) {
            ApplicationUser user = applicationUserOptional.get();
            return insuranceRepository.findAllBySeller(user);
        }

        throw new NotFound("User with ID: " + id + "not found");

    }

    public List<InsuranceApplication> findLike(Map<String, String> attribute, Long id) {

        return
                super.findAllByCriteria(likeCriteria(), attribute)
                        .stream()
                        .filter(x -> x.getSeller().getId() == id)
                        .collect(Collectors.toList());

    }


    public List<ChartData> getChartData(Long id, LocalDate start, LocalDate end) {


        List<InsuranceApplication> temp = this.insuranceRepository
                .findAll((Specification<InsuranceApplication>) (root, criteriaQuery, criteriaBuilder) ->
                        criteriaBuilder.between(root.get("registerDate"), start, end));


        return temp.stream()
                .filter(x -> x.getSeller().getId() == id)
                .collect(Collectors.groupingBy(InsuranceApplication::getRegisterDate))
                .entrySet()
                .stream()
                .map(t -> {
                    return new ChartData(t.getKey().toString(),
                            t.getKey(),
                            t.getValue().stream().mapToInt(c -> c.getTotalPolicyValue()).sum(),
                            t.getValue().stream().count()
                    );
                }).sorted(Comparator
                        .comparing(data -> data.getDate()))
                .collect(Collectors.toList());

    }

}
