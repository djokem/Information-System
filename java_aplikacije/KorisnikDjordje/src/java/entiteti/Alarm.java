/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entiteti;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author SRA-DJORDJE
 */
@Entity
@Table(name = "alarm")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Alarm.findAll", query = "SELECT a FROM Alarm a")
    , @NamedQuery(name = "Alarm.findByIDAlarm", query = "SELECT a FROM Alarm a WHERE a.iDAlarm = :iDAlarm")
    , @NamedQuery(name = "Alarm.findByVreme", query = "SELECT a FROM Alarm a WHERE a.vreme = :vreme")
    , @NamedQuery(name = "Alarm.findByPeriodican", query = "SELECT a FROM Alarm a WHERE a.periodican = :periodican")
    , @NamedQuery(name = "Alarm.findByPerioda", query = "SELECT a FROM Alarm a WHERE a.perioda = :perioda")})
public class Alarm implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 6)
    @Column(name = "Vreme")
    private String vreme;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Periodican")
    private boolean periodican;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "IDAlarm")
    private Integer iDAlarm;
    @Column(name = "Perioda")
    private Integer perioda;
    @OneToMany(mappedBy = "iDAlarm")
    private List<Dogadjaj> dogadjajList;
    @JoinColumn(name = "IDPesma", referencedColumnName = "IDPesma")
    @ManyToOne(optional = false)
    private Pesma iDPesma;

    public Alarm() {
    }

    public Alarm(Integer iDAlarm) {
        this.iDAlarm = iDAlarm;
    }

    public Alarm(Integer iDAlarm, String vreme, boolean periodican) {
        this.iDAlarm = iDAlarm;
        this.vreme = vreme;
        this.periodican = periodican;
    }

    public Integer getIDAlarm() {
        return iDAlarm;
    }

    public void setIDAlarm(Integer iDAlarm) {
        this.iDAlarm = iDAlarm;
    }


    public Integer getPerioda() {
        return perioda;
    }

    public void setPerioda(Integer perioda) {
        this.perioda = perioda;
    }

    @XmlTransient
    public List<Dogadjaj> getDogadjajList() {
        return dogadjajList;
    }

    public void setDogadjajList(List<Dogadjaj> dogadjajList) {
        this.dogadjajList = dogadjajList;
    }

    public Pesma getIDPesma() {
        return iDPesma;
    }

    public void setIDPesma(Pesma iDPesma) {
        this.iDPesma = iDPesma;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (iDAlarm != null ? iDAlarm.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Alarm)) {
            return false;
        }
        Alarm other = (Alarm) object;
        if ((this.iDAlarm == null && other.iDAlarm != null) || (this.iDAlarm != null && !this.iDAlarm.equals(other.iDAlarm))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entiteti.Alarm[ iDAlarm=" + iDAlarm + " ]";
    }

    public String getVreme() {
        return vreme;
    }

    public void setVreme(String vreme) {
        this.vreme = vreme;
    }

    public boolean getPeriodican() {
        return periodican;
    }

    public void setPeriodican(boolean periodican) {
        this.periodican = periodican;
    }
    
}
