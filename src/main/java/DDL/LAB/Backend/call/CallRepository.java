package DDL.LAB.Backend.call;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CallRepository extends JpaRepository<Call, Long> {

    List<Call> findAllByOrderByStartedAtDesc();

    List<Call> findByCustomerIdOrderByStartedAtDesc(Long customerId);
}