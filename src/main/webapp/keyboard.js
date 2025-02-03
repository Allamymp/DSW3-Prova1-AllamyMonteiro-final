import { session } from './session.js';  
import { display } from './display.js';  
import { fetchCandidateByParty, confirmVote, voteBlank, getAllParties } from './api.js';  

let numeroDigitado = "";  
let partidoAtual = null;  // Variável para armazenar o partido atual

export function adicionarNumero(numero) {
    if (numeroDigitado.length < 5) {
        numeroDigitado += numero;
        atualizarDisplay();

        // Verifica se o número digitado tem 5 caracteres (buscar candidato)
        if (numeroDigitado.length === 5) {
            procurarCandidato(numeroDigitado);  
        }

        // Verifica se o número digitado tem 2 caracteres (procurar partido)
        if (numeroDigitado.length === 2) {
            procurarPartido(numeroDigitado);  
        }
    }
}

function procurarPartido(numero) {
    const partido = session.partidos.find(p => p.number.toString().startsWith(numero));

    if (partido) {
        session.political_party = partido;  // Atualiza a sessão com o partido encontrado
        partidoAtual = partido;  // Armazena o partido encontrado
        atualizarDisplayPartido(partido.abbreviation);  // Atualiza o display com a sigla do partido
    } else {
        display.atualizarTexto(
            "Partido inexistente. Pressione corrige", 
            'Arial', 'larger', 'bold', 'center', 'middle'
        );
        partidoAtual = null;  // Limpa o partido atual, mas não limpa o número digitado ainda
    }
}
function procurarCandidato(numero) {
    if (!partidoAtual) {
        display.atualizarTexto(
            "Selecione um partido primeiro", 
            'Arial', 'larger', 'bold', 'center', 'middle'
        );
        return; // Se não houver partido selecionado, retorna sem fazer a requisição
    }

    // Extrai os 3 últimos dígitos do número digitado como número do candidato
    const candidateNumber = parseInt(numero.substring(2));  // Extrai os 3 últimos dígitos como número do candidato
    
    // Cria o objeto CandidateRequest
    const candidateRequest = {
        partyNumber: partidoAtual.number,  // Usa o partido armazenado
        candidateNumber: candidateNumber
    };

    // Chama a função de api.js para buscar o candidato
    fetchCandidateByParty(candidateRequest)
        .then(data => {
            if (data) {
                // Atualiza a sessão com o candidato encontrado
                session.last_candidate = data;  // Salva o candidato no atributo last_candidate
                
                // Atualiza o display com os dados do candidato
                const partido = `Partido: ${partidoAtual.abbreviation}`;  // Usa o partido atual
                const candidato = `Candidato: ${data.name}`;  // Supondo que o candidato tenha um campo 'name'
                display.atualizarTexto(
                    `${partido}\n${candidato}\nNúmero: ${numeroDigitado}`,
                    'Arial', 'larger', 'bold', 'left', 'flex-start'
                );
            } else {
                display.atualizarTexto(
                    "Candidato não encontrado", 
                    'Arial', 'larger', 'bold', 'center', 'middle'
                );
            }
        })
        .catch(error => {
            display.atualizarTexto(
                "Erro ao buscar candidato", 
                'Arial', 'larger', 'bold', 'center', 'middle'
            );
        });
}

function atualizarDisplayPartido(abbreviation) {
    const partido = `Partido: ${abbreviation}`;
    const candidato = "Candidato: [Candidato Exemplo]"; // Exemplo, será substituído após a requisição
    const numero = `Número: ${numeroDigitado}`;

    display.atualizarTexto(
        `${partido}\n${candidato}\n${numero}`,
        'Arial', 'larger', 'bold', 'left', 'flex-start'
    );
}

function atualizarDisplay() {
    const partido = partidoAtual ? `Partido: ${partidoAtual.abbreviation}` : "[Partido Exemplo]";
    const candidato = "Candidato: [Candidato Exemplo]";
    const numero = `Número: ${numeroDigitado}`;

    display.atualizarTexto(
        `${partido}\n${candidato}\n${numero}`,
        'Arial', 'larger', 'bold', 'left', 'flex-start'
    );
}

function functionCorrige() {
    numeroDigitado = ""; 
    partidoAtual = null;  // Limpa o partido atual
    session.political_party = null;  // Limpa o partido da sessão
    session.last_candidate = null;  // Limpa o candidato da sessão
    atualizarDisplay(); 

    // Recarrega os partidos e reinicia a sessão
    getAllParties()
        .then(partidos => {
            session.partidos = partidos;  // Atualiza a sessão com a lista de partidos
            display.atualizarTexto(
                "Dados limpos. Escolha um novo partido.", 
                'Arial', 'larger', 'bold', 'center', 'middle'
            );
            atualizarDisplay();  // Atualiza o display para o estado inicial
        })
        .catch(error => {
            display.atualizarTexto(
                "Erro ao carregar os partidos", 
                'Arial', 'larger', 'bold', 'center', 'middle'
            );
        });
}

function functionBranco() {
    numeroDigitado = "BRANCO";
    atualizarDisplay();
}

function confirmarVoto() {
    if (numeroDigitado === "BRANCO") {
        // Se o número for "BRANCO", chama a função para votar em branco
        votarEmBranco();
        return;
    }

    if (!partidoAtual || !session.last_candidate) {
        display.atualizarTexto(
            "Selecione um partido e candidato válidos", 
            'Arial', 'larger', 'bold', 'center', 'middle'
        );
        return;
    }

    const voteRequest = {
        partyId: partidoAtual.id,  // Supondo que o ID do partido esteja em session.political_party.id
        candidateId: session.last_candidate.id  // Supondo que o ID do candidato esteja em session.last_candidate.id
    };

    // Envia a requisição PUT para registrar o voto
    confirmVote(voteRequest)
        .then(response => {
            if (response.ok) {
                display.atualizarTexto(
                    "Voto confirmado!", 
                    'Arial', 'larger', 'bold', 'center', 'middle'
                );
                setTimeout(resetarVotacao, 2000);  // Adiciona um delay de 3 segundos antes de resetar a votação
            } else {
                display.atualizarTexto(
                    "Erro ao confirmar voto", 
                    'Arial', 'larger', 'bold', 'center', 'middle'
                );
            }
        })
        .catch(error => {
            display.atualizarTexto(
                "Erro ao confirmar voto", 
                'Arial', 'larger', 'bold', 'center', 'middle'
            );
        });
}

function votarEmBranco() {
    // Chama a função de api.js para enviar o voto em branco
                display.atualizarTexto(
                    "Voto em branco registrado com sucesso!", 
                    'Arial', 'larger', 'bold', 'center', 'middle'
                );
                setTimeout(resetarVotacao, 2000);  // Adiciona um delay de 3 segundos antes de resetar a votação
            
}

function resetarVotacao() {
    // Limpa a sessão
    session.political_party = null;
    session.last_candidate = null;
    session.partidos = [];  // Limpa a lista de partidos

    // Recarrega os partidos
    getAllParties()
        .then(partidos => {
            session.partidos = partidos; // Atualiza a sessão com a lista de partidos
            // Volta para a tela de bolas (tela inicial ou de escolha de partido)
            window.location.href = 'index.html';  // Redireciona para a página inicial
        })
        .catch(error => {
            display.atualizarTexto(
                "Erro ao carregar os partidos", 
                'Arial', 'larger', 'bold', 'center', 'middle'
            );
        });
}

window.addEventListener('DOMContentLoaded', () => {
    const buttons = document.querySelectorAll('.botao-num');
    buttons.forEach(button => {
        button.addEventListener('click', (event) => {
            const numero = event.target.textContent;
            adicionarNumero(numero);
        });
    });

    document.getElementById('btnBranco').addEventListener('click', functionBranco);
    document.getElementById('btnCorrige').addEventListener('click', functionCorrige);
    document.getElementById('btnConfirma').addEventListener('click', confirmarVoto); // Chama a função para confirmar o voto
});
