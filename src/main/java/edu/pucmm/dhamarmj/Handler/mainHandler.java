package edu.pucmm.dhamarmj.Handler;

import com.google.gson.Gson;
import edu.pucmm.dhamarmj.Encapsulation.Groupby;
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

    static User currentUser = new User();
    Gson gson = new Gson();
    static String ip_val, hex_val, new_url, base_url = "https://www.hadesprograms.me/dmj/";
    static Url url_value;
    User aux;
    String so, browser, auxS;
    int auxi, auxi2;

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
            //VisitServices.getInstancia().getVisitbyBrowser(33);
            String user = request.cookie("LoginU");
            if (user != null) {
                String passw = Encryption.Decrypt(request.cookie("LoginP"));
                String usern = Encryption.Decrypt(user);
                currentUser = UserServices.getInstancia().getUser(usern, passw);
                CreateSession(request, currentUser);
            }
            currentUser = getSessionUsuario(request);
            if (currentUser == null)
                currentUser = CreateSession(request, null);

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
            Url url = UrlServices.getInstancia().buscar(Long.parseLong(request.params("id")));
            attributes.put("clickNum", url.getVisits().size());
            attributes.put("urlId", request.params("id"));
            attributes.put("url", url.getUrl());
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
                response.header("Content-Type", "application/json");
                for (Url aux :
                        vals) {
                    aux.setUser(null);
                    aux.setVisits(null);
                }
                return vals;
            }
            response.status(404);
            System.out.println("ERROR");
            return null;
        }, JsonTransformer.json());

        get("/rest/urls", (request, response) -> {
            List<Url> vals = UrlServices.getInstancia().buscarTodos();
            response.header("Content-Type", "application/json");
            for (Url aux :
                    vals) {
                aux.setUser(null);
                aux.setVisits(null);
            }
            if (vals.size() == 0)
                response.status(404);

            return vals;
        }, JsonTransformer.json());

        get("/deleteUrl/:id", (request, response) -> {
            Url url = UrlServices.getInstancia().buscar(Long.parseLong(request.params("id")));
            for (Visit v:
                 url.getVisits()) {
                VisitServices.getInstancia().eliminar(v.getId());
            }
            UrlServices.getInstancia().eliminar(url.getId());
            response.redirect("/AllUrls/");
            return null;
        });
        get("/rest/browserUrl/:id", (request, response) -> {
            List<Visit> visits = VisitServices.getInstancia().getVisitbyUrl(Long.parseLong(request.params("id")));
            response.header("Content-Type", "application/json");
            return groupbyBrowser(visits);
        }, JsonTransformer.json());

        get("/rest/osUrl/:id", (request, response) -> {
            List<Visit> visits = VisitServices.getInstancia().getVisitbyUrl(Long.parseLong(request.params("id")));
            response.header("Content-Type", "application/json");
            return groupbySo(visits);
        }, JsonTransformer.json());

        get("/rest/dateUrl/:id", (request, response) -> {
            List<Visit> visits = VisitServices.getInstancia().getVisitbyUrl(Long.parseLong(request.params("id")));
            response.header("Content-Type", "application/json");
            return groupbyDate(visits);
        }, JsonTransformer.json());

        get("/rest/ipUrl/:id", (request, response) -> {
            List<Visit> visits = VisitServices.getInstancia().getVisitbyUrl(Long.parseLong(request.params("id")));
            response.header("Content-Type", "application/json");
            return groupbyIP(visits);
        }, JsonTransformer.json());

        // HttpResponse<String> response = Unirest.get('https://api.microlink.io?url=https%3A%2F%2Ftwitter.com%2Ffuturism%2Fstatus%2F882987478541533189');

    }

    public static Url generateURL(String a) throws Exception {
        if(a.isEmpty())
            return null;

        URL direction = new URL(a);
        direction.toURI();
        InetAddress ip = InetAddress.getByName(direction.getHost());
        ip_val = ip.getHostAddress();
        ip_val = ip_val.replace(".", "");
        ip_val = (Long.parseLong(ip_val) + LocalDateTime.now().getMinute() + LocalDateTime.now().getSecond()) + "";
        hex_val = longToHex(ip_val);
        new_url = base_url + hex_val;
        return new Url(a, ip.getHostAddress(), new_url, currentUser);
    }

    private static String longToHex(String value) {
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

    private User CreateSession(Request request, User user) {
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
        return user;
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
        } else if (userAgent.contains("chrome")) {
            browser = "Chrome";
        } else if (userAgent.contains("firefox")) {
            browser = "Firefox";
        } else if (userAgent.contains("safari")) {
            browser = "Safari";
        } else {
            browser = "Other";
        }
        return browser;
    }

    private List<Groupby> groupbyBrowser(List<Visit> visits) {
        List<Groupby> chart_val = new ArrayList<>();
        for (Visit visit :
                visits) {
            auxi = find(chart_val, visit.getBrowser());
            if (auxi != -1) {
                auxi2 = chart_val.get(auxi).getValue();
                chart_val.get(auxi).setValue(auxi2 + 1);
            } else {
                chart_val.add(new Groupby(visit.getBrowser(), 1));
            }
        }
        return chart_val;
    }

    private List<Groupby> groupbySo(List<Visit> visits) {
        List<Groupby> chart_val = new ArrayList<>();
        for (Visit visit :
                visits) {
            auxi = find(chart_val, visit.getSo());
            if (auxi != -1) {
                auxi2 = chart_val.get(auxi).getValue();
                chart_val.get(auxi).setValue(auxi2 + 1);
            } else {
                chart_val.add(new Groupby(visit.getSo(), 1));
            }
        }
        return chart_val;
    }

    private List<Groupby> groupbyDate(List<Visit> visits) {
        List<Groupby> chart_val = new ArrayList<>();
        for (Visit visit :
                visits) {
            auxi = find(chart_val, visit.getFechaS());
            if (auxi != -1) {
                auxi2 = chart_val.get(auxi).getValue();
                chart_val.get(auxi).setValue(auxi2 + 1);
            } else {
                chart_val.add(new Groupby(visit.getFechaS(), 1));
            }
        }
        return chart_val;
    }

    private List<Groupby> groupbyIP(List<Visit> visits) {
        List<Groupby> chart_val = new ArrayList<>();
        for (Visit visit :
                visits) {
            auxi = find(chart_val, visit.getIp());
            if (auxi != -1) {
                auxi2 = chart_val.get(auxi).getValue();
                chart_val.get(auxi).setValue(auxi2 + 1);
            } else {
                chart_val.add(new Groupby(visit.getIp(), 1));
            }
        }
        return chart_val;
    }

    public int find(List<Groupby> array, String target) {
        if (array.size() == 0)
            return -1;

        int i = 0, return_val = -1;
        while (i < array.size() && return_val == -1) {
            if (array.get(i).getName() == target)
                return_val = i;
            i++;
        }
        return return_val;
    }

}
