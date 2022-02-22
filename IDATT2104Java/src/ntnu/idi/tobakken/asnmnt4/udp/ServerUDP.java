package ntnu.idi.tobakken.asnmnt4.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class ServerUDP {
    public static void main(String[] args) throws IOException {
        new CalcServerThread().start();
    }



}

class CalcServerThread extends Thread {

    DatagramSocket socket;

    public CalcServerThread () throws IOException{
        socket = new DatagramSocket(4445);
    }

    public void run() {
        byte[] numbers = new byte[3];
        while (true) {
            try {
                System.out.println("Waiting for packet");

                byte[] buf = new byte[256];
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                socket.receive(packet);
                for (int i = 0; i < 3; i++) {
                    numbers[i] = packet.getData()[i];
                }
                InetAddress address = packet.getAddress();
                int port = packet.getPort();

                System.out.println(getPacketString(packet));

                byte[] bufOut = evaluate(numbers).getBytes();
                packet = new DatagramPacket(bufOut, bufOut.length, address, port);
                socket.send(packet);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private String getPacketString (DatagramPacket pack){
        return new String(pack.getData(), 0, pack.getLength());
    }

    private String evaluate (byte[] numbers){
        if (numbers[1] == '+') {
            int res = (numbers[0]-48) + (numbers[2]-48);
            return "Result of addition = " + res;
        } else if (numbers[1] == '-') {
            int res = (numbers[0]-48) - (numbers[2]-48);
            return "Result of subtraction = " + res;
        } else return "Something went wrong";
    }
}
