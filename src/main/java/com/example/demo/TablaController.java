package com.example.demo;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;

@Controller
public class TablaController {

    DataBase DB=new DataBase();

    String inbox = "<a class=\"nav-link\" href=\"inbox\">Inbox</a>";
    String userLogged = "<a class=\"nav-link\" href=\"user\">Moj profil</a>";
    String dodajOglas = "<a class=\"nav-link\" href=\"dodajOglas\">Dodaj oglas</a>";

    @GetMapping("/inbox")
    public String oglasnaTabla(@CookieValue("id") String cookieId, @CookieValue("role") String cookieRole, HttpServletResponse response, Model model)
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
            return "redirect:error";
        }
        if (role > 0 && role<4) {
            model.addAttribute("inbox", inbox);
            model.addAttribute("user", userLogged);
        }
        if (role == 2 || role == 3) {
            model.addAttribute("dodajOglas", dodajOglas);
        }
        if(role==1) {
            model.addAttribute("popuni",DB.popuniInfoInboxKorisnik(id));
        }
        if(role==2) {
            model.addAttribute("popuni",DB.popuniInfoInboxPoslodavac(id));
        }
        if(role==3)
        {
            model.addAttribute("popuni",DB.popuniInfoInboxKorisnik(id)+DB.popuniInfoInboxPoslodavac(id));
        }
        return "oglasnaTabla";
    }

    @GetMapping("/dismissUser")
    public String dismissKorisnik(@RequestParam("id") int id)
    {
        DB.dismissUser(id);
        return "redirect:inbox";
    }

    @GetMapping("/dismissPoslodavac")
    public String dismissPoslodavac(@RequestParam("id") int id)
    {
        DB.dismissPoslodavac(id);
        return "redirect:inbox";
    }

    @GetMapping("/accept")
    public String accept(@RequestParam("id") int id)
    {
        DB.accept(id);
        return "redirect:inbox";
    }

    @GetMapping("/decline")
    public String decline(@RequestParam("id") int id)
    {
        DB.decline(id);
        return "redirect:inbox";
    }

}
