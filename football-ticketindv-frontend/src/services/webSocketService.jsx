import { Client } from '@stomp/stompjs';

class WebSocketService {
    constructor() {
        this.client = null;
        this.subscriptions = new Map();
    }

    connect(onConnected, onError) {
        this.client = new Client({
            brokerURL: 'ws://localhost:8080/ws',
            connectHeaders: {},
            debug: function (str) {
                console.log('STOMP: ' + str);
            },
            reconnectDelay: 5000,
            heartbeatIncoming: 4000,
            heartbeatOutgoing: 4000,
            onConnect: () => {
                console.log('WebSocket Connected');
                if (onConnected) onConnected();
            },
            onDisconnect: () => {
                console.log('WebSocket Disconnected');
            },
            onStompError: (frame) => {
                console.error('STOMP Error:', frame);
                if (onError) onError(frame);
            }
        });

        this.client.activate();
    }

    subscribeToSeatUpdates(matchId, callback) {
        if (!this.client || !this.client.connected) {
            console.error('STOMP client not connected');
            return;
        }

        const subscription = this.client.subscribe(
            '/topic/seat-updates',
            (message) => {
                const seatUpdate = JSON.parse(message.body);
                console.log('Received seat update:', seatUpdate);
                
                if (seatUpdate.matchId === matchId) {
                    
                    if (seatUpdate.action === 'DESELECT' && seatUpdate.expired) {
                        console.log('Seat selection expired:', seatUpdate);
                    }
                    callback(seatUpdate);
                }
            }
        );

        this.subscriptions.set(matchId, subscription);
    }

    unsubscribeFromSeatUpdates(matchId) {
        const subscription = this.subscriptions.get(matchId);
        if (subscription) {
            subscription.unsubscribe();
            this.subscriptions.delete(matchId);
        }
    }

    sendSeatSelection(matchId, seatId, action, userId) {
        if (!this.client || !this.client.connected) {
            console.error('STOMP client not connected');
            return;
        }

        const message = {
            matchId,
            seatId,
            action,
            userId,
            timestamp: Date.now()
        };

        console.log('Sending seat selection:', message);

        this.client.publish({
            destination: '/app/seat-selection',
            body: JSON.stringify(message)
        });
    }

    
    isConnected() {
        return this.client && this.client.connected;
    }

    
    getActiveSubscriptions() {
        return Array.from(this.subscriptions.keys());
    }

    disconnect() {
        if (this.client) {
            console.log('Disconnecting WebSocket...');
            
            this.subscriptions.forEach((subscription, matchId) => {
                console.log(`Unsubscribing from match ${matchId}`);
                subscription.unsubscribe();
            });
            this.subscriptions.clear();
            
           
            this.client.deactivate();
            console.log('WebSocket disconnected');
        }
    }

    
    reconnect() {
        if (this.client) {
            console.log('Attempting to reconnect...');
            this.client.deactivate();
            this.client.activate();
        }
    }

    
    handleConnectionError() {
        console.log('Connection error, attempting to reconnect...');
        setTimeout(() => this.reconnect(), 5000); // 5 seconds
    }
}

export const webSocketService = new WebSocketService();