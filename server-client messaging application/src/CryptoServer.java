import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class CryptoServer {
    private static final int AES_KEY_SIZE = 16;
    private static final int DES_KEY_SIZE = 8;
    private final int mPort;
    private final Map<String, CryptoClient> mClientMap;
    private ServerSocket mServerSocket;
    private boolean mRunning;
    private byte[] mAESIV;
    private byte[] mAESKey;
    private byte[] mDESIV;
    private byte[] mDESKey;

    public CryptoServer(int port) {
        this.mPort = port;
        this.mClientMap = new HashMap<>();

        init();
    }

    public static void main(String[] args) {
        CryptoServer server = new CryptoServer(10000);

        while (true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.err.println(e.toString());
            }
        }
    }

    private void init() {
        mAESIV = CryptoUtilServer.createRandomIV(AES_KEY_SIZE);
        mAESKey = CryptoUtilServer.createRandomKey(AES_KEY_SIZE);

        mDESIV = CryptoUtilServer.createRandomIV(DES_KEY_SIZE);
        mDESKey = CryptoUtilServer.createRandomKey(DES_KEY_SIZE);

        mRunning = true;

        Runnable runnable = () -> {
            try {
                mServerSocket = new ServerSocket(mPort);

                CryptoLog logger = CryptoLog.getInstance();

                while (mRunning) {
                    Socket socket = mServerSocket.accept();

                    CryptoClient client = new CryptoClient(socket, new CryptoClientEvent() {
                        @Override
                        public void onReceived(CryptoClient client, String name, String data) {
                            if (data == null) {
                                if (!mClientMap.containsKey(name)) {
                                    mClientMap.put(name, client);
                                }

                                client.send("AESIV " + CryptoUtilServer.toBase64(mAESIV));
                                client.send("AESKey " + CryptoUtilServer.toBase64(mAESKey));

                                client.send("DESIV " + CryptoUtilServer.toBase64(mDESIV));
                                client.send("DESKey " + CryptoUtilServer.toBase64(mDESKey));

                                logger.writeLine("Received: " + name);
                                logger.writeLine("Sent: " + "AESIV " + CryptoUtilServer.toBase64(mAESIV));
                                logger.writeLine("Sent: " + "AESKey " + CryptoUtilServer.toBase64(mAESKey));
                                logger.writeLine("Sent: " + "DESIV " + CryptoUtilServer.toBase64(mDESIV));
                                logger.writeLine("Sent: " + "DESKey " + CryptoUtilServer.toBase64(mDESKey));
                            } else {
                                logger.writeLine("Received: " + name + " " + data);

                                for (Map.Entry<String, CryptoClient> entry : mClientMap.entrySet()) {
                                    if (!name.equalsIgnoreCase(entry.getKey())) {
                                        entry.getValue().send(name + " " + data);
                                    } else {
                                        if (!mClientMap.containsKey(name)) {
                                            mClientMap.put(name, client);
                                        }

                                        entry.getValue().send(name + " " + data);
                                    }
                                }

                                logger.writeLine("Sent: " + name + " " + data);
                            }
                        }

                        @Override
                        public void onDisconnected(String name) {
                            if (name != null) {
                                CryptoClient client = mClientMap.remove(name);

                                logger.writeLine("Disconnected: " + name);

                                if (client != null) {
                                    client.send("");
                                    client.disconnect();
                                }
                            }
                        }
                    });

                    client.start();
                }
            } catch (Exception e) {
                System.out.println(e);
            }
        };

        Thread connThread = new Thread(runnable);
        connThread.start();
    }

    interface CryptoClientEvent {
        void onReceived(CryptoClient client, String name, String data);

        void onDisconnected(String name);
    }

    class CryptoClient implements Runnable {
        private final Socket mSocket;
        private final CryptoClientEvent mEvent;
        private PrintWriter mPrintWriter;
        private BufferedReader mBufferedReader;

        private String mName;

        public CryptoClient(Socket socket, CryptoClientEvent event) {
            mSocket = socket;
            mEvent = event;
            mName = null;

            try {
                mPrintWriter = new PrintWriter(socket.getOutputStream(), true);
            } catch (Exception e) {
                System.err.println(e.toString());
            }
        }

        public void start() {
            Thread thread = new Thread(this);
            thread.start();
        }

        @Override
        public void run() {
            try {
                final InputStream inputStream = mSocket.getInputStream();
                mBufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                String str;

                while ((str = mBufferedReader.readLine()) != null) {
                    if (str.isEmpty())
                        break;

                    String[] strArray = str.split("\\s+");

                    if (strArray.length < 2) {
                        String name = strArray[0];

                        if (mName == null)
                            mName = name;

                        // System.out.println(str);

                        if (mEvent != null)
                            mEvent.onReceived(this, name, null);
                    } else {
                        String name = strArray[0];
                        String data = strArray[1];

                        if (mName == null)
                            mName = name;

                        // System.out.println(str);

                        if (mEvent != null)
                            mEvent.onReceived(this, name, data);
                    }
                }

                if (mEvent != null)
                    mEvent.onDisconnected(mName);
            } catch (Exception e) {
                System.err.println(e.toString());

                if (mEvent != null)
                    mEvent.onDisconnected(mName);
            }
        }

        public void send(String str) {
            try {
                mPrintWriter.println(str);
            } catch (Exception e) {
                System.err.println(e.toString());
            }
        }

        public void disconnect() {
            try {
                mPrintWriter.close();
                mBufferedReader.close();
                mSocket.close();
            } catch (Exception e) {
                System.err.println(e.toString());
            }
        }

        public String getName() {
            return mName;
        }

        public Socket getSocket() {
            return mSocket;
        }
    }
}
