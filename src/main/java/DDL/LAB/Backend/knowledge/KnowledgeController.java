package DDL.LAB.Backend.knowledge;

import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/knowledge")
@CrossOrigin(origins = "http://localhost:5173")
public class KnowledgeController {

    private final KnowledgeRepository knowledgeRepository;

    public KnowledgeController(KnowledgeRepository knowledgeRepository) {
        this.knowledgeRepository = knowledgeRepository;
    }

    @GetMapping
    public List<Knowledge> getKnowledge() {
        return knowledgeRepository.findAllByOrderByUpdatedAtDesc();
    }

    @GetMapping("/{id}")
    public Knowledge getKnowledgeItem(@PathVariable Long id) {
        return knowledgeRepository.findById(id).orElse(null);
    }

    @PostMapping
    public Knowledge createKnowledge(@RequestBody Knowledge knowledge) {

        LocalDateTime now = LocalDateTime.now();

        knowledge.setId(null);
        knowledge.setCreatedAt(now);
        knowledge.setUpdatedAt(now);

        return knowledgeRepository.save(knowledge);
    }

    @PutMapping("/{id}")
    public Knowledge updateKnowledge(
            @PathVariable Long id,
            @RequestBody Knowledge knowledge
    ) {

        Knowledge existing = knowledgeRepository.findById(id).orElse(null);

        if (existing == null) {
            return null;
        }

        existing.setTitle(knowledge.getTitle());
        existing.setContent(knowledge.getContent());
        existing.setCategory(knowledge.getCategory());
        existing.setSource(knowledge.getSource());
        existing.setUpdatedAt(LocalDateTime.now());

        return knowledgeRepository.save(existing);
    }

    @DeleteMapping("/{id}")
    public void deleteKnowledge(@PathVariable Long id) {
        knowledgeRepository.deleteById(id);
    }
}