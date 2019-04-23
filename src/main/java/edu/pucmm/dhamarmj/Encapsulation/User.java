package edu.pucmm.dhamarmj.Encapsulation;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

@Entity
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String username;
    private String name;
    private String password;
    private boolean admin;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "user")
    private Set<Url> urls;

    public User() {
    }

    public User(String username, String name, String password, boolean admin, Set<Url> urls) {
        this.username = username;
        this.name = name;
        this.password = password;
        this.admin = admin;
        this.urls = urls;
    }

    public User(String username, String name, String password, boolean admin) {
        this.username = username;
        this.name = name;
        this.password = password;
        this.admin = admin;
        this.urls = new HashSet<>();
    }

    public Set<Url> getUrls() {
        return urls;
    }

    public void setUrls(Set<Url> urls) {
        if (this.urls == null)
            this.urls = new HashSet<>();

        this.urls = urls;
    }

    public void setUrls(Url url) {
        if (this.urls == null)
            this.urls = new HashSet<>();

        this.urls.add(url);
    }

    public long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }
}
