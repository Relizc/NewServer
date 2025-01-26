package net.itsrelizc.modchecker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class Main {
    public static void main(String[] args) {
        try {
            // Set the Look and Feel to the system's default
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

            // Remove the 3D border style from SplitPane
            UIManager.put("SplitPane.border", BorderFactory.createEmptyBorder());
            UIManager.put("SplitPaneDivider.background", Color.GRAY); // Set the background color of the divider
            UIManager.put("SplitPaneDivider.border", BorderFactory.createLineBorder(Color.GRAY)); // Remove 3D effect by setting a flat border
            UIManager.put("SplitPaneDivider.focusCellBackground", Color.GRAY); // Remove focus background
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Create the main frame
        JFrame frame = new JFrame("Minecraft Mod List Checker");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);

        // Create panels
        JPanel buttonPanel = new JPanel();
        JPanel topPanel = new JPanel(new BorderLayout());

        // Create components
        JLabel statusLabel = new JLabel("Select .minecraft directory");
        JButton loadPlayerModsButton = new JButton("Load Player Mods");
        JButton loadOfficialModsButton = new JButton("Load Official Mod List");
        JButton compareButton = new JButton("Compare Mod Lists");
        JButton browseButton = new JButton("Browse...");

        JTextField directoryTextField = new JTextField();
        directoryTextField.setText(System.getenv("APPDATA") + "\\.minecraft");

        DefaultListModel<String> playerModsModel = new DefaultListModel<>();
        DefaultListModel<String> officialModsModel = new DefaultListModel<>();

        JList<String> playerModsList = new JList<>(playerModsModel);
        JList<String> officialModsList = new JList<>(officialModsModel);

        JScrollPane playerModsScrollPane = new JScrollPane(playerModsList);
        JScrollPane officialModsScrollPane = new JScrollPane(officialModsList);

        // Set titles for lists
        playerModsScrollPane.setBorder(BorderFactory.createTitledBorder("Player Mods"));
        officialModsScrollPane.setBorder(BorderFactory.createTitledBorder("Official Mods"));

        // Add components to the top panel
        JPanel directoryPanel = new JPanel(new BorderLayout());
        directoryPanel.add(directoryTextField, BorderLayout.CENTER);
        directoryPanel.add(browseButton, BorderLayout.EAST);
        topPanel.add(statusLabel, BorderLayout.NORTH);
        topPanel.add(directoryPanel, BorderLayout.CENTER);

        // Add buttons to the button panel
        buttonPanel.add(loadPlayerModsButton);
        buttonPanel.add(loadOfficialModsButton);
        buttonPanel.add(compareButton);

        // Create JSplitPane to split the player and official mod lists
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, playerModsScrollPane, officialModsScrollPane);
        splitPane.setDividerLocation(0.5); // 50/50 split
        splitPane.setContinuousLayout(true);

        // Disable divider resizing (fixed at 50/50 split)
        splitPane.setDividerSize(0); // Makes the divider invisible
        splitPane.setEnabled(false); // Disables dragging of the divider

        // Create the main panel with BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(splitPane, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        mainPanel.add(topPanel, BorderLayout.NORTH);

        // Add the main panel to the frame
        frame.add(mainPanel);

        // Set the frame to be visible
        frame.setVisible(true);

        // Placeholder action listeners for buttons
        loadPlayerModsButton.addActionListener(e -> {
            statusLabel.setText("Loading player mods...");
            // Logic to load player's mod list
        });

        loadOfficialModsButton.addActionListener(e -> {
            statusLabel.setText("Loading official mod list...");
            // Logic to load official mod list from text file
        });

        compareButton.addActionListener(e -> {
            statusLabel.setText("Comparing mod lists...");
            // Logic to compare lists and populate mismatchModsModel
        });

        browseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser(directoryTextField.getText());
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int returnValue = fileChooser.showOpenDialog(frame);

                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    if (!fileChooser.getSelectedFile().getPath().endsWith(".minecraft")) {
                        int response = JOptionPane.showConfirmDialog(frame,
                                "Looks like you did not select a .minecraft folder\nHowever, some launchers might not use the name .minecraft as a root folder.\nDo you wish to proceed?", // Message
                                "Invalid Directory", // Title
                                JOptionPane.YES_NO_OPTION, // Option type (Yes/No)
                                JOptionPane.QUESTION_MESSAGE); // Message type (Question)

                        // Handle the user's response
                        if (response == JOptionPane.YES_OPTION) {
                            File selectedDirectory = fileChooser.getSelectedFile();
                            directoryTextField.setText(selectedDirectory.getAbsolutePath());
                        } else if (response == JOptionPane.NO_OPTION) {
                            return;
                        }
                    }
                }
            }
        });
    }

    // Helper method to check if the directory is valid
    private static boolean isValidDirectory(String path) {
        File dir = new File(path);
        return dir.exists() && dir.isDirectory();
    }
}
