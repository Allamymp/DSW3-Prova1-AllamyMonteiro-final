package org.ifpe.jakarta.main.entities;

import jakarta.json.bind.annotation.JsonbTransient;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "political_party", uniqueConstraints = {
        @UniqueConstraint(columnNames = "name"),
        @UniqueConstraint(columnNames = "number"),
        @UniqueConstraint(columnNames = "abbreviation")
})
public class PoliticalParty {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "number", nullable = false)
    private Integer number;

    @OneToMany(mappedBy = "party")
    @JsonbTransient
    private List<Candidate> candidates = new ArrayList<>();

    @Column(name = "votes")
    private Integer votes;

    @Column(name = "abbreviation", nullable = false)
    private String abbreviation;

    public void removeCandidate(Candidate candidate) {
        this.candidates.remove(candidate);
    }

    public void addCandidate(Candidate candidate) {
        this.candidates.add(candidate);
    }

    public void addVote() {
        this.votes++;
    }
}
