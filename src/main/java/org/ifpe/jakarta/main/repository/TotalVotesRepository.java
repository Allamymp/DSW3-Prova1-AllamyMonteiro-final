package org.ifpe.jakarta.main.repository;


import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.ifpe.jakarta.main.entities.Candidate;
import org.ifpe.jakarta.main.entities.TotalVotes;

import java.util.Optional;

@Stateless
public class TotalVotesRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public Optional<TotalVotes> findById(Long id) {
        TotalVotes votes = entityManager.find(TotalVotes.class, id);
        return Optional.ofNullable(votes);
    }

    @Transactional
    public TotalVotes save(TotalVotes votes) {
        if (votes.getId() == null) {
            entityManager.persist(votes);
        } else {
            entityManager.merge(votes);
        }
        return votes;
    }
}
