// Establish a WebSocket connection to the echo server
const ws = new WebSocket('ws://localhost:4545');
// Add a listener that will be triggered when the WebSocket is ready to use
ws.addEventListener('open', () => {
  const message = 'Hello!';
  console.log('Sending:', message);
});