package edu.pucmm.dhamarmj.Encapsulation;

import javax.persistence.*;
import java.io.Serializable;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
public class Visit implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String browser;
    private String so;
    @Transient
    private Date fecha;
    private String fechaS;
    private String ip;
    @Transient
    private SimpleDateFormat dt;
    @ManyToOne
    private Url url;

    public Visit() {
    }

    public Visit(String browser, String so, Date fecha, String ip, Url url) {
        dt = new SimpleDateFormat("yyyy-MM-dd");
        this.browser = browser;
        this.so = so;
        this.fecha = fecha;
        this.fechaS = dt.format(fecha);
        this.url = url;
        this.ip = ip;
    }

    public String getFechaS() {
        return fechaS;
    }

    public void setFechaS(String fechaS) {
        this.fechaS = fechaS;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Url getUrl() {
        return url;
    }

    public void setUrl(Url url) {
        this.url = url;
    }

    public String getBrowser() {
        return browser;
    }

    public void setBrowser(String browser) {
        this.browser = browser;
    }

    public String getSo() {
        return so;
    }

    public void setSo(String so) {
        this.so = so;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
}
