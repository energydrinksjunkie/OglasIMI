package com.example.demo;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.File;

@Controller
public class UserController {

    DataBase DB = new DataBase();

    String inbox = "<a class=\"nav-link\" href=\"inbox\">Inbox</a>";
    String userLogged = "<a class=\"nav-link\" href=\"user\">Moj profil</a>";
    String userSign = "<a class=\"nav-link\" href=\"login\">Uloguj se</a>";
    String dodajOglas = "<a class=\"nav-link\" href=\"dodajOglas\">Dodaj oglas</a>";

    @GetMapping("/user")
    public String user(@CookieValue(value = "id", defaultValue = "2") String cookieId, @CookieValue(value="role",defaultValue = "0") String cookieRole,@RequestParam (value = "id",defaultValue = "0") int id, Model model, HttpServletResponse response) {
        int idKolaca=Integer.parseInt(Security.decryptCookie(cookieId));
        int role=Integer.parseInt(Security.decryptCookie(cookieRole));

        boolean provera=Security.proveriCookies(role,id,response);
        if(provera)
        {
            id=0;
            role=0;
        }
        if (role == 0) {
            model.addAttribute("user", userSign);
        }
        if (role > 0 && role<4) {
            model.addAttribute("inbox", inbox);
            model.addAttribute("user", userLogged);
        }
        if (role == 2 || role == 3) {
            model.addAttribute("dodajOglas", dodajOglas);
        }
        User korisnik;
        if(id>0 && id!=idKolaca) {
            korisnik = new User(id);
            if(role==3) {
                String dugme = "<form method=\"POST\" action=\"izbrisi?idKorisnika="+id+"\"><input type=\"submit\" value=\"Izbrisi profil\" class=\"btn btn-dark\"/></form>";
                model.addAttribute("dugme", dugme);
            }
        }
        else {
            korisnik = new User(idKolaca);
            String dugme="<form method=\"GET\" action=\"signout\"><button type=\"submit\" class=\"btn btn-dark\"/>Odjavi se</button></form>";
            model.addAttribute("dugme", dugme);
            String dugmeChange="<a href=\"changeInfo\" class=\"btn btn-dark\">\n" +

                    "                                Izmeni informacije\n" +
                    "                            </a>";
            model.addAttribute("dugmeChange",dugmeChange);
        }

        //TODO: dopuniti html sa novim informacijama

        String vestine="";
        String edukacija="";
        for(String skill: korisnik.getSkills())
        {
            vestine+="<div class=\"tag\">"+skill+"</div>";
        }
        for(String diploma: korisnik.getEdukacija())
        {
            edukacija+="<div class=\"edu\">"+diploma+"</div>";
        }
        model.addAttribute("edukacija",edukacija);
        model.addAttribute("skills",vestine);
        model.addAttribute("ime",korisnik.getIme()+" "+korisnik.getPrezime());
        model.addAttribute("opis",korisnik.getBio());
        model.addAttribute("lokacija",korisnik.getLokacija());
        model.addAttribute("email",korisnik.getEmail());
        model.addAttribute("broj",korisnik.getMobilni());
        model.addAttribute("avatar",korisnik.getSlikaUrl());

        return "user";
    }

    @GetMapping("/signout")
    public String signout(HttpServletResponse response)
    {

        // create a cookie
        Cookie cookieId = new Cookie("id", null);
        Cookie cookieRole = new Cookie("role",null);
        cookieId.setMaxAge(0);
        cookieRole.setMaxAge(0);
        cookieId.setPath("/");
        cookieRole.setPath("/");

        //add cookie to response
        response.addCookie(cookieId);
        response.addCookie(cookieRole);
        return "redirect:/";
    }

    @PostMapping("/izbrisi")
    public String deleteUser(@RequestParam("idKorisnika") int idKorisnika)
    {
        DB.izbrisiKorisnika(idKorisnika);
        return "redirect:/";
    }

    @GetMapping("/login")
    public String test(@CookieValue(value = "id", defaultValue = "AIXP") String cookieId,
                       @CookieValue(value = "role", defaultValue = "AIXP") String cookieRole,
                       @RequestParam(value = "username", defaultValue = "") String username,
                       Model model,
                       HttpServletResponse response) {
        //TODO: dodati obavestenje za promasen i ispravan user
        int role=Integer.parseInt(Security.decryptCookie(cookieRole));
        int id=Integer.parseInt(Security.decryptCookie(cookieId));
        boolean provera=Security.proveriCookies(role,id,response);
        if(provera)
        {
            id=0;
            role=0;
        }
        if(role!=0 && id!=0)
        {
            return "redirect:error";
        }
        model.addAttribute("username", username);
        return "login";
    }

    @PostMapping("/login")
    public String test(@RequestParam("username") String username,
                       @RequestParam("password") String password,
                       HttpServletResponse response) {

        String usernameExists=DB.vratiUsername(username);

        if(usernameExists==null) {
            return "redirect:login";
        }

        String passwordHash=DB.vratiPassword(username);
        boolean matched=Security.matchPassword(password,passwordHash);

        if (!matched) {
            return "redirect:login?username=" + usernameExists;
        }

        int userID=DB.vratiUserID(username);
        String role = DB.roleKorisnika(userID)+"";
        Cookie cookieId = new Cookie("id", Security.encryptCookie(userID+""));
        cookieId.setPath("/");
        Cookie cookieRole = new Cookie("role", Security.encryptCookie(role));
        cookieRole.setPath("/");

        cookieId.setSecure(true);
        cookieRole.setSecure(true);
        cookieId.setHttpOnly(true);
        cookieRole.setHttpOnly(true);
        response.addCookie(cookieId);
        response.addCookie(cookieRole);
        return "redirect:user";
    }

    @GetMapping("/register")
    public String register(
            @CookieValue(value = "id", defaultValue = "AIXP") String cookieId,
            @CookieValue(value = "role", defaultValue = "AIXP") String cookieRole,
            Model model,
            HttpServletResponse response) {
        int role=Integer.parseInt(Security.decryptCookie(cookieRole));
        int id=Integer.parseInt(Security.decryptCookie(cookieId));
        boolean provera=Security.proveriCookies(role,id,response);
        if(provera)
        {
            id=0;
            role=0;
        }
        if(role!=0 && id!=0)
        {
            return "redirect:error";
        }
        if (role == 0) {
            model.addAttribute("user", userSign);
        }
        if (role > 0 && role<4) {
            model.addAttribute("inbox", inbox);
            model.addAttribute("user", userLogged);
        }
        if (role == 2 || role == 3) {
            model.addAttribute("dodajOglas", dodajOglas);
        }

        String lokacije="";
        for (String drzava:DB.vratiListuDrzava()) {
            lokacije+="<optgroup label=\""+drzava+"\">";
            lokacije+=DB.vratiListuGradova(drzava);
            lokacije+="</optgroup>";
        }
        String diplome="";
        for (String tipSkole:DB.vratiTipoveSkola()) {
            diplome+="<optgroup label=\""+tipSkole+"\">";
            for (String nazivSkole:DB.vratiListuSkola(tipSkole)) {
                diplome+="<optgroup label=\"&nbsp;&nbsp;&nbsp;&nbsp;"+nazivSkole+"\">";
                diplome+=DB.vratiListuZvanja(nazivSkole);
                diplome+="</optgroup>";
            }
            diplome+="</optgroup>";
        }

        String izaberiTip="<div class=\"form-group col-md-4\">" +
                "<label for=\"korisnikUloga\">*Tip Profila</label>" +
                "<select class=\"form-control\" id=\"korisnikUloga\" name=\"korisnikUloga\">" +
                "<option value=\"2\">Poslodavac</option>" +
                "<option value=\"1\">Korisnik</option>\n" +
                "</select>" +
                "</div>";

        model.addAttribute("naziv","Registruj se");
        model.addAttribute("musko","");
        model.addAttribute("edukacija",diplome);
        model.addAttribute("skills",DB.vratiListuSkills());
        model.addAttribute("lokacija",lokacije);
        model.addAttribute("tipKorisnika", izaberiTip);
        model.addAttribute("dugme","Registruj Se");
        model.addAttribute("akcija","/register");
        return "register";
    }

    @PostMapping("/register")
    String registruj(@RequestParam(value = "korisnikIme") String korisnikIme,
                     @RequestParam(value = "korisnikPrezime") String korisnikPrezime,
                     @RequestParam(value = "korisnikRodjen") String korisnikRodjen,
                     @RequestParam(value = "pol") int pol,
                     @RequestParam(value = "username") String username,
                     @RequestParam(value = "password") String password,
                     @RequestParam(value = "email") String email,
                     @RequestParam(value = "korisnikMob") String korisnikMob,
                     @RequestParam("skills") int[] skills,
                     @RequestParam("edukacija") int[] edukacija,
                     @RequestParam("lokacija") int lokacija,
                     @RequestParam(value = "korisnikBio") String korisnikBio,
                     @RequestParam(value = "korisnikUloga") int korisnikUloga,
                     @RequestParam(value = "korisnikSlika") String korisnikSlika,
                     HttpServletResponse response) {
        password=Security.generatePassword(password);
        int korisnikID = DB.registrujKorisnika(
                korisnikIme,
                korisnikPrezime,
                korisnikRodjen,
                pol,
                username,
                password,
                email,
                korisnikMob,
                lokacija,
                korisnikBio,
                korisnikUloga,
                korisnikSlika);

        String usernameExists=DB.vratiUsername(username);

        if(usernameExists==null) {
            return "redirect:register";
        }
        DB.dodajSkillKorisnik(korisnikID,skills);
        DB.dodajEdukacijuKorisnik(korisnikID,edukacija);
        Cookie cookieId = new Cookie("id", Security.encryptCookie(korisnikID + ""));
        Cookie cookieRole = new Cookie("role", Security.encryptCookie(korisnikUloga + ""));
        cookieId.setPath("/");
        cookieRole.setPath("/");
        cookieId.setSecure(true);
        cookieRole.setSecure(true);
        cookieId.setHttpOnly(true);
        cookieRole.setHttpOnly(true);

        response.addCookie(cookieId);
        response.addCookie(cookieRole);
        return "redirect:/";
    }

    @GetMapping("/changeInfo")
    String izmeniInfo(@CookieValue("id") String cookieId,
                      @CookieValue("role") String cookieRole,
                      HttpServletResponse response,
                      Model model) {
        int id=Integer.parseInt(Security.decryptCookie(cookieId));
        int role=Integer.parseInt(Security.decryptCookie(cookieRole));

        boolean provera=Security.proveriCookies(role,id,response);
        if(provera)
        {
            id=0;
            role=0;
        }
        if (role == 0) {
            return "redirect:error";
        }
        if (role > 0 && role<4) {
            model.addAttribute("inbox", inbox);
            model.addAttribute("user", userLogged);
        }
        if (role == 2 || role == 3) {
            model.addAttribute("dodajOglas", dodajOglas);
        }
        User korisnik=new User(id);
        String lokacije="";
        for (String drzava:DB.vratiListuDrzava()) {
            lokacije+="<optgroup label=\""+drzava+"\">";
            lokacije+=DB.vratiListuGradovaKorisnik(drzava, korisnik.getLokacija());
            lokacije+="</optgroup>";
        }
        String diplome="";
        for (String tipSkole:DB.vratiTipoveSkola()) {
            diplome+="<optgroup label=\" "+tipSkole+"\">";
            for (String nazivSkole:DB.vratiListuSkola(tipSkole)) {
                diplome+="<optgroup label=\"&nbsp;&nbsp;&nbsp;&nbsp;"+nazivSkole+"\">";
                diplome+=DB.vratiListuZvanjaKorisnik(nazivSkole,id);
                diplome+="</optgroup>";
            }
            diplome+="</optgroup>";
        }
        if(korisnik.isPol())
        {
            model.addAttribute("musko","");
        }
        else
        {
            model.addAttribute("zensko","");
        }
        model.addAttribute("edukacija",diplome);
        model.addAttribute("naziv","Izmeni informacije");
        model.addAttribute("skills",DB.vratiListuSkillsKorisnik(id));
        model.addAttribute("lokacija",lokacije);
        model.addAttribute("korisnikIme",korisnik.getIme());
        model.addAttribute("korisnikPrezime",korisnik.getPrezime());
        model.addAttribute("korisnikRodjen",korisnik.getDatumRodjenja());
        model.addAttribute("username",DB.vratiUsernameID(id));
        model.addAttribute("akcija","/changeInfo");
        model.addAttribute("email",korisnik.getEmail());
        model.addAttribute("korisnikMob",korisnik.getMobilni());
        model.addAttribute("korisnikBio",korisnik.getBio());
        model.addAttribute("uloga",korisnik.getTipKorisnika());
        model.addAttribute("dugme","Izmeni Informacije");
        String odustani="<a href=\"user\" class=\"btn btn-dark\">\n" +

                "                               Odustani\n" +
                "                            </a>";
        model.addAttribute("odustani", odustani);

        return "register";
    }

    @PostMapping("/changeInfo")
    public String promeniInfo(@CookieValue("id") String cookieId,
                       @RequestParam(value = "korisnikIme") String korisnikIme,
                     @RequestParam(value = "korisnikPrezime") String korisnikPrezime,
                     @RequestParam(value = "korisnikRodjen") String korisnikRodjen,
                     @RequestParam(value = "pol") int pol,
                     @RequestParam(value = "username") String username,
                     @RequestParam(value = "password") String password,
                     @RequestParam(value = "email") String email,
                     @RequestParam(value = "korisnikMob") String korisnikMob,
                              @RequestParam(value = "skills",defaultValue = "-1") int[] skills,
                              @RequestParam(value = "edukacija",defaultValue = "-1") int[] edukacija,
                     @RequestParam("lokacija") int lokacija,
                     @RequestParam(value = "korisnikBio") String korisnikBio,
                     @RequestParam(value = "korisnikSlika") String korisnikSlika)
    {
        int id=Integer.parseInt(Security.decryptCookie(cookieId));
        DB.updateKorisnika(id,korisnikIme,korisnikPrezime,korisnikRodjen,pol,username,password,email,korisnikMob,lokacija,korisnikBio,korisnikSlika,skills,edukacija);
        return "redirect:user";
    }

}
