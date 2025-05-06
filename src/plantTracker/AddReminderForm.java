import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.text.SimpleDateFormat;
import javax.swing.border.EmptyBorder;

public class AddReminderForm extends JFrame {
    private JComboBox<String> plantComboBox;
    private JCheckBox[] plantCheckBoxes;
    private JPanel plantSelectionPanel;
    private JTextField dateField;
    private JCheckBox recurringCheckBox;
    private JPanel intervalPanel;
    private JComboBox<String> intervalUnitComboBox;
    private JSpinner intervalValueSpinner;
    private JButton saveButton;
    
    // Sample plant data
    private String[] plantOptions = {"Monstera", "Snake Plant", "Pothos", "Peace Lily", "ZZ Plant", "Fiddle Leaf Fig"};
    
    public AddReminderForm() {
        setTitle("Add Reminder");
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(new Color(220, 230, 242)); // Light blue background
        
        // Title
        JLabel titleLabel = new JLabel("Add Reminder");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(titleLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        // Plant Selection Section
        JPanel plantPanel = new JPanel(new BorderLayout());
        plantPanel.setBackground(new Color(220, 230, 242));
        JLabel plantLabel = new JLabel("Plant selection (allows multiple select)");
        plantPanel.add(plantLabel, BorderLayout.NORTH);
        
        plantSelectionPanel = new JPanel();
        plantSelectionPanel.setLayout(new BoxLayout(plantSelectionPanel, BoxLayout.Y_AXIS));
        plantSelectionPanel.setBackground(Color.WHITE);
        
        plantCheckBoxes = new JCheckBox[plantOptions.length];
        for (int i = 0; i < plantOptions.length; i++) {
            plantCheckBoxes[i] = new JCheckBox(plantOptions[i]);
            plantCheckBoxes[i].setBackground(Color.WHITE);
            plantSelectionPanel.add(plantCheckBoxes[i]);
        }
        
        JScrollPane plantScrollPane = new JScrollPane(plantSelectionPanel);
        plantScrollPane.setPreferredSize(new Dimension(400, 100));
        plantPanel.add(plantScrollPane, BorderLayout.CENTER);
        mainPanel.add(plantPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        
        // Date Section
        JPanel datePanel = new JPanel(new BorderLayout());
        datePanel.setBackground(new Color(220, 230, 242));
        JLabel dateLabel = new JLabel("Date");
        datePanel.add(dateLabel, BorderLayout.NORTH);
        
        dateField = new JTextField();
        dateField.setPreferredSize(new Dimension(400, 30));
        dateField.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                showDatePicker();
            }
        });
        datePanel.add(dateField, BorderLayout.CENTER);
        mainPanel.add(datePanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        
        // Recurring Section
        JPanel recurringPanel = new JPanel(new BorderLayout());
        recurringPanel.setBackground(new Color(220, 230, 242));
        recurringCheckBox = new JCheckBox("Recurring?");
        recurringCheckBox.setBackground(new Color(220, 230, 242));
        recurringCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                intervalPanel.setVisible(recurringCheckBox.isSelected());
            }
        });
        recurringPanel.add(recurringCheckBox, BorderLayout.CENTER);
        mainPanel.add(recurringPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        
        // Interval Section
        intervalPanel = new JPanel(new BorderLayout());
        intervalPanel.setBackground(new Color(220, 230, 242));
        JLabel intervalLabel = new JLabel("Set Interval");
        intervalPanel.add(intervalLabel, BorderLayout.NORTH);
        
        JPanel intervalInputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        intervalInputPanel.setBackground(Color.WHITE);
        
        intervalValueSpinner = new JSpinner(new SpinnerNumberModel(7, 1, 365, 1));
        intervalValueSpinner.setPreferredSize(new Dimension(60, 25));
        intervalInputPanel.add(intervalValueSpinner);
        
        intervalUnitComboBox = new JComboBox<>(new String[]{"Days", "Weeks", "Months"});
        intervalInputPanel.add(intervalUnitComboBox);
        
        intervalPanel.add(intervalInputPanel, BorderLayout.CENTER);
        intervalPanel.setVisible(false); // Initially hidden
        mainPanel.add(intervalPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        
        // Save Button
        saveButton = new JButton("Save");
        saveButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        saveButton.setPreferredSize(new Dimension(100, 40));
        saveButton.setMaximumSize(new Dimension(100, 40));
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveReminder();
            }
        });
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(220, 230, 242));
        buttonPanel.add(saveButton);
        mainPanel.add(buttonPanel);
        
        add(mainPanel);
    }
    
    private void showDatePicker() {
        // In a real application, this would show a date picker
        // For this example, we'll just set a default date
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        dateField.setText(dateFormat.format(new Date()));
    }
    
    private void saveReminder() {
        StringBuilder plantSelection = new StringBuilder("Selected Plants: ");
        boolean anySelected = false;
        
        for (int i = 0; i < plantCheckBoxes.length; i++) {
            if (plantCheckBoxes[i].isSelected()) {
                if (anySelected) {
                    plantSelection.append(", ");
                }
                plantSelection.append(plantOptions[i]);
                anySelected = true;
            }
        }
        
        if (!anySelected) {
            JOptionPane.showMessageDialog(this, "Please select at least one plant.");
            return;
        }
        
        if (dateField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select a date.");
            return;
        }
        
        StringBuilder reminderDetails = new StringBuilder();
        reminderDetails.append(plantSelection).append("\n");
        reminderDetails.append("Date: ").append(dateField.getText()).append("\n");
        reminderDetails.append("Recurring: ").append(recurringCheckBox.isSelected() ? "Yes" : "No").append("\n");
        
        if (recurringCheckBox.isSelected()) {
            reminderDetails.append("Interval: ")
                          .append(intervalValueSpinner.getValue())
                          .append(" ")
                          .append(intervalUnitComboBox.getSelectedItem());
        }
        
        JOptionPane.showMessageDialog(this, "Reminder saved!\n\n" + reminderDetails.toString());
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                new AddReminderForm().setVisible(true);
            }
        });
    }
}