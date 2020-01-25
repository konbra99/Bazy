package com.company;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;


public class GlowneOkno {
    private JFrame frame = new JFrame("Biblioteka v2019.22.01");
    private StrategiaDostepu strategia;
    public static Polaczenie polaczenie;
    public static Integer id;
    DefaultListModel<String> listModel;
    JList<String> list;

    private void setStrategia(int mode) {
        if (mode == 0)
            strategia = new StrategiaUzytkownika();
        else if (mode == 1)
            strategia = new StrategiaAdmina();
        else if (mode == 2)
            strategia = new StrategiaNiezalogowanego();
    }

    public static void addChangeListener(JTextComponent component, ChangeListener changeListener) {
        Objects.requireNonNull(component);
        Objects.requireNonNull(changeListener);
        DocumentListener dl = new DocumentListener() {
            private int lastChange = 0, lastNotifiedChange = 0;

            @Override
            public void insertUpdate(DocumentEvent e) {
                changedUpdate(e);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                changedUpdate(e);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                lastChange++;
                SwingUtilities.invokeLater(() -> {
                    if (lastNotifiedChange != lastChange) {
                        lastNotifiedChange = lastChange;
                        changeListener.stateChanged(new ChangeEvent(component));
                    }
                });
            }
        };

        component.addPropertyChangeListener("document", (PropertyChangeEvent e) -> {
            Document d1 = (Document) e.getOldValue();
            Document d2 = (Document) e.getNewValue();
            if (d1 != null)
                d1.removeDocumentListener(dl);
            if (d2 != null)
                d2.addDocumentListener(dl);
            dl.changedUpdate(null);
        });

        Document d = component.getDocument();
        if (d != null)
            d.addDocumentListener(dl);
    }

    void poszukiwanie(JTextComponent component) {
        try {
            listModel.removeAllElements();
            ResultSet resultSet = polaczenie.zapytanie(new QueryBuilder().select("tytul, id, l_sztuk").from("Ksiazki").
                    like("tytul", component.getText()).getQuery().toString());
            while (resultSet.next())
                listModel.addElement(resultSet.getString("id") + " : "
                        + resultSet.getString("tytul") + " : "
                        + resultSet.getString("l_sztuk"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public GlowneOkno() throws SQLException, InterruptedException {
        polaczenie = new Polaczenie();

        OknoLogowania log = new OknoLogowania();
        Login login = log.okno();
        if (login == Login.PRZERWANY)
            System.exit(0);
        else if (login == Login.REJESTRACJA)
            System.out.println("rejestracja");
        else if (login == Login.NIEZALOGOWANY)
            System.out.println("niezalogowany");
        setStrategia(log.getMode());
        id = log.getId();

        // deklaracja panelu
        var panel = new JPanel();
        panel.setBackground(Color.BLACK);
        panel.setLayout(new GridLayout(4, 0));

        // deklaracja pola do wyszukiwan
        JTextComponent query = new JTextField();
        addChangeListener(query, e -> poszukiwanie(query));

        // deklaracja przycisku do zamawiania
        JButton zamow = new JButton("Zamow ksiazke");
        zamow.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                strategia.zamawiaj(list.getSelectedValue());
            }
        });

        // deklaracja przycisku dla okna uzytkownikow
        JButton uzytkownicy = new JButton("Uzytkownicy");
        uzytkownicy.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                strategia.uzytkownicy();
            }
        });

        // deklaracja scrolla
        listModel = new DefaultListModel<>();
        list = new JList<>(listModel);
        list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        list.setLayoutOrientation(JList.VERTICAL);
        list.setVisibleRowCount(-1);
        JScrollPane scrollPane = new JScrollPane(list);

        // dodanie komponentow do panelu
        panel.add(scrollPane);
        panel.add(query);
        panel.add(zamow);
        panel.add(uzytkownicy);

        // dodanie panelu do ramki
        frame.getContentPane().add(panel, BorderLayout.CENTER);
    }

    public static void main(String[] args) throws SQLException, InterruptedException {
        GlowneOkno glowneOkno = new GlowneOkno();
        glowneOkno.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        glowneOkno.frame.setVisible(true);
        glowneOkno.frame.setSize(400, 400);
    }
}
