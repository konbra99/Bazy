package com.company;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.sql.SQLException;

public class OknoUzytkownikow {
    private JFrame frame = new JFrame("Uzytkownicy");
    DefaultListModel<String> listModel;
    JList<String> list;

    private void uzupelnij() {
        try {
            listModel.removeAllElements();
            ResultSet resultSet = GlowneOkno.polaczenie.zapytanie(new QueryBuilder().select("*")
                    .from("Uzytkownicy U JOIN Dane D ON U.id = D.id")
                    .getQuery().toString());
            while (resultSet.next())
                listModel.addElement(resultSet.getString("login")
                        + " : " + resultSet.getString("imie")
                        + " : " + resultSet.getString("nazwisko"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void okno() {
        // deklaracja scrolla
        listModel = new DefaultListModel<>();
        list = new JList<>(listModel);
        list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        list.setLayoutOrientation(JList.VERTICAL);
        list.setVisibleRowCount(-1);
        JScrollPane scrollPane = new JScrollPane(list);
        uzupelnij();

        // deklaracja przycisku do usuwania
        JButton banuj = new JButton("Usun uzytkownika");
        banuj.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                try {
                    int czas = Integer.parseInt(JOptionPane.showInputDialog(null, "Podaj czas",
                            "Banuj", JOptionPane.INFORMATION_MESSAGE));
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Zly format");
                }
            }
        });

        // deklaracja panelu
        var panel = new JPanel();
        panel.setBackground(Color.BLACK);
        panel.setLayout(new GridLayout(4, 0));

        // dodanie komponentow do panelu
        panel.add(scrollPane);
        panel.add(banuj);

        // dodanie panelu do ramki
        frame.getContentPane().add(panel, BorderLayout.CENTER);

        // wyswietlenie ramki
        frame.getContentPane().add(panel, BorderLayout.CENTER);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setSize(400, 400);
    }
}
