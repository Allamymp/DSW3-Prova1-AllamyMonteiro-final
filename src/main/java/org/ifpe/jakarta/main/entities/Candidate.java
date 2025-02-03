package org.ifpe.jakarta.main.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Candidate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(name="name")
    private String name;

    @Column(name="number", nullable = false)
    private Integer number;

    @ManyToOne
    @JoinColumn(name = "party_id")
    private PoliticalParty party;
    @Column(name = "votes")
    private Integer votes = 0;

    public void addVote() {
        this.votes++;
    }
}
