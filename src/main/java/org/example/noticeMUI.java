package org.example;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;
import java.util.Vector;

public class noticeMUI extends JFrame {
    private JLabel heading;
    private JTable table1;
    private JLabel description;
    private JTextArea textArea1;
    private JButton addButton;
    private JButton delete;
    private JPanel noticeManagementPanel;
    private DefaultTableModel dtm;
    private Connection con;
    private PreparedStatement pstmt;
    private ResultSet rs;
    private int selectRow;
    private String query;

    public noticeMUI() {
        setTitle("add notice");
        setSize(400,500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(noticeManagementPanel);
        setVisible(true);
        setLocationRelativeTo(null);
        textArea1.setLineWrap(true);
        textArea1.setWrapStyleWord(true);

        createTable();
        tableLoad();
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                addNoticeUI addnotice = new addNoticeUI();
            }
        });
        table1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                try{
                    dtm = (DefaultTableModel)table1.getModel();
                      selectRow = table1.getSelectedRow();
                      String id = dtm.getValueAt(selectRow,0).toString();
                      query = "SELECT * FROM notices WHERE notice_id = ?";
                      con = Database.getConnection();
                      pstmt = con.prepareStatement(query);
                      pstmt.setString(1,id);
                      rs = pstmt.executeQuery();

                      if(rs.next()){
                          String title = rs.getString(2);
                          String content = rs.getString(3);
                          String pDate = rs.getString(4);
                          String by = rs.getString(5);
                          String to = rs.getString(6);

                          textArea1.setText(
                                          "📌 Title      : " + title + "\n"+
                                          "📝 Content    : " + content + "\n" +
                                          "📅 Posted On  : " + pDate + "\n" +
                                          "👤 Posted By  : " + by + "\n" +
                                          "🎯 Target Role: " + to
                          );
                      }else{
                          textArea1.setText("No data found for that notice!..");
                      }



                }catch (Exception q){
                    JOptionPane.showMessageDialog(null,q.getMessage());
                }


            }
        });
        delete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    query = "DELETE FROM notices WHERE notice_id = ?";
                    con = Database.getConnection();
                    pstmt = con.prepareStatement(query);
                    if(selectRow<=0){
                        JOptionPane.showMessageDialog(null,"First you need to click on notice which you want to delete");
                    }else{
                        pstmt.setString(1,dtm.getValueAt(selectRow,0).toString());
                        int i = pstmt.executeUpdate();
                        if(i>0){
                            JOptionPane.showMessageDialog(null,"Notoce deleted successfully");
                        }else{
                            JOptionPane.showMessageDialog(null,"Error in deletion");
                            return;
                        }
                        tableLoad();
                    }
                }catch (Exception x){
                    JOptionPane.showMessageDialog(null,x.getMessage());
                }
            }
        });
    }

    public void createTable(){
        table1.setModel(new javax.swing.table.DefaultTableModel(null,new String[]{"Id","Title","Posted Date","Target"}));
    }

    public void tableLoad(){
        int count;
        try{
            con = Database.getConnection();
            pstmt = con.prepareStatement("SELECT * FROM notices");
            rs = pstmt.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            count = rsmd.getColumnCount();
            DefaultTableModel dtm = (DefaultTableModel)table1.getModel();
            dtm.setRowCount(0);
            while(rs.next()){
                Vector v = new Vector();
                for(int i = 1;i<=count;i++){
                    v.add(rs.getString(1));
                    v.add(rs.getString(2));
                    v.add((rs.getString(4)));
                    v.add(rs.getString(6));
                }
                dtm.addRow(v);
            }
        }catch (SQLException e){
            JOptionPane.showMessageDialog(null,e.getMessage());
        }
    }
}
