const net = require('net');
const crypto = require('crypto');

const generateWsAccept = acceptKey =>
crypto
.createHash("sha1")
.update(acceptKey + "258EAFA5-E914-47DA-95CA-C5AB0DC85B11", "binary")
.digest("base64");

//Simple HTTP server responds with a simple WebSocket client test
const httpServer = net.createServer((connection) => {
    connection.on('data', () => {
        let content = `<!DOCTYPE html>
        <html>
          <head>
            <meta charset="UTF-8" />
          </head>
          <body>
            <script>
            let ws = new WebSocket('ws://localhost:3001');
            ws.onmessage = event => document.getElementById("chat").innerHTML = document.getElementById("chat").innerHTML + "</br> Friend: "+ event.data;
            const message = () => {
              let msg = document.getElementById("message").value
              ws.send(msg);
              document.getElementById("chat").innerHTML = document.getElementById("chat").innerHTML + "</br> Me: "+ msg;
            }
            </script>
            <h1>WebSocket test page</h1>
            <label for="lname">Write your message:</label></br>
            <input type="text" id="message"><br><br>
            <input type="submit" value="Submit" onclick="return message();">
            <div id="chat" class="div">
            </div>
          </body>
        </html>
        `;
        connection.write('HTTP/1.1 200 OK\r\nContent-Length: ' + content.length + '\r\n\r\n' + content)
    });
});
httpServer.listen(3000, () => {
    console.log('HTTP server listening on port 3000');
});


//Handle mulitple clients
let clients = [];

const wsServer = net.createServer((connection) => {
    console.log('Client connected');

    connection.on('data', (data) => {
        if (data.toString()[0] == "G") {
            var key = data.toString().substring(data.toString().indexOf("-Key: ") + 6, data.toString().indexOf("==") + 2);

            var acceptValue = generateWsAccept(key);

            const responds = [
                "HTTP/1.1 101 Web Socket Protocol Handhsake",
                "Upgrade: websocket",
                "Connection: Upgrade",
                "Sec-WebSocket-Accept:" + acceptValue
            ];
            connection.write(responds.join("\r\n") + "\r\n\r\n");
            clients.push(connection);
            console.log("Handshake completed");
        }
        else {
            let message = "";
            let length = data[1] & 127;
            let maskStart = 2;
            let dataStart = maskStart + 4;
            for (let i = dataStart; i < dataStart + length; i++){
                let byte = data[i] ^ data[maskStart + ((i-dataStart) % 4)];
                message += String.fromCharCode(byte);
            }
            console.log(message);
            sendToAllClients(message, connection);
        }
    });

    const sendToAllClients = (message, c) => {
        let buffer = Buffer.concat([
            new Buffer.from([
                0x81,
                "0x" + 
                (message.length + 0x10000)
                    .toString(16)
                    .substr(-2)
                    .toUpperCase()
            ]),
            Buffer.from(message)
        ]);
        console.log(buffer);
        clients.forEach(client => {
            if (c != client) client.write(buffer)
        });
    };

    connection.on('end', () => {
        console.log('Client disconnected');
    });
});
wsServer.on('error', (error) => {
    console.log('Error: ', error);
});
wsServer.listen(3001, () => {
    console.log('Websocket server listening on port 3001');
});

