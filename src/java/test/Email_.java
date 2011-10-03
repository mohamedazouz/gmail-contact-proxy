package test;

import java.util.Properties;
import javax.mail.MessagingException;
import javax.mail.Session;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import javax.mail.internet.MimeMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;

/**
 *
 * @author ${user}
 */
public class Email_ {

    private JavaMailSenderImpl mailSender = null;
//    private SimpleMailMessage mimeMessageHelper = null;
    private MimeMessageHelper mimeMessageHelper = null;
    MimeMessage mimeMessage = null;

    /**
     * Defualt private constructor for testing
     */
    private  Email_() throws MessagingException {
        mailSender = new JavaMailSenderImpl();
//        mimeMessageHelper = new SimpleMailMessage();
        mimeMessage = mailSender.createMimeMessage();
        mimeMessageHelper = new MimeMessageHelper(mimeMessage);

        mailSender.setHost("smtp.gmail.com");
        mailSender.setPassword("qunEMIF12");
        mailSender.setUsername("noreply@forga.com");

        setUpSession();

        mimeMessageHelper.setFrom("shawary@activedd.com");
        mimeMessageHelper.setSubject("xxx");
        mimeMessageHelper.setText("xxxx", true);
        

    }

    /**
     * Send email to a single user
     * @param user
     */
    public void send(final String recipientEmail, final String mailBody, final String messageSubject) throws MessagingException {
        if (mailSender == null) {
            return;
        }
        try {
            MimeMessagePreparator preparator = new MimeMessagePreparator() {

                public void prepare(MimeMessage mimeMessage) throws Exception {
                    MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
                    message.setTo(recipientEmail);
                    message.setSubject(messageSubject);

                    message.setText(mailBody, true);
                }
            };
            mailSender.send(preparator);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Send email to a collection of user
     * @param user
     */
    public void send(final String[] recipientsEmails, final String mailBody, final String messageSubject) throws MessagingException {

        if (mailSender == null) {
            return;
        }

        try {
            MimeMessagePreparator preparator = new MimeMessagePreparator() {

                public void prepare(MimeMessage mimeMessage) throws Exception {
                    MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
                    message.setTo(recipientsEmails);
                    message.setSubject(messageSubject);

                    message.setText(mailBody, true);
                }
            };
            mailSender.send(preparator);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Setup email session attributes
     */
    private void setUpSession() {
        Properties props = System.getProperties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.auth", "true");

        Session session = Session.getDefaultInstance(props, null);
        session.setDebug(true);

        mailSender.setSession(session);
    }

    /**
     * Testing stub
     * @param args
     */
    public static void main(String[] args) throws MessagingException {

        Email_ email = new Email_();

        email.send("m_aliazouz@yahoo.com", "", "");

    }
}
