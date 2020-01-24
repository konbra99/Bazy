package com.company;

public class StrategiaAdmina implements StrategiaDostepu {
    @Override
    public void zamawiaj(String s) {
        String wybrany = s;
        System.out.println("admin zamowil ksiazke " + wybrany);
    }

    @Override
    public void uzytkownicy() {
        new OknoUzytkownikow().okno();
    }
}
