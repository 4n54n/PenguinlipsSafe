/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package penguinlipsguard;

import java.awt.Desktop;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author penguinlips
 */
public class Home extends javax.swing.JFrame {
    
    
    String version = "V 1.0";
    
    
    String message,modify,uname,pass,port,database,anim,saveconfig;
    int inival = 1;
    

    /**
     * Creates new form Home
     */
    public Home() throws IOException {
        update();
        popup(); //config popup
        config(); //setting configurations
        initComponents();
        table();//list data from sql to table
        
        this.setTitle("PenguinlipsSafe "+version);
        
        //here goes the function while clicking on close button
        addWindowListener(new WindowAdapter() {
        public void windowClosing(WindowEvent e) {
        try {
            config();
        } catch (IOException ex) {
            Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
        }
        if("false".equals(saveconfig)){

        try {
          FileWriter myWriter = new FileWriter("src/penguinlipsguard/custom.ini");
          myWriter.write("");
          myWriter.close();
        } catch (IOException s) {
          JOptionPane.showMessageDialog(null,"an error occured","error !",JOptionPane.INFORMATION_MESSAGE);
          s.printStackTrace();
        }
       }
      } 
    });
        
    
    }
    
    
    private void table(){
        try{
        
        Connection con = db.getConnection();
        Statement stmt=con.createStatement();  
        ResultSet rs=stmt.executeQuery("select * from data");  
        

        DefaultTableModel tm = (DefaultTableModel)jTable1.getModel();
        tm.setRowCount(0);

        while(rs.next()){

            Object o[] = {rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6)};
            tm.addRow(o);
           
        }
        con.close();
        }
        catch(Exception e){

        JOptionPane.showMessageDialog(null,e+"\ncheck your configuration","error !",JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    
    private void animation(){
        
        String hello= message;
        String temp = "";
        for(int i=0;i<hello.length();i++){
        
        try {
            TimeUnit.MILLISECONDS.sleep(40);
        } catch (InterruptedException ex) {
            Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
        }
        String convert =  String.valueOf(hello.charAt(i));
        temp = temp+convert;
        jTextFieldNoti.setText(temp);
        jTextFieldNoti.update(jTextFieldNoti.getGraphics());
        
    }
    
    }
    
    
    private void selectTable(){
        
        try{
        Connection con = db.getConnection();

        Statement stmt=con.createStatement(); 
        
        
        ResultSet rs=stmt.executeQuery("select * from data");  
        

        DefaultTableModel tm = (DefaultTableModel)jTable1.getModel();
        tm.setRowCount(0);

        while(rs.next()){

            Object o[] = {rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6)};
            tm.addRow(o);
           
        }
        con.close();
        }
        catch(Exception e){

        JOptionPane.showMessageDialog(null,e+"\ncheck your configuration","error !",JOptionPane.INFORMATION_MESSAGE);
        }
                
    }
    

    private void popup() {
        

        try {
            config();
        } catch (IOException ex) {
            Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        File file = new File("src/penguinlipsguard/custom.ini");
        if (file.length() < inival) {
            
            
            JTextField field1 = new JTextField(uname);
            JTextField field2 = new JTextField(pass);
            JTextField field3 = new JTextField(port);
            JTextField field4 = new JTextField("penguinlipssafe");
            field4.setEditable(false);//due to issue while converting """ to string in db.java
            JCheckBox field5 = new JCheckBox();
            if ("true".equals(anim)){
                field5.setSelected(true);

            }
            
            JCheckBox field6 = new JCheckBox();
            
            if ("true".equals(saveconfig)){

                field6.setSelected(true);

            }
            
            JPanel panel = new JPanel(new GridLayout(0, 2));
            

            panel.add(new JLabel("Username:"));
            panel.add(field1);
            panel.add(new JLabel("Pass:"));
            panel.add(field2);
            panel.add(new JLabel("Port (def: 3306):"));
            panel.add(field3);
            panel.add(new JLabel("Database:"));
            panel.add(field4);
            panel.add(new JLabel("Animation"));
            panel.add(field5);
            panel.add(new JLabel("Save config?"));
            panel.add(field6);



            int result = JOptionPane.showConfirmDialog(null, panel, "Configure",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (result == JOptionPane.OK_OPTION) {

                boolean val1 = field5.isSelected();
                boolean val2 = field6.isSelected();


                try {
                  FileWriter myWriter = new FileWriter("src/penguinlipsguard/custom.ini");
                  myWriter.write(";this file is not allowed to modify"+"\n\n"+";mysql custom"+"\n"+"Username = "+field1.getText()+"\n"+"Password = "+field2.getText()+"\n"+"Port = "+field3.getText()+"\n"+"Database = "+field4.getText()+"\n\n\n"+";Additional"+"\n"+"Animation = "+val1+"\n"+"Saveconfig = "+val2+"\n\n\n\n"+";penguinlips");
                  myWriter.close();
                  

                } catch (IOException e) {
                  JOptionPane.showMessageDialog(null,"an error occured","error !",JOptionPane.INFORMATION_MESSAGE);
                  e.printStackTrace();
                }
                
                
                //here goes database population
                
                try{

                Connection con = db.getConnection();
                Statement stmt=con.createStatement();  
                stmt.executeUpdate("CREATE TABLE `penguinlipssafe`.`data` ( `id` INT(10) NOT NULL , `domain` VARCHAR(100) NULL DEFAULT NULL , `uname` VARCHAR(60) NULL DEFAULT NULL , `email` VARCHAR(60) NULL DEFAULT NULL , `pwd` VARCHAR(20) NULL DEFAULT NULL , `note` TEXT NULL DEFAULT NULL , PRIMARY KEY (`id`)) ENGINE = InnoDB;");


                con.close();
                }
                catch(Exception e){

                //System.out.println("already populated");
                }
                

                
            } else {
                if (file.length() < 1) {
                System.exit(0);
                
                }
            }
        }
    }
    
    
    public void config() throws IOException {
        
    List<String> list = Files.readAllLines(new File("src/penguinlipsguard/custom.ini").toPath(), Charset.defaultCharset());
    File file = new File("src/penguinlipsguard/custom.ini");
    if (file.length() > 1) {
    
    uname = list.get(3).replace("Username = ", "");

    pass = list.get(4).replace("Password = ", "");

    port = list.get(5).replace("Port = ", "");

    database = list.get(6).replace("Database = ", "");
    
    anim = list.get(10).replace("Animation = ","");
    
    saveconfig = list.get(11).replace("Saveconfig = ","");

    }
    }

    
    
    public void update() throws MalformedURLException, IOException{
        
    InputStream response = null;
    try {
        String url = "https://penguinlips.github.io/updates/safe/safe.html";
        response = new URL(url).openStream();


        Scanner scanner = new Scanner(response);
        String responseBody = scanner.useDelimiter("\\A").next();
        String title = (responseBody.substring(responseBody.indexOf("<title>") + 7, responseBody.indexOf("</title>")));
        String[] strParts = title.split( "," );

        
        if (version == null ? strParts[0] != null : !version.equals(strParts[0])){
            
            
            UIManager.put("OptionPane.okButtonText", "Download exe");
            UIManager.put("OptionPane.cancelButtonText", "Download jar");
            int input =  JOptionPane.showOptionDialog(null,"Version "+strParts[0]+" released\n\n"+strParts[1]+"\n"+strParts[2]+"\n","Update Available !",JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, null);
            UIManager.put("OptionPane.okButtonText", "OK");
            UIManager.put("OptionPane.cancelButtonText", "Cancel");
            
                if(input == JOptionPane.OK_OPTION)
                {
                    if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                        try {
                            Desktop.getDesktop().browse(new URI("https://github.com/penguinlips/updates/raw/main/safe/PenguinlipsSafeexe.zip"));
                        } catch (URISyntaxException ex) {
                            Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (IOException ex) {
                            Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
                
                if(input == JOptionPane.CANCEL_OPTION)
                {
                    
                    if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                        try {
                            Desktop.getDesktop().browse(new URI("https://github.com/penguinlips/updates/raw/main/safe/PenguinlipsSafejar.zip"));
                        } catch (URISyntaxException ex) {
                            Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (IOException ex) {
                            Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                     
                }
  
        }

        
        
    } catch (IOException ex) {
        System.out.println("error checking updates");

    }
    }
    
    public void refresh(){ //refresh notification and icon
    
        ImageIcon icon = new ImageIcon("src/Images/penguinlips-red.png");
        icon.getImage().flush();
        jLabel3.setIcon(icon);
            
        jTextFieldNoti.setText("");
    
    }
    
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jTextFieldNoti = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jButton12 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jTextField2 = new javax.swing.JTextField();
        jTextField3 = new javax.swing.JTextField();
        jTextField4 = new javax.swing.JTextField();
        jTextField5 = new javax.swing.JTextField();
        jTextField6 = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        jButton10 = new javax.swing.JButton();
        jButton11 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(212, 243, 238));
        jPanel1.setName(""); // NOI18N

        jPanel2.setBackground(new java.awt.Color(43, 87, 154));

        jTextFieldNoti.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextFieldNotiMouseClicked(evt);
            }
        });
        jTextFieldNoti.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldNotiActionPerformed(evt);
            }
        });
        jTextFieldNoti.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextFieldNotiKeyTyped(evt);
            }
        });

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/penguinlips-red.png"))); // NOI18N
        jLabel3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel3MouseClicked(evt);
            }
        });

        jButton12.setBackground(new java.awt.Color(43, 87, 154));
        jButton12.setForeground(new java.awt.Color(255, 255, 255));
        jButton12.setText("penguinlips");
        jButton12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton12ActionPerformed(evt);
            }
        });

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/GitHub_logo.png"))); // NOI18N
        jLabel2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel2MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(328, 328, 328)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(jLabel3)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(27, 27, 27)
                        .addComponent(jTextFieldNoti, javax.swing.GroupLayout.PREFERRED_SIZE, 216, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(321, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton12)
                        .addContainerGap())))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(0, 9, Short.MAX_VALUE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel3)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jTextFieldNoti, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addContainerGap())
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addGap(70, 70, 70))))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jButton12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(0, 0, Short.MAX_VALUE))))
        );

        jPanel3.setBackground(new java.awt.Color(204, 204, 255));

        jButton1.setText("domain");

        jButton2.setText("username/ph");

        jButton3.setText("email");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setText("password");

        jButton6.setText("notes");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jTextField2, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 172, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, 104, Short.MAX_VALUE)
                    .addComponent(jTextField3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, 146, Short.MAX_VALUE)
                    .addComponent(jTextField4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, 147, Short.MAX_VALUE)
                    .addComponent(jTextField5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jTextField6)))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2)
                    .addComponent(jButton3)
                    .addComponent(jButton4)
                    .addComponent(jButton6))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(21, Short.MAX_VALUE))
        );

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null}
            },
            new String [] {
                "domain", "username/ph", "email", "pass", "notes"
            }
        )

        //{public boolean isCellEditable(int row, int column){return false;}}

    );
    jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
        public void mouseClicked(java.awt.event.MouseEvent evt) {
            jTable1MouseClicked(evt);
        }
    });
    jScrollPane1.setViewportView(jTable1);

    jButton7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/browser-icon.png"))); // NOI18N
    jButton7.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jButton7ActionPerformed(evt);
        }
    });

    jButton8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/sql-update.png"))); // NOI18N
    jButton8.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jButton8ActionPerformed(evt);
        }
    });

    jButton9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/delete.png"))); // NOI18N
    jButton9.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jButton9ActionPerformed(evt);
        }
    });

    jButton10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/sync-icon.png"))); // NOI18N
    jButton10.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jButton10ActionPerformed(evt);
        }
    });

    jButton11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/plus icon green.png"))); // NOI18N
    jButton11.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jButton11ActionPerformed(evt);
        }
    });

    jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Settings-icon.png"))); // NOI18N
    jButton5.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jButton5ActionPerformed(evt);
        }
    });

    javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
    jPanel1.setLayout(jPanel1Layout);
    jPanel1Layout.setHorizontalGroup(
        jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 884, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGap(18, 18, 18)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jButton10, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jButton8, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton9, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addComponent(jButton11, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addContainerGap(18, Short.MAX_VALUE))
    );
    jPanel1Layout.setVerticalGroup(
        jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel1Layout.createSequentialGroup()
            .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(18, 18, 18)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 325, Short.MAX_VALUE)
                    .addContainerGap())
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGap(49, 49, 49)
                    .addComponent(jButton11)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton7)
                    .addGap(27, 27, 27)
                    .addComponent(jButton8)
                    .addGap(26, 26, 26)
                    .addComponent(jButton10)
                    .addGap(26, 26, 26)
                    .addComponent(jButton9)
                    .addGap(28, 28, 28)
                    .addComponent(jButton5)
                    .addGap(35, 35, 35))))
    );

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
    );
    layout.setVerticalGroup(
        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
    );

    pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jLabel3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel3MouseClicked
        // TODO add your handling code here:
	int len = 16;
                
        final String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz`~!@#$%^&*()_+-={}[]|\";:,<.>/?0123456789";

	SecureRandom random = new SecureRandom();
	StringBuilder sb = new StringBuilder();

	for (int i = 0; i < len; i++)
	{
		int randomIndex = random.nextInt(chars.length());
		sb.append(chars.charAt(randomIndex));
	}

        jTextField5.setText(sb.toString());
        
        
        message = "password generated !";
        
        try {
            config();
        } catch (IOException ex) {
            Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
        }
        if("true".equals(anim)){

            animation();

        }else{
            jTextFieldNoti.setText(message);

        }
        
        
    }//GEN-LAST:event_jLabel3MouseClicked

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        // TODO add your handling code here:
        refresh();
        
        boolean table = jTable1.getSelectionModel().isSelectionEmpty();
        if (table == false){
        
            int row = jTable1.getSelectedRow();
            String br = (String) jTable1.getValueAt(row, 0);



            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                try {
                    Desktop.getDesktop().browse(new URI(br));
                } catch (URISyntaxException ex) {
                    Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }else{
            JOptionPane.showMessageDialog(null,"select a row to browse","warning !",JOptionPane.INFORMATION_MESSAGE);
        }
        
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        // TODO add your handling code here:
        
        String domnf = null,umef = null,elf = null,pswdf = null,notesf = null;
        
        boolean table = jTable1.getSelectionModel().isSelectionEmpty();
        if (table == false){
            
        
            int row = jTable1.getSelectedRow();

            String dm = (String) jTable1.getValueAt(row, 0);
            String un = (String) jTable1.getValueAt(row, 1);
            String em = (String) jTable1.getValueAt(row, 2);
            String ps = (String) jTable1.getValueAt(row, 3);
            String nt = (String) jTable1.getValueAt(row, 4);




            try{
            Connection con = db.getConnection();

            Statement stmt=con.createStatement();

            //need to optimize here: a limit of 1000 password arises


            int[] arr = new int[1000];
            ResultSet rst=stmt.executeQuery("select * from data");  

            int i =0;
            while(rst.next()){

                arr[i] = rst.getInt(1);
                i = i+1;


            }


            String dom = ("SELECT domain FROM data WHERE id='"+arr[row]+"';");
            ResultSet domn = stmt.executeQuery(dom);
            while (domn.next()) {

            domnf = domn.getString(1);

            }

            String uname = ("SELECT uname FROM data WHERE id='"+arr[row]+"';");
            ResultSet ume = stmt.executeQuery(uname);
            while (ume.next()) {
            umef = ume.getString(1);
            }


            String eml = ("SELECT email FROM data WHERE id='"+arr[row]+"';");
            ResultSet el = stmt.executeQuery(eml);
            while (el.next()) {
            elf = el.getString(1);
            }


            String pass = ("SELECT pwd FROM data WHERE id='"+arr[row]+"';");
            ResultSet pswd = stmt.executeQuery(pass);
            while (pswd.next()) {
            pswdf = pswd.getString(1);
            }


            String note = ("SELECT note FROM data WHERE id='"+arr[row]+"';");
            ResultSet notes = stmt.executeQuery(note);
            while (notes.next()) {
            notesf = notes.getString(1);
            }


            String from="",to="";
            if(dm == null ? domnf != null : !dm.equals(domnf)){
                from = from+"domain : "+domnf+"  ";
                to = to+"domain : "+dm+"  ";
            }
            if(un == null ? umef != null : !un.equals(umef)){
                from = from+"username : "+umef+"  ";
                to = to+"username : "+un+"  ";
            }
            if(em == null ? elf != null : !em.equals(elf)){
                from = from+"email : "+elf+"  ";
                to = to+"email : "+em+"  ";
            }
            if(ps == null ? pswdf != null : !ps.equals(pswdf)){
                from = from+"password : "+pswdf+"  ";
                to = to+"password : "+ps+"  ";
            }
            if(nt == null ? notesf != null : !nt.equals(notesf)){
                from = from+"notes : "+notesf+"  ";
                to = to+"notes : "+nt+"  ";
            }



            int input = JOptionPane.showOptionDialog(null, "Changing from "+"\n"+from+"\n"+"To "+"\n"+to,"warning !", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, null);

            if(input == JOptionPane.OK_OPTION)
            {
                String upd = ("UPDATE data SET domain='"+dm+"', uname='"+un+"', email='"+em+"', pwd='"+ps+"', note='"+nt+"' WHERE id='"+arr[row]+"';");
                stmt.executeUpdate(upd);
                message = "data updated !";
                config();
                if ("true".equals(anim)){
                    animation();

                }else{
                    jTextFieldNoti.setText(message);
                }


            }
            if(input == JOptionPane.CANCEL_OPTION)
            {

                selectTable();

            }


            con.close();


            }
            catch(Exception e){

            JOptionPane.showMessageDialog(null,e+"\ncheck your configuration","error !",JOptionPane.INFORMATION_MESSAGE);
            }
        }else{
            JOptionPane.showMessageDialog(null,"select a row to update","warning !",JOptionPane.INFORMATION_MESSAGE);
        }

    }//GEN-LAST:event_jButton8ActionPerformed

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed
        // TODO add your handling code here:
        
        int id = 0;
        
        String dm = jTextField2.getText();
        jTextField2.setText("");
        String un = jTextField3.getText();
        jTextField3.setText("");
        String em = jTextField4.getText();
        jTextField4.setText("");
        String pd = jTextField5.getText();
        jTextField5.setText("");
        String nt = jTextField6.getText();
        jTextField6.setText("");
        
        
        
        
        
        if((!"".equals(dm) && !"".equals(pd)) && (!"".equals(un) || !"".equals(em)))
        {

            //sanitizing domain
            
            String fullText = dm;
            String singleWord = "https://";
            String singleWord2 = "http://";
            boolean find;


            if (fullText.toLowerCase().indexOf(singleWord.toLowerCase()) > -1) {

                find = true;
            }else{
                if (fullText.toLowerCase().indexOf(singleWord2.toLowerCase()) > -1){
                    find = true;
                }else{
                dm = dm.replace("https://", "");
                dm = "https://"+dm;
                }
            }

            //connection

            try{
            Connection con = db.getConnection();

            Statement st=con.createStatement();
            String query = ("SELECT * FROM data ORDER BY id ASC LIMIT 1;");
            ResultSet rs = st.executeQuery(query);
            if (rs.next()) {
                id = rs.getInt("id");

            }

            id = id-1;
            st.executeUpdate("INSERT INTO data " + "VALUES ('" + id + "','" + dm + "','" + un + "','" + em + "','" + pd + "','" + nt + "')");

            selectTable();
            message = "data inserted !";
            
            config();
            if("true".equals(anim)){

            animation();

            }else{
                jTextFieldNoti.setText(message);

            }
            
            ImageIcon icon = new ImageIcon("src/Images/penguinlips-green.png");
            icon.getImage().flush();
            jLabel3.setIcon( icon );
            
            }


            catch(Exception e){

            JOptionPane.showMessageDialog(null,e+"\ncheck your configuration","error !",JOptionPane.INFORMATION_MESSAGE);
            }
        
        }else{
            JOptionPane.showMessageDialog(null,"you have missed mandatory fields","warning !",JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_jButton11ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        // TODO add your handling code here:
        
        boolean table = jTable1.getSelectionModel().isSelectionEmpty();
        if (table == false){
            

        
            int row = jTable1.getSelectedRow();
            String dm = (String) jTable1.getValueAt(row, 0);
            String un = (String) jTable1.getValueAt(row, 1);
            String em = (String) jTable1.getValueAt(row, 2);
            String ps = (String) jTable1.getValueAt(row, 3);

            
            int input = JOptionPane.showOptionDialog(null, "deleting row : "+(row+1)+"\ndomain : "+dm+"\nusername : "+un, "warning !", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, null);

            if(input == JOptionPane.OK_OPTION)
            {

                
                try{
                Connection con = db.getConnection();

                Statement st=con.createStatement();
                String dlt = ("DELETE FROM data WHERE domain='"+dm+"' and uname='"+un+"' and email='"+em+"' and pwd='"+ps+"';");
                st.executeUpdate(dlt);

                selectTable();
                message = "data deleted !";

                config();
                if("true".equals(anim)){
                animation();

                }else{
                    jTextFieldNoti.setText(message);
                }
                
                ImageIcon icon = new ImageIcon("src/Images/penguinlips-darkred.png");
                icon.getImage().flush();
                jLabel3.setIcon( icon );
                
                }


                catch(Exception e){

                JOptionPane.showMessageDialog(null,e+"\ncheck your configuration","error !",JOptionPane.INFORMATION_MESSAGE);
                }


            }
            
            


        }else{
            JOptionPane.showMessageDialog(null,"select a row to delete","warning !",JOptionPane.INFORMATION_MESSAGE);
        }
        
        
        
    }//GEN-LAST:event_jButton9ActionPerformed

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        // TODO add your handling code here:
        selectTable();
        refresh();
        
        jTextField2.setText("");
        jTextField3.setText("");
        jTextField4.setText("");
        jTextField5.setText("");
        jTextField6.setText("");
        
                
    }//GEN-LAST:event_jButton10ActionPerformed

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        // TODO add your handling code here:

    }//GEN-LAST:event_jTable1MouseClicked

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
        refresh();
        
        inival = 250;
        popup();
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton12ActionPerformed
        // TODO add your handling code here:
        refresh();
        
        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
            try {
                Desktop.getDesktop().browse(new URI("https://penguinlips.github.io/ansan_mathew/"));
            } catch (URISyntaxException ex) {
                Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_jButton12ActionPerformed

    private void jLabel2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel2MouseClicked
        // TODO add your handling code here:
        refresh();
        
        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
            try {
                Desktop.getDesktop().browse(new URI("https://www.github.com/penguinlips/PenguinlipsSafe"));
            } catch (URISyntaxException ex) {
                Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_jLabel2MouseClicked

    private void jTextFieldNotiMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextFieldNotiMouseClicked
        // TODO add your handling code here:
        refresh();
        
    }//GEN-LAST:event_jTextFieldNotiMouseClicked

    private void jTextFieldNotiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldNotiActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldNotiActionPerformed

    private void jTextFieldNotiKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldNotiKeyTyped
        // TODO add your handling code here
    }//GEN-LAST:event_jTextFieldNotiKeyTyped

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Home.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Home.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Home.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Home.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                
                try {
                    new Home().setVisible(true);
                } catch (IOException ex) {
                    Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JTextField jTextFieldNoti;
    // End of variables declaration//GEN-END:variables
}
