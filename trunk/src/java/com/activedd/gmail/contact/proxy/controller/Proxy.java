/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.activedd.gmail.contact.proxy.controller;

import com.google.gdata.client.Query;
import com.google.gdata.data.contacts.ContactEntry;
import com.google.gdata.data.extensions.Email;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import com.google.gdata.client.contacts.ContactsService;
import com.google.gdata.client.http.AuthSubUtil;
import com.google.gdata.data.calendar.CalendarFeed;
import com.google.gdata.data.contacts.ContactFeed;
import com.google.gdata.util.ServiceException;
import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.json.JSONArray;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;
import test.GmailSender;

/**
 *
 * @author prog
 */
public class Proxy extends MultiActionController {

    HttpSession session;

    /**
     * 
     * @param request
     * @param response
     */
    public void login(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.sendRedirect(getAuthLink());
    }

    public void logincalender(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.sendRedirect(getAuthCalender());
    }

    public void getcontacts(HttpServletRequest request, HttpServletResponse response) {
        //TO DO: go online on facebook.
        //get the seesion key from url as parameter
        try {
            request.setCharacterEncoding("UTF-8");
            response.setCharacterEncoding("UTF-8");
            JSONArray jSONArray = null;
            int status = 0;
            String message = "";

            if (request.getParameter("token") != null && !(request.getParameter("token").equals(""))) {
                String token = request.getParameter("token");
                ContactsService myService = new ContactsService("ActiveDD-TODOLIST-1.0");
                myService.setAuthSubToken(token, null);
                jSONArray = printAllContacts(myService);
            } else {
                jSONArray = new JSONArray("[{error:error}]");
            }
            jSONArray.write(response.getWriter());
            response.getWriter().close();
        } catch (Exception e) {
        }


    }

    public void getUserDetails(HttpServletRequest request, HttpServletResponse response) {
        //TO DO: go online on facebook.
        //get the seesion key from url as parameter
        try {
            request.setCharacterEncoding("UTF-8");
            response.setCharacterEncoding("UTF-8");
            JSONObject userDetails = null;
            int status = 0;
            String message = "";

            if (request.getParameter("token") != null && !(request.getParameter("token").equals(""))) {
                String token = request.getParameter("token");
                ContactsService myService = new ContactsService("ActiveDD-TODOLIST-1.0");
                myService.setAuthSubToken(token, null);
                userDetails = printUserDetails(myService);
            } else {
                userDetails = new JSONObject("{'status':400}");
            }
            userDetails.write(response.getWriter());
            response.getWriter().close();
        } catch (Exception e) {
        }
    }

    public void send(HttpServletRequest request, HttpServletResponse response) {
        try {
            request.setCharacterEncoding("UTF-8");
            response.setCharacterEncoding("UTF-8");
            
            response.setContentType("text/html;charset=UTF-8");
            int status = 0;
            String message = "";
            JSONObject jSONObject = new JSONObject();
            if ((request.getParameter("msg") != null && !(request.getParameter("msg").equals(""))) && (request.getParameter("to") != null && !(request.getParameter("to").equals(""))) && (request.getParameter("su") != null && !(request.getParameter("su").equals("")))) {
                try {
                    //GmailSender.send(URLDecoder.decode(request.getParameter("to"),"UTF-8"), URLDecoder.decode(request.getParameter("su"),"UTF-8"), URLDecoder.decode(request.getParameter("msg"),"UTF-8"));
                    GmailSender.send(request.getParameter("to"), request.getParameter("su"),request.getParameter("msg"));
                    status = 200;
                    message = "success";
                } catch (Exception ex) {
                    status = 417;//http exception failed code
                    message = "Expectation Failed";
                }
            } else {
                status = 400; //http bad request code
                message = "Bad Request";
            }
            jSONObject = new JSONObject("{status:" + status + ",message:" + message + "}");
            jSONObject.write(response.getWriter());
            response.getWriter().close();
        } catch (Exception e) {
        }
    }

    private String getAuthLink() {
        String authSubLoginLink = AuthSubUtil.getRequestUrl("http://calendar.activedd.com/authsub/getToken.htm",
                "https://www.google.com/m8/feeds/ ",
                false,
                true);
        return authSubLoginLink;
    }

    private String getAuthCalender() {
        String authSubLoginLink = AuthSubUtil.getRequestUrl("http://calendar.activedd.com/authsub/getToken.htm",
                "https://www.google.com/m8/feeds/ https://www.google.com/calendar/feeds",
                false,
                true);
        return authSubLoginLink;
    }

    private static JSONArray printAllContacts(ContactsService myService)
            throws ServiceException, IOException, JSONException {
        JSONArray contacts = new JSONArray();
        // Request the feed
        URL feedUrl = new URL("https://www.google.com/m8/feeds/contacts/default/full");
        Query myQuery = new Query(feedUrl);
        myQuery.setMaxResults(2000);
        ContactFeed resultFeed = myService.getFeed(myQuery, ContactFeed.class);
        String username = resultFeed.getTitle().getPlainText();
        username = username.substring(0, username.length() - "'s Contacts".length());
        JSONObject jSONObject = new JSONObject();
        jSONObject.accumulate("name", username);
        jSONObject.accumulate("email", resultFeed.getId());
        contacts.put(jSONObject);
        for (int i = 0; i < resultFeed.getEntries().size(); i++) {
            ContactEntry entry = resultFeed.getEntries().get(i);
            if (entry.getEmailAddresses().size() > 0) {
                JSONObject contact = new JSONObject();
                contact.put("name", entry.getTitle().getPlainText());
                int j = 0;
                JSONArray jsona = new JSONArray();
                for (Email email : entry.getEmailAddresses()) {
                    jsona.put(email.getAddress());
                }
                contact.put("email", jsona);
                contacts.put(contact);
            }
        }
        return contacts;
    }

    private static JSONObject printUserDetails(ContactsService myService)
            throws ServiceException, IOException, JSONException {
        JSONArray contacts = new JSONArray();
        // Request the feed
        URL feedUrl = new URL("https://www.google.com/m8/feeds/contacts/default/full");
        ContactFeed resultFeed = myService.getFeed(feedUrl, ContactFeed.class);
        String username = resultFeed.getTitle().getPlainText();
        username = username.substring(0, username.length() - "'s Contacts".length());
        JSONObject userDetails = new JSONObject();
        userDetails.accumulate("name", username);
        userDetails.accumulate("email", resultFeed.getId());
        return userDetails;
    }

    public static void main(String[] args) throws UnsupportedEncodingException {
        /*String token = "1/XTGoF8SMwwETGnmVW5jCuwBw-YdCParMDHc8Jrlk22E";
        ContactsService myService = new ContactsService("ActiveDD-TODOLIST-1.0");
        myService.setAuthSubToken(token, null);
        JSONObject userDetails;
        try {
            userDetails = printUserDetails(myService);
            System.out.println((userDetails.toString(5)));
        } catch (Exception ex) {
            Logger.getLogger(Proxy.class.getName()).log(Level.SEVERE, null, ex);
        }*/
        System.out.println(URLDecoder.decode("%D9%88%D9%84%D8%A7%20%D8%A7%D9%8A%D9%87%20%D8%9F %D9%88%D9%84%D8%A7%20%D8%A7%D9%8A%D8%B9","UTF-8"));


    }
}
