package com.example.demo;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class User {

    DataBase DB = new DataBase();

    private int korisnikID;
    private String ime;
    private String prezime;
    private String datumRodjenja;
    private String email;
    private boolean pol;
    private String mobilni;
    private String slikaUrl;
    private String bio;
    private int lokacijaID;
    private String lokacija;
    private int tipKorisnika;
    private ArrayList<String> skills = new ArrayList<String>();
    private ArrayList<String> edukacija = new ArrayList<String>();

    public User(int korisnikID) {
        this.korisnikID=korisnikID;
        try {
            ResultSet rs = DB.executeCustom("SELECT * FROM korisnik WHERE korisnikID=" + korisnikID);
            if(rs.first())
            {
                ime=rs.getString("ime");
                prezime=rs.getString("prezime");
                datumRodjenja=rs.getString("datumRodjenja");
                email=rs.getString("email");
                pol=rs.getBoolean("pol");
                lokacijaID=rs.getInt("lokacijaID");
                tipKorisnika=rs.getInt("tipKorisnika");
                mobilni=rs.getString("mobilni");
                slikaUrl=rs.getString("profilnaSlikaURL");
                bio=rs.getString("bio");
                lokacija=DB.lokacija(lokacijaID);
                skills = DB.vratiSkillsKorisnik(korisnikID);
                edukacija = DB.vratiEduKorisnik(korisnikID);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<String> getSkills() {
        return skills;
    }

    public int getKorisnikID() {
        return korisnikID;
    }

    public String getIme() {
        return ime;
    }

    public String getPrezime() {
        return prezime;
    }

    public String getDatumRodjenja() {
        return datumRodjenja;
    }

    public String getEmail() {
        return email;
    }

    public boolean isPol() {
        return pol;
    }

    public String getMobilni() {
        return mobilni;
    }

    public String getSlikaUrl() {
        return slikaUrl;
    }

    public String getBio() {
        return bio;
    }

    public int getLokacijaID() {
        return lokacijaID;
    }

    public ArrayList<String> getEdukacija() {
        return edukacija;
    }

    public String getLokacija() {
        return lokacija;
    }

    public int getTipKorisnika() {
        return tipKorisnika;
    }
}
