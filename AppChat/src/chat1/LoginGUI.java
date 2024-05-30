package chat1;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginGUI extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JCheckBox termsCheckBox;
    private JButton loginButton;
    private JButton forgotPasswordButton;

    public LoginGUI() {
        setTitle("Đăng nhập");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Tạo giao diện
        JPanel contentPane = new JPanel();
        contentPane.setLayout(new BorderLayout(10, 10));

        // Tên người dùng và mật khẩu
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.add(createUsernameField());
        formPanel.add(createPasswordField());
        contentPane.add(formPanel, BorderLayout.CENTER);

        // Checkbox điều khoản
        JPanel termsPanel = new JPanel();
        termsPanel.add(createTermsCheckBox());
        contentPane.add(termsPanel, BorderLayout.SOUTH);

        // Nút đăng nhập và quên mật khẩu
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(createLoginButton());
        buttonPanel.add(createForgotPasswordButton());
        contentPane.add(buttonPanel, BorderLayout.NORTH);

        setContentPane(contentPane);

        // Xử lý sự kiện click nút "Đăng nhập"
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Kiểm tra thông tin đăng nhập và chuyển sang class khác nếu hợp lệ
                if (isValidLogin()) {
                    // Chuyển sang class DashboardGUI
                    new DashboardGUI();
                    dispose(); // Đóng LoginGUI
                } else {
                    JOptionPane.showMessageDialog(LoginGUI.this, "Tên người dùng hoặc mật khẩu không hợp lệ.");
                }
            }
        });

        // Xử lý sự kiện click nút "Quên mật khẩu"
        forgotPasswordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Chuyển sang class ForgotPasswordGUI
                new ForgotPasswordGUI();
                dispose(); // Đóng LoginGUI
            }
        });

        setVisible(true);
    }

    private JPanel createUsernameField() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.add(new JLabel("Tên người dùng:"));
        usernameField = new JTextField(20);
        panel.add(usernameField);
        return panel;
    }

    private JPanel createPasswordField() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.add(new JLabel("Mật khẩu:"));
        passwordField = new JPasswordField(20);
        panel.add(passwordField);
        return panel;
    }

    private JPanel createTermsCheckBox() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        termsCheckBox = new JCheckBox("Tôi đồng ý với các điều khoản.");
        panel.add(termsCheckBox);
        return panel;
    }

    private JButton createLoginButton() {
        loginButton = new JButton("Đăng nhập");
        return loginButton;
    }

    private JButton createForgotPasswordButton() {
        forgotPasswordButton = new JButton("Quên mật khẩu");
        return forgotPasswordButton;
    }

    private boolean isValidLogin() {
        // Viết code kiểm tra thông tin đăng nhập tại đây
        // Trả về true nếu thông tin đăng nhập hợp lệ
        return true;
    }

    public static void main(String[] args) {
        new LoginGUI();
    }
}

class DashboardGUI extends JFrame {
    public DashboardGUI() {
        setTitle("Dashboard");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Tạo giao diện dashboard
        JPanel contentPane = new JPanel(new GridLayout(2, 2, 10, 10));
        contentPane.add(new JButton("Quản lý người dùng"));
        contentPane.add(new JButton("Quản lý sản phẩm"));
        contentPane.add(new JButton("Quản lý đơn hàng"));
        contentPane.add(new JButton("Thống kê"));
        setContentPane(contentPane);

        setVisible(true);
    }
}

class ForgotPasswordGUI extends JFrame {
    private JTextField emailField;
    private JButton resetButton;

    public ForgotPasswordGUI() {
        setTitle("Quên mật khẩu");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Tạo giao diện quên mật khẩu
        JPanel contentPane = new JPanel(new BorderLayout(10, 10));

        JPanel formPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        formPanel.add(new JLabel("Nhập email:"));
        emailField = new JTextField(20);
        formPanel.add(emailField);
        contentPane.add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        resetButton = new JButton("Đặt lại mật khẩu");
        buttonPanel.add(resetButton);
        contentPane.add(buttonPanel, BorderLayout.SOUTH);

        setContentPane(contentPane);

        setVisible(true);
    }
}