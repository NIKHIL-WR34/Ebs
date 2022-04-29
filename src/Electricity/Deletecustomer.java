package Electricity;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.sql.*;

public class Deletecustomer extends JFrame implements ActionListener{
    JLabel l1, name, address, city, state, email, phone;
    JLabel nameValue, addressValue, cityValue, stateValue, emailValue, phoneValue;
    Choice c1,c2;
    JButton b1,b2;
    String selectedMeter;
    Deletecustomer(){
        setLayout(null);
        
        setBounds(550, 220, 900, 600);
        
        JLabel title = new JLabel("Delete Customer");
        title.setFont(new Font("Tahoma", Font.BOLD, 24));
        title.setBounds(120, 5, 400, 30);
        add(title);
        
        l1 = new JLabel("Meter No");
        l1.setBounds(35, 80, 200, 20);
        add(l1);
        
        name = new JLabel("Name");
        name.setBounds(35, 140, 200, 20);
        add(name);
           
        address = new JLabel("Address");
        address.setBounds(35, 200, 200, 20);
        add(address);
        
        city = new JLabel("City");
        city.setBounds(35, 260, 200, 20);
        add(city);
        
        state = new JLabel("State");
        state.setBounds(35, 320, 200, 20);
        add(state);
        
        email = new JLabel("Email");
        email.setBounds(35, 380, 200, 20);
        add(email);
        
        phone = new JLabel("Phone");
        phone.setBounds(35, 440, 200, 20);
        add(phone);
        
        nameValue = new JLabel();
        nameValue.setBounds(300, 140, 200, 20);
        add(nameValue);
        
        addressValue = new JLabel();
        addressValue.setBounds(300, 200, 200, 20);
        add(addressValue);
        
        cityValue = new JLabel();
        cityValue.setBounds(300, 260, 200, 20);
        add(cityValue);
        
        stateValue = new JLabel();
        stateValue.setBounds(300, 320, 200, 20);
        add(stateValue);
        
        emailValue = new JLabel();
        emailValue.setBounds(300, 380, 200, 20);
        add(emailValue);
        
        phoneValue = new JLabel();
        phoneValue.setBounds(300, 440, 200, 20);
        add(phoneValue);
        
        
        c1 = new Choice();
        c1.setBounds(300, 80, 200, 20);
        c1.add("Please Select A Meter No");
        
        try{
            Conn c = new Conn();
            ResultSet rs = c.s.executeQuery("select meter_number from meter_info");
            while(rs.next()){
                c1.add(rs.getString("meter_number"));
            }
            add(c1);
        }catch(Exception e){}
        
        c1.addItemListener(new ItemListener(){
            @Override
            public void itemStateChanged(ItemEvent ae){
                selectedMeter = ae.getItem().toString();
                try {
                    Conn c = new Conn();
                    ResultSet rs = c.s.executeQuery("select * from customer where meter = " + selectedMeter);
                    rs.next();
                    nameValue.setText(rs.getString("name"));
                    addressValue.setText(rs.getString("address"));
                    cityValue.setText(rs.getString("city"));
                    stateValue.setText(rs.getString("state"));
                    emailValue.setText(rs.getString("email"));
                    phoneValue.setText(rs.getString("phone"));                    
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        
        b1 = new JButton("Delete");
        b1.setBounds(100, 490, 100, 25);
        add(b1);
        b2 = new JButton("Cancel");
        b2.setBounds(230, 490, 100, 25);
        add(b2);
        
        b1.setBackground(Color.BLACK);
        b1.setForeground(Color.WHITE);

        b2.setBackground(Color.BLACK);
        b2.setForeground(Color.WHITE);
        
        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("icon/pop.png"));
        Image i2 = i1.getImage().getScaledInstance(225, 225,Image.SCALE_DEFAULT);
        ImageIcon i3 = new ImageIcon(i2);
        JLabel l21 = new JLabel(i3);
        l21.setBounds(400, 120, 600, 300);
        add(l21);
        
        b1.addActionListener(this);
        b2.addActionListener(this);
        
        getContentPane().setBackground(Color.WHITE);        
    }
    public void actionPerformed(ActionEvent ae){
        if(ae.getSource() == b1) {
            Conn c = new Conn();
            try {
            c.s.executeUpdate("DELETE FROM customer WHERE meter = " + this.selectedMeter);
            c.s.executeUpdate("DELETE FROM meter_info WHERE meter_number = " + this.selectedMeter);
            c.s.executeUpdate("DELETE FROM login WHERE meter_no = " + this.selectedMeter);
            this.setVisible(false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if(ae.getSource() == b2) {
            this.setVisible(false);
        } 
    } 
    
       
    public static void main(String[] args){
        new Deletecustomer().setVisible(true);
    }
}
