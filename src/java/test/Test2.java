/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import com.google.gdata.client.Query;
import com.google.gdata.client.contacts.ContactsService;
import com.google.gdata.data.contacts.ContactEntry;
import com.google.gdata.data.contacts.ContactFeed;
import com.google.gdata.data.contacts.GroupMembershipInfo;
import com.google.gdata.data.extensions.Email;
import com.google.gdata.data.extensions.ExtendedProperty;
import com.google.gdata.data.extensions.Im;
import com.google.gdata.util.ServiceException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author ibrahim
 */
public class Test2 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws MalformedURLException {
        try {
            ContactsService myService = new ContactsService("ActiveDD-TODOLIST-1.0");
            myService.setAuthSubToken("1/5ynaeEh4_l6E1JmHx8d7KcEymOHrfpUDI1uNaPRwV_U", null);
            System.out.println(printAllContacts(myService).toString(5));
            ;
            // com.google.gdata.data.projecthosting.SendEmail email=new com.google.gdata.data.projecthosting.SendEmail();
            //email.se
        } catch (Exception ex) {
            Logger.getLogger(Test2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static JSONArray printAllContacts(ContactsService myService)
            throws ServiceException, IOException, JSONException {
        JSONArray contacts = new JSONArray();
        // Request the feed
        URL feedUrl = new URL("https://www.google.com/m8/feeds/contacts/default/full");
        Query myQuery = new Query(feedUrl);
        myQuery.setMaxResults(2000);
        ContactFeed resultFeed = myService.getFeed(myQuery, ContactFeed.class);
        String username = resultFeed.getTitle().getPlainText();
        username = username.substring(0, username.length() - "'s Contacts".length());
        JSONObject jSONObject = new JSONObject("{name:" + username + ",email:" + resultFeed.getId() + "}");
        contacts.put("userName:" + jSONObject + "");
        System.out.println(resultFeed.getEntries().size());
        for (int i = 0; i < resultFeed.getEntries().size(); i++) {

            ContactEntry entry = resultFeed.getEntries().get(i);
            if (entry.getEmailAddresses().size() > 0) {
                JSONObject contact = new JSONObject();
                contact.put("name", entry.getTitle().getPlainText());
                int j=0;
                JSONArray jsona=new JSONArray();
                for (Email email : entry.getEmailAddresses()) {
                    jsona.put(email.getAddress());
                }
                contact.put("email",jsona);
                contacts.put(contact);
            }
        }
        return contacts;
    }
}
