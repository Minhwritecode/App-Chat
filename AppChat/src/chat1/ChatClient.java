package chat1;
import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.TargetDataLine;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

public class ChatClient extends JFrame {
    private JTextField messageField;
    private JTextArea chatArea;
    private JScrollPane chatScroll;
    private JButton sendButton, fileButton, audioButton, stickerButton;
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private Thread receiveThread;
    private TargetDataLine targetDataLine;
    private boolean isRecording = false;
    private JList<String> userList;
    private DefaultListModel<String> userListModel;
    public ChatClient() {
        setTitle("Chat Client");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Layout
        JPanel contentPane = new JPanel(new BorderLayout());
        chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatScroll = new JScrollPane(chatArea);
        contentPane.add(chatScroll, BorderLayout.CENTER);
        

        JPanel bottomPanel = new JPanel(new FlowLayout());
        messageField = new JTextField(40);
        sendButton = new JButton("Send");
        fileButton = new JButton("File");
        audioButton = new JButton("Audio");
        stickerButton = new JButton("Sticker");
        bottomPanel.add(messageField);
        bottomPanel.add(sendButton);
        bottomPanel.add(fileButton);
        bottomPanel.add(audioButton);
        bottomPanel.add(stickerButton);
        contentPane.add(bottomPanel, BorderLayout.SOUTH);

        setContentPane(contentPane);

        // Sự kiện
        sendButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });

        fileButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                sendFile();
            }
        });

        audioButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	recordAudio();
            }
        });

        stickerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                sendSticker();
            }
        });

        connectToServer();
    }

    private void connectToServer() {
        try {
            socket = new Socket("192.168.1.4", 1345);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            // Nhận danh sách người dùng
            String users = in.readLine();
            String[] userArray = users.split(",");
            for (String user : userArray) {
                userListModel.addElement(user);
            }

            // Nhận và hiển thị tin nhắn
            new Thread(() -> {
                try {
                    while (true) {
                        String message = in.readLine();
                        chatArea.append(message + "\n");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendMessage() {
        String message = messageField.getText().trim(); // Lấy nội dung của messageField, bỏ khoảng trắng ở đầu và cuối
        if (!message.isEmpty()) {
            out.write(message); // Gửi tin nhắn qua out
			chatArea.append("You: " + message + "\n"); // Thêm tin nhắn vào chatArea
			chatArea.setCaretPosition(chatArea.getDocument().getLength()); // Cuộn xuống tin nhắn mới nhất
			messageField.setText(""); // Xóa nội dung của messageField
        }
    }
    private void sendSticker() {
    	JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Image Files", "png", "jpg", "gif","jfif"));
        int result = fileChooser.showOpenDialog(this);
        
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            
            // Hiển thị icon đã chọn trên giao diện
            ImageIcon selectedIcon = new ImageIcon(selectedFile.getPath());
            
            // Điều chỉnh kích thước của ảnh
            int maxWidth = 200; // Thiết lập kích thước tối đa cho ảnh
            int maxHeight = 200;
            Image image = selectedIcon.getImage();
            int width = image.getWidth(null);
            int height = image.getHeight(null);
            if (width > maxWidth || height > maxHeight) {
                double ratio = Math.min((double)maxWidth/width, (double)maxHeight/height);
                int newWidth = (int)(width * ratio);
                int newHeight = (int)(height * ratio);
                image = image.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
                selectedIcon = new ImageIcon(image);
            }
            
            // Tạo một JLabel mới với ImageIcon đã điều chỉnh
            JLabel iconLabel = new JLabel(selectedIcon);
            
            // Thêm JLabel vào chatArea
            chatArea.append(" [You sent an icon]\n");
            chatArea.setLayout(new FlowLayout(FlowLayout.LEFT));
            chatArea.add(iconLabel);
            chatArea.validate();
            chatArea.repaint();
        }
    }
    private void sendFile() {
        // Create a JFileChooser to allow the user to select a file
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Image and Video Files", "png", "jpg", "gif", "jfif", "mp4","pgf","pptx"));
        int result = fileChooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            String filePath = selectedFile.getPath();
            String fileName = selectedFile.getName();

            // Check if the selected file is an image or a video
            if (fileName.endsWith(".png") || fileName.endsWith(".jpg") || fileName.endsWith(".gif") || fileName.endsWith(".jfif")) {
                // Display the image in the chat area
                displayImage(filePath);
            
            } else {
                // Display an error message if the file type is not supported
                JOptionPane.showMessageDialog(this, "Unsupported file type. Please select an image or video file.");
            }
        }
    }
    private void displayImage(String filePath) {
        // Create an ImageIcon from the selected file
        ImageIcon selectedIcon = new ImageIcon(filePath);

        // Resize the image if it's larger than the maximum size
        int maxWidth = 500;
        int maxHeight = 500;
        Image image = selectedIcon.getImage();
        int width = image.getWidth(null);
        int height = image.getHeight(null);
        if (width > maxWidth || height > maxHeight) {
            double ratio = Math.min((double) maxWidth / width, (double) maxHeight / height);
            int newWidth = (int) (width * ratio);
            int newHeight = (int) (height * ratio);
            image = image.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
            selectedIcon = new ImageIcon(image);
        }

        // Create a JLabel with the resized ImageIcon and add it to the chat area
        JLabel iconLabel = new JLabel(selectedIcon);
        chatArea.append("[You sent an image]\n");
        chatArea.setLayout(new FlowLayout(FlowLayout.LEFT));
        chatArea.add(iconLabel);
        chatArea.validate();
        chatArea.repaint();
    }


    
	private <SpeechRecognitionResult> void recordAudio() {
        if (!isRecording) {
            isRecording = true;

            // Start a separate thread to handle the recording process
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                    	 
                        // Set up the audio format
                        AudioFormat audioFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 44100, 16, 2, 4, 44100, false);
                        DataLine.Info dataLineInfo = new DataLine.Info(TargetDataLine.class, audioFormat);
                        targetDataLine = (TargetDataLine) AudioSystem.getLine(dataLineInfo);
                        targetDataLine.open(audioFormat);
                        targetDataLine.start();

                        // Start recording and get the start time
                        long startTime = System.currentTimeMillis();

                        // Record the audio and save it to a temporary file
                        File tempFile = File.createTempFile("recorded_audio", ".wav");
                        AudioInputStream audioInputStream = new AudioInputStream(targetDataLine);
                        AudioSystem.write(audioInputStream, AudioFileFormat.Type.WAVE, tempFile);

                        // Calculate the duration of the recording
                        long duration = System.currentTimeMillis() - startTime;

                        // Display the recording duration in the message text area
                        chatArea.append("Recording duration: " + duration + " ms\n");

                        // Upload the recorded audio to the server
                        uploadAudioToServer(tempFile, duration);

                        // Display a message in the message text area
                        chatArea.append("Audio recorded and uploaded to server. Duration: " + duration + " ms\n");
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        isRecording = false;
                        if (targetDataLine != null) {
                            targetDataLine.stop();
                            targetDataLine.close();
                        }
                    }
                }

				private void uploadAudioToServer(File tempFile, long duration) {
					// TODO Auto-generated method stub
					
				}
            }).start();
        } else {
            // Stop recording
            if (targetDataLine != null) {
                targetDataLine.stop();
                targetDataLine.close();
            }
            isRecording = false;
        }
    }
	
    private void uploadImageToServer(File imageFile) {
		// TODO Auto-generated method stub
		
	}
	private File getImageFile() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ChatClient().setVisible(true);
            }
        });
    }
}
	