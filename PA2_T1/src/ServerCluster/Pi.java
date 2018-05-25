package ServerCluster;

import java.awt.EventQueue;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Pi extends Thread {

    private final Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private ServerGUI gui;

    public Pi(Socket socket,ServerGUI gui) {
        this.socket = socket;
        this.gui=gui;
    }

    @Override
    public void run() {
        try {
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String request = in.readLine();
            System.out.println("Request recebido pelo servidor: "+request);
            if (request != null) {
                display(request);
                String[] parts = request.split("\\|");

                int precision = Integer.parseInt(parts[4]);
                int delay = Integer.parseInt(parts[5]);

                double pi = compute(precision, delay);
                String response = "result: |" + parts[1] + "|" + parts[2] + "|02|" + parts[4] + "|" + parts[5] + "|" + pi ;
                out.println(response);
                display(response);
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

    private void display(String message) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                gui.appendEvents(message);
            }
        });
    }
}
