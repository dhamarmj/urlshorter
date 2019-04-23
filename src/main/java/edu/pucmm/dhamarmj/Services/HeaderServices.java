package edu.pucmm.dhamarmj.Services;

import edu.pucmm.dhamarmj.Encapsulation.Url;

public class HeaderServices extends DatabaseServices<Url> {
    private static HeaderServices instancia;
    int pageSize = 5;

    private HeaderServices() {
        super(Url.class);
    }

    public static HeaderServices getInstancia() {
        if (instancia == null) {
            instancia = new HeaderServices();
        }
        return instancia;
    }
}
