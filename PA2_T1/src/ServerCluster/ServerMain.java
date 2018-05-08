/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ServerCluster;

import javax.swing.JTextField;

/**
 *
 * @author david
 */

public class ServerMain {

    public static void main(String[] args) {
        int serverId = 1;

        ServerGUI serverGUI = new ServerGUI();
        serverGUI.changeServerID(serverId);
        serverGUI.setVisible(true);

        Server server = new Server(serverId);

        serverGUI.getConnectToMonitorButton().addActionListener((java.awt.event.ActionEvent evt) -> {
            JTextField ipTextBox = serverGUI.getIpTextBox();
            JTextField portMonitorTextBox = serverGUI.getPortMonitorTextBox();
            if (!ipTextBox.getText().equals("") && portMonitorTextBox.getText().length() >= 4) {
                server.connectToMonitor(ipTextBox.getText(), Integer.parseInt(portMonitorTextBox.getText()));
                ipTextBox.setEditable(false);
                portMonitorTextBox.setEditable(false);
                serverGUI.getConnectToMonitorButton().setEnabled(false);
            }
        });

        serverGUI.getConnectToRequestsButton().addActionListener((java.awt.event.ActionEvent evt) -> {
            JTextField portRequestsTextBox = serverGUI.getPortRequestsTextBox();
            if (portRequestsTextBox.getText().length() >= 4) {
                server.connectToRequestsHandler(Integer.parseInt(portRequestsTextBox.getText()), serverGUI.getMessagesTextArea());
                portRequestsTextBox.setEditable(false);
                serverGUI.getConnectToRequestsButton().setEnabled(false);
                if (!serverGUI.getQueueButton().isEnabled()) {
                    serverGUI.getIpTextBox().setEditable(true);
                    serverGUI.getPortMonitorTextBox().setEditable(true);
                    serverGUI.getConnectToMonitorButton().setEnabled(true);
                }
            }
        });

        serverGUI.getQueueButton().addActionListener((java.awt.event.ActionEvent evt) -> {
            JTextField queueSizeTextBox = serverGUI.getQueueSizeTextBox();
            if (queueSizeTextBox.getText().length() != 0) {
                server.setQueueSize(Integer.parseInt(queueSizeTextBox.getText()));
                queueSizeTextBox.setEditable(false);
                serverGUI.getQueueButton().setEnabled(false);
                if (!serverGUI.getConnectToRequestsButton().isEnabled()) {
                    serverGUI.getIpTextBox().setEditable(true);
                    serverGUI.getPortMonitorTextBox().setEditable(true);
                    serverGUI.getConnectToMonitorButton().setEnabled(true);
                }
            }
        });
    }
}
