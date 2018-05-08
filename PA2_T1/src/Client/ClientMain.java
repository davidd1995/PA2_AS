package Client;

import java.awt.event.ActionListener;
import java.util.Random;
import javax.swing.JTextField;

/**
 *
 * @author david
 */
public class ClientMain {

    public static void main(String[] args) {
        int clientId = 1;
        int requestId = 1;

        ClientGUI clientGUI = new ClientGUI();
        clientGUI.changeClientID(clientId);
        clientGUI.setVisible(true);

        Client client = new Client(clientId, requestId);

        clientGUI.getConnectButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JTextField ipTextBox = clientGUI.getIPTextBox();
                JTextField portTextBox = clientGUI.getPortTextBox();
                if (!ipTextBox.getText().equals("") && portTextBox.getText().length() >= 4) {
                    client.setIpAddress(ipTextBox.getText());
                    client.setPort(Integer.parseInt(portTextBox.getText()));
                    boolean flag = client.connectToSocket();
                    if (flag) {
                        ipTextBox.setEditable(false);
                        portTextBox.setEditable(false);
                        clientGUI.getConnectButton().setEnabled(false);
                        clientGUI.getPrecisionTextBox().setEditable(true);
                        clientGUI.getDelayTextBox().setEditable(true);
                        clientGUI.getSendButton().setEnabled(true);
                    }
                }
            }
        });

        clientGUI.getSendButton().addActionListener((java.awt.event.ActionEvent evt) -> {
            JTextField precisionTextBox = clientGUI.getPrecisionTextBox();
            JTextField delayTextBox = clientGUI.getDelayTextBox();
            if (precisionTextBox.getText().length() != 0 && delayTextBox.getText().length() != 0) {
                int precision = Integer.parseInt(precisionTextBox.getText());
                int delay = Integer.parseInt(delayTextBox.getText());
                precisionTextBox.setEditable(false);
                delayTextBox.setEditable(false);
                clientGUI.getSendButton().setEnabled(false);
                client.sendMessage(precision, delay, clientGUI);
            }
        });
    }

}
