// session.js
import { getAllParties } from './api.js';  // Importe a função de api.js

// Objeto session para armazenar informações da sessão
export const session = {
    api_url: "http://localhost:8080/DSW3-Prova1-AllamyMonteiro/api",
    partidos: [],  // Lista de partidos
    last_candidate: {},  // Objeto do tipo candidato
    political_party: {}
};

// Função para adicionar partido à sessão
export function setPartidos(partidos) {
    session.partidos = partidos;
}

// Função para definir o último candidato
export function setLastCandidate(candidato) {
    session.last_candidate = candidato;
}

// Função para obter todos os partidos da sessão
export function getPartidos() {
    return session.partidos;
}

// Função para obter o último candidato
export function getLastCandidate() {
    return session.last_candidate;
}

// Inicialização da aplicação
export function inicializar() {
    // Dispara a requisição para buscar os partidos
    console.log("entrou em inicializar()");
    getAllParties().then(partidos => {
        // Armazena a lista de partidos na sessão
        setPartidos(partidos);
        console.log('Lista de partidos armazenada na sessão:', getPartidos());
    }).catch(error => {
        console.error('Erro ao inicializar a sessão:', error);
    });
}

// Chama a função de inicialização quando a aplicação for carregada
inicializar();
