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
    private String ip;

    @ManyToOne
    private User user;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "url")
    private Set<Visit> visits;

    public Url() {
    }

    public Url(String url, String redirect,  String browser, String so, String ip) {
        this.url = url;
        this.redirect = redirect;
        this.ip = ip;
        this.user = null;
    }

    public Url(String url, String ip, String redirect, User user) {
        this.url = url;
        this.redirect = redirect;
        this.ip = ip;
        this.user = user;
    }

    public Set<Visit> getVisits() {
        return visits;
    }

    public void setVisits(Set<Visit> visits) {
        this.visits = visits;
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

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}
