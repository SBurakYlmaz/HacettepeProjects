import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;
import java.util.Scanner;

public class CryptoUser {
    private static final CryptoLogClient sLogger = CryptoLogClient.getInstance();

    private static void executeServerOperation(String servername, String sessionKey, String ticket) {
        try {
            String serverAddress = "127.0.0.1";
            int serverPort = CryptoConst.getPort(servername);

            Base64.Encoder encoder = Base64.getEncoder();

            final long[] nonceVal1 = new long[1];
            final long[] nonceVal2 = new long[1];

            CryptoClient client = new CryptoClient(serverAddress, serverPort, new CryptoClient.CryptoClientEvent() {
                @Override
                public void onReceived(CryptoClient client, String data) {
                    try {
                        if (data.equals("OK")) {
                            sLogger.writeLine(servername + "->Alice : " + " Authentication is completed!");
                            client.disconnect();
                            return;
                        } else if (data.equals("NOK")) {
                            sLogger.writeLine(servername + "->Alice : " + " Authentication Error!");
                            client.disconnect();
                            return;
                        }

                        sLogger.writeLine(servername + "->" + "Alice : " + "Alice," + data);

                        Base64.Decoder decoder = Base64.getDecoder();
                        byte[] decodedPacket = decoder.decode(data);

                        String packet = CryptoUtil.decrypt(new String(decodedPacket), sessionKey);

                        String[] tokens = packet.split("[,]", 0);

                        if (tokens.length == 2) {
                            long nonce1 = Long.parseLong(tokens[0]);
                            long nonce2 = Long.parseLong(tokens[1]);

                            if (nonce1 != (nonceVal1[0] + 1)) {
                                System.out.println("Nonce1 error");
                                return;
                            }

                            sLogger.writeLine("Message Decrypted : N1 is OK, N2=" + nonce2);

                            nonceVal2[0] = nonce2;
                            String nonce = (nonceVal2[0] + 1) + "";
                            String nonceEncryptedWithSessionKey = CryptoUtil.encrypt(nonce, sessionKey);

                            sLogger.writeLine("Alice->" + servername + " : " + nonce);
                            sLogger.writeLine("Alice->" + servername + " : " + nonceEncryptedWithSessionKey);

                            client.send(encoder.encodeToString(nonceEncryptedWithSessionKey.getBytes()));
                        } else {
                            // Error
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onDisconnected(CryptoClient client) {
                    System.out.println("Disconnected " + servername);
                }
            });

            nonceVal1[0] = CryptoUtil.createNonce();
            String nonce = String.valueOf(nonceVal1[0]);
            String nonceEncryptedWithSessionKey = CryptoUtil.encrypt(nonce, sessionKey);
            String packet = CryptoConst.ALICE_NAME + "," + ticket + "," + nonceEncryptedWithSessionKey;

            sLogger.writeLine("Alice->" + servername + " : " + "Alice," + nonceVal1[0]);
            sLogger.writeLine("Alice->" + servername + " : " + packet);

            client.send(encoder.encodeToString(packet.getBytes()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String args[]) {
        while (true) {
            try {
                Scanner scanner = new Scanner(System.in);

                System.out.print("Enter password: ");
                String password = scanner.nextLine();

                System.out.print("Enter servername: ");
                String servername = scanner.nextLine();

                boolean isValid = false;
                switch (servername) {
                    case CryptoConst.MAIL_NAME:
                    case CryptoConst.WEB_NAME:
                    case CryptoConst.DATABASE_NAME:
                        isValid = true;
                        break;
                    default:
                        break;
                }

                if (!isValid) {
                    System.out.println("Not a valid servername\n");
                    continue;
                }

                String kdcServerAddress = "127.0.0.1";
                int kdcServerPort = CryptoConst.getPort(CryptoConst.KDC_NAME);

                Base64.Encoder encoder = Base64.getEncoder();
                final PrivateKey privateKey = CryptoFiles.readPrivateKey(CryptoConst.getPrivateKeyPath(CryptoConst.ALICE_NAME));

                CryptoClient clientKDC = new CryptoClient(kdcServerAddress, kdcServerPort, new CryptoClient.CryptoClientEvent() {
                    private boolean mMessageReceived = false;

                    @Override
                    public void onReceived(CryptoClient client, String data) {
                        try {
                            mMessageReceived = true;

                            sLogger.writeLine("KDC->Alice : Password Verified");
                            sLogger.writeLine("KDC->Alice : " + data);

                            Base64.Decoder decoder = Base64.getDecoder();
                            String[] parts = data.split("[,]", 0);

                            if (parts.length == 2) {
                                String dataAlice = parts[0];
                                String ticket = parts[1];

                                byte[] encryptedPacket = decoder.decode(dataAlice);

                                String decryptedPacket = CryptoUtil.decrypt(encryptedPacket, privateKey);
                                String[] tokens = decryptedPacket.split("[,]", 0);

                                sLogger.writeLine("Message Decrypted : " + decryptedPacket);

                                if (tokens.length == 3) {
                                    String sessionKey = tokens[0];
                                    String servername = tokens[1];
                                    long timestamp2 = Long.parseLong(tokens[2]);

                                    System.out.println(sessionKey + "," + servername + "," + timestamp2);
                                    System.out.println(ticket);

                                    executeServerOperation(servername, sessionKey, ticket);
                                    client.disconnect();
                                } else {
                                    // Error
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
                        if (!mMessageReceived) {
                            sLogger.writeLine("KDC->Alice : Password Denied");
                        }

                        System.out.println("Disconnected KDC");
                    }
                });

                String packet = CryptoConst.ALICE_NAME + "," + password + "," + servername + "," + CryptoUtil.getTimestamp();
                PublicKey publicKeyKDC = CryptoFiles.readPublicKey(CryptoConst.getPublicKeyPath(CryptoConst.KDC_NAME));
                byte[] encryptedPacket = CryptoUtil.encrypt(packet, publicKeyKDC);
                String encryptedPacketBase64 = encoder.encodeToString(encryptedPacket);

                sLogger.writeLine("Alice->KDC : " + packet);
                sLogger.writeLine("Alice->KDC : " + "Alice," +encryptedPacketBase64);

                clientKDC.send(encryptedPacketBase64);

                while (true) {
                    try {
                        if (!clientKDC.isConnected()) {
                            break;
                        }
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
