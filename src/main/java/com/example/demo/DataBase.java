package com.example.demo;

import java.io.File;

import java.sql.*;
import java.util.ArrayList;
import java.util.Objects;

public class DataBase {
    Connection conn = null;
    Statement stmt = null;
    String sql;
    DataBase(){

        System.out.println("Connecting...");

        try {
            conn = DriverManager.getConnection(
                    "jdbc:mariadb://localhost:3307/poop", "root", ""
            );
        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println("Connected.");

        try {
            stmt = conn.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void kreirajPocetnuBazu(){
        System.out.println("Creating tables...");

        String lokacija ="CREATE TABLE Lokacija" +
                "(" +
                "lokacijaID INT NOT NULL AUTO_INCREMENT PRIMARY KEY," +
                "drzava VARCHAR(255)," +
                "grad VARCHAR(255)" +
                ")";
        String vestine = "CREATE TABLE Vestine" +
                "(" +
                "vestinaID INT NOT NULL AUTO_INCREMENT PRIMARY KEY," +
                "naziv VARCHAR(255)" +
                ")";
        String diplome ="CREATE TABLE Diplome" +
                "(" +
                "diplomaID INT NOT NULL AUTO_INCREMENT PRIMARY KEY," +
                "zvanje VARCHAR(255)," +
                "nazivSkole VARCHAR(255)," +
                "tipDiplome VARCHAR(255)" +
                ")";
        String kategorija_oglasa = "CREATE TABLE Kategorija_Oglasa" +
                "(" +
                "kategorijaID INT NOT NULL AUTO_INCREMENT PRIMARY KEY," +
                "naziv VARCHAR(255)" +
                ")";
        String korisnik = "CREATE TABLE Korisnik" +
                "(" +
                "korisnikID INT NOT NULL AUTO_INCREMENT PRIMARY KEY," +
                "ime VARCHAR(255)," +
                "prezime VARCHAR(255)," +
                "datumRodjenja VARCHAR(255)," +
                "email VARCHAR(255)," +
                "pol BIT," +
                "mobilni VARCHAR(255)," +
                "profilnaSlikaURL VARCHAR(255)," +
                "bio VARCHAR(2000)," +
                "lokacijaID INT," +
                "tipKorisnika INT," +
                "CONSTRAINT FK_lokacijaID_1 FOREIGN KEY (lokacijaID) REFERENCES Lokacija(lokacijaID)," +
                "CONSTRAINT FK_tipKorisnika FOREIGN KEY (tipKorisnika) REFERENCES Tip_Korisnika(tipID)" +
                ")";
        String prijava = "CREATE TABLE Prijava " +
                "(" +
                "korisnikID INT PRIMARY KEY," +
                "sifraKorisnika VARCHAR(255)," +
                "imeKorisnika VARCHAR(255)," +
                "CONSTRAINT FK_korisnikID_1 FOREIGN KEY (korisnikID) REFERENCES Korisnik(korisnikID)" +
                ")";
        String korisnik_vestine = "CREATE TABLE Korisnik_Vestine " +
                "(" +
                "korisnikID INT," +
                "vestinaID INT," +
                "PRIMARY KEY(korisnikID,vestinaID),"+
                "CONSTRAINT FK_korisnikID_2 FOREIGN KEY (korisnikID) REFERENCES Korisnik(korisnikID)," +
                "CONSTRAINT FK_vestinaID_1 FOREIGN KEY(vestinaID) REFERENCES Vestine(vestinaID)" +
                ")";
        String korisnik_edukacija = "CREATE TABLE Korisnik_Edukacija " +
                "(" +
                "korisnikID INT," +
                "diplomaID INT," +
                "PRIMARY KEY(korisnikID, diplomaID)," +
                "CONSTRAINT FK_korisnikID_3 FOREIGN KEY (korisnikID) REFERENCES Korisnik(korisnikID)," +
                "CONSTRAINT FK_diplomaID_1 FOREIGN KEY (diplomaID) REFERENCES Diplome(diplomaID)" +
                ")";
        String oglasi = "CREATE TABLE Oglasi " +
                "(" +
                "oglasID INT NOT NULL AUTO_INCREMENT PRIMARY KEY," +
                "vlasnikID INT," +
                "lokacijaID INT," +
                "kategorijaID INT," +
                "datumKreiranja DATE," +
                "nazivOglasa VARCHAR(2000)," +
                "opisOglasa VARCHAR(5000)," +
                "iskustvo BIT,"+
                "radOdKuce BIT," +
                "CONSTRAINT FK_lokacijaID_2 FOREIGN KEY (lokacijaID) REFERENCES Lokacija(lokacijaID)," +
                "CONSTRAINT FK_kategorijaID FOREIGN KEY (kategorijaID) REFERENCES Kategorija_Oglasa(kategorijaID)," +
                "CONSTRAINT FK_vlasnikID FOREIGN KEY (vlasnikID) REFERENCES Korisnik(korisnikID)" +
                ")";
        String oglas_vestine = "CREATE TABLE Oglas_Vestine" +
                "(" +
                "oglasID INT," +
                "vestinaID INT," +
                "PRIMARY KEY(oglasID, vestinaID)," +
                "CONSTRAINT FK_oglasID_1 FOREIGN KEY(oglasID) REFERENCES Oglasi(oglasID)," +
                "CONSTRAINT FK_vestinaID_2 FOREIGN KEY(vestinaID) REFERENCES Vestine(vestinaID)" +
                ")";
        String oglas_edukacija = "CREATE TABLE Oglas_Edukacija " +
                "(" +
                "oglasID INT," +
                "diplomaID INT," +
                "PRIMARY KEY(oglasID, diplomaID)," +
                "CONSTRAINT FK_oglasID_2 FOREIGN KEY (oglasID) REFERENCES Oglasi(oglasID)," +
                "CONSTRAINT FK_diplomaID_2 FOREIGN KEY (diplomaID) REFERENCES Diplome(diplomaID)" +
                ")";
        String aplikacija = "CREATE TABLE Aplikacija " +
                "(" +
                "aplikacijaID INT NOT NULL AUTO_INCREMENT PRIMARY KEY," +
                "korisnikID INT," +
                "oglasID INT," +
                "porukaPoslodavca VARCHAR(2000)," +
                "primljen BIT," +
                "sakrijVlasnik BIT," +
                "sakrijUser BIT," +
                "CONSTRAINT FK_korisnikID_5 FOREIGN KEY (korisnikID) REFERENCES Korisnik(korisnikID)," +
                "CONSTRAINT FK_oglasID_4 FOREIGN KEY (oglasID) REFERENCES Oglasi(oglasID)" +
                ")";
        String pregled_oglasa="CREATE TABLE Pregled_Oglasa" +
                "(" +
                "korisnikID INT," +
                "oglasID INT," +
                "lajk BIT," +
                "PRIMARY KEY(korisnikID, oglasID)," +
                "CONSTRAINT FK_korisnikID_6 FOREIGN KEY (korisnikID) REFERENCES Korisnik(korisnikID)," +
                "CONSTRAINT FK_oglasID_4 FOREIGN KEY (oglasID) REFERENCES Oglasi(oglasID)" +
                ")";

        try {
            stmt.executeUpdate(diplome);
            stmt.executeUpdate(kategorija_oglasa);
            stmt.executeUpdate(prijava);
            stmt.executeUpdate(lokacija);
            stmt.executeUpdate(korisnik);
            stmt.executeUpdate(vestine);
            stmt.executeUpdate(korisnik_vestine);
            stmt.executeUpdate(korisnik_edukacija);
            stmt.executeUpdate(oglas_vestine);
            stmt.executeUpdate(oglasi);
            stmt.executeUpdate(oglas_edukacija);
            stmt.executeUpdate(aplikacija);
            stmt.executeUpdate(pregled_oglasa);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Tables created.");
        popuniPocetneBaze();
    }

    public void popuniPocetneBaze()
    {
        String[] vestine ={"INSERT INTO Vestine(naziv) values ('Java')",
                "INSERT INTO Vestine(naziv) values ('C')" ,
                "INSERT INTO Vestine(naziv) values ('Python')\n" ,
                "INSERT INTO Vestine(naziv) values ('Html')\n" ,
                "INSERT INTO Vestine(naziv) values ('Css')\n" ,
                "INSERT INTO Vestine(naziv) values ('MySQL')\n" ,
                "INSERT INTO Vestine(naziv) values ('Digital Arts')\n" ,
                "INSERT INTO Vestine(naziv) values ('Photoshop')\n" ,
                "INSERT INTO Vestine(naziv) values ('Illustrator')\n" ,
                "INSERT INTO Vestine(naziv) values ('R')\n" ,
                "INSERT INTO Vestine(naziv) values ('Excel')\n" ,
                "INSERT INTO Vestine(naziv) values ('Word')\n" ,
                "INSERT INTO Vestine(naziv) values ('PowerPoint')\n" ,
                "INSERT INTO Vestine(naziv) values ('Kuvanje')\n" ,
                "INSERT INTO Vestine(naziv) values ('Poznavanje osnova elektrotehnike')\n" ,
                "INSERT INTO Vestine(naziv) values ('Napredno poznavanje elektrotehnickih principa')\n" ,
                "INSERT INTO Vestine(naziv) values ('Maser')\n",
                "INSERT INTO Vestine(naziv) values ('Poznavanje osnova ekonomije')\n" ,
                "INSERT INTO Vestine(naziv) values ('Napredno poznavanje ekonomskih principa')\n" ,
                "INSERT INTO Vestine(naziv) values ('Poznavanje racunovodstvenih praksi')\n" ,
                "INSERT INTO Vestine(naziv) values ('Poznavanje medicinskih praksi')\n" ,
                "INSERT INTO Vestine(naziv) values ('Napredno znanje iz oblasti farmakologije')\n" ,
                "INSERT INTO Vestine(naziv) values ('Napredno upravljanje pesticidima')\n" ,
                "INSERT INTO Vestine(naziv) values ('Marketing')\n" ,
                "INSERT INTO Vestine(naziv) values ('Poznavanje finansijskih principa')\n" ,
                "INSERT INTO Vestine(naziv) values ('Eksperimentalni rad')"};

        String[] lokacije={"INSERT INTO Lokacija(drzava,grad) values ('Srbija','Azanja')",
                "INSERT INTO Lokacija(drzava,grad) values ('Srbija','Beograd')",
                "INSERT INTO Lokacija (drzava,grad) values ('Srbija','Novi Sad')",
                "INSERT INTO Lokacija (drzava,grad) values ('Srbija','Kragujevac')",
                "INSERT INTO Lokacija (drzava,grad)values ('Srbija','Topola')",
                "INSERT INTO Lokacija (drzava,grad) values  ('Srbija','Jagodina')",
                "INSERT INTO Lokacija (drzava,grad) values ('Srbija','Nis')",
                "INSERT INTO Lokacija (drzava,grad) values ('Srbija','Raca')",
                "INSERT INTO Lokacija (drzava,grad) values ('Srbija','Smederevska Palanka')",
                "INSERT INTO Lokacija (drzava,grad) values ('Srbija','Kraljevo')",
                "INSERT INTO Lokacija (drzava,grad) values ('Srbija','Krusevac')",
                "INSERT INTO Lokacija (drzava,grad) values ('Srbija','Cacak')",
                "INSERT INTO Lokacija (drzava,grad) values ('Srbija','Novi Pazar')",
                "INSERT INTO Lokacija (drzava,grad) values ('Srbija','Subotica')",
                "INSERT INTO Lokacija (drzava,grad) values ('Srbija','Zrenjanin')",
                "INSERT INTO Lokacija (drzava,grad) values ('Bosna i Hercegovina','Sarajevo')",
                "INSERT INTO Lokacija (drzava,grad) values ('Bosna i Hercegovina','Banja Luka')",
                "INSERT INTO Lokacija (drzava,grad) values ('Bosna i Hercegovina','Mostar')",
                "INSERT INTO Lokacija (drzava,grad) values ('Bosna i Hercegovina','Trebinje')",
                "INSERT INTO Lokacija (drzava,grad) values ('Bosna i Hercegovina','Tuzla')",
                "INSERT INTO Lokacija (drzava,grad) values ('Crna Gora','Podgorica')",
                "INSERT INTO Lokacija (drzava,grad) values ('Crna Gora','Cetinje')",
                "INSERT INTO Lokacija (drzava,grad) values ('Crna Gora','Ulcinj')",
                "INSERT INTO Lokacija (drzava,grad) values ('Crna Gora','Bar')",
                "INSERT INTO Lokacija (drzava,grad) values ('Crna Gora','Kotor')",
                "INSERT INTO Lokacija (drzava,grad) values ('Makedonija','Skoplje')",
                "INSERT INTO Lokacija (drzava,grad) values ('Makedonija','Ohrid')",
                "INSERT INTO Lokacija (drzava,grad) values ('Makedonija','Stip')",
                "INSERT INTO Lokacija (drzava,grad) values ('Hrvatska','Zagreb')",
                "INSERT INTO Lokacija (drzava,grad) values ('Hrvatska','Zadar')",
                "INSERT INTO Lokacija (drzava,grad) values ('Hrvatska','Osijek')",
                "INSERT INTO Lokacija (drzava,grad) values ('Hrvatska','Hvar')",
                "INSERT INTO Lokacija (drzava,grad) values ('SAD','Dallas')",
                "INSERT INTO Lokacija (drzava,grad) values ('SAD','Denver')",
                "INSERT INTO Lokacija (drzava,grad) values ('SAD','Los Angeles')",
                "INSERT INTO Lokacija (drzava,grad) values ('SAD','San Diego')",
                "INSERT INTO Lokacija (drzava,grad) values ('SAD','New York')",
                "INSERT INTO Lokacija (drzava,grad) values ('SAD','Boston')",
                "INSERT INTO Lokacija (drzava,grad) values ('Rusija','Moskva')",
                "INSERT INTO Lokacija (drzava,grad) values ('Rusija','Soci')",
                "INSERT INTO Lokacija (drzava,grad) values ('Rusija','Vladivostok')",
                "INSERT INTO Lokacija (drzava,grad) values ('Rusija','Kazanj')",
                "INSERT INTO Lokacija (drzava,grad) values ('Rusija','Murmansk')",
                "INSERT INTO Lokacija (drzava,grad) values ('Nemacka','Berlin')",
                "INSERT INTO Lokacija (drzava,grad) values ('Nemacka','Frankfurt')",
                "INSERT INTO Lokacija (drzava,grad) values ('Nemacka','Dortmund')",
                "INSERT INTO Lokacija (drzava,grad) values ('Nemacka','Stutgart')",
                "INSERT INTO Lokacija (drzava,grad) values ('Nemacka','Minhen')",
                "INSERT INTO Lokacija (drzava,grad) values ('Nemacka','Keln')"};

        String[] diplome={"INSERT INTO Diplome (zvanje, nazivSkole,tipDiplome) VALUES ('Diplomirani Programer','PMF','Fakultet')\n",
                "INSERT INTO Diplome (zvanje, nazivSkole,tipDiplome) VALUES ('Diplomirani Matematicar','PMF','Fakultet')\n",
                "INSERT INTO Diplome (zvanje, nazivSkole,tipDiplome) VALUES ('Diplomirani Farmaceut','Medicinski Fakultet','Fakultet')\n",
                "INSERT INTO Diplome (zvanje, nazivSkole,tipDiplome) VALUES ('Diplomirani Fizioterapeut','Visa Medicinska Skola','Visa Skola')\n",
                "INSERT INTO Diplome (zvanje, nazivSkole,tipDiplome) VALUES ('Diplomirani Ekonomista','EKOF','Fakultet')\n",
                "INSERT INTO Diplome (zvanje, nazivSkole,tipDiplome) VALUES ('Diplomirani Turizmolog','GEF','Fakultet')\n",
                "INSERT INTO Diplome (zvanje, nazivSkole,tipDiplome) VALUES ('Diplomirani Hotelijerski menadzer','Univerzitet Singidunum','Fakultet')\n",
                "INSERT INTO Diplome (zvanje, nazivSkole,tipDiplome) VALUES ('Diplomirani Biolog','PMF','Fakultet')\n",
                "INSERT INTO Diplome (zvanje, nazivSkole,tipDiplome) VALUES ('Diplomirani Hemicar','PMF','Fakultet')\n",
                "INSERT INTO Diplome (zvanje, nazivSkole,tipDiplome) VALUES ('Diplomirani Ekolog','VISAR','Visa skola')\n",
                "INSERT INTO Diplome (zvanje, nazivSkole,tipDiplome) VALUES ('Diplomirani Fizicar','PMF','Fakultet')\n",
                "INSERT INTO Diplome (zvanje, nazivSkole,tipDiplome) VALUES ('Diplomirani Masinski inzenjer','FIN','Fakultet')\n",
                "INSERT INTO Diplome (zvanje, nazivSkole,tipDiplome) VALUES ('Diplomirani Geograf','GEF','Fakultet')\n",
                "INSERT INTO Diplome (zvanje, nazivSkole,tipDiplome) VALUES ('Diplomirani Prehrambeni tehnolog','Poljoprivredni Fakultet','Fakultet')\n",
                "INSERT INTO Diplome (zvanje, nazivSkole,tipDiplome) VALUES ('Diplomirani Ekonomista','Visa Poslovna Skola','Visa Skola')\n",
                "INSERT INTO Diplome (zvanje, nazivSkole,tipDiplome) VALUES ('Diplomirani Komercijalista','EUS Slobodan Minic','Srednja Skola')\n",
                "INSERT INTO Diplome (zvanje, nazivSkole,tipDiplome) VALUES ('Diplomirani Kuvar','EUS Slobodan Minic','Srednja Skola')\n",
                "INSERT INTO Diplome (zvanje, nazivSkole,tipDiplome) VALUES ('Diplomirani Maser','Medicinska skola Zvezdara','Srednja Skola')\n",
                "INSERT INTO Diplome (zvanje, nazivSkole,tipDiplome) VALUES ('Diplomirani Vaspitac','Medicinska skola Sestre Ninkovic','Srednja Skola')\n",
                "INSERT INTO Diplome (zvanje, nazivSkole,tipDiplome) VALUES ('Diplomirani Veterinar','Srednja skola Kralj Petar I','Srednja Skola')\n",
                "INSERT INTO Diplome (zvanje, nazivSkole,tipDiplome) VALUES ('Diplomirani Elektricar','ES Rade Koncar','Srednja Skola')\n",
                "INSERT INTO Diplome (zvanje, nazivSkole,tipDiplome) VALUES ('Diplomirani Laborant','Medicinska skola Sestre Ninkovic','Srednja Skola')\n",
                "INSERT INTO Diplome (zvanje, nazivSkole,tipDiplome) VALUES ('Diplomirani Autoelektricar','Srednja skola Kralj Petar I','Srednja Skola')\n",
                "INSERT INTO Diplome (zvanje, nazivSkole,tipDiplome) VALUES ('Diplomirani Zubni-tehnicar','Medicinska skola Sestre Ninkovic','Srednja Skola')",
                "INSERT INTO Diplome (zvanje, nazivSkole,tipDiplome) VALUES ('Diplomirani Poljprivredni tehnicar','Srednja skola Svilajnac','Srednja Skola')"};

        String[] kategorije={"INSERT INTO kategorija_oglasa(naziv) VALUES('Accounting & Consulting')",
                "INSERT INTO kategorija_oglasa(naziv) VALUES('Admin Support')",
                "INSERT INTO kategorija_oglasa(naziv) VALUES('Customer Service')",
                "INSERT INTO kategorija_oglasa(naziv) VALUES('Data Science & Analytics')",
                "INSERT INTO kategorija_oglasa(naziv) VALUES('Design & Creative')",
                "INSERT INTO kategorija_oglasa(naziv) VALUES('Engineering & Architecture')",
                "INSERT INTO kategorija_oglasa(naziv) VALUES('IT & Networking')",
                "INSERT INTO kategorija_oglasa(naziv) VALUES('Legal')",
                "INSERT INTO kategorija_oglasa(naziv) VALUES('Sales & Marketing')",
                "INSERT INTO kategorija_oglasa(naziv) VALUES('Translation')",
                "INSERT INTO kategorija_oglasa(naziv) VALUES('Web, Mobile & Software Dev')",
                "INSERT INTO kategorija_oglasa(naziv) VALUES('Writing')"};

        try {
            for (String upit:vestine) {
                stmt.executeQuery(upit);
            }
            for (String upit:lokacije) {
                stmt.executeQuery(upit);
            }
            for (String upit:diplome) {
                stmt.executeQuery(upit);
            }
            for (String upit:kategorije) {
                stmt.executeQuery(upit);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ResultSet executeCustom(String sql)
    {
        ResultSet rs=null;
        try {
            rs = stmt.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            assert rs != null;
            if(rs.next())
                return rs;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ResultSet popuniListuOglasa(String search,int[] skills,int[] lokacija,int[] kategorija) throws SQLException {
        sql="SELECT * FROM oglasi o WHERE nazivOglasa LIKE '%"+search+"%'";
        if(lokacija[0]!=-1)
        {
            sql+=" AND ";
            for (int i=0;i<lokacija.length-1;i++) {
                sql+="lokacijaID="+lokacija[i]+" OR ";
            }
            sql+="lokacijaID="+lokacija[lokacija.length-1];
        }
        if(kategorija[0]!=-1)
        {
            sql+=" AND ";
            for (int i=0;i<kategorija.length-1;i++) {
                sql+="kategorijaID="+kategorija[i]+" OR ";
            }
            sql+="kategorijaID="+kategorija[kategorija.length-1];
        }
        String vestine="";
        if (skills[0]!=-1) {
            for (int i=0;i<skills.length-1;i++) {
                vestine+="vestinaID="+skills[i]+" OR ";
            }
            vestine+="vestinaID="+skills[skills.length-1];
            sql+=" AND EXISTS (SELECT oglasID FROM oglas_vestine WHERE "+vestine+" AND o.oglasID=oglasID)";
        }
        sql+=" ORDER BY oglasID DESC";
        ResultSet rs = null;
        try {
            rs = stmt.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rs;
    }

    public String vratiUsername(String username)
    {
        String userReturn=null;
        String sql="SELECT * FROM prijava WHERE imeKorisnika='" + username + "'";
        try {
            ResultSet rs = stmt.executeQuery(sql);
            if(rs.first())
            {
                userReturn=rs.getString("imeKorisnika");
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return userReturn;
    }

    public String vratiUsernameID(int id)
    {
        String userReturn=null;
        String sql="SELECT * FROM prijava WHERE korisnikID=" + id + "";
        try {
            ResultSet rs = stmt.executeQuery(sql);
            if(rs.first())
            {
                userReturn=rs.getString("imeKorisnika");
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return userReturn;
    }

    public String vratiPassword(String username) {
        String passwordReturn=null;
        String sql="SELECT * FROM prijava WHERE imeKorisnika='" + username + "'";
        try {
            ResultSet rs = stmt.executeQuery(sql);
            if(rs.first())
            {
                passwordReturn=rs.getString("sifraKorisnika");
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return passwordReturn;
    }

    public int vratiUserID(String username)
    {
        int korisnikID=0;
        String sql="SELECT * FROM prijava WHERE imeKorisnika='" + username + "'";
        try{
            ResultSet rs = stmt.executeQuery(sql);
            if(rs.first())
            {
                korisnikID=rs.getInt("korisnikID");
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return  korisnikID;
    }

    public int roleKorisnika(int korisnikID){
        int role=-1;
        String sql="SELECT * FROM korisnik WHERE korisnikID=" + korisnikID;
        try{
            ResultSet rs = stmt.executeQuery(sql);
            if(rs.first())
            {
                role=rs.getInt("tipKorisnika");
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return  role;
    }

    public int registrujKorisnika(String ime, String prezime, String datumRodjenja, int pol, String username, String password, String email, String mobilni, int lokacijaID, String bio, int role, String avatar){
        String sql="INSERT INTO korisnik(ime,prezime,datumRodjenja,email,pol,mobilni,profilnaSlikaURL,bio,lokacijaID,tipKorisnika)\n" +
                "VALUES('"+ime+"','"+prezime+"','"+datumRodjenja+"','"+email+"',"+pol+",'"+mobilni+"','"+avatar+"','"+bio+"',"+lokacijaID+","+role+")" +
                " RETURNING korisnikID AS id";
        int id=0;
        try {
            ResultSet rs = stmt.executeQuery(sql);
            if(rs.first()) {
                id = rs.getInt("id");
                String sql1="INSERT INTO prijava VALUES("+id+",'"+password+"','"+username+"')";
                stmt.executeQuery(sql1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return id;
    }

    public void updateKorisnika(int id, String ime, String prezime, String datumRodjenja, int pol, String username, String password, String email, String mobilni, int lokacijaID, String bio, String avatar, int[] skills, int[] edukacija){
        String sql="UPDATE korisnik " +
                "SET ime='"+ime+"',prezime='"+prezime+"',datumRodjenja='"+datumRodjenja+"',email='"+email+"',pol="+pol+",mobilni='"+mobilni+"',profilnaSlikaURL='"+avatar+"',bio='"+bio+"',lokacijaID="+lokacijaID+" "+
                "WHERE korisnikID="+id;
        String sql1="UPDATE prijava SET imeKorisnika='"+username+"',sifraKorisnika='"+Security.generatePassword(password)+"' WHERE korisnikID="+id;
        String izbrisiIzVestina = "DELETE FROM korisnik_vestine WHERE korisnikID="+id;
        String izbrisiIzEdukacija = "DELETE FROM korisnik_edukacija WHERE korisnikID="+id;
        try {
           stmt.executeQuery(sql);
           stmt.executeQuery(sql1);
           stmt.executeQuery(izbrisiIzVestina);
           stmt.executeQuery(izbrisiIzEdukacija);
            if(edukacija[0]!=-1) {
                dodajEdukacijuKorisnik(id, edukacija);
            }
            if(skills[0]!=-1) {
                dodajSkillKorisnik(id, skills);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int dodajOglas(int vlasnikID, int lokacijaID, int kategorijaID, String datumKreiranja, String naziv, String opis,boolean radOdKuce, boolean iskustvo){
        String sql="INSERT INTO oglasi(vlasnikID,lokacijaID,kategorijaID,datumKreiranja,nazivOglasa,opisOglasa,iskustvo,radOdKuce)\n" +
                "VALUES('"+vlasnikID+"',"+lokacijaID+","+kategorijaID+",'"+datumKreiranja+"','"+naziv+"','"+opis+"',"+iskustvo+","+radOdKuce+")" +
                "RETURNING oglasID AS id";
        int id=0;
        try {
            ResultSet rs = stmt.executeQuery(sql);
            if(rs.first()) {
                id = rs.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return id;
    }

    public void updateOglas(int id, int lokacijaID, int kategorijaID, String naziv, String opis,boolean radOdKuce, boolean iskustvo,int[] skills, int[] edukacija){
        String sql="UPDATE oglasi " +
                "SET lokacijaID="+lokacijaID+",kategorijaID="+kategorijaID+",nazivOglasa='"+naziv+"',opisOglasa='"+opis+"',iskustvo="+iskustvo+",radOdKuce="+radOdKuce+" " +
                "WHERE oglasID="+id;
        String izbrisiIzVestina = "DELETE FROM oglas_vestine WHERE oglasID="+id;
        String izbrisiIzEdukacija = "DELETE FROM oglas_edukacija WHERE oglasID="+id;
        try {
            stmt.executeQuery(sql);
            stmt.executeQuery(izbrisiIzVestina);
            stmt.executeQuery(izbrisiIzEdukacija);
            if(edukacija[0]!=-1) {
                dodajEdukacijuOglas(id, edukacija);
            }
            if(skills[0]!=-1) {
                dodajSkillOglas(id, skills);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<String> vratiSkillsOglas(int oglasID)
    {
        ArrayList<String> skills=new ArrayList<String>();
        String upit="SELECT v.naziv FROM oglas_vestine ov JOIN vestine v ON ov.vestinaID=v.vestinaID WHERE ov.oglasID="+oglasID;
        try {
                ResultSet rs = stmt.executeQuery(upit);
                while (rs.next()) {
                    skills.add(rs.getString("naziv"));
                }
        }  catch (SQLException e) {
            e.printStackTrace();
        }
        return skills;
    }

    public ArrayList<String> vratiEduOglas(int oglasID)
    {
        ArrayList<String> edukacija=new ArrayList<String>();
        String upit="SELECT * FROM oglas_edukacija oe JOIN diplome d ON oe.diplomaID=d.diplomaID WHERE oe.oglasID="+oglasID;
        try {
            ResultSet rs = stmt.executeQuery(upit);
                while (rs.next()) {
                    edukacija.add(rs.getString("zvanje") + ", " + rs.getString("nazivSkole"));
                }
        }  catch (SQLException e) {
            e.printStackTrace();
        }
        return edukacija;
    }

    public ArrayList<String> vratiSkillsKorisnik(int korisnikID) {
        ArrayList<String> skills = new ArrayList<String>();
        String upit = "SELECT v.naziv FROM korisnik_vestine kv JOIN vestine v ON kv.vestinaID=v.vestinaID WHERE kv.korisnikID=" + korisnikID;
        try {
            ResultSet rs = stmt.executeQuery(upit);
            while (rs.next()) {
                skills.add(rs.getString("naziv"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return skills;
    }

    public ArrayList<String> vratiEduKorisnik(int korisnikID) {
        ArrayList<String> edukacija = new ArrayList<String>();
        String upit = "SELECT * FROM korisnik_edukacija ke JOIN diplome d ON ke.diplomaID=d.diplomaID WHERE ke.korisnikID=" + korisnikID;
        try {
            ResultSet rs = stmt.executeQuery(upit);
            while (rs.next()) {
                edukacija.add(rs.getString("zvanje") + ", " + rs.getString("nazivSkole"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return edukacija;
    }

    public String lokacija(int ID)
    {
        String lokacija="";
        String sql="SELECT * FROM lokacija WHERE lokacijaID="+ID;
        try {
            ResultSet rs = stmt.executeQuery(sql);
            if(rs.first())
            {
                lokacija=rs.getString("grad")+", "+rs.getString("drzava");
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return lokacija;
    }

    public void izbrisiOglas(int oglasID)
    {
        String izbrisiIzOglasa = "DELETE FROM Oglasi WHERE oglasID="+oglasID;
        String izbrisiIzVestina = "DELETE FROM oglas_vestine WHERE oglasID="+oglasID;
        String izbrisiIzEdukacija = "DELETE FROM oglas_edukacija WHERE oglasID="+oglasID;
        String izbrisiIzPregleda = "DELETE FROM pregled_oglasa WHERE oglasID="+oglasID;
        String izbrisiIzAplikacija = "DELETE FROM aplikacija WHERE oglasID="+oglasID;
        try {
            stmt.executeQuery(izbrisiIzOglasa);
            stmt.executeQuery(izbrisiIzVestina);
            stmt.executeQuery(izbrisiIzEdukacija);
            stmt.executeQuery(izbrisiIzPregleda);
            stmt.executeQuery(izbrisiIzAplikacija);
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    public void izbrisiKorisnika(int korisnikID)
    {
        String izbrisiIzOglasa = "DELETE FROM Korisnik WHERE korisnikID="+korisnikID;
        String izbrisiIzVestina = "DELETE FROM korisnik_vestine WHERE korisnikID="+korisnikID;
        String izbrisiIzEdukacija = "DELETE FROM korisnik_edukacija WHERE korisnikID="+korisnikID;
        String izbrisiIzPregleda = "DELETE FROM pregled_oglasa WHERE korisnikID="+korisnikID;
        String izbrisiIzPrijave = "DELETE FROM prijava WHERE korisnikID="+korisnikID;
        String izbrisiIzAplikacija = "DELETE FROM aplikacija WHERE korisnikID="+korisnikID;
        try {
            stmt.executeQuery(izbrisiIzOglasa);
            stmt.executeQuery(izbrisiIzVestina);
            stmt.executeQuery(izbrisiIzEdukacija);
            stmt.executeQuery(izbrisiIzPregleda);
            stmt.executeQuery(izbrisiIzPrijave);
            stmt.executeQuery(izbrisiIzAplikacija);
            String sql = "SELECT * FROM oglasi WHERE vlasnikID="+korisnikID;
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next())
            {
                izbrisiOglas(rs.getInt("oglasID"));
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    public void posaljiAplikaciju(int cookieId, int idOglasa, String message)
    {
        String sql="INSERT INTO Aplikacija(korisnikID,oglasID,porukaPoslodavca,sakrijVlasnik,sakrijUser) VALUES ("+cookieId+","+idOglasa+",\""+message+"\",0,0)";
        try{
            stmt.executeQuery(sql);
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public boolean prijavioAplikaciju(int cookieId, int oglasID)
    {
        String sql = "SELECT korisnikID,oglasID FROM Aplikacija WHERE korisnikID="+cookieId+" AND oglasID="+oglasID;
        boolean prijavio=false;
        try {
            ResultSet rs = stmt.executeQuery(sql);
            if(rs.first()) {
                prijavio = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return prijavio;
    }

    public int brojPregleda(int oglasID)
    {
        String sql = "SELECT COUNT(*) AS broj FROM pregled_oglasa WHERE oglasID="+oglasID;
        int broj=0;
        try {
            ResultSet rs = stmt.executeQuery(sql);
            if(rs.first())
            {
                broj=rs.getInt("broj");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return broj;
    }

    public int brojLajkova(int oglasID)
    {
        String sql = "SELECT COUNT(lajk) AS broj FROM pregled_oglasa WHERE oglasID="+oglasID+" AND lajk=1";
        int broj=0;
        try {
            ResultSet rs = stmt.executeQuery(sql);
            if(rs.first())
            {
                broj=rs.getInt("broj");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return broj;
    }

    public void dodajLajk(int oglasID,int userID)
    {
        String sql = "UPDATE pregled_oglasa SET lajk=1 WHERE oglasID="+oglasID+" AND korisnikID="+userID;
        try {
            stmt.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean lajkovao(int oglasID,int userID)
    {
        String sql = "SELECT COUNT(*) as broj FROM pregled_oglasa WHERE lajk=1 AND oglasID="+oglasID+" AND korisnikID="+userID;
        int broj=0;
        try {
            ResultSet rs = stmt.executeQuery(sql);
            if(rs.first())
            {
                broj=rs.getInt("broj");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println(broj);
        if(broj==1)
        {
            return true;
        }
        else {
            return false;
        }
    }

    public void skiniLajk(int oglasID,int userID)
    {
        String sql = "UPDATE pregled_oglasa SET lajk=0 WHERE oglasID="+oglasID+" AND korisnikID="+userID;
        int broj=0;
        try {
            stmt.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String kategorija(int kategorijaID)
    {
        String sql = "SELECT * FROM kategorija_oglasa WHERE kategorijaID="+kategorijaID;
        String kategorija="";
        try {
            ResultSet rs = stmt.executeQuery(sql);
            if(rs.first())
            {
                kategorija=rs.getString("naziv");
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return kategorija;
    }

    public String vratiListuSkillsKorisnik(int idKorinsika) {
        ArrayList<String> selected=vratiSkillsKorisnik(idKorinsika);
        String sql = "SELECT * FROM vestine";
        String skills="";
        boolean ind;
        try {
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next())
            {
                ind=true;
                for (String skill:selected) {
                    if(Objects.equals(rs.getString("naziv"), skill))
                    {
                        skills+="<option selected value=\""+rs.getInt("vestinaID")+"\"> "+rs.getString("naziv")+"</option>";
                        ind=false;
                    }
                }
                if(ind) {
                    skills += "<option value=\"" + rs.getInt("vestinaID") + "\"> " + rs.getString("naziv") + "</option>";
                }
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return skills;
    }

    public String vratiListuSkillsOglas(int idOglasa) {
        ArrayList<String> selected=vratiSkillsOglas(idOglasa);
        String sql = "SELECT * FROM vestine";
        String skills="";
        boolean ind;
        try {
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next())
            {
                ind=true;
                for (String skill:selected) {
                    if(Objects.equals(rs.getString("naziv"), skill))
                    {
                        skills+="<option selected value=\""+rs.getInt("vestinaID")+"\"> "+rs.getString("naziv")+"</option>";
                        ind=false;
                    }
                }
                if(ind) {
                    skills += "<option value=\"" + rs.getInt("vestinaID") + "\"> " + rs.getString("naziv") + "</option>";
                }
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return skills;
    }

    public String vratiListuSkills() {
        String sql = "SELECT * FROM vestine";
        String skills="";
        try {
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next())
            {
                    skills += "<option value=\"" + rs.getInt("vestinaID") + "\"> " + rs.getString("naziv") + "</option>";
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return skills;
    }

    public String vratiListuSkillsFilter(int[] skillovi) {
        String sql = "SELECT * FROM vestine";
        String skills="";
        boolean ind;
        try {
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next())
            {
                ind=true;
                for (int skill:skillovi) {
                    if(rs.getInt("vestinaID")==skill)
                    {
                        skills+="<option selected value=\""+rs.getInt("vestinaID")+"\"> "+rs.getString("naziv")+"</option>";
                        ind=false;
                    }
                }
                if(ind) {
                    skills += "<option value=\"" + rs.getInt("vestinaID") + "\"> " + rs.getString("naziv") + "</option>";
                }
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return skills;
    }

    public String vratiListuKategorija() {
        String sql = "SELECT * FROM kategorija_oglasa";
        String kategorije="";
        try {
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next())
            {
                kategorije+="<option value=\""+rs.getInt("kategorijaID")+"\"> "+rs.getString("naziv")+"</option>";
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return kategorije;
    }

    public String vratiListuKategorijaOglas(int idKategorija) {
        String sql = "SELECT * FROM kategorija_oglasa";
        String kategorije="";
        try {
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()) {
                if (rs.getInt("kategorijaID") == idKategorija) {
                    kategorije += "<option selected value=\"" + rs.getInt("kategorijaID") + "\"> " + rs.getString("naziv") + "</option>";
                }
                else {
                    kategorije += "<option value=\"" + rs.getInt("kategorijaID") + "\"> " + rs.getString("naziv") + "</option>";
                }
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return kategorije;
    }

    public String vratiListuKategorijaFilter(int[] kategorija) {
        String sql = "SELECT * FROM kategorija_oglasa";
        String kategorije="";
        boolean ind;
        try {
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()) {
                ind=true;
                for (int kat:kategorija) {
                    if (rs.getInt("kategorijaID") == kat) {
                        kategorije += "<option selected value=\"" + rs.getInt("kategorijaID") + "\"> " + rs.getString("naziv") + "</option>";
                        ind=false;
                    }
                }
                if(ind) {
                    kategorije += "<option value=\"" + rs.getInt("kategorijaID") + "\"> " + rs.getString("naziv") + "</option>";
                }
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return kategorije;
    }

    public ArrayList<String> vratiListuDrzava() {
        String sql = "SELECT DISTINCT drzava FROM lokacija";
        ArrayList<String> drzave=new ArrayList<String>();
        try {
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next())
            {
                drzave.add(rs.getString("drzava"));
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return drzave;
    }

    public String vratiListuGradova(String drzava) {
        String sql = "SELECT * FROM lokacija WHERE drzava='"+drzava+"'";
        String gradovi="";
        try {
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next())
            {
                gradovi+="<option value=\""+rs.getInt("lokacijaID")+"\"> "+rs.getString("grad")+"</option>";
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return gradovi;
    }

    public String vratiListuGradovaKorisnik(String drzava,String lokacija) {
        String sql = "SELECT * FROM lokacija WHERE drzava='"+drzava+"'";
        String gradovi="";
        try {
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next())
            {

                if(Objects.equals(rs.getString("grad")+", "+rs.getString("drzava"), lokacija)) {
                    gradovi += "<option selected value=\""+rs.getInt("lokacijaID")+"\"> "+rs.getString("grad")+"</option>";
                }
                else {
                    gradovi+="<option value=\""+rs.getInt("lokacijaID")+"\"> "+rs.getString("grad")+"</option>";
                }
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return gradovi;
    }

    public String vratiListuGradovaOglas(String drzava,String lokacija) {
        String sql = "SELECT * FROM lokacija WHERE drzava='"+drzava+"'";
        String gradovi="";
        try {
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next())
            {

                if(Objects.equals(rs.getString("grad")+", "+rs.getString("drzava"), lokacija)) {
                    gradovi += "<option selected value=\""+rs.getInt("lokacijaID")+"\"> "+rs.getString("grad")+"</option>";
                }
                else {
                    gradovi+="<option value=\""+rs.getInt("lokacijaID")+"\"> "+rs.getString("grad")+"</option>";
                }
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return gradovi;
    }

    public String vratiListuGradovaFilter(String drzava,int[] lokacija) {
        String sql = "SELECT * FROM lokacija WHERE drzava='"+drzava+"'";
        String gradovi="";
        boolean ind;
        try {
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next())
            {
                ind=true;
                for (int lok:lokacija) {
                    if(lok==rs.getInt("lokacijaID"))
                    {
                        gradovi+="<option selected value=\""+rs.getInt("lokacijaID")+"\"> "+rs.getString("grad")+"</option>";
                        ind=false;
                    }
                }
                if(ind) {
                    gradovi+="<option value=\""+rs.getInt("lokacijaID")+"\"> "+rs.getString("grad")+"</option>";
                }
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return gradovi;
    }

    public ArrayList<String> vratiTipoveSkola() {
        String sql = "SELECT DISTINCT tipDiplome FROM diplome";
        ArrayList<String> skole=new ArrayList<String>();
        try {
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next())
            {
                skole.add(rs.getString("tipDiplome"));
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return skole;
    }

    public ArrayList<String> vratiListuSkola(String skola) {
        String sql = "SELECT DISTINCT nazivSkole FROM diplome WHERE tipDiplome='"+skola+"'";
        ArrayList<String> skole=new ArrayList<String>();
        try {
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next())
            {
                skole.add(rs.getString("nazivSkole"));
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return skole;
    }

    public String vratiListuZvanja(String nazivSkole) {
        String sql = "SELECT * FROM diplome WHERE nazivSkole='"+nazivSkole+"'";
        String zvanja="";
        try {
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next())
            {
                zvanja+="<option value=\""+rs.getInt("diplomaID")+"\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "+rs.getString("zvanje")+"</option>";
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return zvanja;
    }

    public String vratiListuZvanjaKorisnik(String nazivSkole,int id) {
        ArrayList<String> selected=vratiEduKorisnik(id);
        String sql = "SELECT * FROM diplome WHERE nazivSkole='"+nazivSkole+"'";
        String zvanja="";
        boolean ind;
        try {
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next())
            {
                ind=true;
                for (String edu:selected) {
                    if(Objects.equals(rs.getString("zvanje")+", "+rs.getString("nazivSkole"), edu))
                    {
                        zvanja+="<option selected value=\""+rs.getInt("diplomaID")+"\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "+rs.getString("zvanje")+"</option>";
                        ind=false;
                    }
                }
                if(ind) {
                    zvanja += "<option value=\""+rs.getInt("diplomaID")+"\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "+rs.getString("zvanje")+"</option>";
                }
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return zvanja;
    }

    public String vratiListuZvanjaOglas(String nazivSkole,int id) {
        ArrayList<String> selected=vratiEduOglas(id);
        String sql = "SELECT * FROM diplome WHERE nazivSkole='"+nazivSkole+"'";
        String zvanja="";
        boolean ind;
        try {
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next())
            {
                ind=true;
                for (String edu:selected) {
                    if(Objects.equals(rs.getString("zvanje")+", "+rs.getString("nazivSkole"), edu))
                    {
                        zvanja+="<option selected value=\""+rs.getInt("diplomaID")+"\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "+rs.getString("zvanje")+"</option>";
                        ind=false;
                    }
                }
                if(ind) {
                    zvanja += "<option value=\""+rs.getInt("diplomaID")+"\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "+rs.getString("zvanje")+"</option>";
                }
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return zvanja;
    }

    public void dodajSkillOglas(int idOglasa,int[] skills)
    {
        for (int idSkill:skills) {
            String sql = "INSERT INTO oglas_vestine(oglasID,vestinaID) VALUES("+idOglasa+","+idSkill+")";
            try {
                ResultSet rs = stmt.executeQuery(sql);
            } catch (SQLException e)
            {
                e.printStackTrace();
            }
        }
    }

    public void dodajEdukacijuOglas(int idOglasa,int[] edukacija)
    {
        for (int idEdu:edukacija) {
            String sql = "INSERT INTO oglas_edukacija(oglasID,diplomaID) VALUES("+idOglasa+","+idEdu+")";
            try {
                ResultSet rs = stmt.executeQuery(sql);
            } catch (SQLException e)
            {
                e.printStackTrace();
            }
        }
    }

    public void dodajSkillKorisnik(int idKorisnika,int[] skills)
    {
        for (int idSkill:skills) {
            String sql = "INSERT INTO korisnik_vestine(korisnikID,vestinaID) VALUES("+idKorisnika+","+idSkill+")";
            try {
                ResultSet rs = stmt.executeQuery(sql);
            } catch (SQLException e)
            {
                e.printStackTrace();
            }
        }
    }

    public void dodajEdukacijuKorisnik(int idKorisnika,int[] edukacija)
    {
        for (int idEdu:edukacija) {
            String sql = "INSERT INTO korisnik_edukacija(korisnikID,diplomaID) VALUES("+idKorisnika+","+idEdu+")";
            try {
                ResultSet rs = stmt.executeQuery(sql);
            } catch (SQLException e)
            {
                e.printStackTrace();
            }
        }
    }

    public ArrayList<Integer> vratiIDPoslove(int korisnikID) {
        ArrayList<Integer> poslovi = new ArrayList<Integer>();
        String upit = "SELECT * FROM oglasi WHERE vlasnikID=" + korisnikID;
        try {
            ResultSet rs = stmt.executeQuery(upit);
            while (rs.next()) {
                poslovi.add(rs.getInt("oglasID"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return poslovi;
    }


    public String popuniInfoInboxKorisnik(int id)
    {
        String sql = "SELECT * FROM aplikacija WHERE korisnikID=" + id+" AND sakrijUser=0 AND primljen is not NULL";
        String infoInbox="";
        try {
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next())
            {
                Oglas oglas=new Oglas(rs.getInt("oglasID"));
                String prihvacen="";
                if(rs.getBoolean("primljen"))
                {
                    prihvacen="Prihvacen";
                }
                else {
                    prihvacen="Nije prihvacen";
                }
                infoInbox+="<div class=\"card\">\n" +
                        "                <h5 class=\"card-header\"><a href=\"/proposal?idOglasa="+oglas.getOglasID()+"+\">"+oglas.getNazivOglasa()+"</a></h5>\n" +
                        "                <div class=\"card-body\">\n" +
                        "                    <div class=\"row align-items-center\">\n" +
                        "                        <div class=\"col\">\n" +
                        "                            <h5 class=\"card-title\">"+prihvacen+"</h5>\n" +
                        "                        </div>\n" +
                        "                        <div class=\"col-2\">\n" +
                        "                            <a href=\"/dismissUser?id="+oglas.getOglasID()+"\" class=\"btn btn-danger\">\n" +
                        "                                <svg xmlns=\"http://www.w3.org/2000/svg\" width=\"30\" height=\"30\" fill=\"white\"\n" +
                        "                                    class=\"bi bi-x\" viewBox=\"0 0 16 16\">\n" +
                        "                                    <path\n" +
                        "                                        d=\"M4.646 4.646a.5.5 0 0 1 .708 0L8 7.293l2.646-2.647a.5.5 0 0 1 .708.708L8.707 8l2.647 2.646a.5.5 0 0 1-.708.708L8 8.707l-2.646 2.647a.5.5 0 0 1-.708-.708L7.293 8 4.646 5.354a.5.5 0 0 1 0-.708z\" />\n" +
                        "                                </svg>\n" +
                        "                                Obrisi Poruku &nbsp; &nbsp;\n" +
                        "                            </a>\n" +
                        "                        </div>\n" +
                        "                    </div>\n" +
                        "                </div>\n" +
                        "            </div><br><br>";
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return infoInbox;
    }

    public String popuniInfoInboxPoslodavac(int id)
    {
        String infoInbox = "";
        for (int oglasID:vratiIDPoslove(id)) {
        String sql = "SELECT * FROM aplikacija WHERE oglasID=" + oglasID + " AND sakrijVlasnik=0";
        try {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Oglas oglas = new Oglas(rs.getInt("oglasID"));
                User user=new User(rs.getInt("korisnikID"));
                String prihvacen = "";
                if(rs.getString("primljen")==null)
                {
                    infoInbox+="<div class=\"card\">\n" +
                            "                <h5 class=\"card-header\"><a href=\"proposal?idOglasa="+oglas.getOglasID()+"\">"+oglas.getNazivOglasa()+"</a></h5>\n" +
                            "                <div class=\"card-body\">\n" +
                            "                    <div class=\"row align-items-center\">\n" +
                            "                        <div class=\"col\">\n" +
                            "                            <h5 class=\"card-title\"><a href=\"user?id="+rs.getInt("korisnikID")+"\">"+user.getIme()+" "+user.getPrezime()+"</a></h5>\n" +
                            "                            <p class=\"card-text\">"+rs.getString("porukaPoslodavca")+"</p>\n" +
                            "                        </div>\n" +
                            "                        <div class=\"col-2\">\n" +
                            "                            <a href=\"accept?id="+rs.getInt("aplikacijaID")+"\" class=\"btn btn-success\">\n" +
                            "                                <svg xmlns=\"http://www.w3.org/2000/svg\" width=\"30\" height=\"30\" fill=\"white\"\n" +
                            "                                    class=\"bi bi-check\" viewBox=\"0 0 16 16\">\n" +
                            "                                    <path\n" +
                            "                                        d=\"M10.97 4.97a.75.75 0 0 1 1.07 1.05l-3.99 4.99a.75.75 0 0 1-1.08.02L4.324 8.384a.75.75 0 1 1 1.06-1.06l2.094 2.093 3.473-4.425a.267.267 0 0 1 .02-.022z\" />\n" +
                            "                                </svg>\n" +
                            "                            </a>\n" +
                            "                            &nbsp;\n" +
                            "                            <a href=\"decline?id="+rs.getInt("aplikacijaID")+"\" class=\"btn btn-danger\">\n" +
                            "                                <svg xmlns=\"http://www.w3.org/2000/svg\" width=\"30\" height=\"30\" fill=\"white\"\n" +
                            "                                    class=\"bi bi-x\" viewBox=\"0 0 16 16\">\n" +
                            "                                    <path\n" +
                            "                                        d=\"M4.646 4.646a.5.5 0 0 1 .708 0L8 7.293l2.646-2.647a.5.5 0 0 1 .708.708L8.707 8l2.647 2.646a.5.5 0 0 1-.708.708L8 8.707l-2.646 2.647a.5.5 0 0 1-.708-.708L7.293 8 4.646 5.354a.5.5 0 0 1 0-.708z\" />\n" +
                            "                                </svg>\n" +
                            "                            </a>\n" +
                            "                        </div>\n" +
                            "                    </div>\n" +
                            "                </div>\n" +
                            "            </div><br><br>";
                }
                else {
                    if (rs.getBoolean("primljen")) {
                        prihvacen = "<p class=\"fw-bold\" style=\"color:green;\">Prihvacen</p>";
                    } else {
                        prihvacen = "<p class=\"fw-bold\" style=\"color:red;\">Nije prihvacen</p>";
                    }
                    infoInbox+="<div class=\"card\">\n" +
                            "                <h5 class=\"card-header\"><a href=\"proposal?idOglasa="+oglas.getOglasID()+"\">"+oglas.getNazivOglasa()+"</a></h5>\n" +
                            "                <div class=\"card-body\">\n" +
                            "                    <div class=\"row align-items-center\">\n" +
                            "                        <div class=\"col\">\n" +
                            "                            <h5 class=\"card-title\"><a href=\"user?id="+rs.getInt("korisnikID")+"\">"+user.getIme()+" "+user.getPrezime()+"</a></h5>\n" +
                            "                            <p class=\"card-text\">"+rs.getString("porukaPoslodavca")+"</p>\n" +
                            "                        </div>\n" +
                            "                        <div class=\"col-2\">\n" +
                            prihvacen+
                            "                            <a href=\"dismissPoslodavac?id="+rs.getInt("aplikacijaID")+"\" class=\"btn btn-danger\">\n" +
                            "                                <svg xmlns=\"http://www.w3.org/2000/svg\" width=\"30\" height=\"30\" fill=\"white\"\n" +
                            "                                    class=\"bi bi-x\" viewBox=\"0 0 16 16\">\n" +
                            "                                    <path\n" +
                            "                                        d=\"M4.646 4.646a.5.5 0 0 1 .708 0L8 7.293l2.646-2.647a.5.5 0 0 1 .708.708L8.707 8l2.647 2.646a.5.5 0 0 1-.708.708L8 8.707l-2.646 2.647a.5.5 0 0 1-.708-.708L7.293 8 4.646 5.354a.5.5 0 0 1 0-.708z\" />\n" +
                            "                                </svg>\n" +
                            "                                Obrisi &nbsp; &nbsp;\n" +
                            "                            </a>\n" +
                            "                        </div>" +
                            "                    </div>\n" +
                            "                </div>\n" +
                            "            </div><br><br>";
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
        return infoInbox;
    }

    public void dismissUser(int id)
    {
        String sql = "UPDATE aplikacija SET sakrijUser=1 WHERE oglasID=" + id;
        try {
            ResultSet rs = stmt.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void dismissPoslodavac(int id)
    {
        String sql = "UPDATE aplikacija SET sakrijVlasnik=1 WHERE aplikacijaID=" + id;
        try {
            ResultSet rs = stmt.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void accept(int id)
    {
        String sql = "UPDATE aplikacija SET primljen=1 WHERE aplikacijaID=" + id;
        try {
            ResultSet rs = stmt.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void decline(int id)
    {
        String sql = "UPDATE aplikacija SET primljen=0 WHERE aplikacijaID=" + id;
        try {
            ResultSet rs = stmt.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}