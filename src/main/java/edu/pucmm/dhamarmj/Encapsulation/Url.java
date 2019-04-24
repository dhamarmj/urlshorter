package edu.pucmm.dhamarmj.Encapsulation;

import javax.jws.soap.SOAPBinding;
import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
public class Url implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String url;
    private String redirect;
    @Column(nullable = true)
    private String browser;
    private String so;
    private String ip;

    @ManyToOne
    private User user;


    public Url() {
    }

    public Url(String url, String redirect,  String browser, String so, String ip) {
        this.url = url;
        this.redirect = redirect;
        this.browser = browser;
        this.so = so;
        this.ip = ip;
        this.user = null;
    }

    public Url(String url, String ip, String redirect, User user) {
        this.url = url;
        this.redirect = redirect;
        this.browser = "";
        this.so = "";
        this.ip = ip;
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getRedirect() {
        return redirect;
    }

    public void setRedirect(String redirect) {
        this.redirect = redirect;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
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

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}
