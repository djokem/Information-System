/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package korisnikdjordje;

/**
 *
 * @author SRA-DJORDJE
 */
import entiteti.Alarm;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.TextMessage;

/**
 *
 * @author SRA-DJORDJE
 */
public class Main {

    @Resource(lookup = "jms/__defaultConnectionFactory")
    private static javax.jms.ConnectionFactory connectionFactory;

    @Resource(lookup = "AlarmQueue")
    private static javax.jms.Queue AlarmQueue;

    @Resource(lookup = "UrQueue")
    private static javax.jms.Queue UrQueue;

    @Resource(lookup = "KorQueue")
    private static javax.jms.Queue KorQueue;

    private static void alarmInterface() {
        System.out.println("=====================A L A R M=====================\n\n");
        while (true) {
            System.out.println("\n1.Navij alarm\n"
                    + "2.Navij periodican alarm\n"
                    + "3.Navij alarm u jednom od ponudjenih trenutaka\n"
                    + "4.Konfigurisi zvono alarma\n\n"
                    + "0.Povratak na pocetak");
            System.out.print("\nVas izbor: ");
            Scanner sc = new Scanner(System.in);
            String izborStr = sc.nextLine();
            System.out.print("\n");
            int izbor = 0;
            try {
                izbor = Integer.parseInt(izborStr);
            } catch (NumberFormatException e) {
                System.out.println("Izbor mora da bude ceo broj!!!");
                continue;
            }
            TextMessage tm = context.createTextMessage();
            try {
                tm.setIntProperty("izbor", izbor);
            } catch (JMSException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
            switch (izbor) {
                case 1:
                    System.out.print("Unesite vreme alarma(HH:mm): ");
                    String vreme = sc.nextLine();
                    try {
                        tm.setStringProperty("vreme", vreme);
                    } catch (JMSException ex) {
                        Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    producer.send(AlarmQueue, tm);
                    break;
                case 2:
                    System.out.print("Unesite vreme alarma(HH:mm): ");
                    String vremeP = sc.nextLine();
                    System.out.print("\nUnesite period: ");
                    String period = sc.nextLine();
                    try {
                        tm.setStringProperty("vreme", vremeP);
                        tm.setStringProperty("perioda", period);
                    } catch (JMSException ex) {
                        Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    producer.send(AlarmQueue, tm);
                    break;
                case 3:
                    System.out.println("Preporuceni alarmi:\n"
                            + "\t1)07:00\n"
                            + "\t2)09:00\n"
                            + "\t3)10:00\n");
                    System.out.print("\nVas izbor: ");
                    String izborPreporucen = sc.nextLine();
                    System.out.print("\n");
                    izbor = 0;
                    try {
                        izbor = Integer.parseInt(izborPreporucen);
                    } catch (NumberFormatException e) {
                        System.out.println("Izbor mora da bude ceo broj!!!");
                        continue;
                    }
                    switch (izbor) {
                        case 1:
                            try {
                                tm.setStringProperty("vreme", "07:00");
                            } catch (JMSException ex) {
                                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            break;
                        case 2:
                            try {
                                tm.setStringProperty("vreme", "09:00");
                            } catch (JMSException ex) {
                                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            break;
                        case 3:
                            try {
                                tm.setStringProperty("vreme", "10:00");
                            } catch (JMSException ex) {
                                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            break;
                        default:
                            System.out.println("Ne postoji preporucen alarm pod rednim brojem " + izborPreporucen + "!!");
                            continue;
                    }
                    producer.send(AlarmQueue, tm);
                    break;
                case 4:
                    producer.send(AlarmQueue, tm);
                    Message m = consumer.receive();
                    if (m instanceof ObjectMessage) {
                        ObjectMessage om = (ObjectMessage) m;
                        List<Alarm> alrms = null;
                        try {
                            alrms = (List<Alarm>) om.getObject();
                        } catch (JMSException ex) {
                            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        if (alrms != null) {
                            System.out.println("Lista navijenih alarma: \n");
                            for (int i = 0; i < alrms.size(); i++) {
                                System.out.println("\t" + alrms.get(i).getIDAlarm() + ": " + alrms.get(i).getVreme() + " " + alrms.get(i).getIDPesma().getNaziv());
                            }

                        }
                        System.out.print("\n\tVas izbor: ");
                        String izborAlarma = sc.nextLine();
                        System.out.print("\n");
                        izbor = 0;
                        try {
                            izbor = Integer.parseInt(izborAlarma);
                        } catch (NumberFormatException e) {
                            System.out.println("Izbor mora da bude ceo broj!!!");
                            continue;
                        }
                        if (izbor <= 0 || izbor > alrms.size()) {
                            System.out.println("IZBOR NE POSTOJI!!!");
                            continue;
                        }
                        Alarm a = alrms.get(izbor - 1);
                        if (a != null) {
                            System.out.println("Alarm " + a.getIDAlarm() + " - " + a.getVreme());
                            System.out.print("Unesite naziv melodije: ");
                            String melodija = sc.nextLine();

                            ObjectMessage om1 = context.createObjectMessage();
                            try {
                                om1.setObject(a);
                                om1.setStringProperty("melodija", melodija);
                                producer.send(AlarmQueue, om1);
                            } catch (JMSException ex) {
                                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    }
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Ne postoji uredjaj pod rednim brojem " + izborStr + "!!!");
                    continue;
            }
        }
    }

    private static void uredjajInterface() {
        System.out.println("=====================Uredjaj za reprodukciju zvuka=====================\n\n");
        while (true) {
            System.out.println("1.Pusti pesmu\n"
                    + "2.Istorija\n");
            System.out.print("Vas izbor: ");
            Scanner s = new Scanner(System.in);
            String izborStr = s.nextLine();
            System.out.print("\n");
            int izbor = 0;
            try {
                izbor = Integer.parseInt(izborStr);
            } catch (NumberFormatException e) {
                System.out.println("Izbor mora da bude ceo broj!!!");
                continue;
            }
            TextMessage tm = context.createTextMessage();
            switch (izbor) {
                case 1:
                    System.out.print("Unesite naziv pesme: ");
                    String naziv = s.nextLine();
                    try {
                        tm.setIntProperty("izborUr", 1);
                        tm.setStringProperty("pesma", naziv);
                        System.out.print("\n");
                    } catch (JMSException ex) {
                        Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    producer.send(UrQueue, tm);
                    break;
                case 2:
                    try {
                        tm.setIntProperty("izborUr", 2);
                    } catch (JMSException ex) {
                        Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    producer.send(UrQueue, tm);
                    Message m = consumer.receive();
                    if (m instanceof TextMessage) {
                        TextMessage txtm = (TextMessage) m;
                        try {
                            String istorija = txtm.getStringProperty("istorija");
                            System.out.println("Istorija:");
                            System.out.print("\t" + istorija + "\n\n");
                        } catch (JMSException ex) {
                            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    break;
                default:
                    continue;
            }

        }
    }

    private static JMSContext context;
    private static JMSProducer producer;
    private static JMSConsumer consumer;

    public static void main(String[] args) throws ParseException {
        Scanner sc = new Scanner(System.in);
        context = connectionFactory.createContext();
        producer = context.createProducer();
        consumer = context.createConsumer(KorQueue);
        while (true) {
            System.out.println("\n1.Alarm\n"
                    + "2.Uredjaj za reprodukciju zvuka\n"
                    + "3.Planer\n\n"
                    + "0.Kraj");
            System.out.print("\nVas izbor: ");

            String izborStr = sc.nextLine();
            System.out.print("\n");
            int izbor = 0;
            try {
                izbor = Integer.parseInt(izborStr);
            } catch (NumberFormatException e) {
                System.out.println("Izbor uredjaja mora da bude ceo broj!!!");
                continue;
            }

            switch (izbor) {
                case 1:
                    alarmInterface();
                    break;
                case 2:
                    uredjajInterface();
                    break;
                case 3:
                    break;
                case 0:
                    System.exit(1);
                default:
                    System.out.println("Ne postoji uredjaj pod rednim brojem " + izborStr + "!!!");
                    continue;
            }

        }
    }

}
