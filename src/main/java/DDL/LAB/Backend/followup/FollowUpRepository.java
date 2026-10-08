package DDL.LAB.Backend.followup;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FollowUpRepository extends JpaRepository<FollowUp, Long> {

    List<FollowUp> findAllByOrderByDueDateAsc();

    List<FollowUp> findByCustomerIdOrderByDueDateAsc(Long customerId);
}