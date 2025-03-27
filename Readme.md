# WebSocket Chat Application (Spring Boot + STOMP + WebSockets)

This is a simple real-time chat application built using Spring Boot and WebSockets.
The application allows multiple users to send and receive messages instantly using WebSocket connections.

## Features
- ✅ Real-time chat using WebSockets
- ✅ Supports multiple users in a chatroom
- ✅ Uses STOMP (Simple Text Oriented Messaging Protocol) for WebSocket messaging
- ✅ Lightweight frontend using HTML + JavaScript
- ✅ Lombok for reducing boilerplate code
- ✅ Uses Spring Boot DevTools for faster development

## Project Structure
```
websocket-demo
│── src
│   ├── main
│   │   ├── java
│   │   │   ├── com.example.websocketdemo
│   │   │   │   ├── config
│   │   │   │   │   ├── WebSocketConfig.java   # WebSocket Configuration
│   │   │   │   ├── controller
│   │   │   │   │   ├── ChatController.java   # Handles incoming chat messages
│   │   │   │   ├── model
│   │   │   │   │   ├── ChatMessage.java      # Chat message model
│   ├── resources
│   │   ├── static
│   │   │   ├── index.html  # Chat UI
│   │   ├── application.properties  # Spring Boot configuration
│── pom.xml  # Project dependencies
```

## How the Application Works (Project Workflow)

1. **User Connects to WebSocket Server**
    - A user opens the webpage (`index.html`), and a WebSocket connection is established at `/chat`.
    - SockJS and STOMP are used to manage WebSocket communication.

2. **Sending a Message**
    - The user types a message and clicks "Send".
    - The message is sent to `/app/sendMessage` via WebSocket.
    - The `ChatController` receives the message and broadcasts it.

3. **Broadcasting the Message**
    - The server sends the message to `/topic/messages`.
    - All connected users receive the message in real time.

4. **Displaying the Message**
    - The frontend listens for new messages on `/topic/messages`.
    - When a new message arrives, it is displayed in the chat window.

## Installation & Setup

### Prerequisites
Make sure you have:
- Java 17 or later installed
- Maven installed (`mvn -version`)
- Any IDE (IntelliJ, Eclipse, VS Code)

### Clone the Repository
```sh
git clone https://github.com/your-repo/websocket-chat.git
cd websocket-chat
```

### Build and Run the Application
```sh
mvn clean package
mvn spring-boot:run
```

### Open the Chat App
Go to:
```
http://localhost:8080/index.html
```

## Code Explanation

### 1. WebSocket Configuration
#### `WebSocketConfig.java` (Handles WebSocket connections)
```java
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/chat").withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic");
        registry.setApplicationDestinationPrefixes("/app");
    }
}
```
🔹 **What It Does?**
- Registers the WebSocket endpoint `/chat`.
- Defines a message broker (`/topic`) to broadcast messages.
- Sets a prefix (`/app`) for client requests.

### 2. Chat Message Model
#### `ChatMessage.java` (Defines the chat message structure)
```java
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessage {
    private String sender;
    private String content;
}
```
🔹 **What It Does?**
- Represents a chat message with `sender` and `content`.
- Uses Lombok (`@Data`) to generate getters, setters, and constructors.

### 3. Chat Controller
#### `ChatController.java` (Handles chat messages)
```java
@Controller
public class ChatController {
    @MessageMapping("/sendMessage")
    @SendTo("/topic/messages")
    public ChatMessage sendMessage(ChatMessage message) {
        return message;
    }
}
```
🔹 **What It Does?**
- Receives messages at `/app/sendMessage`.
- Broadcasts messages to `/topic/messages` (so all clients receive them).

### 4. Frontend (Chat UI)
#### `index.html` (Chat UI with JavaScript WebSocket connection)
```html
<!DOCTYPE html>
<html lang="en">
<head>
    <script src="https://cdn.jsdelivr.net/npm/sockjs-client/dist/sockjs.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/stomp-websocket/lib/stomp.min.js"></script>
</head>
<body>
    <h2>Chat Application</h2>
    <input type="text" id="username" placeholder="Enter your name">
    <input type="text" id="message" placeholder="Enter message">
    <button onclick="sendMessage()">Send</button>
    <div id="chatBox"></div>

    <script>
        let stompClient = null;

        function connect() {
            let socket = new SockJS('/chat');
            stompClient = Stomp.over(socket);
            stompClient.connect({}, function () {
                stompClient.subscribe('/topic/messages', function (messageOutput) {
                    let message = JSON.parse(messageOutput.body);
                    document.getElementById("chatBox").innerHTML += `<p><strong>${message.sender}:</strong> ${message.content}</p>`;
                });
            });
        }

        function sendMessage() {
            let sender = document.getElementById("username").value;
            let content = document.getElementById("message").value;
            if (stompClient && sender && content) {
                stompClient.send("/app/sendMessage", {}, JSON.stringify({ sender, content }));
            }
        }

        window.onload = connect;
    </script>
</body>
</html>
```
🔹 **What It Does?**
- Connects to WebSocket (`/chat`).
- Sends messages to `/app/sendMessage`.
- Receives messages from `/topic/messages` and displays them.

## Testing the Application
1. **Open Two Browser Tabs**
    - Go to `http://localhost:8080/index.html` in two tabs.
    - Enter different names and send messages.
    - Messages appear in real time in both tabs.

## Future Enhancements
✔ Add user list to show who is online
✔ Add private messaging between users
✔ Store chat history in a database

## Summary
- ✅ WebSockets + Spring Boot for real-time chat
- ✅ Frontend (HTML + JS) for sending/receiving messages
- ✅ STOMP for WebSocket message handling
- ✅ Lombok for reducing boilerplate code
