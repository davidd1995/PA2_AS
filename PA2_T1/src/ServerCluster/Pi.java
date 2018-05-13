/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ServerCluster;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Pi extends Thread {

    private final String request;
    private final Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    public Pi(Socket socket, String request) {
        this.socket = socket;
        this.request = request;
    }

    @Override
    public void run() {
        try {
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String request = in.readLine();
            if (request != null) {
                //display(request);
                String[] parts = request.split("\\|");

                int precision = Integer.parseInt(parts[4]);
                int delay = Integer.parseInt(parts[5]);

                double pi = compute(precision, delay);
                String response = "result: |" + parts[1] + "|" + parts[2] + "|02|" + parts[4] + "|" + parts[5] + "|" + pi + "|";
                out.println(response);
                //display(response);
            }

            socket.close();
            out.close();
            in.close();
        } catch (IOException ex) {
        }
    }

    public Double compute(double iterations, double delay) {

        double pi = 0;
        double denominator = 1;

        for (int n = 0; n < iterations; n++) {
            pi = (n % 2 == 0) ? pi + (1. / denominator) : pi - (1. / denominator);
            denominator += 2;
        }
        try {
            Thread.sleep((long) (delay * 1000));
        } catch (Exception e) {
        }
        return pi * 4;
    }

    /*private void display(final String message) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                messagesTextArea.setText(messagesTextArea.getText() + message + "\n");
            }
        });
    }*/
}
