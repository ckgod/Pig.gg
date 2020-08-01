package com.example.apiuse;

public class Record {

    int imres;
    String wins;
    boolean isWin;
    String kills;
    String deaths;
    String assists;
    String championKey;

    public Record(int imres, String championKey ,String wins, String kills, String deaths, String assists) {
        this.imres = imres;
        this.wins = wins;
        this.kills = kills;
        this.deaths = deaths;
        this.assists = assists;
        this.championKey = championKey;
    }
    public Record(int imres){
        this.imres = imres;
    }

    public int getImres() {
        return imres;
    }

    public void setImres(int imres) {
        this.imres = imres;
    }

    public String getWins() {
        return wins;
    }

    public void setWins(String wins) {
        this.wins = wins;
    }

    public boolean isWin() {
        return isWin;
    }

    public void setWin(boolean win) {
        isWin = win;
    }

    public String getKills() {
        return kills;
    }

    public void setKills(String kills) {
        this.kills = kills;
    }

    public String getDeaths() {
        return deaths;
    }

    public void setDeaths(String deaths) {
        this.deaths = deaths;
    }

    public String getAssists() {
        return assists;
    }

    public void setAssists(String assists) {
        this.assists = assists;
    }

    public String getChampionKey() {
        return championKey;
    }

    public void setChampionKey(String championKey) {
        this.championKey = championKey;
    }
}
