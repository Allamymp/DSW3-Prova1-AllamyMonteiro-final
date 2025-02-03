package org.ifpe.jakarta.main.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.ifpe.jakarta.main.entities.Candidate;
import org.ifpe.jakarta.main.entities.PoliticalParty;
import org.ifpe.jakarta.main.logging.CustomLogger;
import org.ifpe.jakarta.main.repository.PoliticalPartyRepository;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class PoliticalPartyService {

    @Inject
    private PoliticalPartyRepository politicalPartyRepository;
    @Inject
    private CandidateService candidateService;

    /**
     * Saves a political party to the repository.
     *
     * @param politicalParty The political party to be saved.
     * @return The saved political party.
     */
    @Transactional
    public PoliticalParty save(PoliticalParty politicalParty) {
        CustomLogger.info("Saving political party: " + politicalParty.getName());
        PoliticalParty savedParty = politicalPartyRepository.save(politicalParty);
        CustomLogger.info("Political party saved: " + savedParty.getName());
        return savedParty;
    }

    /**
     * Retrieves all political parties from the repository.
     *
     * @return A list of all political parties.
     */
    public List<PoliticalParty> findAll() {
        CustomLogger.info("Retrieving all political parties.");
        List<PoliticalParty> parties = politicalPartyRepository.findAll();
        CustomLogger.info("Retrieved " + parties.size() + " political parties.");
        return parties;
    }

    /**
     * Finds a political party by its ID.
     *
     * @param id The ID of the political party.
     * @return An Optional containing the political party, or empty if not found.
     */
    public Optional<PoliticalParty> findById(Long id) {
        CustomLogger.info("Finding political party by ID: " + id);
        return politicalPartyRepository.findById(id);
    }
    public Optional<PoliticalParty> findByIdWithoutLazyLoad(Long id) {
        CustomLogger.info("Finding political party by ID without lazy load: " + id);
        return politicalPartyRepository.findByIdWithoutLazyLoad(id);
    }

    /**
     * Finds a political party by its name.
     *
     * @param name The name of the political party.
     * @return An Optional containing the political party, or empty if not found.
     */
    public Optional<PoliticalParty> findByName(String name) {
        CustomLogger.info("Finding political party by name: " + name);
        return politicalPartyRepository.findByName(name);
    }

    /**
     * Finds a political party by its number.
     *
     * @param number The number of the political party.
     * @return An Optional containing the political party, or empty if not found.
     */
    public Optional<PoliticalParty> findByNumber(Integer number) {
        CustomLogger.info("Finding political party by number: " + number);
        return politicalPartyRepository.findByNumber(number);
    }

    public Optional<PoliticalParty> findByNumberWithoutLazy(Integer number) {
        CustomLogger.info("Finding political party by number: " + number);
        return politicalPartyRepository.findByNumberWithoutLazy(number);
    }

    /**
     * Finds a political party by its abbreviation (sigla).
     *
     * @param abbreviation The abbreviation of the political party.
     * @return An Optional containing the political party, or empty if not found.
     */
    public Optional<PoliticalParty> findByAbbreviation(String abbreviation) {
        CustomLogger.info("Finding political party by abbreviation (sigla): " + abbreviation);
        return politicalPartyRepository.findByAbbreviation(abbreviation);
    }

    /**
     * Adds a candidate to a political party.
     *
     * @param partyId     The ID of the political party.
     * @param candidateId The ID of the candidate to be added.
     */
    @Transactional
    public void addCandidate(Long partyId, Long candidateId) {
        CustomLogger.info("Adding candidate with ID: " + candidateId + " to political party ID: " + partyId);

        // Busca pelo partido e candidato
        Optional<PoliticalParty> politicalPartyOpt = findById(partyId);
        Optional<Candidate> candidateOpt = candidateService.findById(candidateId);

        if (politicalPartyOpt.isPresent()) {
            PoliticalParty politicalParty = politicalPartyOpt.get();
            if (candidateOpt.isPresent()) {
                Candidate candidate = candidateOpt.get();

                boolean candidateExists = politicalParty.getCandidates().stream()
                        .anyMatch(existingCandidate -> existingCandidate.getNumber().equals(candidate.getNumber()));

                if (candidateExists) {
                    CustomLogger.warn("Candidate with number " + candidate.getNumber() + " already exists in party " + politicalParty.getName());
                    throw new IllegalArgumentException("Candidate with number " + candidate.getNumber() + " already exists in party " + politicalParty.getName());
                }

                politicalParty.addCandidate(candidate);
                politicalPartyRepository.save(politicalParty);
                CustomLogger.info("Candidate " + candidate.getName() + " added to party " + politicalParty.getName());
            } else {
                CustomLogger.warn("No candidate found with ID: " + candidateId);
            }
        } else {
            CustomLogger.warn("No political party found with ID: " + partyId);
        }
    }

    /**
     * Removes a vote from a candidate within a political party.
     *
     * @param partyId     The ID of the political party.
     * @param candidateId The ID of the candidate to remove the vote from.
     */
    @Transactional
    public void removeVote(Long partyId, Long candidateId) {
        CustomLogger.info("Removing vote from candidate with ID: " + candidateId + " in party with ID: " + partyId);

        Optional<PoliticalParty> politicalPartyOpt = findById(partyId);
        if (politicalPartyOpt.isPresent()) {
            PoliticalParty politicalParty = politicalPartyOpt.get();
            Optional<Candidate> candidateOpt = politicalParty.getCandidates().stream()
                    .filter(c -> c.getId().equals(candidateId))
                    .findFirst();

            if (candidateOpt.isPresent()) {
                Candidate candidate = candidateOpt.get();
                politicalParty.removeCandidate(candidate);
                politicalPartyRepository.save(politicalParty);
                CustomLogger.info("Candidate " + candidate.getName() + " removed from party " + politicalParty.getName());
            } else {
                CustomLogger.warn("No candidate found with ID: " + candidateId);
            }
        } else {
            CustomLogger.warn("No political party found with ID: " + partyId);
        }
    }

    /**
     * Adds a vote to a political party.
     *
     * @param partyId The ID of the political party.
     */
    @Transactional
    public void addVote(Long partyId) {
        CustomLogger.info("Adding vote to political party with ID: " + partyId);

        Optional<PoliticalParty> politicalPartyOpt = findById(partyId);
        if (politicalPartyOpt.isPresent()) {
            PoliticalParty politicalParty = politicalPartyOpt.get();
            politicalParty.addVote();
            politicalPartyRepository.save(politicalParty);
            CustomLogger.info("Vote added to party: " + politicalParty.getName());
        } else {
            CustomLogger.warn("No political party found with ID: " + partyId);
        }
    }

    /**
     * Retrieves all political parties ordered by votes in descending order.
     *
     * @return A list of political parties ordered by votes in descending order.
     */
    public List<PoliticalParty> findAllOrderedByVotesDesc() {
        CustomLogger.info("Retrieving all political parties ordered by votes in descending order.");
        return politicalPartyRepository.findAllOrderedByVotesDesc();
    }

    /**
     * Retrieves all candidates for a political party ordered by votes in descending order.
     *
     * @param partyId The ID of the political party.
     * @return A list of candidates ordered by votes in descending order.
     */
    public List<Candidate> findAllCandidatesOrderedByVotesDesc(Long partyId) {
        CustomLogger.info("Retrieving all candidates for political party with ID: " + partyId + " ordered by votes in descending order.");
        return politicalPartyRepository.findAllCandidatesOfPartyOrderedByVotesDesc(partyId);
    }


}
