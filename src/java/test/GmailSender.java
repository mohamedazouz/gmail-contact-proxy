/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import javax.mail.*;
import javax.mail.internet.*;
import javax.mail.internet.MimeMessage.RecipientType;

import java.util.*;

/**
 * Simple Class to send an email using JavaMail API (javax.mail) and Gmail SMTP server
 * @author Dunith Dhanushka, <a href="mailto:dunithd@gmail.com">dunithd@gmail.com</a>
 * @version 1.0
 */
public class GmailSender {

    private static String HOST = "smtp.gmail.com";
    private static String USER = "noreply@activedd.com";
    private static String PASSWORD = "qunEMIF";
    private static String PORT = "465";
    private static String FROM = "mazouz@activedd.com";
    //private static String TO = "mido15210@hotmail.com";
    private static String STARTTLS = "true";
    private static String AUTH = "true";
    private static String DEBUG = "true";
    private static String SOCKET_FACTORY = "javax.net.ssl.SSLSocketFactory";

    public static synchronized void send(String to,String messageSubject,String messageText) throws Exception {
        //Use Properties object to set environment properties
        Properties props = new Properties();

        props.put("mail.smtp.host", HOST);
        props.put("mail.smtp.port", PORT);
        props.put("mail.smtp.user", USER);

        props.put("mail.smtp.auth", AUTH);
        props.put("mail.smtp.starttls.enable", STARTTLS);
        props.put("mail.smtp.debug", DEBUG);

        props.put("mail.smtp.socketFactory.port", PORT);
        props.put("mail.smtp.socketFactory.class", SOCKET_FACTORY);
        props.put("mail.smtp.socketFactory.fallback", "false");



        //Obtain the default mail session
        Session session = Session.getDefaultInstance(props, null);
        session.setDebug(true);

        //Construct the mail message
        MimeMessage message = new MimeMessage(session);
        message.setText(messageText);
        message.setSubject(messageSubject);
        message.setFrom(new InternetAddress(FROM));
        message.addRecipient(RecipientType.TO, new InternetAddress(to));
        message.saveChanges();

        //Use Transport to deliver the message
        Transport transport = session.getTransport("smtp");
        transport.connect(HOST, USER, PASSWORD);
        transport.sendMessage(message, message.getAllRecipients());
        transport.close();
    }

    public static void main(String[] args) throws Exception {
        GmailSender.send("mazouz@activedd.com","الالالالالالال","صشمشمش");
        System.out.println("Mail sent successfully!");
    }
}
