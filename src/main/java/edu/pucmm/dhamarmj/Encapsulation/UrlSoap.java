package edu.pucmm.dhamarmj.Encapsulation;

public class UrlSoap {
    private String url;
    private String redirect;

    public UrlSoap() {
    }

    public UrlSoap(String url, String redirect) {
        this.url = url;
        this.redirect = redirect;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getRedirect() {
        return redirect;
    }

    public void setRedirect(String redirect) {
        this.redirect = redirect;
    }
}