package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;

public class OknoZamowien {
    private JFrame frame = new JFrame("Zamowienia");
    DefaultListModel<String> listModel;
    JList<String> list;
    private boolean klikniety = false;

    private void uzupelnij() {
        try {
            listModel.removeAllElements();
            ResultSet resultSet = GlowneOkno.polaczenie.zapytanie(new QueryBuilder().select("tytul, login")
                    .from("Lista_wypozyczen L JOIN Dane U ON U.id = L.id_uzytkownika " +
                            "JOIN Ksiazki K ON L.id_ksiazki = K.id")
                    .where("L.status = 'zamowiona'")
                    .getQuery().toString());
            while (resultSet.next())
                listModel.addElement(resultSet.getString("tytul")
                        + " : " + resultSet.getString("login"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void okno() throws InterruptedException {
        // deklaracja scrolla
        listModel = new DefaultListModel<>();
        list = new JList<>(listModel);
        list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        list.setLayoutOrientation(JList.VERTICAL);
        list.setVisibleRowCount(-1);
        JScrollPane scrollPane = new JScrollPane(list);
        uzupelnij();

        // deklaracja przycisku do usuwania
        JButton akceptuj = new JButton("Przyjmij zamowienie");
        akceptuj.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                try {
                    String[] war = list.getSelectedValue().split(":");
                    String tytul = war[0];
                    tytul = tytul.replace(" ", "");
                    String login = war[1];
                    login = login.replace(" ", "");

                    QueryBuilder id_k = new QueryBuilder();
                    id_k.select("id").from("Ksiazki").where("tytul = '" + tytul + "'");
                    QueryBuilder id_u = new QueryBuilder();
                    id_u.select("id").from("Dane").where("login = '" + login + "'");
                    int count = GlowneOkno.polaczenie.wstawianie(new QueryBuilder()
                            .update("Lista_wypozyczen", new ArrayList<>(Collections.singletonList("status = 'wypozyczona'")))
                            .where("(id_ksiazki = (" + id_k.getQuery().toString() + ")) AND "
                                    + "(id_uzytkownika = (" + id_u.getQuery().toString() + "))")
                            .getQuery().toString());
                    uzupelnij();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Zly syntax");
                }
            }
        });

        JButton ok = new JButton("Ok");
        ok.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                klikniety = true;
            }
        });

        // deklaracja panelu
        var panel = new JPanel();
        panel.setBackground(Color.BLACK);
        panel.setLayout(new GridLayout(4, 0));

        // dodanie komponentow do panelu
        panel.add(scrollPane);
        panel.add(akceptuj);
        panel.add(ok);

        // dodanie panelu do ramki
        frame.getContentPane().add(panel, BorderLayout.CENTER);

        // wyswietlenie ramki
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setSize(400, 400);

        while (!klikniety)
            Thread.sleep(100);

        frame.setVisible(false);
    }
}
