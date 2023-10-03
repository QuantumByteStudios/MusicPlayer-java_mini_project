import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class MusicPlayer extends JFrame {
    private JButton openButton;
    private JButton playButton;
    private JButton stopButton;
    private Clip clip;
    private JLabel statusLabel;

    public MusicPlayer() {
        setTitle("Music Player");
        setSize(500, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        openButton = new JButton("Open");
        playButton = new JButton("Play");
        stopButton = new JButton("Stop");
        statusLabel = new JLabel("No file loaded");

        openButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openFile();
            }
        });

        playButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                play();
            }
        });

        stopButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                stop();
            }
        });

        playButton.setEnabled(false);
        stopButton.setEnabled(false);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(openButton);
        buttonPanel.add(playButton);
        buttonPanel.add(stopButton);

        JPanel statusPanel = new JPanel();
        statusPanel.add(statusLabel);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        mainPanel.add(statusPanel, BorderLayout.CENTER);

        add(mainPanel);
    }

    private void openFile() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            try {
                AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(selectedFile);
                clip = AudioSystem.getClip();
                clip.open(audioInputStream);
                playButton.setEnabled(true);
                stopButton.setEnabled(true);
                statusLabel.setText("File loaded: " + selectedFile.getName());
            } catch (UnsupportedAudioFileException | LineUnavailableException | IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error opening the file", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void play() {
        if (clip != null && !clip.isRunning()) {
            clip.start();
            playButton.setEnabled(false);
            stopButton.setEnabled(true);
            statusLabel.setText("Playing...");
        }
    }

    private void stop() {
        if (clip != null && clip.isRunning()) {
            clip.stop();
            clip.setFramePosition(0);
            playButton.setEnabled(true);
            stopButton.setEnabled(false);
            statusLabel.setText("Playback stopped");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new MusicPlayer().setVisible(true);
            }
        });
    }
}
