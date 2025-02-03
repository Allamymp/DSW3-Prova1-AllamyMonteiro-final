package org.ifpe.jakarta.main.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="total_votes")
@NoArgsConstructor
@AllArgsConstructor
public class TotalVotes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer total;


    public void addVote() {
        this.total++;
    }
}
