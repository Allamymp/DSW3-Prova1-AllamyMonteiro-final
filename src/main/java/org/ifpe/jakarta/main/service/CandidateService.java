package org.ifpe.jakarta.main.service;


import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.ifpe.jakarta.main.entities.Candidate;
import org.ifpe.jakarta.main.logging.CustomLogger;
import org.ifpe.jakarta.main.repository.CandidateRepository;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class CandidateService {

    @Inject
    private CandidateRepository candidateRepository;


    /**
     * Creates a new candidate and saves it to the repository.
     *
     * @param candidate The candidate to be created.
     * @return The created candidate.
     */
    public Candidate create(Candidate candidate) {
        CustomLogger.info("Creating new candidate: " + candidate.getName());
        validateCandidate(candidate);  // Validates the candidate before saving
        return candidateRepository.save(candidate);
    }

    /**
     * Retrieves all candidates from the repository.
     *
     * @return A list of all candidates.
     */
    public List<Candidate> findAll() {
        CustomLogger.info("Retrieving all candidates.");
        return candidateRepository.findAll();
    }

    /**
     * Finds a candidate by their ID.
     *
     * @param id The ID of the candidate.
     * @return An Optional containing the candidate, or empty if not found.
     */
    public Optional<Candidate> findById(Long id) {
        CustomLogger.info("Searching for candidate with ID: " + id);
        return candidateRepository.findById(id);
    }

    /**
     * Deletes a candidate by their ID.
     *
     * @param id The ID of the candidate to be deleted.
     */
    public void delete(Long id) {
        CustomLogger.info("Deleting candidate with ID: " + id);
        Optional<Candidate> candidateOpt = findById(id);
        candidateOpt.ifPresent(candidate -> {
            candidateRepository.remove(candidate.getId());
            CustomLogger.info("Candidate with ID: " + id + " has been deleted.");
        });
    }

    /**
     * Increments the vote count for a candidate by their ID.
     *
     * @param id The ID of the candidate whose vote count will be incremented.
     */
    public void addVote(Long id) {
        CustomLogger.info("Incrementing vote count for candidate with ID: " + id);
        Optional<Candidate> candidateOpt = findById(id);
        if (candidateOpt.isPresent()) {
            Candidate candidate = candidateOpt.get();
            candidate.addVote();
            CustomLogger.info("Candidate with ID: " + id + " now has " + candidate.getVotes() + " votes.");
            candidateRepository.save(candidate);
        } else {
            CustomLogger.warn("No candidate found with ID: " + id);
            throw new IllegalArgumentException("Candidate not found with ID: " + id);
        }
    }

    /**
     * Retrieves all candidates ordered by their vote count.
     *
     * @return A list of candidates ordered by vote count.
     */
    public List<Candidate> findAllOrderedByVotes() {
        CustomLogger.info("Retrieving all candidates ordered by vote count.");
        return candidateRepository.findAllOrderedByVotesDesc();
    }

    /**
     * Updates an existing candidate in the repository.
     *
     * @param candidate The candidate with updated data.
     * @return The updated candidate.
     */
    public Candidate update(Candidate candidate) {
        CustomLogger.info("Updating candidate with ID: " + candidate.getId());

        validateCandidate(candidate);

        Optional<Candidate> existingCandidateOpt = findById(candidate.getId());
        if (existingCandidateOpt.isPresent()) {
            Candidate existingCandidate = existingCandidateOpt.get();

            existingCandidate.setName(candidate.getName());
            existingCandidate.setNumber(candidate.getNumber());

            CustomLogger.info("Candidate with ID: " + candidate.getId() + " has been updated.");

            return candidateRepository.save(existingCandidate);
        } else {
            CustomLogger.warn("No candidate found with ID: " + candidate.getId());
            throw new IllegalArgumentException("Candidate not found with ID: " + candidate.getId());
        }
    }


    /**
     * Validates the candidate's attributes before saving or performing other operations.
     * Throws IllegalArgumentException if any validation fails.
     *
     * @param candidate The candidate to be validated.
     */
    private void validateCandidate(Candidate candidate) {
        if (candidate == null) {
            CustomLogger.error("Validation failed: Candidate cannot be null.",
                    new IllegalArgumentException("Candidate cannot be null."));
            throw new IllegalArgumentException("Candidate cannot be null.");
        }
        if (candidate.getName() == null || candidate.getName().isBlank()) {
            CustomLogger.error("Validation failed: Candidate's name cannot be blank.",
                    new IllegalArgumentException("Candidate's name cannot be blank."));
            throw new IllegalArgumentException("Candidate's name cannot be blank.");
        }
        if (candidate.getNumber() == null) {
            CustomLogger.error("Validation failed: Candidate's number cannot be null.",
                    new IllegalArgumentException("Candidate's number cannot be null."));
            throw new IllegalArgumentException("Candidate's number cannot be null.");
        }
    }
}
