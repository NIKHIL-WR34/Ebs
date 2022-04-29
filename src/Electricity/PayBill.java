package Electricity;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.sql.*;
import com.paytm.pg.merchant.*;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.SecureRandom;
import org.json.JSONObject;

public class PayBill extends JFrame implements ActionListener{
    JLabel l1,l2,l3,l4,l5, l6;
    JLabel l11, l12, l13, l14, l15;
    JTextField t1;
    Choice c1,c2;
    JButton b1,b2;
    String meter;
    String totalBill;
    PayBill(String meter){
        this.meter = meter;
        setLayout(null);
        
        setBounds(550, 220, 900, 600);
        
        JLabel title = new JLabel("Electricity Bill");
        title.setFont(new Font("Tahoma", Font.BOLD, 24));
        title.setBounds(120, 5, 400, 30);
        add(title);
        
        l1 = new JLabel("Meter No");
        l1.setBounds(35, 80, 200, 20);
        add(l1);
        
        JLabel l11 = new JLabel();
        l11.setBounds(300, 80, 200, 20);
        add(l11);
        
        JLabel l2 = new JLabel("Name");
        l2.setBounds(35, 140, 200, 20);
        add(l2);
        
        JLabel l12 = new JLabel();
        l12.setBounds(300, 140, 200, 20);
        add(l12);
        
        l3 = new JLabel("Month");
        l3.setBounds(35, 200, 200, 20);
        add(l3);
        
        c1 = new Choice();
        c1.setBounds(300, 200, 200, 20);
        c1.add("January");
        c1.add("February");
        c1.add("March");
        c1.add("April");
        c1.add("May");
        c1.add("June");
        c1.add("July");
        c1.add("August");
        c1.add("September");
        c1.add("October");
        c1.add("November");
        c1.add("December");
        add(c1);
        
        
        l4 = new JLabel("Units");
        l4.setBounds(35, 260, 200, 20);
        add(l4);
        
        JLabel l13 = new JLabel();
        l13.setBounds(300, 260, 200, 20);
        add(l13);
        
        l5 = new JLabel("Total Bill");
        l5.setBounds(35, 320, 200, 20);
        add(l5);
        
        JLabel l14 = new JLabel();
        l14.setBounds(300, 320, 200, 20);
        add(l14);
        
        l6 = new JLabel("Status");
        l6.setBounds(35, 380, 200, 20);
        add(l6);
        
        JLabel l15 = new JLabel();
        l15.setBounds(300, 380, 200, 20);
        l15.setForeground(Color.RED);
        add(l15);
        
                
        b1 = new JButton("Pay");
        b1.setBounds(100, 460, 100, 25);
        add(b1);
        b2 = new JButton("Back");
        b2.setBounds(230, 460, 100, 25);
        add(b2);
        
        b1.setBackground(Color.BLACK);
        b1.setForeground(Color.WHITE);

        b2.setBackground(Color.BLACK);
        b2.setForeground(Color.WHITE);
        
        
        try{
            Conn c = new Conn();
            ResultSet rs = c.s.executeQuery("select * from customer where meter = '"+meter+"'");
            while(rs.next()){
                l11.setText(rs.getString("meter"));
                l12.setText(rs.getString("name"));
            }
            rs = c.s.executeQuery("select * from bill where meter = '"+meter+"' AND month = 'January' ");
                    if(rs.next()) {
                        l13.setText(rs.getString("units"));
                            l14.setText(rs.getString("total_bill"));
                            l15.setText(rs.getString("status"));
                            totalBill = rs.getString("total_bill");
                            if(rs.getString("status").equalsIgnoreCase("Paid")) {
                                b1.setVisible(false);
                            } else {
                                b1.setVisible(true);
                            }
                    } else {
                        l13.setText("N/A");
                        l14.setText("N/A");
                        l15.setText("N/A");
                        b1.setVisible(false);
                    }
        }catch(Exception e){}
        
        c1.addItemListener(new ItemListener(){
            @Override
            public void itemStateChanged(ItemEvent ae){
                try{
                    Conn c = new Conn();
                    ResultSet rs = c.s.executeQuery("select * from bill where meter = '"+meter+"' AND month = '"+c1.getSelectedItem()+"'");
                    if(rs.next()) {
                        l13.setText(rs.getString("units"));
                            l14.setText(rs.getString("total_bill"));
                            l15.setText(rs.getString("status"));
                            totalBill = rs.getString("total_bill");
                            if(rs.getString("status").equalsIgnoreCase("Paid")) {
                                b1.setVisible(false);
                            } else {
                                b1.setVisible(true);
                            }
                    } else {
                        l13.setText("N/A");
                        l14.setText("N/A");
                        l15.setText("N/A");
                        b1.setVisible(false);
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        });

        
        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("icon/bill.png"));
        Image i2 = i1.getImage().getScaledInstance(600, 300,Image.SCALE_DEFAULT);
        ImageIcon i3 = new ImageIcon(i2);
        JLabel l21 = new JLabel(i3);
        l21.setBounds(400, 120, 600, 300);
        add(l21);
        
        b1.addActionListener(this);
        b2.addActionListener(this);
        
        getContentPane().setBackground(Color.WHITE);        
    }
    public void actionPerformed(ActionEvent ae){
        if(ae.getSource() == b1){
            try{
                Conn c = new Conn();
//                c.s.executeUpdate("update bill set status = 'Paid' where meter = '"+meter+"' AND month = '"+c1.getSelectedItem()+"'");
                    
                SecureRandom random = new SecureRandom();
                int ranum = random.nextInt(1000);
                String franum = String.format("%05d", ranum);
                String orderId = meter+"_"+c1.getSelectedItem()+"_"+franum;
                JSONObject paytmParams = new JSONObject();

                JSONObject body = new JSONObject();
                body.put("requestType", "Payment");
                body.put("mid", "PXMSNA96568147257337");
                body.put("websiteName", "WEBSTAGING");
                body.put("orderId", orderId);
                body.put("callbackUrl", "http://localhost/paytm/callback.php");

                JSONObject txnAmount = new JSONObject();
                txnAmount.put("value", totalBill);
                txnAmount.put("currency", "INR");

                JSONObject userInfo = new JSONObject();
                userInfo.put("custId", meter);
                body.put("txnAmount", txnAmount);
                body.put("userInfo", userInfo);

                String checksum = PaytmChecksum.generateSignature(body.toString(), "ChAxe&6FbpHvTTOH");

                JSONObject head = new JSONObject();
                head.put("signature", checksum);

                paytmParams.put("body", body);
                paytmParams.put("head", head);

                String post_data = paytmParams.toString();

                URL url = new URL("https://securegw-stage.paytm.in/theia/api/v1/initiateTransaction?mid=PXMSNA96568147257337&orderId="+orderId);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setDoOutput(true);
                connection.connect();

                DataOutputStream requestWriter = new DataOutputStream(connection.getOutputStream());
                requestWriter.writeBytes(post_data);
                requestWriter.close();
                String responseData = "";
                InputStream is = connection.getInputStream();
                BufferedReader responseReader = new BufferedReader(new InputStreamReader(is));
                    if ((responseData = responseReader.readLine()) != null) {
                        JSONObject repsonseJson = new JSONObject(responseData);
                        JSONObject responseBody = repsonseJson.getJSONObject("body");
                        String txnToken = repsonseJson.getJSONObject("body").getString("txnToken");
                        Desktop.getDesktop().browse(new URL("http://localhost/paytm/payment.php?order_id="+orderId+"&txn_token="+txnToken).toURI());
                        this.setVisible(false);
                    }
                responseReader.close();
            } catch(Exception e){
                e.printStackTrace();
//            this.setVisible(false);
//            new Paytm(meter).setVisible(true);
            }
        }else if(ae.getSource()== b2){
            this.setVisible(false);
        }        
    }
    
       
    public static void main(String[] args){
        new PayBill("").setVisible(true);
    }
}
