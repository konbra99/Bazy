package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.sql.SQLException;

enum Login {
    POPRAWNY,
    NIEPOPRAWNY,
    PRZERWANY,
    REJESTRACJA,
    NIEZALOGOWANY
}

public class OknoLogowania {
    private JFrame frame;
    private boolean rej = false;
    private boolean nie = false;
    private boolean ok = false;
    private boolean klikniety = false;
    private int id, mode;   // mode == 0 -> uzytkownik, 1 -> admin, 2 -> niezalogowany

    public int getId() {
        return id;
    }

    public int getMode() {
        return mode;
    }

    private void UstawRej() {
        rej = true;
        klikniety = true;
    }

    private void UstawNie() {
        nie = true;
        klikniety = true;
    }

    private void UstawOk() {
        ok = true;
        klikniety = true;
    }

    public Login okno() throws SQLException, InterruptedException {
        JTextField login = new JTextField(5);
        JTextField haslo = new JPasswordField(5);
        JButton rejestracja = new JButton("Zaloz konto");
        JButton incognito = new JButton("Kontynuuj niezalogowany");
        JButton okej = new JButton("                                         OK                                           ");

        var panel = new JPanel();

        rejestracja.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                UstawRej();
            }
        });

        incognito.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                UstawNie();
            }
        });

        okej.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                UstawOk();
            }
        });

        panel.add(new JLabel("Login:"));
        panel.add(login);
        panel.add(Box.createHorizontalStrut(15)); // a spacer
        panel.add(new JLabel("Haslo:"));
        panel.add(haslo);
        panel.add(okej);
        panel.add(new JLabel("Nie masz konta?"));
        panel.add(rejestracja);
        panel.add(incognito);

        frame = new JFrame("Login");
        frame.getContentPane().add(panel, BorderLayout.CENTER);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setSize(400, 400);

        while (!klikniety)
            Thread.sleep(100);
        frame.setVisible(false);
        if (rej)
            return Login.REJESTRACJA;
        if (nie) {
            mode = 2;
            return Login.NIEZALOGOWANY;
        }
        if (ok) {
            ResultSet resultSet = GlowneOkno.polaczenie.zapytanie(new QueryBuilder().logowanie(login.getText(), haslo.getText()).getQuery().toString());
            if (resultSet.next()) {
                try {
                    id = Integer.parseInt(resultSet.getString(1));
                } catch (NumberFormatException ignored) {}
                mode = (resultSet.getString(4).equals("1")) ? 1 : 0;
                return Login.POPRAWNY;
            } else {
                return Login.NIEPOPRAWNY;
            }
        }
        return Login.POPRAWNY;
    }
}
