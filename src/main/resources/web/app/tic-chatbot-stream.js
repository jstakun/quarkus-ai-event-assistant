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
        this.timeoutId = null; // Add a property to hold the timeout ID
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

            // Clear the timeout when a message is received
            this._clearTimeout();

            this._hideProgressBar();

            if (data === "[DONE]") {
                streamingText = "";
                return;
            }

            streamingText += data;

            // Replace the last item with updated text
            const updatedItems = [...this._chatItems];
            if (
                updatedItems.length > 0 &&
                updatedItems[updatedItems.length - 1].userName === "A007"
            )  {
                // Update existing streaming message
                updatedItems[updatedItems.length - 1] = {
                    ...updatedItems[updatedItems.length - 1],
                    text: streamingText
                };
            } else {
                // Add a new streaming message
                updatedItems.push({
                    text: streamingText,
                    userName: "A007",
                    userColorIndex: 1
                });
            }

            this._chatItems = updatedItems; // Triggers re-render
        });
    }

    disconnectedCallback() {
        super.disconnectedCallback();
        console.log("Received disconnect callback");
        this._clearTimeout(); // Clear timeout on disconnect
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
        return html`
            <div class="chat">
                <vaadin-message-list .items="${this._chatItems}"></vaadin-message-list>
                <vaadin-progress-bar class="${this._progressBarClass}" indeterminate></vaadin-progress-bar>
                <vaadin-message-input @submit="${this._handleSendChat}"></vaadin-message-input>
            </div>`;
    }

    _addBotMessage(message) {
        this._addMessage(message, "AI", 3);
    }

    _addUserMessage(message) {
        this._addMessage(message, "Me", 1);
    }

    _addStyledMessage(message, user, colorIndex, className) {
        let newItem = this._createNewItem(message, user, colorIndex);
        newItem.className = className;
        this._addMessageItem(newItem);
    }

    _addMessage(message, user, colorIndex) {
        this._addMessageItem(this._createNewItem(message, user, colorIndex));
    }

    _hideProgressBar() {
        this._progressBarClass = "hidden";
    }

    _showProgressBar() {
        this._progressBarClass = "show";
    }

    // New method to handle the timeout
    _handleTimeout() {
        this._hideProgressBar();
        this._chatItems = [
            ...this._chatItems,
            {
                text: 'Przykro mi, ale nie dostałem żadnej odpowiedzi od modelu. Spróbuj ponownie!',
                userName: 'A007',
                userColorIndex: 1
            }
        ];
        this.requestUpdate();
    }
    
    // New method to clear the timeout
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
            
            // Start the 60-second timeout
            this._clearTimeout();
            this.timeoutId = setTimeout(() => {
                this._handleTimeout();
            }, 60000); // 60 seconds

            this.ws.send(message);
        }
    }
}

customElements.define('tic-chatbot-stream', TicChatbotStream);