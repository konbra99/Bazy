package com.company;

public class StrategiaUzytkownika implements StrategiaDostepu {
    @Override
    public void zamawiaj(String s) {
        String wybrany = s;
        System.out.println("klient zamowil ksiazke " + wybrany);
    }

    @Override
    public void uzytkownicy() {

    }
}
