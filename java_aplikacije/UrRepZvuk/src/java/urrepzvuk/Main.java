/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package urrepzvuk;

import entiteti.Pesma;
import java.net.URI;
import java.awt.Desktop;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.jms.ConnectionFactory;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.Message;
import javax.jms.Queue;
import javax.jms.TextMessage;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

/**
 *
 * @author SRA-DJORDJE
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    @Resource(lookup = "jms/__defaultConnectionFactory")
    private static ConnectionFactory connectionFactory;
    @Resource(lookup = "AlarmQueue")
    private static Queue AlarmQueue;
    @Resource(lookup = "KorQueue")
    private static Queue KorQueue;
    @Resource(lookup = "UrQueue")
    private static javax.jms.Queue UrQueue;

    private static void dodajPesmu(EntityManager em, String imePesme) {
        Pesma p = new Pesma();
        p.setNaziv(imePesme);
        em.getTransaction().begin();
        em.persist(p);
        em.getTransaction().commit();
        em.clear();
    }

    public static String vratiIstoriju(EntityManager em) {
        Query query = em.createNamedQuery("Pesma.findAll");
        List<Pesma> pesme = query.getResultList();
        String istorija = "";
        String pomocni;
        for (Pesma p : pesme) {
            pomocni = istorija.concat(p.getNaziv() + ":");
            istorija = pomocni;
        }
        return istorija;
    }

    public static void main(String[] args) {
        // TODO code application logic here

        GoogleAPI g = new GoogleAPI();

        JMSContext context = connectionFactory.createContext();
        JMSConsumer consumer = context.createConsumer(UrQueue);
        JMSProducer producer = context.createProducer();

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("UrRepZvukPU");
        EntityManager em = emf.createEntityManager();

        while (true) {
            Message message = consumer.receive();
            if (message instanceof TextMessage) {
                try {
                    TextMessage textMessage = (TextMessage) message;
                    int izbor = textMessage.getIntProperty("izborUr");

                    switch (izbor) {
                        case 1:
                            String pesma = textMessage.getStringProperty("pesma");
                            g.pustiPesmu(pesma);
                            dodajPesmu(em, pesma);
                            break;
                        case 2:
                            String istorija = vratiIstoriju(em);
                            TextMessage textMessage1 = context.createTextMessage();
                            textMessage1.setStringProperty("istorija", istorija);
                            producer.send(KorQueue, textMessage1);
                            break;
                    }
                } catch (JMSException ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                System.out.println("Greska u slanju od uredjaja ka korisniku ");
            }
        }

    }

}
