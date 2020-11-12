/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package alarmdjordje;

import entiteti.Alarm;
import entiteti.Pesma;
import java.io.Serializable;
import java.util.ArrayList;
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
import javax.jms.ObjectMessage;
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
    public static ConnectionFactory connectionFactory;

    @Resource(lookup = "AlarmQueue")
    public static javax.jms.Queue AlarmQueue;

    @Resource(lookup = "KorQueue")
    public static javax.jms.Queue KorQueue;
    
    @Resource(lookup = "UrQueue")
    public static javax.jms.Queue UrQueue;

    public static JMSContext context;
    public static JMSProducer producer;
    public static JMSConsumer consumer;

    private static EntityManager em;
    private static EntityManagerFactory emf;

    private static Pesma getDefaultMelody() {
        Query q = em.createNamedQuery("Pesma.findByIDPesma");
        q.setParameter("iDPesma", 1);
        List<Pesma> res = q.getResultList();
        Pesma p = res.get(0);
        return p;
    }

    public static long IzracunajVreme(String vreme) {
        String[] niz = vreme.split(":");
        int hh = Integer.parseInt(niz[0]) * 3600000;
        int mm = Integer.parseInt(niz[1]) * 60000;
        long ukupnoVreme = hh + mm;
        return ukupnoVreme;

    }

    public static void main(String[] args) {

        context = connectionFactory.createContext();
        producer = context.createProducer();
        consumer = context.createConsumer(AlarmQueue);

        emf = Persistence.createEntityManagerFactory("AlarmDjordjePU");
        em = emf.createEntityManager();

        while (true) {

            Message m = consumer.receive();

            if (m instanceof TextMessage) {
                TextMessage tm = (TextMessage) m;
                try {
                    int izbor = tm.getIntProperty("izbor");
                    // 1. Navij alarm , 2.Navij periodican alarm, 3.Preporuceni alarm
                    switch (izbor) {
                        case 1:
                            Alarm a = new Alarm();
                            a.setVreme(tm.getStringProperty("vreme"));
                            a.setIDPesma(getDefaultMelody());
                            a.setPeriodican(false);
                            em.getTransaction().begin();
                            em.persist(a);
                            em.getTransaction().commit();
                            em.clear();
                            AlarmNit an = new AlarmNit(a.getVreme(), a);
                            an.start();
                            break;
                        case 2:
                            Alarm ap = new Alarm();
                            ap.setVreme(tm.getStringProperty("vreme"));
                            ap.setIDPesma(getDefaultMelody());
                            ap.setPeriodican(true);
                            long per = IzracunajVreme(tm.getStringProperty("perioda"));
                            ap.setPerioda((int) per);
                            em.getTransaction().begin();
                            em.persist(ap);
                            em.getTransaction().commit();
                            em.clear();
                            AlarmNit an1 = new AlarmNit(ap.getVreme(), ap);
                            an1.start();
                            break;
                        case 3:
                            Alarm apr = new Alarm();
                            apr.setVreme(tm.getStringProperty("vreme"));
                            apr.setIDPesma(getDefaultMelody());
                            apr.setPeriodican(false);
                            em.getTransaction().begin();
                            em.persist(apr);
                            em.getTransaction().commit();
                            em.clear();
                            AlarmNit an2 = new AlarmNit(apr.getVreme(), apr);
                            an2.start();
                            break;
                        case 4:
                            Query q = em.createNamedQuery("Alarm.findAll");
                            List<Alarm> alrms = q.getResultList();
                            ObjectMessage om = context.createObjectMessage();
                            om.setStringProperty("porukaOd", "alarm");
                            om.setObject((Serializable) alrms);
                            producer.send(KorQueue, om);
                            Message am = consumer.receive();
                            if (am instanceof ObjectMessage) {
                                ObjectMessage aom = (ObjectMessage) am;
                                Alarm ma = (Alarm) aom.getObject();
                                String melodija = aom.getStringProperty("melodija");
                                Pesma ps = new Pesma();
                                ps.setNaziv(melodija);

                                em.getTransaction().begin();
                                em.persist(ps);
                                em.getTransaction().commit();
                                em.clear();

                                Alarm af = em.find(Alarm.class, ma.getIDAlarm());
                                if (af != null) {
                                    em.getTransaction().begin();
                                    af.setIDPesma(ps);
                                    em.getTransaction().commit();
                                    em.clear();
                                }
                            }
                            break;
                    }
                } catch (JMSException ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

    }

}
