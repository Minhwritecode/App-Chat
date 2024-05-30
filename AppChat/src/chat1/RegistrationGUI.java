package chat1;

import javax.swing.*;
import java.awt.*;
import java.security.SecureRandom;
import java.util.Random;

import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import javax.swing.*;

public class RegistrationGUI extends JFrame {
    private JTextField nameField, emailField, passwordField, confirmPasswordField;
    private JLabel verificationCodeLabel;
    private JButton registerButton;
    private String verificationCode;

    public RegistrationGUI() {
        setTitle("Register");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 450);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Name field
        JPanel namePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel nameLabel = new JLabel("Full Name:");
        nameField = new JTextField(20);
        namePanel.add(nameLabel);
        namePanel.add(nameField);

        // Email field
        JPanel emailPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel emailLabel = new JLabel("Email:");
        emailField = new JTextField(20);
        emailPanel.add(emailLabel);
        emailPanel.add(emailField);

        // Password field
        JPanel passwordPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField(20);
        passwordPanel.add(passwordLabel);
        passwordPanel.add(passwordField);

        // Confirm password field
        JPanel confirmPasswordPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel confirmPasswordLabel = new JLabel("Confirm Password:");
        confirmPasswordField = new JPasswordField(20);
        confirmPasswordPanel.add(confirmPasswordLabel);
        confirmPasswordPanel.add(confirmPasswordField);

        // Verification code
        JPanel verificationPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel verificationLabel = new JLabel("Verification Code:");
        verificationCodeLabel = new JLabel();
        generateVerificationCode();
        verificationPanel.add(verificationLabel);
        verificationPanel.add(verificationCodeLabel);

        // Register button
        registerButton = new JButton("Register");
        registerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText();
                String email = emailField.getText();
                String password = new String(((JPasswordField) passwordField).getPassword());
                String confirmPassword = new String(((JPasswordField) confirmPasswordField).getPassword());
                String code = verificationCodeLabel.getText();

                // Validate input and register user
                if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || code.isEmpty()) {
                    JOptionPane.showMessageDialog(RegistrationGUI.this, "Please fill in all the fields.");
                } else if (!password.equals(confirmPassword)) {
                    JOptionPane.showMessageDialog(RegistrationGUI.this, "Passwords do not match.");
                } else if (!code.equals(verificationCode)) {
                    JOptionPane.showMessageDialog(RegistrationGUI.this, "Incorrect verification code.");
                } else {
                    // Register user and navigate to login page
                    JOptionPane.showMessageDialog(RegistrationGUI.this, "Registration successful!");
                    dispose();
                    new LoginGUI().setVisible(true);
                }
            }
        });

        mainPanel.add(Box.createVerticalStrut(20));
        mainPanel.add(namePanel);
        mainPanel.add(Box.createVerticalStrut(20));
        mainPanel.add(emailPanel);
        mainPanel.add(Box.createVerticalStrut(20));
        mainPanel.add(passwordPanel);
        mainPanel.add(Box.createVerticalStrut(20));
        mainPanel.add(confirmPasswordPanel);
        mainPanel.add(Box.createVerticalStrut(20));
        mainPanel.add(verificationPanel);
        mainPanel.add(Box.createVerticalStrut(20));
        mainPanel.add(registerButton);

        add(mainPanel, BorderLayout.CENTER);
    }

    private void generateVerificationCode() {
        Random random = new Random();
        verificationCode = String.format("%04d", random.nextInt(10000));
        verificationCodeLabel.setText(verificationCode);
    }

    public static void main(String[] args) {
        new RegistrationGUI().setVisible(true);
    }
}