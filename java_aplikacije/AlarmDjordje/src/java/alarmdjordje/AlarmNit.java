/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package alarmdjordje;

import entiteti.Alarm;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.JMSException;
import javax.jms.TextMessage;

/**
 *
 * @author SRA-DJORDJE
 */
public class AlarmNit extends Thread {

    private String vreme;
    private Alarm alarm;

    private Date IzracunajVremeDatum() {
        String[] niz = vreme.split(":");
        int hh = Integer.parseInt(niz[0]);
        int mm = Integer.parseInt(niz[1]);
        Date d = new Date();
        d.setHours(hh);
        d.setMinutes(mm);
        return d;
    }

    public AlarmNit(String vr, Alarm al) {
        this.vreme = vr;
        this.alarm = al;
    }

    @Override
    public void run() {
        System.out.println("Alarm vreme: " + vreme);
        Date now = new Date();
        System.out.println("Trenutno vreme " + now.getHours() + ":" + now.getMinutes());
        Date alrmTime = IzracunajVremeDatum();
        System.out.println("Vreme za alarm " + alrmTime.getHours() + ":" + alrmTime.getMinutes());
        long t =  alrmTime.getTime() - now.getTime();
        Date dd = new Date(t);
        System.out.println("Razlika "+dd.getHours() + ":" + dd.getMinutes());
        try {
            AlarmNit.sleep(t);
            System.out.println("Probudjena nit");
            TextMessage tm = Main.context.createTextMessage();
            try {
                tm.setIntProperty("izborUr", 1);
                tm.setStringProperty("pesma", alarm.getIDPesma().getNaziv());
            } catch (JMSException ex) {
                Logger.getLogger(AlarmNit.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.print("\n");
            Main.producer.send(Main.UrQueue, tm);
            if (alarm.getPeriodican()) {
                long perioda = alarm.getPerioda();
                while (true) {
                    Date p = new Date();
                    p.setTime(perioda);
                    AlarmNit.sleep(p.getTime());
                    try {
                        tm.setIntProperty("izborUr", 1);
                        tm.setStringProperty("pesma", alarm.getIDPesma().getNaziv());
                    } catch (JMSException ex) {
                        Logger.getLogger(AlarmNit.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    System.out.print("\n");
                    Main.producer.send(Main.UrQueue, tm);
                }
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(AlarmNit.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalArgumentException e) {
            System.out.println("POGRESNO VREME!");
        }

    }
}
