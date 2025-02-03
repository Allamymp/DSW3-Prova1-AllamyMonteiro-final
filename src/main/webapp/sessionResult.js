// sessionResult.js

// Inicializa a estrutura de sessão de resultados
export const sessionResult = {
    candidates: [],  // Lista de candidatos
    politicalParties: [],  // Lista de partidos políticos
    totalVotes: null
};

export function setTotalVotes(votes) {
    sessionResult.totalVotes = votes;
}

export function getTotalVotes() {
    return sessionResult.totalVotes;
}

// Função para adicionar candidatos à lista
export function addCandidates(candidates) {
    sessionResult.candidates = candidates;  // Armazena a lista de candidatos
}

// Função para adicionar partidos à lista
export function addPoliticalParties(parties) {
    sessionResult.politicalParties = parties;  // Armazena a lista de partidos
}

// Função para obter a lista de candidatos
export function getCandidates() {
    return sessionResult.candidates;  // Retorna a lista de candidatos
}

// Função para obter a lista de partidos
export function getPoliticalParties() {
    return sessionResult.politicalParties;  // Retorna a lista de partidos
}

// Função para limpar as listas de candidatos e partidos
export function clearSession() {
    sessionResult.candidates = [];  // Limpa a lista de candidatos
    sessionResult.politicalParties = [];  // Limpa a lista de partidos
}

// Função que chama as APIs para carregar os dados
export async function loadData() {
    try {
        // Ajuste dos endpoints para incluir a URL base correta sem "/api"
        const baseURL = 'http://localhost:8080/DSW3-Prova1-AllamyMonteiro/api/';

        // Carregar dados de candidatos
        const candidatesResponse = await fetch(`${baseURL}candidates`);
        const candidatesData = await candidatesResponse.json();
        addCandidates(candidatesData);

        // Carregar dados de partidos
        const partiesResponse = await fetch(`${baseURL}parties`);
        const partiesData = await partiesResponse.json();
        addPoliticalParties(partiesData);

        // Carregar o total de votos
        const totalVotesResponse = await fetch(`${baseURL}votes/result`);
        const totalVotesData = await totalVotesResponse.json();
        setTotalVotes(totalVotesData);

    } catch (error) {
        console.error("Erro ao carregar dados:", error);
    }
}
