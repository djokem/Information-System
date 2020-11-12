/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package urrepzvuk;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.customsearch.Customsearch;
import com.google.api.services.customsearch.CustomsearchRequestInitializer;
import com.google.api.services.customsearch.model.Result;
import com.google.api.services.customsearch.model.Search;
import java.net.URI;
import java.awt.Desktop;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author SRA-DJORDJE
 */
public class GoogleAPI {
     public void pustiPesmu(String imePesme){
        try {
            String ytStr = "Youtube ";
            String searchStr = ytStr.concat(imePesme);
            String searchQuery = searchStr; //The query to search
            String cx = "002845322276752338984:vxqzfa86nqc"; //Your search engine //!!
            
            //Instance Customsearch
            Customsearch cs = new Customsearch.Builder(GoogleNetHttpTransport.newTrustedTransport(), JacksonFactory.getDefaultInstance(), null)
                    .setApplicationName("MyApplication")
                    .setGoogleClientRequestInitializer(new CustomsearchRequestInitializer("AIzaSyCbYDb3yZf_NIlgQtCOJGCbZrgPk9T4NR4"))
                    .build();//!!promenili smo app name i key
            
            //Set search parameter
            Customsearch.Cse.List list = cs.cse().list(searchQuery).setCx(cx);
            
            //Execute search
            Search result = list.execute();
            URI uri = null;
            if (result.getItems() != null) {
                for (Result ri : result.getItems()) {
                    //Get title, link, body etc. from search
                    if (uri == null) {
                        uri = URI.create(ri.getLink());
                    }
                    System.out.println(ri.getTitle() + ", " + ri.getLink());
                }
            }
            
            Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
            if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
                try {
                    desktop.browse(uri);
//                return true;
                } catch (IOException e) {
                    System.out.println("Problem sa desktop.browse(uri)");
                }
            } else {
                System.out.println("Problem");
            }
        } catch (GeneralSecurityException | IOException ex) {
            Logger.getLogger(GoogleAPI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
