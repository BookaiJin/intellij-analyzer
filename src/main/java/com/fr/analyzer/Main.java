package com.fr.analyzer;

import com.fr.analyzer.choosefile.ChooseFile;
import com.fr.analyzer.log.LogFactory;
import com.fr.analyzer.writelogs.WriteLogs;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * 丑陋的大框框
 *
 * @author bokai
 * @version 10.0
 * Created by bokai on 2019-12-11
 */
public class Main extends JFrame {

    private JLabel inputText = new JLabel();
    private JLabel outputText = new JLabel();

    private Main() {
        //init a panel
        JPanel treasInputPanel = new JPanel(new GridLayout(1, 3));
        JPanel treasOutputPanel = new JPanel(new GridLayout(1, 3));
        JPanel treasAnalyzePanel = new JPanel(new GridLayout(1, 1));

        JLabel treasInputLabel = new JLabel("Dir included treasure: ", JLabel.CENTER);
        JLabel treasOutputLabel = new JLabel("log path to output: ", JLabel.CENTER);
        JButton inputFileChooseButton = new JButton("choose treas");
        JButton outputFileChooseButton = new JButton("choose output log dir");
        JButton analyzeButton = new JButton("Analyze");

        treasInputPanel.add(treasInputLabel);
        treasInputPanel.add(inputFileChooseButton);
        treasInputPanel.add(inputText);
        treasOutputPanel.add(treasOutputLabel);
        treasOutputPanel.add(outputFileChooseButton);
        treasOutputPanel.add(outputText);
        treasAnalyzePanel.add(analyzeButton);

        JPanel mainPanel = new JPanel(new GridLayout(3, 1));
        mainPanel.add(treasInputPanel);
        mainPanel.add(treasOutputPanel);
        mainPanel.add(treasAnalyzePanel);

        addInputButtonAction(inputFileChooseButton);
        addOutputButtonAction(outputFileChooseButton);
        analyze(analyzeButton);

        this.getContentPane().add(mainPanel, BorderLayout.CENTER);
        this.setSize(2000, 400);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        this.setTitle("Intellij analyzer tool");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String... args) {
        new Main();
    }

    private void addInputButtonAction(JButton inputButton) {
        inputButton.addActionListener(e -> chooseFile(inputText));
    }

    private void addOutputButtonAction(JButton outputButton) {
        outputButton.addActionListener(e -> {
            chooseFile(outputText);
            LogFactory.refreshSystemLogger(outputText.getText());
        });
    }

    private void chooseFile(JLabel text) {
        JFileChooser inputFileChooser = new JFileChooser("/users/bokai/downloads");
        inputFileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        int val = inputFileChooser.showOpenDialog(null);
        if (val == JFileChooser.APPROVE_OPTION) {
            //正常选择文件
            text.setText(inputFileChooser.getSelectedFile().toString());
        } else {
            //未正常选择文件，如选择取消按钮
            text.setText("未选择文件");
        }
    }

    private void analyze(JButton analyzeButton) {
        analyzeButton.addActionListener(e -> {
            //get files
            ChooseFile.getInstance().getFilesPathToAnalyze(inputText.getText(), outputText.getText());
            //write logs
            WriteLogs.getInstance().analyzeFile(outputText.getText(), outputText.getText());
            JOptionPane.showMessageDialog(getParent(), "Mission complete");
        });
    }
}
