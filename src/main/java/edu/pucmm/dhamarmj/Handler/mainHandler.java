package edu.pucmm.dhamarmj.Handler;

import com.google.gson.Gson;
import com.sun.xml.internal.ws.api.message.Header;
import edu.pucmm.dhamarmj.Encapsulation.Url;
import edu.pucmm.dhamarmj.Encapsulation.User;
import edu.pucmm.dhamarmj.Encapsulation.Visit;
import edu.pucmm.dhamarmj.Services.Encryption;
import edu.pucmm.dhamarmj.Services.UrlServices;
import edu.pucmm.dhamarmj.Services.UserServices;
import edu.pucmm.dhamarmj.Services.VisitServices;
import edu.pucmm.dhamarmj.Transformation.JsonTransformer;
import freemarker.template.Configuration;
import spark.ModelAndView;
import spark.Request;
import spark.Session;
import spark.template.freemarker.FreeMarkerEngine;

import java.net.InetAddress;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.*;

import static spark.Spark.*;

public class mainHandler {
    public mainHandler() {
    }

    User currentUser = new User();
    Gson gson = new Gson();
    String ip_val, hex_val, new_url, base_url = "http://localhost:4567/dmj/";
    Url url_value;
    User aux;
    String so, browser, auxS;

    public void startup() {
        staticFiles.location("/publico");

        Configuration configuration = new Configuration(Configuration.VERSION_2_3_23);
        configuration.setClassForTemplateLoading(mainHandler.class, "/templates");
        FreeMarkerEngine freeMarkerEngine = new FreeMarkerEngine(configuration);

        before("/dmj/*", (request, response) -> {
            String redirect = base_url + request.uri().split("/")[2];
            Url ret_val = UrlServices.getInstancia().getUrl(redirect);
            if (ret_val == null)
                response.redirect("/");
            else {
                so = getSo(request.userAgent().toLowerCase());
                browser = getBrowser(request.userAgent().toLowerCase());
                auxS = request.ip();
                Visit v = new Visit(browser, so, new Date(), auxS, ret_val);
                VisitServices.getInstancia().insert(v);
                response.redirect(ret_val.getUrl());
            }
        });

        get("/", (request, response) -> {
            StartUser();
            String user = request.cookie("LoginU");
            if (user != null) {
                String passw = Encryption.Decrypt(request.cookie("LoginP"));
                String usern = Encryption.Decrypt(user);
                currentUser = UserServices.getInstancia().getUser(usern, passw);
                CreateSession(request, currentUser);
            } else
                CreateSession(request, null);

            currentUser = getSessionUsuario(request);
            System.out.println(currentUser.getId());
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

        post("/generateUrl", (request, response) -> {
            currentUser = getSessionUsuario(request);
            System.out.println(currentUser.getId());
            Url fu = gson.fromJson(request.body(), Url.class);
            Url returned_val = generateURL(fu.getUrl());
            UrlServices.getInstancia().insert(returned_val);
            return null;
        }, JsonTransformer.json());

        get("/AllUrls/", (request, response) -> {
            Map<String, Object> attributes = validateUser();
            return new ModelAndView(attributes, "urlList.ftl");
        }, freeMarkerEngine);

        get("/StatsUrl/:id", (request, response) -> {
            Map<String, Object> attributes = validateUser();
            return new ModelAndView(attributes, "statPage.ftl");
        }, freeMarkerEngine);


        get("/LogOut/", (request, response) -> {
            request.session().removeAttribute("usuario");
            request.session().invalidate();
            currentUser = null;
            response.removeCookie("/", "LoginU");
            response.removeCookie("/", "LoginP");
            response.redirect("/");
            return null;
        }, freeMarkerEngine);

//        REST REQUESTS
        get("/rest/urlUser", (request, response) -> {
            currentUser = getSessionUsuario(request);
            if (currentUser != null) {
                Set<Url> vals = UserServices.getInstancia().buscar(currentUser.getId()).getUrls();
                //System.out.println("rest: " + vals.size());
                response.header("Content-Type", "application/json");
                for (Url aux :
                        vals) {
                    //System.out.println(aux.getUrl());
                    aux.setUser(null);
                }
                System.out.println("URLS: " + vals.size());
                return vals;
            }
            response.status(404);
            return null;
        }, JsonTransformer.json());

        get("/rest/urls", (request, response) -> {
            List<Url> vals = UrlServices.getInstancia().buscarTodos();
            response.header("Content-Type", "application/json");
            for (Url aux :
                    vals) {
                aux.setUser(null);
            }
            if (vals.size() == 0)
                response.status(404);

            return vals;
        }, JsonTransformer.json());

        get("/rest/url/:id", (request, response) -> {
            UrlServices.getInstancia().eliminar(Long.parseLong(request.params("id")));
            response.redirect("/AllUrls/");
            return null;
        });
        get("/rest/browserUrl/:id", (request, response) -> {
          //  Url url = UrlServices.getInstancia().buscar();
            List<Visit> visits = VisitServices.getInstancia().getVisitbyBrowser(Long.parseLong(request.params("id")));
            response.header("Content-Type", "application/json");
            System.out.println(visits.size());
            for (Visit item:
                 visits) {
                item.setUrl(null);
            }
            return visits;
        }, JsonTransformer.json());
    }

    private Url generateURL(String a) throws Exception {
        URL direction = new URL(a);
        direction.toURI();
        InetAddress ip = InetAddress.getByName(direction.getHost());
        ip_val = ip.getHostAddress();
        ip_val = ip_val.replace(".", "");
        ip_val = (Long.parseLong(ip_val) + LocalDateTime.now().getMinute() + LocalDateTime.now().getSecond()) + "";
        hex_val = longToHex(ip_val);
        new_url = base_url + hex_val;
        //System.out.println(currentUser.getId());
        return new Url(a, ip.getHostAddress(), new_url, currentUser);
    }

    private String longToHex(String value) {
        return Long.toHexString(Long.parseLong(value));
    }

    private Map<String, Object> validateUser() {
        Map<String, Object> attributes = new HashMap<>();
        if (currentUser.getPassword() == null) {
            attributes.put("usuario", "other");
            attributes.put("userSigned", false);
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
        if (user == null) {
            aux = UserServices.getInstancia().getUser(session.id());
            if (aux == null) {
                user = new User(session.id(), false);
                UserServices.getInstancia().insert(user);
            } else
                user = aux;
        }
        session.attribute("usuario", user);
    }

    private User getSessionUsuario(Request request) {
        User user = request.session().attribute("usuario");
        //System.out.println(user.getUsername() + " " + user.getId());
        return user;
    }

    private String getSo(String userAgent) {
        String so;
        if (userAgent.contains("android")) {
            so = "Android";
        } else if (userAgent.contains("iphone")) {
            so = "iPhone";
        } else if (userAgent.contains("windows")) {
            so = "Windows";
        } else if (userAgent.contains("macintosh")) {
            so = "Macintosh";
        } else if (userAgent.contains("linux")) {
            so = "Linux";
        } else {
            so = "UnKnown";
        }

        return so;
    }

    private String getBrowser(String userAgent) {
        String browser;
        if (userAgent.contains("ie") || userAgent.contains("rv")) {
            browser = "IE";
        } else if (userAgent.contains("opr") || userAgent.contains("opera")) {
            browser = "Opera";
        } else if (userAgent.contains("safari")) {
            browser = "Safari";
        } else if (userAgent.contains("chrome")) {
            browser = "Chrome";
        } else if (userAgent.contains("firefox")) {
            browser = "Firefox";
        } else {
            browser = "Other";
        }
        return browser;
    }

}
