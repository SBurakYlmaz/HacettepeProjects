import javax.swing.*;

public class CryptoClientFrame extends javax.swing.JFrame {
    private String mServerAddress;
    private int mServerPort;

    private byte[] mAESIV;
    private byte[] mAESKey;

    private byte[] mDESIV;
    private byte[] mDESKey;

    private String mUserName;
    private CryptoClient mClient;
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JRadioButton aesRadioButton;
    private javax.swing.JRadioButton cbcRadioButton;
    private javax.swing.JButton connectButton;
    private javax.swing.JPanel cryptedTextPanel;
    private javax.swing.JTextArea cryptedTextTextArea;
    private javax.swing.JRadioButton desRadioButton;
    private javax.swing.JButton disconnectButton;
    private javax.swing.JButton encryptButton;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JMenuBar mainMenuBar;
    private javax.swing.JTextArea messageTextArea;
    private javax.swing.ButtonGroup methodButtonGroup;
    private javax.swing.JPanel methodPanel;
    private javax.swing.ButtonGroup modeButtonGroup;
    private javax.swing.JPanel modePanel;
    private javax.swing.JRadioButton ofbRadioButton;
    private javax.swing.JPanel plainTextPanel;
    private javax.swing.JTextArea plainTextTextArea;
    private javax.swing.JButton sendButton;
    private javax.swing.JMenu serverMenu;

    /**
     * Creates new form CryptoClientFrame
     */
    public CryptoClientFrame() {
        initComponents();

        init();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(CryptoClientFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(CryptoClientFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(CryptoClientFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(CryptoClientFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            CryptoClientFrame frame = new CryptoClientFrame();
            frame.setVisible(true);
            frame.setResizable(false);
        });
    }

    private void init() {
        mServerAddress = "127.0.0.1";
        mServerPort = 10000;

        connectButton.setEnabled(true);
        disconnectButton.setEnabled(false);

        messageTextArea.setEnabled(false);
        cryptedTextTextArea.setEnabled(false);

        sendButton.setEnabled(false);
        encryptButton.setEnabled(false);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        methodButtonGroup = new javax.swing.ButtonGroup();
        modeButtonGroup = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        messageTextArea = new javax.swing.JTextArea();
        connectButton = new javax.swing.JButton();
        disconnectButton = new javax.swing.JButton();
        methodPanel = new javax.swing.JPanel();
        aesRadioButton = new javax.swing.JRadioButton();
        desRadioButton = new javax.swing.JRadioButton();
        modePanel = new javax.swing.JPanel();
        cbcRadioButton = new javax.swing.JRadioButton();
        ofbRadioButton = new javax.swing.JRadioButton();
        plainTextPanel = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        plainTextTextArea = new javax.swing.JTextArea();
        cryptedTextPanel = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        cryptedTextTextArea = new javax.swing.JTextArea();
        encryptButton = new javax.swing.JButton();
        sendButton = new javax.swing.JButton();
        mainMenuBar = new javax.swing.JMenuBar();
        serverMenu = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Crypto Messenger");
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        messageTextArea.setColumns(20);
        messageTextArea.setRows(5);
        jScrollPane1.setViewportView(messageTextArea);

        connectButton.setText("Connect");
        connectButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                connectButtonActionPerformed(evt);
            }
        });

        disconnectButton.setText("Disconnect");
        disconnectButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                disconnectButtonActionPerformed(evt);
            }
        });

        methodPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Method"));

        methodButtonGroup.add(aesRadioButton);
        aesRadioButton.setSelected(true);
        aesRadioButton.setText("AES");

        methodButtonGroup.add(desRadioButton);
        desRadioButton.setText("DES");

        javax.swing.GroupLayout methodPanelLayout = new javax.swing.GroupLayout(methodPanel);
        methodPanel.setLayout(methodPanelLayout);
        methodPanelLayout.setHorizontalGroup(
                methodPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(methodPanelLayout.createSequentialGroup()
                                .addComponent(aesRadioButton)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 36, Short.MAX_VALUE)
                                .addComponent(desRadioButton)
                                .addContainerGap())
        );
        methodPanelLayout.setVerticalGroup(
                methodPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(methodPanelLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(methodPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(aesRadioButton)
                                        .addComponent(desRadioButton))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        modePanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Mode"));

        modeButtonGroup.add(cbcRadioButton);
        cbcRadioButton.setSelected(true);
        cbcRadioButton.setText("CBC");

        modeButtonGroup.add(ofbRadioButton);
        ofbRadioButton.setText("OFB");

        javax.swing.GroupLayout modePanelLayout = new javax.swing.GroupLayout(modePanel);
        modePanel.setLayout(modePanelLayout);
        modePanelLayout.setHorizontalGroup(
                modePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(modePanelLayout.createSequentialGroup()
                                .addComponent(cbcRadioButton, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 35, Short.MAX_VALUE)
                                .addComponent(ofbRadioButton)
                                .addContainerGap())
        );
        modePanelLayout.setVerticalGroup(
                modePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(modePanelLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(modePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(cbcRadioButton)
                                        .addComponent(ofbRadioButton))
                                .addContainerGap(21, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(48, 48, 48)
                                .addComponent(connectButton, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(50, 50, 50)
                                .addComponent(disconnectButton, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(62, 62, 62)
                                .addComponent(methodPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(52, 52, 52)
                                .addComponent(modePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addComponent(jScrollPane1)
        );
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addContainerGap()
                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                        .addComponent(modePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(methodPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addGap(37, 37, 37)
                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(connectButton, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(disconnectButton, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 26, Short.MAX_VALUE)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 327, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        plainTextPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Text"));

        plainTextTextArea.setColumns(20);
        plainTextTextArea.setRows(5);
        jScrollPane2.setViewportView(plainTextTextArea);

        javax.swing.GroupLayout plainTextPanelLayout = new javax.swing.GroupLayout(plainTextPanel);
        plainTextPanel.setLayout(plainTextPanelLayout);
        plainTextPanelLayout.setHorizontalGroup(
                plainTextPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 245, Short.MAX_VALUE)
        );
        plainTextPanelLayout.setVerticalGroup(
                plainTextPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 144, Short.MAX_VALUE)
        );

        cryptedTextPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Crypted Text"));

        cryptedTextTextArea.setColumns(20);
        cryptedTextTextArea.setRows(5);
        jScrollPane3.setViewportView(cryptedTextTextArea);

        javax.swing.GroupLayout cryptedTextPanelLayout = new javax.swing.GroupLayout(cryptedTextPanel);
        cryptedTextPanel.setLayout(cryptedTextPanelLayout);
        cryptedTextPanelLayout.setHorizontalGroup(
                cryptedTextPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 253, Short.MAX_VALUE)
        );
        cryptedTextPanelLayout.setVerticalGroup(
                cryptedTextPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 144, Short.MAX_VALUE)
        );

        encryptButton.setText("Encrypt");
        encryptButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                encryptButtonActionPerformed(evt);
            }
        });

        sendButton.setText("Send");
        sendButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sendButtonActionPerformed(evt);
            }
        });

        serverMenu.setText("Server");
        mainMenuBar.add(serverMenu);

        setJMenuBar(mainMenuBar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(plainTextPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(12, 12, 12)
                                .addComponent(cryptedTextPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(encryptButton, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 21, Short.MAX_VALUE)
                                .addComponent(sendButton, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(encryptButton, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(sendButton, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGap(64, 64, 64))
                                        .addGroup(layout.createSequentialGroup()
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(cryptedTextPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(plainTextPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGap(0, 0, Short.MAX_VALUE))))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void connectButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_connectButtonActionPerformed
        mUserName = JOptionPane.showInputDialog(this, "Enter user name:");

        if (mUserName != null) {
            try {
                mClient = new CryptoClient(mServerAddress, mServerPort, new CryptoClient.CryptoClientEvent() {
                    @Override
                    public void onReceived(String name, String data) {
                        if (name.equalsIgnoreCase("AESIV")) {
                            mAESIV = CryptoUtil.fromBase64(data);
                        } else if (name.equalsIgnoreCase("AESKey")) {
                            mAESKey = CryptoUtil.fromBase64(data);
                        } else if (name.equalsIgnoreCase("DESIV")) {
                            mDESIV = CryptoUtil.fromBase64(data);
                        } else if (name.equalsIgnoreCase("DESKey")) {
                            mDESKey = CryptoUtil.fromBase64(data);
                        } else {
                            byte[] key = null;
                            byte[] iv = null;

                            CryptoMethod cryptoMethod = CryptoMethod.AES;
                            CryptoMode cryptoMode = CryptoMode.CBC;

                            if (aesRadioButton.isSelected()) {
                                cryptoMethod = CryptoMethod.AES;
                                key = mAESKey;
                                iv = mAESIV;
                            } else if (desRadioButton.isSelected()) {
                                cryptoMethod = CryptoMethod.DES;
                                key = mDESKey;
                                iv = mDESIV;
                            }

                            if (cbcRadioButton.isSelected()) {
                                cryptoMode = CryptoMode.CBC;
                            } else if (ofbRadioButton.isSelected()) {
                                cryptoMode = CryptoMode.OFB;
                            }

                            System.out.println(name + ": " + data);
                            byte[] cipherData = CryptoUtil.fromBase64(data);
                            byte[] plainData = CryptoUtil.decrypt(cipherData, iv, key, cryptoMethod, cryptoMode);
                            String plainText = new String(plainData);

                            messageTextArea.append(name + ": " + data + "\n");
                            messageTextArea.append(name + ": " + plainText + "\n");
                        }
                    }

                    @Override
                    public void onDisconnected() {
                        System.out.println("Disconnected");
                    }
                });

                mClient.send(mUserName);

                connectButton.setEnabled(false);
                disconnectButton.setEnabled(true);
                sendButton.setEnabled(true);
                encryptButton.setEnabled(true);
            } catch (Exception e) {
                System.err.println(e.toString());
            }
        }
    }//GEN-LAST:event_connectButtonActionPerformed

    private void disconnectButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_disconnectButtonActionPerformed

        if (mClient != null) {
            mClient.send("");
            mClient.disconnect();
        }
        connectButton.setEnabled(true);
        disconnectButton.setEnabled(false);
        sendButton.setEnabled(false);
        encryptButton.setEnabled(false);
    }//GEN-LAST:event_disconnectButtonActionPerformed

    private void encryptButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_encryptButtonActionPerformed
        String plainText = plainTextTextArea.getText().trim();

        if (!plainText.isEmpty()) {
            byte[] key = null;
            byte[] iv = null;

            CryptoMethod cryptoMethod = CryptoMethod.AES;
            CryptoMode cryptoMode = CryptoMode.CBC;

            if (aesRadioButton.isSelected()) {
                cryptoMethod = CryptoMethod.AES;
                key = mAESKey;
                iv = mAESIV;
            } else if (desRadioButton.isSelected()) {
                cryptoMethod = CryptoMethod.DES;
                key = mDESKey;
                iv = mDESIV;
            }

            if (cbcRadioButton.isSelected()) {
                cryptoMode = CryptoMode.CBC;
            } else if (ofbRadioButton.isSelected()) {
                cryptoMode = CryptoMode.OFB;
            }

            byte[] cipherText = CryptoUtil.encrypt(plainText.getBytes(), iv, key, cryptoMethod, cryptoMode);
            String cipherTextBase64 = CryptoUtil.toBase64(cipherText);

            cryptedTextTextArea.setText(cipherTextBase64);
        } else {

        }

        plainTextTextArea.setText("");
    }//GEN-LAST:event_encryptButtonActionPerformed

    private void sendButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sendButtonActionPerformed
        String cipherText = cryptedTextTextArea.getText();

        if (!cipherText.isEmpty()) {
            mClient.send(mUserName + " " + cipherText);
        } else {

        }
    }//GEN-LAST:event_sendButtonActionPerformed
    // End of variables declaration//GEN-END:variables
}