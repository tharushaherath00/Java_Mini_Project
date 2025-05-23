package org.example;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;
import java.util.Vector;

public class noticeMUI extends ManagementUI{
    private JLabel heading;
    private JTable table1;
    private JTextArea textArea1;
    private JButton addButton;
    private JButton delete;
    private JPanel noticeManagementPanel;
    private JButton backtoAdmin;
    //private DefaultTableModel dtm;
    //private Connection con;
    //private PreparedStatement pstmt;
    //private ResultSet rs;
    private int selectRow;
    private String query;
    private String publisherName;
    private String userName = new AdminPanel().getUser().getUsername();

    public noticeMUI() {
        super("Add Notice",600,500);

//        setTitle("add notice");
//        setSize(600,500);
//        setResizable(false);
//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(noticeManagementPanel);
//        setLocationRelativeTo(null);
        textArea1.setLineWrap(true);
        textArea1.setWrapStyleWord(true);
        textArea1.setColumns(30);
        setVisible(true);
        String[] columns = {"Id","Title","Posted Date","Target"};
        createTable(table1,columns);
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
                          try{
                              query = "SELECT Name FROM user JOIN notices ON notices.posted_by = user.NIC WHERE notices.posted_by = ?";
                              con = Database.getConnection();
                              pstmt = con.prepareStatement(query);
                              pstmt.setString(1,by);
                              rs = pstmt.executeQuery();
                              if(rs.next()){
                                  publisherName = rs.getString(1);
                              }else{
                                  publisherName = "Publisher is not defined";
                              }
                          }catch(SQLException t){
                              System.out.println(t.getMessage());
                          }

                          textArea1.setText(
                                          "━━━━━━━━━━━━━━━━━━━━━━━━━━━\n" +
                                          "📌 Title      : " + title + "\n"+
                                          "📝 Content    : " + content + "\n" +
                                          "📅 Posted On  : " + pDate + "\n" +
                                          "👤 Posted By  : " + publisherName + "\n" +
                                          "🎯 Target Role: " + to+"\n"+
                                                  "━━━━━━━━━━━━━━━━━━━━━━━━━━━"
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
                            JOptionPane.showMessageDialog(null,"Notice deleted successfully");
                        }else{
                            JOptionPane.showMessageDialog(null,"Error in deletion");
                            return;
                        }
//                        new noticeMUI(); not worked !
                          tableLoad();

                    }
                }catch (Exception x){
                    JOptionPane.showMessageDialog(null,x.getMessage());
                }
            }
        });
        backtoAdmin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AdminPanel adminPanel = new AdminPanel();
                dispose();
                adminPanel.setVisible(true);

            }
        });
    }

//    public void createTable(){
//        table1.setModel(new javax.swing.table.DefaultTableModel(null,new String[]{"Id","Title","Posted Date","Target"}));
//        table1.setRowHeight(25); // Make rows taller
//        table1.setShowGrid(true);
//        table1.setGridColor(Color.LIGHT_GRAY);
//        table1.setFont(new Font("Segoe UI", Font.PLAIN, 14));
//        table1.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 15));
//        JTableHeader header = table1.getTableHeader();
//        header.setBackground(new Color(30, 144, 255)); // DodgerBlue
//        header.setForeground(Color.WHITE);
//
//    }

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
