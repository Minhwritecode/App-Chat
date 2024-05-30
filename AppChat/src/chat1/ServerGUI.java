package chat1;

import java.awt.BorderLayout;
import java.io.*;
import java.net.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class ServerGUI extends JFrame implements Runnable {

    private static final int PORT = 1345;
    private JTextArea chatArea;
    private JButton startButton, stopButton;
    private ServerSocket serverSocket;
    private boolean isRunning;

    public ServerGUI() {
        setTitle("Chat Server");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 600, 400);
        JPanel contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(new BorderLayout(0, 0));

        chatArea = new JTextArea();
        chatArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(chatArea);
        contentPane.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        startButton = new JButton("Start Server");
        stopButton = new JButton("Stop Server");
        buttonPanel.add(startButton);
        buttonPanel.add(stopButton);
        contentPane.add(buttonPanel, BorderLayout.SOUTH);

        startButton.addActionListener(e -> startServer());
        stopButton.addActionListener(e -> stopServer());
    }

    private void startServer() {
        new Thread(this).start();
        isRunning = true;
        chatArea.append("Server started on port " + PORT + "\n");
        startButton.setEnabled(false);
        stopButton.setEnabled(true);
    }

    private void stopServer() {
        isRunning = false;
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        chatArea.append("Server stopped.\n");
        startButton.setEnabled(true);
        stopButton.setEnabled(false);
    }

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(PORT);
            while (isRunning) {
                Socket clientSocket = serverSocket.accept();
                handleClient(clientSocket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleClient(Socket clientSocket) {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

            String message = in.readLine();
            chatArea.append(getTimeStamp() + " " + message + "\n");
            out.println("Server: Message received.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getTimeStamp() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        return "[" + formatter.format(LocalDateTime.now()) + "]";
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
        	ServerGUI frame = new ServerGUI();
            frame.setVisible(true);
        });
    }
}