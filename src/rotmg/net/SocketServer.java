package rotmg.net;

import utils.symmetric.ICipher;
import rotmg.AGameSprite;
import rotmg.messaging.GameServerConnectionConcrete;
import rotmg.net.impl.Message;
import rotmg.net.impl.MessageCenter;
import rotmg.parameters.Parameters;
import rotmg.util.AssetLoader;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * This class is a very loose implementation of WildShadow's SocketServer,
 * it is more closely related to The Force 2477's RealmClient
 */
public class SocketServer {

    public static SocketServer instance;
    public static int MESSAGE_LENGTH_SIZE_IN_BYTES = 4;
    public MessageCenter messages = MessageCenter.getInstance();
    public Socket socket = null;
    public long lastTimePacketReceived = 0;
    public long lastPingTime = 0;
    private int bufferIndex = 0;
    private boolean write = false;
    private boolean read = false;
    private long startTime = 0;
    private ICipher outgoingCipher; //Renamed from 'ICipher'.
    private ICipher incomingCipher;
    private String server; // host
    private int port;
    private DataInputStream inputStream = null;
    private DataOutputStream outputStream = null;
    private byte[] buffer = new byte[100000];
    private BlockingDeque<Message> packetQueue = new LinkedBlockingDeque<>();

    public static SocketServer getInstance() {
        if (instance == null) {
            instance = new SocketServer();
        }

        return instance;
    }

    public static void main(String[] args) {

        new AssetLoader().load();

        Server s = new Server().setAddress("54.67.119.179").setPort(Parameters.PORT);

        AGameSprite a = new AGameSprite();

        GameServerConnectionConcrete g = new GameServerConnectionConcrete(a, s, -1, false, 404, -1, new byte[0], null,
                false);

        g.connect();

    }

    public SocketServer setOutgoingCipher(ICipher param1) {
        this.outgoingCipher = param1;
        return this;
    }

    public SocketServer setIncomingCipher(ICipher param1) {
        this.incomingCipher = param1;
        return this;
    }

    public void connect(String server, int port) {
        this.bufferIndex = 0;
        this.buffer = new byte[100000];
        this.packetQueue.clear();

        this.server = server;
        this.port = port;

        System.out.println("Connecting to " + server + ":" + port + ".");

        try {
            this.socket = new Socket(server, port);
            this.socket.setReuseAddress(true);

            this.inputStream = new DataInputStream(this.socket.getInputStream());
            this.outputStream = new DataOutputStream(this.socket.getOutputStream());
            this.startTime = System.currentTimeMillis();
            this.startThreadedListener();
            this.startThreadedWriter();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        ///

    }

    private void startThreadedListener() {

        this.read = true;
        new Thread("threadedListenner") {

            @Override
            public void run() {
                Socket sock = SocketServer.this.socket;
                try {
                    while ((sock != null) && sock.isConnected() && SocketServer.this.read && !sock.isClosed()) {
                        Thread.sleep(20);
                        int bytesRead = SocketServer.this.inputStream.read(SocketServer.this.buffer, SocketServer.this.bufferIndex, SocketServer.this.buffer.length - SocketServer.this.bufferIndex);
                        if (bytesRead == -1) {
                            if ((SocketServer.this.packetQueue != null) && (SocketServer.this.packetQueue.size() > 0)) {
                                SocketServer.this.packetQueue.clear();
                                System.err.println("EOF");
                            }
                            break;
                        } else if (bytesRead > 0) {

                            SocketServer.this.lastTimePacketReceived = System.currentTimeMillis();
                            SocketServer.this.bufferIndex += bytesRead;
                            while (SocketServer.this.bufferIndex >= 5) {
                                int packetLength = ByteBuffer.allocate(4).put(SocketServer.this.buffer[0]).put(SocketServer.this.buffer[1])
                                        .put(SocketServer.this.buffer[2]).put(SocketServer.this.buffer[3]).rewind().getInt();
                                if (SocketServer.this.buffer.length < packetLength) {
                                    SocketServer.this.buffer = Arrays.copyOf(SocketServer.this.buffer, packetLength);
                                }
                                if (SocketServer.this.bufferIndex < packetLength) {
                                    break;
                                }
                                byte packetId = SocketServer.this.buffer[4];
                                byte[] packetBytes = new byte[packetLength - 5];
                                System.arraycopy(SocketServer.this.buffer, 5, packetBytes, 0, packetLength - 5);
                                if (SocketServer.this.bufferIndex > packetLength) {
                                    System.arraycopy(SocketServer.this.buffer, packetLength, SocketServer.this.buffer, 0, SocketServer.this.bufferIndex - packetLength);
                                }
                                SocketServer.this.bufferIndex -= packetLength;
                                SocketServer.this.incomingCipher.cipher(packetBytes);

                                Message m = SocketServer.this.messages.require(packetId);

                                if (m == null) {
                                    System.err.println("FATAL: Null packet... Id : " + packetId);
                                } else {
                                    //System.out.println("Received a " + m.getClass().getSimpleName() + " packet.");
                                    m.parseFromInput(new DataInputStream(new ByteArrayInputStream(packetBytes)));
                                    m.consume();
                                }


                            }
                        }

                        int lastPacket = (int) ((System.currentTimeMillis() - SocketServer.this.lastTimePacketReceived));

						/* if (lastPacket > 2000 && !packetQueue.isEmpty()) {
						    reconnect(
						        "Timeout, nothing received in " + lastPacket + "ms.");
						}**/
                    }

                } catch (Exception ex) {

                    ex.printStackTrace();

                }
            }
        }.start();
    }

    private void startThreadedWriter() {
        this.write = true;
        new Thread("threadedWriter") {
            @Override
            public void run() {
                while ((SocketServer.this.socket != null) && SocketServer.this.socket.isConnected() && SocketServer.this.write) {
                    long start = System.currentTimeMillis();
                    while (!SocketServer.this.packetQueue.isEmpty()) {
                        if ((SocketServer.this.packetQueue != null) && (SocketServer.this.packetQueue.size() > 0)) {
                            Message p = null;
                            p = SocketServer.this.packetQueue.peekLast();
                            if (p != null) {
                                SocketServer.this.sendMessage(p);
                                SocketServer.this.packetQueue.removeLast();
                            } else {
                                break;
                            }
                        }
                    }
                    int time = (int) (System.currentTimeMillis() - start);
                    try {
                        Thread.sleep(20 - (time > 20 ? 0 : time));
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }

            }
        }.start();
    }

    /**
     * Send directly the message
     */
    public void sendMessage(Message packet) {
        try {
            if (this.write) {
                byte[] packetBytes = packet.getBytes();
                this.outgoingCipher.cipher(packetBytes);
                int packetLength = packetBytes.length + 5;
                this.outputStream.writeInt(packetLength);
                this.outputStream.writeByte(packet.id);
                this.outputStream.write(packetBytes);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}