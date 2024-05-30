package chat1;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.DataOutputStream;
import java.io.File;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.TargetDataLine;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.filechooser.FileNameExtensionFilter;

public class ClientGUI<JMediaPlayer, SpeechClient> extends JFrame {
    private JButton btnSendEmoji, btnSendSticker, btnSendText;
    private JTextArea chatArea;
    private JTextField messageField;
    private File selectedFile;
    private JComboBox<String> videoTypeComboBox;
    private JButton browseButton;
    private JTextField filePathTextField;
    private JButton playButton;
    private TargetDataLine targetDataLine;
    private boolean isRecording = false;
    private JPanel chatPanel;
    private JLabel imageLabel;
    public ClientGUI() {
        setTitle("Messenger");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(745, 521);
        setLocationRelativeTo(null);
        chatPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setPreferredSize(new Dimension(400, 300));

        // Create the image label
        imageLabel = new JLabel();
        imageLabel.setPreferredSize(new Dimension(100, 100));

        // Create the message field
        messageField = new JTextField();
        messageField.addActionListener(e -> sendTextMessage());
        messageField.setPreferredSize(new Dimension(300, 30));

        // Add the chat area, image label, and message field to the chat panel
        chatPanel.add(imageLabel);
        chatPanel.add(chatArea);
        chatPanel.add(messageField);

        // Add the chat panel to the main panel
        mainPanel.add(chatPanel);

        // Add the main panel to the frame
        add(mainPanel);
        // Tạo panel chứa các thành phần giao diện
        JPanel panel = new JPanel();

        messageField = new JTextField();
        btnSendEmoji = new JButton("Sticker");
        
                btnSendEmoji.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        sendSticker();
                    }
                });
        btnSendSticker = new JButton("Phương tiện");
        
                btnSendSticker.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        sendFile();
                    }
                });
        btnSendText = new JButton("Gửi");
        
                btnSendText.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        sendTextMessage();
                    }
                });
        
                JButton recordButton = new JButton("Record Audio");
                recordButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        recordAudio();
                    }
                });

                // Thêm button vào giao diện
              
                panel.add(recordButton);
                getContentPane().add(panel, BorderLayout.SOUTH);

        GroupLayout gl_panel = new GroupLayout(panel);
        gl_panel.setHorizontalGroup(
        	gl_panel.createParallelGroup(Alignment.LEADING)
        		.addGroup(gl_panel.createSequentialGroup()
        			.addGap(22)
        			.addComponent(btnSendEmoji, GroupLayout.PREFERRED_SIZE, 80, GroupLayout.PREFERRED_SIZE)
        			.addPreferredGap(ComponentPlacement.UNRELATED)
        			.addComponent(btnSendSticker, GroupLayout.PREFERRED_SIZE, 105, GroupLayout.PREFERRED_SIZE)
        			.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        			.addComponent(recordButton)
        			.addGap(18)
        			.addComponent(messageField, GroupLayout.PREFERRED_SIZE, 259, GroupLayout.PREFERRED_SIZE)
        			.addPreferredGap(ComponentPlacement.UNRELATED)
        			.addComponent(btnSendText, GroupLayout.PREFERRED_SIZE, 79, GroupLayout.PREFERRED_SIZE)
        			.addGap(18))
        );
        gl_panel.setVerticalGroup(
        	gl_panel.createParallelGroup(Alignment.LEADING)
        		.addGroup(gl_panel.createSequentialGroup()
        			.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
        				.addGroup(gl_panel.createSequentialGroup()
        					.addGap(6)
        					.addComponent(btnSendText))
        				.addGroup(gl_panel.createSequentialGroup()
        					.addGap(5)
        					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
        						.addComponent(btnSendEmoji)
        						.addComponent(btnSendSticker)
        						.addComponent(messageField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        						.addComponent(recordButton))))
        			.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panel.setLayout(gl_panel);
        
                // Tạo khu vực hiển thị tin nhắn
                chatArea = new JTextArea();
                chatArea.setToolTipText("");
                chatArea.setEditable(false);
        GroupLayout groupLayout = new GroupLayout(getContentPane());
        groupLayout.setHorizontalGroup(
        	groupLayout.createParallelGroup(Alignment.LEADING)
        		.addComponent(panel, GroupLayout.DEFAULT_SIZE, 691, Short.MAX_VALUE)
        		.addGroup(groupLayout.createSequentialGroup()
        			.addGap(10)
        			.addComponent(chatArea, GroupLayout.DEFAULT_SIZE, 681, Short.MAX_VALUE))
        );
        groupLayout.setVerticalGroup(
        	groupLayout.createParallelGroup(Alignment.LEADING)
        		.addGroup(groupLayout.createSequentialGroup()
        			.addContainerGap()
        			.addComponent(chatArea, GroupLayout.PREFERRED_SIZE, 430, GroupLayout.PREFERRED_SIZE)
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addComponent(panel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        			.addGap(1))
        );
        getContentPane().setLayout(groupLayout);
    }JPanel mainPanel = new JPanel(new GridLayout(1, 2));

    

private void sendTextMessage() {
    String message = messageField.getText();
    if (!message.trim().isEmpty()) {
    	        displayMessage(message, true);
        messageField.setText("");
    }
}

private void displayMessage(String message, boolean isText) {
    // Clear the image label
    imageLabel.setIcon(null);

    // Append the message to the chat area
    StringBuilder messageBuilder = new StringBuilder();
    if (isText) {
        messageBuilder.append("[Bạn]: ").append(message).append("\n");
    } else {
        messageBuilder.append("[Image Uploaded]\n");
    }
    chatArea.append(messageBuilder.toString());

    // Scroll to the bottom of the chat area
    chatArea.setCaretPosition(chatArea.getDocument().getLength());
}

public void displayImage(ImageIcon image) {
    // Set the image in the image label
    imageLabel.setIcon(image);
}

    private void sendSticker() {
        // Tạo một JFileChooser để cho phép người dùng chọn icon
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
                new ClientGUI().setVisible(true);
            }
        });
    }
}