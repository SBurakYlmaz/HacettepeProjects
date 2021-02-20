import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.PrivateKey;
import java.util.Base64;

public class CryptoServerWeb {
    private final int mPort;
    private ServerSocket mServerSocket;
    private boolean mRunning;

    public CryptoServerWeb(int port) {
        this.mPort = port;
        init();
    }

    private void init() {
        mRunning = true;

        Runnable runnable = () -> {
            try {
                mServerSocket = new ServerSocket(mPort);

                final CryptoLogWeb logger = CryptoLogWeb.getInstance();
                final String servername = CryptoConst.WEB_NAME;

                final PrivateKey privateKey = CryptoFiles.readPrivateKey(CryptoConst.getPrivateKeyPath(servername));
                final Base64.Encoder encoder = Base64.getEncoder();

                while (mRunning) {
                    Socket socket = mServerSocket.accept();

                    CryptoClient client = new CryptoClient(socket, new CryptoClientEvent() {
                        private int mMessageId = 0;
                        private long mNonce1;
                        private long mNonce2;
                        private String mSessionKey;

                        @Override
                        public void onReceived(CryptoClient client, String data) {
                            try {
                                Base64.Decoder decoder = Base64.getDecoder();
                                byte[] decodedPacket = decoder.decode(data);

                                String packet = new String(decodedPacket);

                                String[] tokens = packet.split("[,]", 0);

                                switch (mMessageId) {
                                    case 0:
                                        logger.writeLine("Alice->" + servername + " : " + packet);

                                        if (tokens.length == 3) {
                                            String username = tokens[0];
                                            String ticket = tokens[1];
                                            String nonceEncryptedWithSessionKey = tokens[2];

                                            String ticketDecryptedPacket = CryptoUtil.decrypt(decoder.decode(ticket), privateKey);
                                            String[] ticketTokens = ticketDecryptedPacket.split("[,]", 0);

                                            logger.writeLine("Ticket Decrypted : " + ticketDecryptedPacket);

                                            if (ticketTokens.length == 4) {
                                                if (!username.equals(ticketTokens[0])) {
                                                    System.out.println("Username's are not the same");
                                                    client.send("NOK");
                                                    return;
                                                }

                                                if (!ticketTokens[1].equals(servername)) {
                                                    System.out.println("Servername's are not the same");
                                                    client.send("NOK");
                                                    return;
                                                }

                                                mSessionKey = ticketTokens[3];

                                                mNonce1 = Long.parseLong(CryptoUtil.decrypt(nonceEncryptedWithSessionKey, mSessionKey));
                                                mNonce2 = CryptoUtil.createNonce();
                                                String nonce = (mNonce1 + 1) + "," + mNonce2;

                                                String noncesWithSessionKey = CryptoUtil.encrypt(nonce, mSessionKey);
                                                String noncesWithSessionKeyBase64 = encoder.encodeToString(noncesWithSessionKey.getBytes());

                                                logger.writeLine("Message Decrypted : N1=" + mNonce1);
                                                logger.writeLine(servername + "->Alice : " + nonce);
                                                logger.writeLine(servername + "->Alice : " + noncesWithSessionKeyBase64);

                                                client.send(noncesWithSessionKeyBase64);
                                            }
                                        } else {
                                            // Error
                                        }
                                        break;
                                    case 1:
                                        logger.writeLine("Alice->" + servername + " : " + data);

                                        if (tokens.length == 1) {
                                            String nonceEncryptedWithSessionKey = tokens[0];

                                            long nonce2 = Long.parseLong(CryptoUtil.decrypt(nonceEncryptedWithSessionKey, mSessionKey));

                                            logger.writeLine("Message Decrypted : " + nonce2);

                                            if (nonce2 != (mNonce2 + 1)) {
                                                System.out.println("Nonce2 error");
                                                client.send("NOK");
                                                return;
                                            }

                                            logger.writeLine(servername + "->Alice : " + "Authentication is completed");
                                            client.send("OK");
                                        } else {
                                            // Error
                                        }
                                        break;
                                    default:
                                        break;
                                }

                                mMessageId++;
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onDisconnected(CryptoClient client) {
                            logger.writeLine("Disconnected: ");

                            if (client != null) {
                                client.send("");
                                client.disconnect();
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
        void onReceived(CryptoClient client, String data);

        void onDisconnected(CryptoClient client);
    }

    class CryptoClient implements Runnable {
        private final Socket mSocket;
        private final CryptoClientEvent mEvent;
        private PrintWriter mPrintWriter;
        private BufferedReader mBufferedReader;

        public CryptoClient(Socket socket, CryptoClientEvent event) {
            mSocket = socket;
            mEvent = event;

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

                String data;

                while ((data = mBufferedReader.readLine()) != null) {
                    if (data.isEmpty())
                        break;

                    if (mEvent != null)
                        mEvent.onReceived(this, data);
                }

                if (mEvent != null)
                    mEvent.onDisconnected(this);
            } catch (Exception e) {
                System.err.println(e.toString());

                if (mEvent != null)
                    mEvent.onDisconnected(this);
            }
        }

        public void send(String data) {
            try {
                mPrintWriter.println(data);
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

        public Socket getSocket() {
            return mSocket;
        }
    }
}

