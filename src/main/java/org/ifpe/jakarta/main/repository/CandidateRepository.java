package org.ifpe.jakarta.main.repository;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.ifpe.jakarta.main.entities.Candidate;

import java.util.List;
import java.util.Optional;

@Stateless
public class CandidateRepository {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Saves or updates a candidate in the database.
     *
     * @param candidate The candidate to be saved or updated.
     * @return The saved or updated candidate.
     */
    @Transactional
    public Candidate save(Candidate candidate) {
        if (candidate.getId() == null) {
            entityManager.persist(candidate);
        } else {
            entityManager.merge(candidate);
        }
        return candidate;
    }

    /**
     * Retrieves all candidates from the database.
     *
     * @return A list containing all candidates.
     */
    public List<Candidate> findAll() {
        return entityManager.createQuery("SELECT c FROM Candidate c", Candidate.class)
                .getResultList();
    }

    /**
     * Finds a candidate by its ID.
     *
     * @param id The ID of the candidate to find.
     * @return An Optional containing the candidate if found, or an empty Optional if no candidate exists with the given ID.
     */
    public Optional<Candidate> findById(Long id) {
        Candidate candidate = entityManager.find(Candidate.class, id);
        return Optional.ofNullable(candidate);
    }

    /**
     * Removes a candidate from the database by its ID.
     *
     * @param id The ID of the candidate to be removed.
     */
    public void remove(Long id) {
        Optional<Candidate> candidateOptional = findById(id);
        candidateOptional.ifPresent(candidate -> entityManager.remove(candidate));
    }

    /**
     * Retrieves all candidates ordered by the number of votes in descending order.
     *
     * @return A list of candidates ordered by vote count in descending order.
     */
    public List<Candidate> findAllOrderedByVotesDesc() {
        return entityManager.createQuery(
                        "SELECT c FROM Candidate c ORDER BY c.vote DESC", Candidate.class)
                .getResultList();
    }
}
