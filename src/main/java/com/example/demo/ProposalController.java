package com.example.demo;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.sql.Date;
import java.sql.ResultSet;
import java.util.Calendar;

@Controller
public class ProposalController {

    DataBase DB = new DataBase();

    String inbox = "<a class=\"nav-link\" href=\"inbox\">Inbox</a>";
    String userLogged = "<a class=\"nav-link\" href=\"user\">Moj profil</a>";
    String userSign = "<a class=\"nav-link\" href=\"login\">Uloguj se</a>";
    String dodajOglas = "<a class=\"nav-link\" href=\"dodajOglas\">Dodaj oglas</a>";

    @GetMapping("/proposal")
    public String proposalSwitch(@CookieValue(value = "id", defaultValue = "-1") String cookieId, @CookieValue(value="role",defaultValue = "0") String cookieRole, @RequestParam(name="idOglasa") int idOglasa, HttpServletResponse response, Model model)
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
        if(id>0) {
            ResultSet pregledRS = DB.executeCustom("SELECT * FROM pregled_oglasa WHERE korisnikID=" + id + " AND oglasID=" + idOglasa);
            if (pregledRS == null) {
                DB.executeCustom("INSERT INTO pregled_oglasa VALUES(" + id + "," + idOglasa + ",0)");
            }
        }
        Oglas oglas = new Oglas(idOglasa);
        User user = new User(oglas.getVlasnikID());
        String vlasnik="<a style=\"color: gray\" href=\"/user?id="+oglas.getVlasnikID()+"\">"+user.getIme()+" "+user.getPrezime()+"</a>";
        String vestine="";
        String edukacija="";
        for(String skill: oglas.getSkills())
        {
            vestine+="<div class=\"tag\">"+skill+"</div>";
        }
        for(String diploma: oglas.getEdukacija())
        {
            edukacija+="<div>"+diploma+"</div>";
        }
        String potrebnoIskustvo="";
        String radOdKuce="";
        if(oglas.isPotrebnoIskustvo()){
            potrebnoIskustvo="Potrebno je iskustvo";
        }
        else {
            potrebnoIskustvo="Nije potrebno iskustvo";
        }
        if(oglas.isRadOdKuce()){
            radOdKuce="Moguc je rad od kuce";
        }
        else {
            radOdKuce="Nije moguc rad od kuce";
        }
        //TODO: dopuni html i th sa novim zahtevima
        model.addAttribute("brojPregleda",oglas.getBrojPregleda());
        model.addAttribute("potrebnoIskustvo",potrebnoIskustvo);
        model.addAttribute("radOdKuce",radOdKuce);
        model.addAttribute("obrazovanje",edukacija);
        model.addAttribute("skills",vestine);
        model.addAttribute("nazivOglasa",oglas.getNazivOglasa());
        model.addAttribute("nazivVlasnika",vlasnik);
        model.addAttribute("datumKreiranja",oglas.getDatumKreiranja());
        model.addAttribute("opisOglasa",oglas.getOpisOglasa());
        model.addAttribute("lokacija",oglas.getLokacija());
        model.addAttribute("kategorijaID",oglas.getKategorija());
        model.addAttribute("id",idOglasa);

        String dugmeLajkY="<a href=\"skiniLajk?idOglasa="+idOglasa+"&userID="+id+"\" class=\"btn btn-dark\">\n" +

                "                                Lajk " + oglas.getBrojLajkova()+
                "                            </a>";

        String dugmeLajkN="<a href=\"dodajLajk?idOglasa="+idOglasa+"&userID="+id+"\" class=\"btn btn-secondary\">\n" +

                "                                Lajk " + oglas.getBrojLajkova()+
                "                            </a>";



        if(role>0) {
            if (DB.lajkovao(idOglasa, id)) {
                model.addAttribute("lajk", dugmeLajkY);
            } else {
                model.addAttribute("lajk", dugmeLajkN);
            }
        }
        else
        {
            model.addAttribute("lajk","Broj lajkova: "+oglas.getBrojLajkova());
        }

        String prijaviAplikaciju = "<div id=\"prijava\" class=\"container justify-content-center\" >" +
                "<div class=\"row\">" +
                "<h3 class=\"h3\">Posalji prijavu</h3>" +
                "<hr>" +
                "<div id=\"poruka\">" +
                "<label for=\"exampleFormControlTextarea1\" class=\"form-label\">Poruka poslodavcu</label>\n" +
                "<form method=\"POST\" action=\"posalji?idOglasa="+idOglasa+"\">  <textarea class=\"form-control\" id=\"exampleFormControlTextarea1\" name=\"message\" rows=\"5\"></textarea>" +
                "<br><input type=\"submit\" class=\"btn btn-dark\" value=\"Posalji\"/>" +
                "</form>" +
                "</div></div></div>";

        String dugme="<form method=\"POST\" action=\"obrisi?idOglasa="+idOglasa+"\"><input type=\"submit\" value=\"Izbrisi oglas\" class=\"btn btn-dark\"/></form>";
        String dugmeChange="<a href=\"izmeniOglas?idOglasa="+idOglasa+"\" class=\"btn btn-dark\">\n" +

                "                                Izmeni oglas\n" +
                "                            </a>";

        if(role==3 || oglas.getVlasnikID()==id)
        {
            model.addAttribute("dugmeChange",dugmeChange);
            model.addAttribute("dugme",dugme);
        }

        boolean prijavio = DB.prijavioAplikaciju(id, idOglasa);

        if((role==1 || role==3) && !prijavio)
        {
            model.addAttribute("prijava", prijaviAplikaciju);
        }


        return "proposal";
    }
    @GetMapping("dodajLajk")
    public String dodajLajkk(@RequestParam("idOglasa") int idOglasa,@RequestParam("userID") int userID)
    {
        DB.dodajLajk(idOglasa,userID);
        return "redirect:proposal?idOglasa="+idOglasa;
    }
    @GetMapping("skiniLajk")
    public String skiniLajkk(@RequestParam("idOglasa") int idOglasa,@RequestParam("userID") int userID)
    {
        DB.skiniLajk(idOglasa,userID);
        return "redirect:proposal?idOglasa="+idOglasa;
    }

    @PostMapping("obrisi")
    public String izbrisiOglas(@RequestParam("idOglasa") int idOglasa)
    {
        DB.izbrisiOglas(idOglasa);
        return "redirect:/";
    }

    @PostMapping("posalji")
    public String posaljiAplikaciju(@RequestParam("idOglasa") int idOglasa,@RequestParam("message") String message, @CookieValue(value = "id") String cookieId)
    {
        int id=Integer.parseInt(Security.decryptCookie(cookieId));
        DB.posaljiAplikaciju(id,idOglasa,message);
        return "redirect:proposal?idOglasa="+idOglasa;
    }

    @GetMapping("/dodajOglas")
    public String dodajOglasPrikaz(@CookieValue(value = "id", defaultValue = "AIXP") String cookieId,
                                   @CookieValue(value = "role", defaultValue = "AIXP") String cookieRole,
                                   HttpServletResponse response,
                                   Model model)
    {
        int id=Integer.parseInt(Security.decryptCookie(cookieId));
        int role=Integer.parseInt(Security.decryptCookie(cookieRole));

        boolean provera=Security.proveriCookies(role,id,response);
        if(provera)
        {
            id=0;
            role=0;
        }
        if (role == 0 || role==1) {
            return "redirect:error";
        }
        String diplome="";
        for (String tipSkole:DB.vratiTipoveSkola()) {
            diplome+="<optgroup label=\" "+tipSkole+"\">";
            for (String nazivSkole:DB.vratiListuSkola(tipSkole)) {
                diplome+="<optgroup label=\"&nbsp;&nbsp;&nbsp;&nbsp;"+nazivSkole+"\">";
                diplome+=DB.vratiListuZvanja(nazivSkole);
                diplome+="</optgroup>";
            }
            diplome+="</optgroup>";
        }
        String lokacije="";
        for (String drzava:DB.vratiListuDrzava()) {
            lokacije+="<optgroup label=\""+drzava+"\">";
            lokacije+=DB.vratiListuGradova(drzava);
            lokacije+="</optgroup>";
        }
        model.addAttribute("edukacija",diplome);
        model.addAttribute("kategorija",DB.vratiListuKategorija());
        model.addAttribute("skills",DB.vratiListuSkills());
        model.addAttribute("lokacija",lokacije);
        model.addAttribute("dugme","Napravi");
        model.addAttribute("akcija","dodajOglas");
        return "uploadJob";
    }

    @PostMapping("/dodajOglas")
    public String dodajOglas(@CookieValue(value = "id", defaultValue = "AIXP") String cookieId,
                             @CookieValue(value = "role", defaultValue = "AIXP") String cookieRole,
                             @RequestParam("kategorija") int kategorija,
                             @RequestParam("skills") int[] skills,
                             @RequestParam("edukacija") int[] edukacija,
                             @RequestParam("lokacija") int lokacija,
                             @RequestParam("nazivOglasa") String naziv,
                             @RequestParam("opisOglasa") String opis,
                             @RequestParam("iskustvo") boolean iskustvo,
                             @RequestParam("radOdKuce") boolean radOdKuce)
    {
        System.out.println(naziv);
        int id = Integer.parseInt(Security.decryptCookie(cookieId));
        Calendar datum=Calendar.getInstance();
        String datumKreiranja=datum.get(Calendar.YEAR)+"-"+datum.get(Calendar.MONTH)+1+"-"+datum.get(Calendar.DATE);
        int idOglasa=DB.dodajOglas(id,lokacija,kategorija,datumKreiranja,naziv,opis,radOdKuce,iskustvo);
        DB.dodajSkillOglas(idOglasa,skills);
        DB.dodajEdukacijuOglas(idOglasa,edukacija);
        return "redirect:/proposal?idOglasa="+idOglasa;
    }

    @GetMapping("/izmeniOglas")
    public String izmeniOglas(@CookieValue(value = "id", defaultValue = "AIXP") String cookieId,
                             @CookieValue(value = "role", defaultValue = "AIXP") String cookieRole,
                             @RequestParam("idOglasa") int idOglasa,
                             HttpServletResponse response,
                             Model model)
    {
        int id=Integer.parseInt(Security.decryptCookie(cookieId));
        int role=Integer.parseInt(Security.decryptCookie(cookieRole));

        boolean provera=Security.proveriCookies(role,id,response);
        if(provera)
        {
            id=0;
            role=0;
        }
        if (role == 0 || role==1) {
            return "redirect:error";
        }
        Oglas oglas=new Oglas(idOglasa);
        if(oglas.getVlasnikID()!=id && role!=3)
        {
            return "redirect:error";
        }
        String lokacije="";
        for (String drzava:DB.vratiListuDrzava()) {
            lokacije+="<optgroup label=\""+drzava+"\">";
            lokacije+=DB.vratiListuGradovaOglas(drzava, oglas.getLokacija());
            lokacije+="</optgroup>";
        }
        System.out.println(oglas.getNazivOglasa());
        String diplome="";
        for (String tipSkole:DB.vratiTipoveSkola()) {
            diplome+="<optgroup label=\" "+tipSkole+"\">";
            for (String nazivSkole:DB.vratiListuSkola(tipSkole)) {
                diplome+="<optgroup label=\"&nbsp;&nbsp;&nbsp;&nbsp;"+nazivSkole+"\">";
                diplome+=DB.vratiListuZvanjaOglas(nazivSkole,idOglasa);
                diplome+="</optgroup>";
            }
            diplome+="</optgroup>";
        }
        if(oglas.isPotrebnoIskustvo())
        {
            model.addAttribute("iskustvoDa","");
        }
        else {
            model.addAttribute("iskustvoNe","");
        }
        if(oglas.isRadOdKuce())
        {
            model.addAttribute("radOdKuceDa","");
        }
        else {
            model.addAttribute("radOdKuceNe","");
        }
        model.addAttribute("kategorija",DB.vratiListuKategorijaOglas(oglas.getKategorijaID()));
        model.addAttribute("edukacija",diplome);
        model.addAttribute("naziv","Izmeni oglas");
        model.addAttribute("skills",DB.vratiListuSkillsOglas(idOglasa));
        model.addAttribute("lokacija",lokacije);
        model.addAttribute("nazivOglasa",oglas.getNazivOglasa());
        model.addAttribute("opisOglasa",oglas.getOpisOglasa());
        model.addAttribute("dugme","Izmeni");
        String odustani="<a href=\"proposal?idOglasa="+idOglasa+"\" class=\"btn btn-dark\">\n" +

            "                               Odustani\n" +
            "                            </a>";
        model.addAttribute("odustani",odustani);
        model.addAttribute("akcija","izmeniOglas?idOglasa="+idOglasa);

        return "uploadJob";
    }

    @PostMapping("/izmeniOglas")
    public String izmeniOglasPost(@RequestParam("idOglasa") int idOglasa,
                                  @RequestParam("kategorija") int kategorija,
                                 @RequestParam(value = "skills",defaultValue = "-1") int[] skills,
                                 @RequestParam(value = "edukacija",defaultValue = "") int[] edukacija,
                                 @RequestParam("lokacija") int lokacija,
                                 @RequestParam("nazivOglasa") String naziv,
                                 @RequestParam("opisOglasa") String opis,
                                 @RequestParam("iskustvo") boolean iskustvo,
                                 @RequestParam("radOdKuce") boolean radOdKuce)
    {
        DB.updateOglas(idOglasa,lokacija,kategorija,naziv,opis,radOdKuce,iskustvo,skills,edukacija);
        return "redirect:proposal?idOglasa="+idOglasa;
    }

}
