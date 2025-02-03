// display.js
class DisplayText {
    constructor(elementId) {
        this.displayElement = document.getElementById(elementId);
    }

    atualizarTexto(texto, fontFamily = 'Arial, sans-serif', fontSize = 'larger', fontWeight = 'bold', textAlign = 'center', verticalAlign = 'middle') {
        this.displayElement.textContent = texto;
        this.displayElement.style.fontFamily = fontFamily;
        this.displayElement.style.fontSize = fontSize;
        this.displayElement.style.fontWeight = fontWeight;
        this.displayElement.style.textAlign = textAlign;
        this.displayElement.style.display = 'flex';
        this.displayElement.style.justifyContent = textAlign === 'center' ? 'center' : 'flex-start';
        this.displayElement.style.alignItems = verticalAlign === 'middle' ? 'center' : 'flex-start';
    }
}

// Cria uma instância da classe DisplayText
const display = new DisplayText("display_text");

// Exporte a instância `display`
export { display };

// Exemplo inicial para o display
display.atualizarTexto("Bem-vindo à Urna Eletrônica", 'Arial', 'larger', 'bold', 'center', 'middle');
