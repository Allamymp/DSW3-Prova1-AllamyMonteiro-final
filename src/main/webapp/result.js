// Importa as funções de sessionResult.js
import { 
    getCandidates, 
    getPoliticalParties, 
    loadData,
    getTotalVotes 
} from './sessionResult.js';

// Quando a página for carregada, chama o loadData para preencher os dados
window.onload = async function() {
    try {
        await loadData();  // Chama a função para carregar os dados

        // Ordena os candidatos e partidos pela quantidade de votos de forma decrescente
        const sortedCandidates = sortByVotes(getCandidates());
        const sortedParties = sortByVotes(getPoliticalParties(), true); // Passa true para ordenar partidos

        // Calcula a distribuição de cadeiras e exibe o resultado
        const distribuicaoFinal = distributeSeatsToCandidates();
        console.log(distribuicaoFinal);  // Exibe no console a distribuição de cadeiras dos candidatos

        // Preenche a tabela com os dados de candidatos e partidos
        fillResultsTable(sortedCandidates, sortedParties, distribuicaoFinal);

    } catch (error) {
        console.error("Erro ao carregar dados:", error);
    }
};

// Função para ordenar por votos (decrescente)
function sortByVotes(items, isParty = false) {
    return items.sort((a, b) => {
        const aVotes = isParty ? a.votes : a.votes; // Para partidos e candidatos
        const bVotes = isParty ? b.votes : b.votes;
        return bVotes - aVotes; // Ordena de forma decrescente
    });
}

function fillResultsTable(candidates, parties, distribuicaoFinal) {
    const resultTableBody = document.getElementById('resultTableBody');

    // Cria um mapa de cadeiras restantes por partido a partir da distribuição final
    const partySeatsRemaining = new Map();
    distribuicaoFinal.forEach(entry => {
        partySeatsRemaining.set(entry.party, entry.cadeirasRestantes);
    });

    const maxRows = Math.max(candidates.length, parties.length);

    // Preenche a tabela com dados combinados
    for (let i = 0; i < maxRows; i++) {
        const row = resultTableBody.insertRow();

        const candidate = candidates[i] || { name: '', number: '', votes: '' }; 
        const candidateParty = candidate.party && candidate.party.name ? candidate.party.name : candidate.party; 

        const candidateCell = row.insertCell();
        candidateCell.textContent = candidate.name;

        const candidateNumberCell = row.insertCell();
        candidateNumberCell.textContent = candidate.number;

        const candidateVotesCell = row.insertCell();
        candidateVotesCell.textContent = candidate.votes;

        const party = parties[i] || { name: '', abbreviation: '', number: '', votes: '' };

        row.insertCell().textContent = party.name;
        row.insertCell().textContent = party.abbreviation;
        row.insertCell().textContent = party.number;
        row.insertCell().textContent = party.votes;

        // Verifica se o partido ainda tem cadeiras para distribuir
        if (partySeatsRemaining.has(candidateParty) && partySeatsRemaining.get(candidateParty) > 0) {
            console.log("Candidato " + candidate.name)
            console.log("Partido: " + candidateParty)
            
            // Destaca as células do candidato em verde
            candidateCell.style.backgroundColor = 'green';
            candidateNumberCell.style.backgroundColor = 'green';
            candidateVotesCell.style.backgroundColor = 'green';

            // Reduz uma cadeira para o partido
            let remainingSeats = partySeatsRemaining.get(candidateParty);
            partySeatsRemaining.set(candidateParty, remainingSeats - 1);
        }
    }
}

// Função para calcular a distribuição de cadeiras para candidatos
function distributeSeatsToCandidates() {
    const votosValidos = getTotalVotes(); // Obtém os votos válidos
    const partidos = getPoliticalParties(); // Obtém os partidos
    const totalCadeiras = 7; // Número de cadeiras para distribuição

    if (!votosValidos || partidos.length === 0) {
        console.error("Faltam dados para calcular a distribuição de cadeiras");
        return;
    }

    // Passo 1: Calcular o Quociente Eleitoral
    const quocienteEleitoral = Math.round(votosValidos / totalCadeiras);

    // Passo 2: Calcular o Quociente Partidário e a distribuição inicial de cadeiras
    let partidosComCadeiras = partidos.map(party => {
        const quocientePartidario = Math.floor(party.votes / quocienteEleitoral);
        return {
            ...party,
            quocientePartidario,
            cadeirasIniciais: quocientePartidario
        };
    });

    // Passo 3: Calcular as sobras e distribuir as cadeiras extras
    let sobras = totalCadeiras - partidosComCadeiras.reduce((acc, party) => acc + party.cadeirasIniciais, 0);

    // Distribuir as sobras
    while (sobras > 0) {
        partidosComCadeiras = partidosComCadeiras.map(party => {
            const media = party.votes / (party.cadeirasIniciais + 1);
            return {
                ...party,
                media
            };
        });

        partidosComCadeiras.sort((a, b) => b.media - a.media);

        partidosComCadeiras[0].cadeirasIniciais += 1;
        sobras--;
    }

    // Passo 4: Criar um mapa com a distribuição final das cadeiras
    const partySeatMap = new Map();
    partidosComCadeiras.forEach(party => {
        partySeatMap.set(party.name, party.cadeirasIniciais); // A chave é o nome do partido e o valor são as cadeiras
    });

    // Log apenas na função de distribuição das cadeiras
    console.log("Distribuição final das cadeiras:", partySeatMap);

    // Passo 5: Retornar o mapa de distribuição final
    const distribuicaoFinal = partidosComCadeiras.map(party => ({
        party: party.name,
        cadeirasRestantes: party.cadeirasIniciais
    }));

    return distribuicaoFinal;
}
