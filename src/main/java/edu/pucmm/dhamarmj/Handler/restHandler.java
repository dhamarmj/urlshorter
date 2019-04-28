package edu.pucmm.dhamarmj.Handler;

import com.google.gson.Gson;
import edu.pucmm.dhamarmj.Encapsulation.*;
import edu.pucmm.dhamarmj.Encapsulation.User;
import edu.pucmm.dhamarmj.Main;
import edu.pucmm.dhamarmj.Services.UrlServices;
import edu.pucmm.dhamarmj.Services.UserServices;
import edu.pucmm.dhamarmj.Transformation.JsonTransformer;
import spark.ModelAndView;
import spark.Request;
import spark.Session;
import spark.template.freemarker.FreeMarkerEngine;

import java.util.List;
import java.util.Map;

import static spark.Spark.*;


public class restHandler {
    public restHandler() {
        currentuser = new User("restRequest", false);
        UserServices.getInstancia().insert(currentuser);
    }

    Gson gson = new Gson();
    User currentuser;
    public final static String ACCEPT_TYPE_JSON = "application/json";
    public final static String ACCEPT_TYPE_XML = "application/xml";

    public void startup() {
        path("/api", () -> {
            path("/url", () -> {
                get("/", (request, response) -> {
                    List<Url> urls= UrlServices.getInstancia().buscarTodos();
                    for (Url item:
                         urls) {
                        item.setVisits(null);
                        item.setUser(null);
                    }
                    return urls;
                }, JsonTransformer.json());

                get("/:id", (request, response) -> {
                    Url urls= UrlServices.getInstancia().buscar(Long.parseLong(request.params("id")));
                    urls.setUser(null);
                    urls.setVisits(null);
                    return urls;
                }, JsonTransformer.json());

                post("/", restHandler.ACCEPT_TYPE_JSON, (request, response) -> {
                    Url fu = gson.fromJson(request.body(), Url.class);
                    Url returned_val = mainHandler.generateURL(fu.getUrl());
                    if (returned_val.getUrl() == null) {
                        response.status(404);
                        return null;
                    } else {

                        UrlServices.getInstancia().insert(returned_val);
                        return returned_val;
                    }
                }, JsonTransformer.json());

                put("/", restHandler.ACCEPT_TYPE_JSON, (request, response) -> {
                    Url fu = gson.fromJson(request.body(), Url.class);
                    if (fu.getUrl() == null) {
                        response.status(404);
                        return null;
                    } else {
                        UrlServices.getInstancia().modificar(fu);
                        return fu;
                    }
                }, JsonTransformer.json());

                delete("/:id", restHandler.ACCEPT_TYPE_JSON, (request, response) -> {
                    if(request.params("id") == null)
                    {
                        response.status(404);
                        return null;
                    }
                    else
                    {
                        Url url = UrlServices.getInstancia().buscar(Long.parseLong(request.params("id")));
                        UrlServices.getInstancia().eliminar(url);
                        return url;
                    }
                }, JsonTransformer.json());
            });

            path("/user", () -> {
                get("/", (request, response) -> {
                    return UserServices.getInstancia().buscarTodos();
                }, JsonTransformer.json());

                post("/", restHandler.ACCEPT_TYPE_JSON, (request, response) -> {
                    User fu = gson.fromJson(request.body(), User.class);
                    if (fu.getUsername() == null) {
                        response.status(404);
                        return null;
                    } else {
                        UserServices.getInstancia().insert(fu);
                        return fu;
                    }
                }, JsonTransformer.json());

                put("/", restHandler.ACCEPT_TYPE_JSON, (request, response) -> {
                    User fu = gson.fromJson(request.body(), User.class);
                    if (fu.getUsername() == null) {
                        response.status(404);
                        return null;
                    } else {
                        UserServices.getInstancia().modificar(fu);
                        return fu;
                    }
                }, JsonTransformer.json());

                delete("/:id", restHandler.ACCEPT_TYPE_JSON, (request, response) -> {
                    if(request.params("id") == null)
                    {
                        response.status(404);
                        return null;
                    }
                    else
                    {
                        User url = UserServices.getInstancia().buscar(Long.parseLong(request.params("id")));
                        UrlServices.getInstancia().eliminar(url);
                        return url;
                    }
                }, JsonTransformer.json());
            });
        });
    }
}
