package com.example.demo;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Oglas {

    DataBase DB = new DataBase();

    private int oglasID;
    private int vlasnikID;
    private int lokacijaID;
    private String lokacija;
    private int kategorijaID;
    private String kategorija;
    private Date datumKreiranja;
    private String nazivOglasa;
    private String opisOglasa;
    private boolean potrebnoIskustvo;
    private boolean radOdKuce;
    private int brojPregleda;
    private int brojLajkova;
    private ArrayList<String> skills = new ArrayList<String>();
    private ArrayList<String> edukacija = new ArrayList<String>();

    Oglas(int oglasID)
    {
        this.oglasID=oglasID;
        String sql="SELECT * FROM oglasi WHERE oglasID="+oglasID;
        try {
            ResultSet rs = DB.executeCustom(sql);
            if(rs.first()) {
                vlasnikID = rs.getInt("vlasnikID");
                kategorijaID = rs.getInt("kategorijaID");
                datumKreiranja = rs.getDate("datumKreiranja");
                nazivOglasa = rs.getString("nazivOglasa");
                opisOglasa = rs.getString("opisOglasa");
                potrebnoIskustvo = rs.getBoolean("iskustvo");
                lokacijaID = rs.getInt("lokacijaID");
                radOdKuce=rs.getBoolean("radOdKuce");
                lokacija = DB.lokacija(lokacijaID);
                brojPregleda=DB.brojPregleda(oglasID);
                brojLajkova=DB.brojLajkova(oglasID);
                skills = DB.vratiSkillsOglas(oglasID);
                edukacija = DB.vratiEduOglas(oglasID);
                kategorija=DB.kategorija(kategorijaID);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getBrojLajkova() {
        return brojLajkova;
    }

    public boolean isRadOdKuce() {
        return radOdKuce;
    }

    public int getBrojPregleda() {
        return brojPregleda;
    }

    public ArrayList<String> getEdukacija() {
        return edukacija;
    }

    public ArrayList<String> getSkills() {
        return skills;
    }

    public int getOglasID() {
        return oglasID;
    }

    public int getVlasnikID() {
        return vlasnikID;
    }

    public int getLokacijaID() {
        return lokacijaID;
    }

    public String getLokacija() {
        return lokacija;
    }

    public int getKategorijaID() {
        return kategorijaID;
    }

    public String getKategorija() {
        return kategorija;
    }

    public Date getDatumKreiranja() {
        return datumKreiranja;
    }

    public String getNazivOglasa() {
        return nazivOglasa;
    }

    public String getOpisOglasa() {
        return opisOglasa;
    }

    public boolean isPotrebnoIskustvo() {
        return potrebnoIskustvo;
    }
}
