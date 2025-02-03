package org.ifpe.jakarta.main.repository;

import jakarta.ejb.Stateless;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.NoResultException;
import org.ifpe.jakarta.main.entities.Candidate;
import org.ifpe.jakarta.main.entities.PoliticalParty;

import java.util.List;
import java.util.Optional;

@Stateless
public class PoliticalPartyRepository {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Saves a political party in the repository.
     *
     * @param politicalParty The political party to be saved.
     * @return The saved political party.
     */
    public PoliticalParty save(PoliticalParty politicalParty) {
        if (politicalParty.getId() == null) {
            entityManager.persist(politicalParty);
        } else {
            entityManager.merge(politicalParty);
        }
        return politicalParty;
    }

    /**
     * Retrieves all political parties from the repository.
     *
     * @return List of all political parties.
     */
    public List<PoliticalParty> findAll() {
        return entityManager.createQuery("SELECT p FROM PoliticalParty p", PoliticalParty.class)
                .getResultList();
    }

    /**
     * Finds a political party by its ID.
     *
     * @param id The ID of the political party.
     * @return The political party or Optional.empty() if not found.
     */
    public Optional<PoliticalParty> findById(Long id) {
        return Optional.ofNullable(entityManager.find(PoliticalParty.class, id));
    }

    public Optional<PoliticalParty> findByIdWithoutLazyLoad(Long id) {
        try {
            PoliticalParty party = entityManager.createQuery(
                            "SELECT p FROM PoliticalParty p LEFT JOIN FETCH p.candidates WHERE p.id = :id",
                            PoliticalParty.class)
                    .setParameter("id", id)
                    .getSingleResult();
            return Optional.of(party);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }


    /**
     * Finds a political party by its name.
     *
     * @param name The name of the political party.
     * @return An Optional containing the political party, or Optional.empty() if not found.
     */
    public Optional<PoliticalParty> findByName(String name) {
        try {
            PoliticalParty party = entityManager.createQuery("SELECT p FROM PoliticalParty p WHERE p.name = :name", PoliticalParty.class)
                    .setParameter("name", name)
                    .getSingleResult();
            return Optional.of(party);
        } catch (NoResultException e) {
            return Optional.empty(); // Retorna Optional vazio caso não encontre
        }
    }

    /**
     * Finds a political party by its number.
     *
     * @param number The number of the political party.
     * @return An Optional containing the political party, or Optional.empty() if not found.
     */
    public Optional<PoliticalParty> findByNumber(Integer number) {
        try {
            PoliticalParty party = entityManager.createQuery("SELECT p FROM PoliticalParty p WHERE p.number = :number", PoliticalParty.class)
                    .setParameter("number", number)
                    .getSingleResult();
            return Optional.of(party);
        } catch (NoResultException e) {
            return Optional.empty(); // Retorna Optional vazio caso não encontre
        }
    }

    public Optional<PoliticalParty> findByNumberWithoutLazy(Integer number) {
        try {
            PoliticalParty party = entityManager.createQuery(
                            "SELECT p FROM PoliticalParty p LEFT JOIN FETCH p.candidates WHERE p.number = :number",
                            PoliticalParty.class)
                    .setParameter("number", number)
                    .getSingleResult();
            return Optional.of(party);
        } catch (NoResultException e) {
            return Optional.empty(); // Retorna Optional vazio caso não encontre
        }
    }

    /**
     * Finds a political party by its abbreviation (sigla).
     *
     * @param abbreviation The abbreviation of the political party.
     * @return An Optional containing the political party, or Optional.empty() if not found.
     */
    public Optional<PoliticalParty> findByAbbreviation(String abbreviation) {
        try {
            PoliticalParty party = entityManager.createQuery("SELECT p FROM PoliticalParty p WHERE p.abbreviation = :abbreviation", PoliticalParty.class)
                    .setParameter("abbreviation", abbreviation)
                    .getSingleResult();
            return Optional.of(party);
        } catch (NoResultException e) {
            return Optional.empty(); // Retorna Optional vazio caso não encontre
        }
    }

    /**
     * Retrieves all political parties ordered by vote count in descending order.
     *
     * @return List of political parties ordered by vote count.
     */
    public List<PoliticalParty> findAllOrderedByVotesDesc() {
        return entityManager.createQuery("SELECT p FROM PoliticalParty p ORDER BY p.vote DESC", PoliticalParty.class)
                .getResultList();
    }

    /**
     * Retrieves all candidates of a political party ordered by vote count in descending order.
     *
     * @param partyId The ID of the political party.
     * @return List of candidates ordered by vote count.
     */
    public List<Candidate> findAllCandidatesOfPartyOrderedByVotesDesc(Long partyId) {
        return entityManager.createQuery("SELECT c FROM Candidate c WHERE c.party.id = :partyId ORDER BY c.vote DESC", Candidate.class)
                .setParameter("partyId", partyId)
                .getResultList();
    }
}
