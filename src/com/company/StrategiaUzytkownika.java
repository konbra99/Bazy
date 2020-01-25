package com.company;

import java.sql.SQLException;
import java.util.ArrayList;

public class StrategiaUzytkownika implements StrategiaDostepu {
    @Override
    public void zamawiaj(String s) {
        String wybrany = s;
        try {
            ArrayList<String> kolumny = new ArrayList<>();
            kolumny.add("id_uzytkownika");
            kolumny.add("id_ksiazki");
            kolumny.add("status");

            String[] war = s.split(":");
            ArrayList<String> wartosci = new ArrayList<>();
            wartosci.add(GlowneOkno.id.toString());
            wartosci.add(war[0]);
            wartosci.add("'" + "zamowiona'");
            GlowneOkno.polaczenie.wstawianie(new QueryBuilder()
                    .insert("Lista_wypozyczen", kolumny, wartosci)
                    .getQuery().toString());
        } catch (SQLException ignored) {
        }
    }

    @Override
    public void uzytkownicy() {

    }
}
