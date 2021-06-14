import java.io.*;
import java.net.Socket;

public class CryptoClient {
    private final String mServerAddress;
    private final int mServerPort;
    private final CryptoClientEvent mEvent;

    private Socket mSocket;
    private PrintWriter mPrintWriter;
    private BufferedReader mBufferedReader;
    private boolean mConnected;

    public CryptoClient(String serverAddress, int serverPort, CryptoClientEvent event) {
        mServerAddress = serverAddress;
        mServerPort = serverPort;
        mEvent = event;
        mConnected = false;

        init();
    }

    private void init() {
        try {
            mSocket = new Socket(mServerAddress, mServerPort);

            final InputStream inputStream = mSocket.getInputStream();
            mBufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            mPrintWriter = new PrintWriter(mSocket.getOutputStream(), true);

            mConnected = true;

            Runnable runnable = () -> {
                try {
                    String data;

                    while ((data = mBufferedReader.readLine()) != null) {
                        if (data.isEmpty())
                            break;

                        if (mEvent != null)
                            mEvent.onReceived(this, data);
                    }

                    mEvent.onDisconnected(this);
                    mConnected = false;
                } catch (Exception e) {
                    mEvent.onDisconnected(this);
                    mConnected = false;
                }
            };

            Thread thread = new Thread(runnable);
            thread.start();
        } catch (IOException e) {
            System.err.println(e.toString());
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

    public boolean isConnected() {
        return mConnected;
    }

    public interface CryptoClientEvent {
        void onReceived(CryptoClient client, String data);

        void onDisconnected(CryptoClient client);
    }
}
