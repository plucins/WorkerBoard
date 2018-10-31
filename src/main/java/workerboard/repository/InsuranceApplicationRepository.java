package workerboard.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import workerboard.model.InsuranceApplication;

import java.time.LocalDate;

public interface InsuranceApplicationRepository extends JpaRepository<InsuranceApplication, Long> {

    Integer countByRegisterDate(LocalDate registerDate);
}
