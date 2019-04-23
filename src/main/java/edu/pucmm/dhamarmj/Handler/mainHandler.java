package edu.pucmm.dhamarmj.Handler;

import com.google.gson.Gson;
import com.sun.org.apache.xpath.internal.SourceTree;
import com.sun.org.apache.xpath.internal.operations.Bool;
import edu.pucmm.dhamarmj.Encapsulation.Url;
import edu.pucmm.dhamarmj.Encapsulation.User;
import edu.pucmm.dhamarmj.Services.UrlServices;
import edu.pucmm.dhamarmj.Services.UserServices;
import edu.pucmm.dhamarmj.Transformation.JsonTransformer;
import freemarker.template.Configuration;
import spark.ModelAndView;
import spark.Request;
import spark.Session;
import spark.template.freemarker.FreeMarkerEngine;

import javax.jws.soap.SOAPBinding;
import java.net.InetAddress;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static spark.Spark.*;

public class mainHandler {
    public mainHandler() {
    }

    User currentUser;
    Gson gson = new Gson();
    String ip_val, hex_val, new_url, base_url = "dhamarmj.";
    Url url_value;

    public void startup() {
        staticFiles.location("/publico");


        Configuration configuration = new Configuration(Configuration.VERSION_2_3_23);
        configuration.setClassForTemplateLoading(mainHandler.class, "/templates");
        FreeMarkerEngine freeMarkerEngine = new FreeMarkerEngine(configuration);

        get("/", (request, response) -> {
            StartUser();
            currentUser = getSessionUsuario(request);
            Map<String, Object> attributes = validateUser();
            return new ModelAndView(attributes, "home.ftl");
        }, freeMarkerEngine);

        get("/LogIn/", (request, response) -> {
            Map<String, Object> attributes = validateUser();
            return new ModelAndView(attributes, "signIn.ftl");
        }, freeMarkerEngine);

        post("/logInUser/", (request, response) -> {
            User user = UserServices.getInstancia().getUser(request.queryParams("username"), encryptPassword(request.queryParams("password")));
            if (user != null) {
                CreateSession(request, user);
                response.redirect("/");
            } else {
                response.redirect("/LogIn/");
            }
            return null;
        }, freeMarkerEngine);

        get("/Register/", (request, response) -> {
            Map<String, Object> attributes = validateUser();
            return new ModelAndView(attributes, "register.ftl");
        }, freeMarkerEngine);

        post("/registerUser/", (request, response) -> {
            User u = new User(request.queryParams("username"),
                    request.queryParams("name"),
                    encryptPassword(request.queryParams("password")),
                    Boolean.parseBoolean(request.queryParams("admin")));
            UserServices.getInstancia().insert(u);
            CreateSession(request, u);
            response.redirect("/");
            return null;
        }, freeMarkerEngine);

        get("/rest/urlUser", (request, response) -> {
            currentUser = getSessionUsuario(request);
            response.header("Content-Type", "application/json");
            return currentUser.getUrls();
        }, JsonTransformer.json());

        post("/generateUrl", (request, response) -> {
            currentUser = getSessionUsuario(request);
            Url returned_val = generateURL(request.queryParams("url"));
            if(currentUser != null)
                returned_val.setUser(currentUser);
            UrlServices.getInstancia().insert(returned_val);
            return null;
        }, freeMarkerEngine);
    }

    private Url generateURL(String a) throws Exception {
        InetAddress ip = InetAddress.getByName(new URL(a).getHost());
        ip_val = ip.getLocalHost().getHostAddress();
        ip_val = ip_val.replace(".", "");
        hex_val = longToHex(ip_val);
        new_url = "http://" + base_url + hex_val;
        return new Url(a, ip.getLocalHost().getHostAddress(), new_url);
    }

    private String longToHex(String value) {
        return Long.toHexString(Long.parseLong(value));
    }

    private long HextoLong(String value) {
        return Long.parseLong(value, 16);
    }

    private Map<String, Object> validateUser() {
        Map<String, Object> attributes = new HashMap<>();
        if (currentUser == null) {
            attributes.put("usuario", "other");
            attributes.put("userSigned", false);
            return attributes;
        }

        if (currentUser.isAdmin()) {
            attributes.put("usuario", "admin");
        } else {
            attributes.put("usuario", "visitor");
        }
        attributes.put("userSigned", true);
        return attributes;
    }

    public static String getHash(String txt, String hashType) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest
                    .getInstance(hashType);
            byte[] array = md.digest(txt.getBytes());
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100)
                        .substring(1, 3));
            }
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public static String encryptPassword(String txt) {
        return getHash(txt, "MD5");
    }

    private void StartUser() {
        if (UserServices.getInstancia().buscarTodos().size() == 0) {
            UserServices.getInstancia().insert(new User("admin",
                    "admin",
                    encryptPassword("admin"),
                    true));
        }
    }

    private void CreateSession(Request request, User user) {
        Session session = request.session(true);
        session.attribute("usuario", user);
    }

    private User getSessionUsuario(Request request) {
        return request.session().attribute("usuario");
    }

}
