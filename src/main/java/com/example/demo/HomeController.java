package com.example.demo;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.sql.ResultSet;
import java.sql.SQLException;

@Controller
public class HomeController {

    DataBase DB = new DataBase();

    String inbox = "<a class=\"nav-link\" href=\"inbox\">Inbox</a>";
    String userLogged = "<a class=\"nav-link\" href=\"user\">Moj profil</a>";
    String userSign = "<a class=\"nav-link\" href=\"login\">Uloguj se</a>";
    String dodajOglas = "<a class=\"nav-link\" href=\"dodajOglas\">Dodaj oglas</a>";


    @GetMapping("/")
    public String pocetna(@RequestParam(value = "search", defaultValue = "") String search,
                          @RequestParam(value = "kategorija",defaultValue = "-1") int[] kategorija,
                          @RequestParam(value = "lokacija",defaultValue = "-1") int[] lokacija,
                          @RequestParam(value = "skills",defaultValue = "-1") int[] skills,
                          @CookieValue(value = "id", defaultValue = "AIXP") String cookieId,
                          @CookieValue(value = "role", defaultValue = "AIXP") String cookieRole,
                          Model model,
                          HttpServletResponse response) {

        int id=Integer.parseInt(Security.decryptCookie(cookieId));
        int role=Integer.parseInt(Security.decryptCookie(cookieRole));

        boolean provera=Security.proveriCookies(role,id,response);
        if(provera)
        {
            id=0;
            role=0;
        }

        model.addAttribute("search",search);
        String lista = popuniOglase(search,skills,lokacija,kategorija);

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
        if(lokacija[0]==-1) {
            for (String drzava : DB.vratiListuDrzava()) {
                lokacije += "<optgroup label=\"" + drzava + "\">";
                lokacije += DB.vratiListuGradova(drzava);
                lokacije += "</optgroup>";
            }
        }
        else {
            for (String drzava : DB.vratiListuDrzava()) {
                lokacije += "<optgroup label=\"" + drzava + "\">";
                lokacije += DB.vratiListuGradovaFilter(drzava,lokacija);
                lokacije += "</optgroup>";
            }
        }
        if(kategorija[0]!=-1) {
            model.addAttribute("kategorija",DB.vratiListuKategorijaFilter(kategorija));
        }
        else {
            model.addAttribute("kategorija",DB.vratiListuKategorija());
        }
        if(skills[0]!=-1) {
            model.addAttribute("skills",DB.vratiListuSkillsFilter(skills));
        }
        else {
            model.addAttribute("skills",DB.vratiListuSkills());
        }

        model.addAttribute("lokacija", lokacije);

        model.addAttribute("poslovi", lista);
        return "index";
    }

    private String popuniOglase(String search,int[] skills,int[] lokacija,int[] kategorija) {
        StringBuilder lista = new StringBuilder();
        try {
            ResultSet rs = DB.popuniListuOglasa(search,skills,lokacija,kategorija);
            while (rs.next()) {
                Oglas oglas = new Oglas(rs.getInt("oglasID"));
                lista.append("<div class=\"posao\">" + "<a href=\"proposal?idOglasa=").append(oglas.getOglasID()).append("\">").append(oglas.getNazivOglasa()).append("</a>").append("<p>").append(oglas.getOpisOglasa()).append("</p>");
                for (String skill : oglas.getSkills()) {
                    lista.append("<div class=\"tag\">").append(skill).append("</div>");
                }
                lista.append("<div class=\"lokacija\">" + "<img src=\"img/loc.png\" alt=\"lokacija\"> " + "<span>").append(oglas.getLokacija()).append("</span>").append("</div></div>");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista.toString();
    }

    @GetMapping("/error")
    public String error(@CookieValue(value = "id", defaultValue = "AIXP") String cookieId,
                        @CookieValue(value = "role", defaultValue = "AIXP") String cookieRole,
                        Model model,
                        HttpServletResponse response)
    {
        int id=Integer.parseInt(Security.decryptCookie(cookieId));
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
        return "error";
    }

}
