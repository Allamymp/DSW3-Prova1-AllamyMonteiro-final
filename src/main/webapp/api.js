// api.js
export function getAllParties() {
    console.log("entrou em getAllParties()");

    return new Promise((resolve, reject) => {
        console.log("entrou em promise()");
        const url = "http://localhost:8080/DSW3-Prova1-AllamyMonteiro/api/parties";
        console.log("Chamando API em:", url);
        const xhr = new XMLHttpRequest();

        xhr.open('GET', url, true);

        xhr.onload = function () {
            if (xhr.status === 200) {
                const partidos = JSON.parse(xhr.responseText);
                resolve(partidos);  // Resolva a Promise com os dados dos partidos
            } else {
                reject('Erro ao obter os partidos: ' + xhr.statusText);
            }
        };

        xhr.onerror = function () {
            reject('Erro de rede ao tentar fazer a requisição');
        };

        xhr.send();
    });
}

export function fetchCandidateByParty(candidateRequest) {
    const xmlData = `
        <CandidateRequest>
            <partyNumber>${candidateRequest.partyNumber}</partyNumber>
            <candidateNumber>${candidateRequest.candidateNumber}</candidateNumber>
        </CandidateRequest>
    `;

    return fetch("http://localhost:8080/DSW3-Prova1-AllamyMonteiro/api/votes/", {
        method: "POST",
        headers: {
            "Content-Type": "application/xml"
        },
        body: xmlData
    })
    .then(response => response.json())
    .then(data => {
        const candidato = {
            name: data.name,
            id: data.id
        };

        return candidato;
    })
    .catch(error => {
        console.error("Erro ao buscar candidato:", error);
        throw error;
    });
}

export function confirmVote(voteRequest) {
    const xmlData = `
        <vote>
            <partyId>${voteRequest.partyId}</partyId>
            <candidateId>${voteRequest.candidateId}</candidateId>
        </vote>
    `;

    return fetch("http://localhost:8080/DSW3-Prova1-AllamyMonteiro/api/votes/addVote", {
        method: "POST",
        headers: {
            "Content-Type": "application/xml"
        },
        body: xmlData
    });
}

export function voteBlank() {
    const xmlData = `<vote><partyId>0</partyId><candidateId>0</candidateId></vote>`;  // Enviando voto em branco

    return fetch("http://localhost:8080/DSW3-Prova1-AllamyMonteiro/api/votes/blank", {
        method: "POST",
        headers: {
            "Content-Type": "application/xml"
        },
        body: xmlData
    });
}
