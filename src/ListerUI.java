import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

/**
 * Simple Swing UI for generating CSV lists of TV series or movies.
 */
public class ListerUI {
    private JFrame frame;
    private JTextField folderField;
    private JComboBox<String> modeBox;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ListerUI().createAndShow());
    }

    private void createAndShow() {
        frame = new JFrame("Lister Tool");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        folderField = new JTextField(25);
        JButton browse = new JButton("Browse");
        browse.addActionListener(this::chooseFolder);

        JPanel folderPanel = new JPanel();
        folderPanel.add(new JLabel("Folder:"));
        folderPanel.add(folderField);
        folderPanel.add(browse);

        modeBox = new JComboBox<>(new String[] {"TV", "Movie"});
        JButton generate = new JButton("Generate CSV");
        generate.addActionListener(this::generateCsv);

        JPanel bottom = new JPanel();
        bottom.add(new JLabel("Mode:"));
        bottom.add(modeBox);
        bottom.add(generate);

        frame.getContentPane().add(folderPanel, BorderLayout.NORTH);
        frame.getContentPane().add(bottom, BorderLayout.SOUTH);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void chooseFolder(ActionEvent e) {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        if (chooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
            folderField.setText(chooser.getSelectedFile().getAbsolutePath());
        }
    }

    private void generateCsv(ActionEvent e) {
        String folderPath = folderField.getText().trim();
        if (folderPath.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Please select a folder.");
            return;
        }
        Path folder = Paths.get(folderPath);
        if (!Files.isDirectory(folder)) {
            JOptionPane.showMessageDialog(frame, "Invalid folder.");
            return;
        }

        boolean tvMode = "TV".equalsIgnoreCase((String) modeBox.getSelectedItem());
        try {
            if (tvMode) {
                TvSeriesLister tvLister = new TvSeriesLister();
                tvLister.generateFromRoots(List.of(folder));
                JOptionPane.showMessageDialog(frame,
                        "CSV generated: " + tvLister.getOutputPath());
            } else {
                MovieLister movieLister = new MovieLister();
                movieLister.generateFromRoots(List.of(folder));
                JOptionPane.showMessageDialog(frame,
                        "CSV generated: " + movieLister.getOutputPath());
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame,
                    "Failed to generate CSV: " + ex.getMessage());
        }
    }
}
