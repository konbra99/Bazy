package com.company;

import java.sql.SQLException;

public interface StrategiaDostepu {
    void zamawiaj(String s);
    void uzytkownicy();
    void start() throws InterruptedException;
}
