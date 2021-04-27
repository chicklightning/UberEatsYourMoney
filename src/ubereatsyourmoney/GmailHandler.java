/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ubereatsyourmoney;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.Base64;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.GmailScopes;
import com.google.api.services.gmail.model.Message;
import com.google.api.services.gmail.model.MessagePart;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Gabbo
 */
public class GmailHandler {
    
    public Gmail Service;
    
    private static final String APPLICATION_NAME = "Uber Eats [Your Money]";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "tokens";
    private static final String USER = "me";

    /**
     * Global instance of the scopes required by this quickstart.
     * If modifying these scopes, delete your previously saved tokens/ folder.
     */
    private static final List<String> SCOPES = Collections.singletonList(GmailScopes.GMAIL_READONLY);
    private static final String CREDENTIALS_FILE_PATH = "./credentials.json";
    
    public GmailHandler (Gmail service) {
        this.Service = service;
    }
    
    public List<Message> getUberEatsMessages(long daysSinceLastMod) throws IOException {
        // Get messages from the last day; because totals are calculated 1x per day, any
        // messages newer than this have not had their
        // totals factored in
        String query = "from:uber.us@uber.com newer_than:" + daysSinceLastMod + "d in:anywhere";
        List<Message> uberEatsMessages = this.Service.users().messages().list(USER).setQ(query).execute().getMessages();
        System.out.println(uberEatsMessages.size() + " total message(s) found in last " + daysSinceLastMod + " day(s).");
        return uberEatsMessages;
    }
    
    public double calculateTotalsFromEmails(List<Message> uberEatsMessages) {
        // Add new Uber Eats totals from email, specifically the snippets
        double sum = 0;
        sum = uberEatsMessages.stream().map(message -> {
            
            String messageContent = "";
            try {
                Message fullMessage = this.Service.users().messages().get(USER, message.getId()).setFormat("FULL").execute();
                messageContent = getContent(fullMessage);
                
                if (!messageContent.contains("updated receipt")) {
                    messageContent = "";
                }
            } catch (IOException ex) {
                System.out.println("Unable to obtain full message for message " + message.getId());
            }
            
            return (messageContent == null || !messageContent.contains("updated receipt")) ? "" : messageContent;
        }).map(snippet -> {
            String regExp = "^\\$(([1-9]\\d{0,2}(,\\d{3})*)|(([1-9]\\d*)?\\d))(\\.\\d\\d)?$";
            Pattern p = Pattern.compile(regExp);
            System.out.println("Finding matches");
            Matcher matcher = p.matcher(snippet);
            
            System.out.println("Matches processed");
            while (matcher.find()) {
                System.out.println("Transaction amount: $" + matcher.group(1));
            }
            System.out.println("Transactions reported");
            return matcher;
        }).map(matcher -> {
            double amountSpent = 0;
            if (matcher.find()) {
                amountSpent = Double.parseDouble(matcher.group(1));
            }
            
            return amountSpent;
        }).reduce(sum, (accumulator, _item) -> accumulator + _item);
        
        return sum;
    }
    
    public static Gmail buildService() throws IOException, GeneralSecurityException {
         // Build a new authorized API client service.
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        Gmail service = new Gmail.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();
        
        return service;
    }
    
    /**
    * Extracts the contents from the message provided.
    * Author: Amy DeGregorio/amdegregorio
    * 
    * @param message an email message
    * @return a string containing the body content of the email
    */
    private String getContent(Message message) {
       StringBuilder stringBuilder = new StringBuilder();
        try {
            getPlainTextFromMessageParts(message.getPayload().getParts(), stringBuilder);
            if (stringBuilder.length() == 0) {
                stringBuilder.append(message.getPayload().getBody().getData());
            }
            byte[] bodyBytes = Base64.decodeBase64(stringBuilder.toString());
            String text = new String(bodyBytes, "UTF-8");
            return text;
        } catch (UnsupportedEncodingException e) {
            System.out.println("UnsupportedEncoding: " + e.toString());
            return message.getSnippet();
       }
    }
    
     /**
    * Compiles all of the message parts into one message
    * Author: Amy DeGregorio/amdegregorio
    * 
    * @param messageParts
    * @param stringBuilder
    */
    private void getPlainTextFromMessageParts(List<MessagePart> messageParts, StringBuilder stringBuilder) {
        if (messageParts != null) {
         
            for (MessagePart messagePart : messageParts) {
                if (messagePart.getMimeType().equals("text/plain")) {
                    stringBuilder.append(messagePart.getBody().getData());
                } 

                if (messagePart.getParts() != null) {
                    getPlainTextFromMessageParts(messagePart.getParts(), stringBuilder);
                }
            }
        }
    }
    
    /**
     * Creates an authorized Credential object.
     * @param HTTP_TRANSPORT The network HTTP Transport.
     * @return An authorized Credential object.
     * @throws IOException If the credentials.json file cannot be found.
     */
    private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
        // Load client secrets.
        InputStream in = new FileInputStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }
}
