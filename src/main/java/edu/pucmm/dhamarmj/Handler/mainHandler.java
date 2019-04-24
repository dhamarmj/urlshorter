package edu.pucmm.dhamarmj.Handler;

import com.google.gson.Gson;
import com.sun.org.apache.xpath.internal.SourceTree;
import com.sun.org.apache.xpath.internal.operations.Bool;
import edu.pucmm.dhamarmj.Encapsulation.Url;
import edu.pucmm.dhamarmj.Encapsulation.User;
import edu.pucmm.dhamarmj.Services.Encryption;
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
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static spark.Spark.*;

public class mainHandler {
    public mainHandler() {
    }

    User currentUser = new User();
    Gson gson = new Gson();
    String ip_val, hex_val, new_url, base_url = "dhamarmj/";
    Url url_value;

    public void startup() {
        staticFiles.location("/publico");


        Configuration configuration = new Configuration(Configuration.VERSION_2_3_23);
        configuration.setClassForTemplateLoading(mainHandler.class, "/templates");
        FreeMarkerEngine freeMarkerEngine = new FreeMarkerEngine(configuration);

        get("/", (request, response) -> {
            StartUser();
            String user = request.cookie("LoginU");
            if (user != null) {
                String passw = Encryption.Decrypt(request.cookie("LoginP"));
                String usern = Encryption.Decrypt(user);
                currentUser = UserServices.getInstancia().getUser(usern, passw);
                CreateSession(request, currentUser);
            }
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
                boolean remember = Boolean.parseBoolean(request.queryParams("remember"));
                if (remember) {
                    response.cookie("/", "LoginU", Encryption.Encrypt(user.getUsername()), 604800, false);
                    response.cookie("/", "LoginP", Encryption.Encrypt(user.getPassword()), 604800, false);
                }
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
            if(currentUser != null){
                Set<Url> vals =  UserServices.getInstancia().buscar(currentUser.getId()).getUrls();
                response.header("Content-Type", "application/json");
                for (Url aux:
                        vals) {
                    aux.setUser(null);
                }
                return vals;
            }
            response.status(404);
            return null;
        }, JsonTransformer.json());

        post("/generateUrl", (request, response) -> {
            currentUser = getSessionUsuario(request);
            Url fu = gson.fromJson(request.body(), Url.class);
            Url returned_val = generateURL(fu.getUrl());
            UrlServices.getInstancia().insert(returned_val);
            return null;
        }, JsonTransformer.json());

//        before("dhamarmj/*", (request, response) -> {
//            System.out.println("IIIINNNNNN");
//            System.out.println(request.uri());
//            //User usuario = request.session().attribute("usuario");
////            if (usuario == null) {
////                response.redirect("/");
////            } else {
////                if (!usuario.isAdmin() && !usuario.isAuthor()) {
////                    response.redirect("/");
////                }
////            }
//        });

        before((request, response) -> {
            System.out.println("IIIINNNNNN");
            System.out.println(request.uri());
           // ... check if authenticated
//            if (!authenticated) {
//                halt(401, "You are not welcome here");
//            }
        });

        get("/LogOut/", (request, response) -> {
            request.session().removeAttribute("usuario");
            request.session().invalidate();
            currentUser = null;
            response.removeCookie("/", "LoginU");
            response.removeCookie("/", "LoginP");
            response.redirect("/");
            return null;
        }, freeMarkerEngine);


    }

    private Url generateURL(String a) throws Exception {
        URL direction = new URL(a);
        direction.toURI();
        InetAddress ip = InetAddress.getByName(direction.getHost());
        ip_val = ip.getHostAddress();
        ip_val = ip_val.replace(".", "");
        ip_val =  (Long.parseLong(ip_val) +  LocalDateTime.now().getMinute() + LocalDateTime.now().getSecond()) + "";
        hex_val = longToHex(ip_val);
        new_url = "http://" + base_url + hex_val;
        return new Url(a, ip.getHostAddress(), new_url, currentUser);
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
