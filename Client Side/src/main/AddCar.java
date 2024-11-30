package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddCar extends JDialog {
    private JTextField carField, typeField, priceField;
    private JButton addButton, cancelButton;
    private boolean succeeded;

    public AddCar(Frame parent, String car, String type, int price) {
        super(parent, "Ajouter/Modifuer Voiture", true);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        carField = new JTextField(car, 15);
        typeField = new JTextField(type, 15);
        priceField = new JTextField(String.valueOf(price), 15);

        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel("Voiture:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        add(carField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        add(new JLabel("Type:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        add(typeField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        add(new JLabel("Prix/Jour:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        add(priceField, gbc);

        addButton = new JButton("Ajouter");
        cancelButton = new JButton("Annuler");

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (carField.getText().isEmpty() || typeField.getText().isEmpty() || priceField.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(AddCar.this, "All fields must be filled", "Warning", JOptionPane.WARNING_MESSAGE);
                } else {
                    succeeded = true;
                    dispose();
                }
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                succeeded = false;
                dispose();
            }
        });

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        buttonPanel.add(addButton);
        buttonPanel.add(cancelButton);
        add(buttonPanel, gbc);

        setPreferredSize(new Dimension(400, 300));
        pack();
        setLocationRelativeTo(parent);
    }

    public String getCar() {
        return carField.getText();
    }

    public String getCarType() {
        return typeField.getText();
    }

    public int getPrice() {
        return Integer.parseInt(priceField.getText());
    }

    public boolean isSucceeded() {
        return succeeded;
    }
}