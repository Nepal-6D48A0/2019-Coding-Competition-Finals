package com.statefarm.codingcomp;

import com.statefarm.codingcomp.model.PolicySummary;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUI extends JFrame {
    PolicyRepository repo;

    private JComboBox states;
    private JComboBox policyType;

    private JLabel policyCount;
    private JLabel averagePremium;
    private JLabel averageIncidents;


    public GUI(){
        super("Insurance Data");
        repo = new PolicyRepository();

        createGUIComponents();

        this.pack();
        this.setLocationRelativeTo(null);
    }

    private void createGUIComponents(){
        JPanel panel = new JPanel();
        panel.setBorder(new EmptyBorder(10,10,10,10));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JPanel grid = new JPanel();
        grid.setLayout(new GridLayout(3, 2, 10,10));

        grid.add(new JLabel("Policy Type"));
        policyType = new JComboBox<>(repo.getDistinctPolicyTypes().toArray());
        grid.add(policyType);

        grid.add(new JLabel("State"));
        states= new JComboBox<>(repo.getDistinctStates().toArray());
        grid.add(states);

        panel.add(grid);

        JButton btnMap = new JButton("Get Summary");
        panel.add(btnMap);
        btnMap.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateSummary();
            }
        });

        panel.add(getSummaryPanel());

        this.getContentPane().add(panel);

    }

    private JPanel getSummaryPanel(){
        JPanel grid = new JPanel();
        grid.setBorder(BorderFactory.createTitledBorder("State Summary"));
        grid.setLayout(new GridLayout(3, 2));


        grid.add(new JLabel("Total Policy Holders"));
        policyCount = new JLabel("");
        grid.add(policyCount);

        grid.add(new JLabel("Average Premium"));
        averagePremium = new JLabel("");
        grid.add(averagePremium);

        grid.add(new JLabel("Average Incidents"));
        averageIncidents = new JLabel("");
        grid.add(averageIncidents);

        return grid;
    }

    private void updateSummary() {
        String state = (String)states.getSelectedItem();
        String policy = (String)policyType.getSelectedItem();

        PolicySummary summary = new PolicySummary(state, policy);
        policyCount.setText(summary.getTotalPolicyHolders() + "");
        averagePremium.setText(String.format("$ %.2f", summary.getTotalPremium() / summary.getTotalPolicyHolders() * 1.0));
        averageIncidents.setText(summary.getTotalIncidents() / summary.getTotalPolicyHolders() * 1.0 + "");
    }
}
