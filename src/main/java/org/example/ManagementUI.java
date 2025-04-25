package org.example;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;

public abstract class ManagementUI extends JFrame {
    public ManagementUI(String title,int width,int height){
        setTitle(title);
        setSize(width, height);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }
    protected void createTable(JTable table,String[] columns){
        table.setModel(new DefaultTableModel(null, columns));
        table.setRowHeight(25);
        table.setShowGrid(true);
        table.setGridColor(Color.LIGHT_GRAY);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 15));
        header.setBackground(new Color(30, 144, 255)); // DodgerBlue
        header.setForeground(Color.WHITE);
    }
    protected abstract void tableLoad();
}
