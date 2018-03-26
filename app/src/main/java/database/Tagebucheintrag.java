package database;

/**
 * Created by bburczek on 19.03.2018.
 */

public class Tagebucheintrag {

    private String uhrzeit;
    private int limit;
    private int id_teintrag;
    private int id_menu;
    private int id_lm;


    public Tagebucheintrag(String uhrzeit, int limit, int id_teintrag, int id_menu, int id_lm ) {
        this.uhrzeit = uhrzeit;
        this.limit = limit;
        this.id_teintrag = id_teintrag;
        this.id_menu = id_menu;
        this.id_lm = id_lm;
    }

    public String getUhrzeit() {
        return uhrzeit;
    }

    public void setUhrzeit(String uhrzeit) {
        this.uhrzeit = uhrzeit;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getId_teintrag() {
        return id_teintrag;
    }

    public void setId_teintrag(int id_teintrag) {
        this.id_teintrag = id_teintrag;
    }

    public int getId_menu() {
        return id_menu;
    }

    public void setId_menu(int id_menu) {
        this.id_menu = id_menu;
    }

    public int getId_lm() {
        return id_lm;
    }

    public void setId_lm(int id_lm) {
        this.id_lm = id_lm;
    }
}
