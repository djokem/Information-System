/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entiteti;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
@Table(name = "pesma")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Pesma.findAll", query = "SELECT p FROM Pesma p")
    , @NamedQuery(name = "Pesma.findByIDPesma", query = "SELECT p FROM Pesma p WHERE p.iDPesma = :iDPesma")
    , @NamedQuery(name = "Pesma.findByNaziv", query = "SELECT p FROM Pesma p WHERE p.naziv = :naziv")})
public class Pesma implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "Naziv")
    private String naziv;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "IDPesma")
    private Integer iDPesma;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "iDPesma")
    private List<Alarm> alarmList;

    public Pesma() {
    }

    public Pesma(Integer iDPesma) {
        this.iDPesma = iDPesma;
    }

    public Pesma(Integer iDPesma, String naziv) {
        this.iDPesma = iDPesma;
        this.naziv = naziv;
    }

    public Integer getIDPesma() {
        return iDPesma;
    }

    public void setIDPesma(Integer iDPesma) {
        this.iDPesma = iDPesma;
    }


    @XmlTransient
    public List<Alarm> getAlarmList() {
        return alarmList;
    }

    public void setAlarmList(List<Alarm> alarmList) {
        this.alarmList = alarmList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (iDPesma != null ? iDPesma.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Pesma)) {
            return false;
        }
        Pesma other = (Pesma) object;
        if ((this.iDPesma == null && other.iDPesma != null) || (this.iDPesma != null && !this.iDPesma.equals(other.iDPesma))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entiteti.Pesma[ iDPesma=" + iDPesma + " ]";
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }
    
}
