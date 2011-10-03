/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import com.google.gdata.client.Query;
import com.google.gdata.client.authn.oauth.OAuthException;
import com.google.gdata.client.contacts.ContactsService;
import com.google.gdata.client.http.AuthSubUtil;
import com.google.gdata.data.contacts.ContactEntry;
import com.google.gdata.data.contacts.ContactFeed;
import com.google.gdata.data.contacts.GroupMembershipInfo;
import com.google.gdata.data.extensions.Email;
import com.google.gdata.data.extensions.ExtendedProperty;
import com.google.gdata.data.extensions.Im;
import com.google.gdata.util.AuthenticationException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ibrahim
 */
public class Test {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws OAuthException {
        try {
            ContactsService myService = new ContactsService("exampleCo-exampleApp-1");
            myService.setUserCredentials("mohamedaliazouz@gmail.com", "mohamedazouz");
            printAllContacts(myService);
        } catch (Exception ex) {
            Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void printAllContacts(ContactsService myService)
            throws Exception {
        // Request the feed
        URL feedUrl = new URL("https://www.google.com/m8/feeds/contacts/mohamedaliazouz@gmail.com/full");


        Query myQuery = new Query(feedUrl);
        myQuery.setMaxResults(2000);
        ContactFeed resultFeed = myService.getFeed(myQuery, ContactFeed.class);
        // Print the results
        System.out.println(resultFeed.getTitle().getPlainText());
        System.out.println(resultFeed.getEntries().size());
        for (int i = 0; i < resultFeed.getEntries().size(); i++) {
            ContactEntry entry = resultFeed.getEntries().get(i);
            System.out.println("\t" + entry.getTitle().getPlainText());

            System.out.println("Email addresses:");
            for (Email email : entry.getEmailAddresses()) {
                System.out.print(" " + email.getAddress());
                if (email.getRel() != null) {
                    System.out.print(" rel:" + email.getRel());
                }
                if (email.getLabel() != null) {
                    System.out.print(" label:" + email.getLabel());
                }
                if (email.getPrimary()) {
                    System.out.print(" (primary) ");
                }
                System.out.print("\n");
            }

            System.out.println("IM addresses:");
            for (Im im : entry.getImAddresses()) {
                System.out.print(" " + im.getAddress());
                if (im.getLabel() != null) {
                    System.out.print(" label:" + im.getLabel());
                }
                if (im.getRel() != null) {
                    System.out.print(" rel:" + im.getRel());
                }
                if (im.getProtocol() != null) {
                    System.out.print(" protocol:" + im.getProtocol());
                }
                if (im.getPrimary()) {
                    System.out.print(" (primary) ");
                }
                System.out.print("\n");
            }

            System.out.println("Groups:");
            for (GroupMembershipInfo group : entry.getGroupMembershipInfos()) {
                String groupHref = group.getHref();
                System.out.println("  Id: " + groupHref);
            }

            System.out.println("Extended Properties:");
            for (ExtendedProperty property : entry.getExtendedProperties()) {
                if (property.getValue() != null) {
                    System.out.println("  " + property.getName() + "(value) = "
                            + property.getValue());
                } else if (property.getXmlBlob() != null) {
                    System.out.println("  " + property.getName() + "(xmlBlob)= "
                            + property.getXmlBlob().getBlob());
                }
            }
        }
    }
}
//http://calendar.activedd.com/authsub/HandleToken.htm

