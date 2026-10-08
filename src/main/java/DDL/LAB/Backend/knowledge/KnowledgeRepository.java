package DDL.LAB.Backend.knowledge;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface KnowledgeRepository extends JpaRepository<Knowledge, Long> {

    List<Knowledge> findAllByOrderByUpdatedAtDesc();
}