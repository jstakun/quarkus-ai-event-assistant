import {css, html, LitElement} from 'lit';
import '@vaadin/progress-bar';
import '@vaadin/grid';
import '@vaadin/grid/vaadin-grid-sort-column.js';
import '@vaadin/button';
import '@vaadin/icon';
import '@vaadin/icons';
import '@vaadin/split-layout';
import '@vaadin/details';
import '@vaadin/upload';
import '@vaadin/message-input';
import '@vaadin/message-list';

const i18n = {
    'en': {
        'noResponse': 'No response from model. Please try again!'
    },
    'es': {
        'noResponse': 'Sin respuesta del modelo'
    },
    'fr': {
        'noResponse': 'Aucune réponse du modèle'
    },
    'pl': {
        'noResponse': 'Przykro mi, ale nie dostałem żadnej odpowiedzi od modelu. Spróbuj ponownie!'
    },    
};

const componentI18n = {
  'en': {
    messageInput: {
      send: 'Send',
      message: 'Message',
    },
  },
  'es': {
    messageInput: {
      send: 'Enviar',
      message: 'Mensaje',
    },
  },
  'fr': {
    messageInput: {
      send: 'Envoyer',
      message: 'Message',
    },
  },
  'pl': {
    messageInput: {
      send: 'Wyślij',
      message: 'Wiadomość',
    },
  }
};

export class TicChatbotStream extends LitElement {
    static styles = css`
        :host {
            display: flex;
            gap: 10px;
            width: 80%;
            height: 80%;
            margin: auto;
            justify-content: start;
            flex-direction: column;
            background: ghostwhite;
            overflow: auto;
        }

        .hidden {
            visibility: hidden;
        }

        .show {
            visibility: visible;
        }
    `;

    static properties = {
        _chatItems: {state: true},
        _progressBarClass: {state: true},
    }

    constructor() {
        super();
        this._chatItems = [];
        this._progressBarClass = "hidden";
        this.ws = null;
        this.timeoutId = null;
        this.locale = this._getLocale();
    }

    connectedCallback() {
        super.connectedCallback();
        const protocol = window.location.protocol === 'http:' ? 'ws' : 'wss';
        let hostname = window.location.hostname;
        if (window.location.port !== '') {
            hostname += ":" + window.location.port;
        }

        this.ws = new WebSocket(`${protocol}://${hostname}/chat/guest`);

        let streamingText = "";

        this.ws.addEventListener("message", (event) => {
            const data = event.data;

            this._clearTimeout();
            this._hideProgressBar();

            if (data === "[DONE]") {
                streamingText = "";
                return;
            }

            streamingText += data;

            const updatedItems = [...this._chatItems];
            if (
                updatedItems.length > 0 &&
                updatedItems[updatedItems.length - 1].userName === "A007"
            )  {
                updatedItems[updatedItems.length - 1] = {
                    ...updatedItems[updatedItems.length - 1],
                    text: streamingText
                };
            } else {
                updatedItems.push({
                    text: streamingText,
                    userName: "A007",
                    userColorIndex: 1
                });
            }

            this._chatItems = updatedItems;
        });
    }

    disconnectedCallback() {
        super.disconnectedCallback();
        console.log("Received disconnect callback");
        this._clearTimeout();
    }

    render() {
        if (this._chatItems) {
            return html`${this._renderChat()}`;
        } else {
            return html`Loading
            <vaadin-progress-bar indeterminate></vaadin-progress-bar>`;
        }
    }

    _renderChat() {
        // Get the translated object for vaadin-message-input
        const messageInputI18n = this._getComponentTranslation('messageInput');

        return html`
            <div class="chat">
                <vaadin-message-list .items="${this._chatItems}"></vaadin-message-list>
                <vaadin-progress-bar class="${this._progressBarClass}" indeterminate></vaadin-progress-bar>
                <vaadin-message-input
                    .i18n="${messageInputI18n}"
                    @submit="${this._handleSendChat}">
                </vaadin-message-input>
            </div>`;
    }

    _getLocale() {
        const lang = navigator.language || 'en';
        return lang.split('-')[0];
    }

    _getTranslation(key) {
        return i18n[this.locale]?.[key] || i18n['en'][key];
    }
    
    // New method for component translations
    _getComponentTranslation(key) {
        return componentI18n[this.locale]?.[key] || componentI18n['en'][key];
    }

    _hideProgressBar() {
        this._progressBarClass = "hidden";
    }

    _showProgressBar() {
        this._progressBarClass = "show";
    }

    _handleTimeout() {
        this._hideProgressBar();
        this._chatItems = [
            ...this._chatItems,
            {
                text: this._getTranslation('noResponse'),
                userName: 'A007',
                userColorIndex: 1
            }
        ];
        this.requestUpdate();
    }
    
    _clearTimeout() {
        if (this.timeoutId) {
            clearTimeout(this.timeoutId);
            this.timeoutId = null;
        }
    }

    _handleSendChat(e) {
        let message = e.detail.value;
        if (message && message.trim().length > 0) {
            this._chatItems = [
                ...this._chatItems,
                {
                    text: message,
                    userName: "Me",
                    userColorIndex: 0
                }
            ];
            this.requestUpdate();
            this._showProgressBar();
            
            this._clearTimeout();
            this.timeoutId = setTimeout(() => {
                this._handleTimeout();
            }, 60000);

            this.ws.send(message);
        }
    }
}

customElements.define('tic-chatbot-stream', TicChatbotStream);