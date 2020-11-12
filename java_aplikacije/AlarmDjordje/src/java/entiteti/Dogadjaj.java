/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entiteti;

import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author SRA-DJORDJE
 */
@Entity
@Table(name = "dogadjaj")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Dogadjaj.findAll", query = "SELECT d FROM Dogadjaj d")
    , @NamedQuery(name = "Dogadjaj.findByIDDogadjaj", query = "SELECT d FROM Dogadjaj d WHERE d.iDDogadjaj = :iDDogadjaj")
    , @NamedQuery(name = "Dogadjaj.findByIme", query = "SELECT d FROM Dogadjaj d WHERE d.ime = :ime")
    , @NamedQuery(name = "Dogadjaj.findByDestinacija", query = "SELECT d FROM Dogadjaj d WHERE d.destinacija = :destinacija")
    , @NamedQuery(name = "Dogadjaj.findByDatum", query = "SELECT d FROM Dogadjaj d WHERE d.datum = :datum")
    , @NamedQuery(name = "Dogadjaj.findByVreme", query = "SELECT d FROM Dogadjaj d WHERE d.vreme = :vreme")
    , @NamedQuery(name = "Dogadjaj.findByImaAlarm", query = "SELECT d FROM Dogadjaj d WHERE d.imaAlarm = :imaAlarm")})
public class Dogadjaj implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "Ime")
    private String ime;
    @Size(max = 45)
    @Column(name = "Destinacija")
    private String destinacija;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 6)
    @Column(name = "Vreme")
    private String vreme;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ImaAlarm")
    private boolean imaAlarm;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "IDDogadjaj")
    private Integer iDDogadjaj;
    @Column(name = "Datum")
    @Temporal(TemporalType.DATE)
    private Date datum;
    @JoinColumn(name = "IDAlarm", referencedColumnName = "IDAlarm")
    @ManyToOne
    private Alarm iDAlarm;

    public Dogadjaj() {
    }

    public Dogadjaj(Integer iDDogadjaj) {
        this.iDDogadjaj = iDDogadjaj;
    }

    public Dogadjaj(Integer iDDogadjaj, String ime, String vreme, boolean imaAlarm) {
        this.iDDogadjaj = iDDogadjaj;
        this.ime = ime;
        this.vreme = vreme;
        this.imaAlarm = imaAlarm;
    }

    public Integer getIDDogadjaj() {
        return iDDogadjaj;
    }

    public void setIDDogadjaj(Integer iDDogadjaj) {
        this.iDDogadjaj = iDDogadjaj;
    }


    public Date getDatum() {
        return datum;
    }

    public void setDatum(Date datum) {
        this.datum = datum;
    }


    public Alarm getIDAlarm() {
        return iDAlarm;
    }

    public void setIDAlarm(Alarm iDAlarm) {
        this.iDAlarm = iDAlarm;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (iDDogadjaj != null ? iDDogadjaj.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Dogadjaj)) {
            return false;
        }
        Dogadjaj other = (Dogadjaj) object;
        if ((this.iDDogadjaj == null && other.iDDogadjaj != null) || (this.iDDogadjaj != null && !this.iDDogadjaj.equals(other.iDDogadjaj))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entiteti.Dogadjaj[ iDDogadjaj=" + iDDogadjaj + " ]";
    }

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public String getDestinacija() {
        return destinacija;
    }

    public void setDestinacija(String destinacija) {
        this.destinacija = destinacija;
    }

    public String getVreme() {
        return vreme;
    }

    public void setVreme(String vreme) {
        this.vreme = vreme;
    }

    public boolean getImaAlarm() {
        return imaAlarm;
    }

    public void setImaAlarm(boolean imaAlarm) {
        this.imaAlarm = imaAlarm;
    }
    
}
