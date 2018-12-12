package workerboard.serivce;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import workerboard.exception.NotFound;
import workerboard.model.ApplicationUser;
import workerboard.model.Experience;
import workerboard.model.Role;
import workerboard.model.enums.UserRole;
import workerboard.repository.ApplicationUserRepository;
import workerboard.repository.ExperienceRepository;

import java.util.Arrays;

@Service
public class SignUpService {


    private ApplicationUserRepository applicationUserRepository;
    private ExperienceRepository repository;
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public SignUpService(ApplicationUserRepository applicationUserRepository,
                         BCryptPasswordEncoder passwordEncoder) {
        this.applicationUserRepository = applicationUserRepository;
        this.passwordEncoder = passwordEncoder;
    }


    public ApplicationUser registrationNewUser(ApplicationUser newApplicationUser) throws NotFound {

        if (applicationUserRepository
                .findByEmail(newApplicationUser.getEmail())
                .isPresent()) {
            throw new NotFound("User with email: " + newApplicationUser.getEmail() + " exist");
        }

        newApplicationUser.setPassword(passwordEncoder.encode(newApplicationUser.getPassword()));

        newApplicationUser.setRoles(Arrays.asList(new Role(UserRole.ROLE_UNSPECIFIED)));
        newApplicationUser.setExperience(new Experience());
        return applicationUserRepository.save(newApplicationUser);
    }
}
