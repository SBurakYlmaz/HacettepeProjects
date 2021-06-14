import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;

public class CryptoServerKDC {
    private final int mPort;
    private ServerSocket mServerSocket;
    private boolean mRunning;

    public CryptoServerKDC(int port) {
        this.mPort = port;
        init();
    }

    private void init() {
        mRunning = true;

        Runnable runnable = () -> {
            try {
                mServerSocket = new ServerSocket(mPort);

                final CryptoLogKDC logger = CryptoLogKDC.getInstance();

                final PrivateKey privateKey = CryptoFiles.readPrivateKey(CryptoConst.getPrivateKeyPath(CryptoConst.KDC_NAME));
                final Base64.Encoder encoder = Base64.getEncoder();

                while (mRunning) {
                    Socket socket = mServerSocket.accept();

                    CryptoClient client = new CryptoClient(socket, new CryptoClientEvent() {
                        @Override
                        public void onReceived(CryptoClient client, String data) {
                            try {
                                logger.writeLine("Alice->KDC : " + "Alice," + data);//burası

                                Base64.Decoder decoder = Base64.getDecoder();
                                byte[] encryptedPacket = decoder.decode(data);

                                String decryptedPacket = CryptoUtil.decrypt(encryptedPacket, privateKey);
                                String[] tokens = decryptedPacket.split("[,]", 0);

                                logger.writeLine("Message Decrypted : " + decryptedPacket);

                                if (tokens.length == 4) {
                                    String username = tokens[0];
                                    String password = tokens[1];
                                    String servername = tokens[2];
                                    long timestamp = Long.parseLong(tokens[3]);

                                    System.out.println(username + "," + password + "," + servername + "," + timestamp);

                                    if (CryptoConst.PASSWD_SHA1.equals(CryptoUtil.createSHA1HashBase64(password))) {
                                        String sessionKey = CryptoUtil.createSessionKey();
                                        long timestamp2 = CryptoUtil.getTimestamp();

                                        logger.writeLine("KDC->Alice : Password Verified");
                                        logger.writeLine("KDC->Alice : " + sessionKey + "," + servername + "," + timestamp2);

                                        switch (servername) {
                                            case CryptoConst.MAIL_NAME:
                                            case CryptoConst.WEB_NAME:
                                            case CryptoConst.DATABASE_NAME:
                                                String packetAlice = sessionKey + "," + servername + "," + timestamp2;
                                                PublicKey publicKeyAlice = CryptoFiles.readPublicKey(CryptoConst.getPublicKeyPath(CryptoConst.ALICE_NAME));
                                                byte[] encryptedPacketAlice = CryptoUtil.encrypt(packetAlice, publicKeyAlice);

                                                String packet = CryptoConst.ALICE_NAME + "," + servername + "," + timestamp2 + "," + sessionKey;
                                                PublicKey publicKeyServer = CryptoFiles.readPublicKey(CryptoConst.getPublicKeyPath(servername));
                                                byte[] encryptedPacketServer = CryptoUtil.encrypt(packet, publicKeyServer);

                                                String content = encoder.encodeToString(encryptedPacketAlice) + "," + encoder.encodeToString(encryptedPacketServer);
                                                logger.writeLine("KDC->Alice : " + content);
                                                client.send(content);
                                                break;
                                            default:
                                                break;
                                        }
                                    } else {
                                        logger.writeLine("KDC->Alice : Password Denied");
                                        client.send("");
                                    }
                                } else {
                                    // Error
                                }
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

