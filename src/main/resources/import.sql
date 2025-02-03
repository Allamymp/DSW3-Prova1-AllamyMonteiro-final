-- Inserindo dados de PoliticalParty
INSERT INTO political_party (id, name, number, votes, abbreviation) VALUES (1, 'Partido A', 10, 9, 'PA');
INSERT INTO political_party (id, name, number, votes, abbreviation) VALUES (2, 'Partido B', 20, 7, 'PB');
INSERT INTO political_party (id, name, number, votes, abbreviation) VALUES (3, 'Partido C', 30, 4, 'PC');

-- Inserindo dados de Candidate
INSERT INTO candidate (id, name, number, votes, party_id) VALUES (1, 'Candidato 1 do Partido A', 100, 3, 1);
INSERT INTO candidate (id, name, number, votes, party_id) VALUES (2, 'Candidato 2 do Partido A', 101, 5, 1);
INSERT INTO candidate (id, name, number, votes, party_id) VALUES (7, 'Candidato 3 do Partido A', 102, 1, 1);

INSERT INTO candidate (id, name, number, votes, party_id) VALUES (3, 'Candidato 1 do Partido B', 200, 4, 2);
INSERT INTO candidate (id, name, number, votes, party_id) VALUES (4, 'Candidato 2 do Partido B', 201, 2, 2);
INSERT INTO candidate (id, name, number, votes, party_id) VALUES (8, 'Candidato 3 do Partido B', 202, 1, 2);

INSERT INTO candidate (id, name, number, votes, party_id) VALUES (5, 'Candidato 1 do Partido C', 300, 1, 3);
INSERT INTO candidate (id, name, number, votes, party_id) VALUES (6, 'Candidato 2 do Partido C', 301, 1, 3);
INSERT INTO candidate (id, name, number, votes, party_id) VALUES (9, 'Candidato 3 do Partido C', 303, 2, 3);

-- Inserindo totalVotes
INSERT INTO total_votes (total) VALUES (20);  -- Total de votos
