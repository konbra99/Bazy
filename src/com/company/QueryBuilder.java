package com.company;

import java.util.ArrayList;

public class QueryBuilder {
    private StringBuilder query;

    public QueryBuilder() {
        this.query = new StringBuilder();
    }

    public QueryBuilder(StringBuilder query) {
        this.query = query;
    }

    public StringBuilder getQuery() {
        return query;
    }

    public QueryBuilder append(String append) {
        return new QueryBuilder(query.append(append));
    }

    public QueryBuilder select(String kolumna) {
        return new QueryBuilder(query.append("SELECT ").append(kolumna).append(" "));
    }

    public QueryBuilder from(String tabela) {
        return new QueryBuilder(query.append("FROM ").append(tabela));
    }

    public QueryBuilder where(String warunek) {
        return new QueryBuilder(query.append(" WHERE ").append(warunek));
    }

    public QueryBuilder like(String kolumna, String wzorzec) {
        return new QueryBuilder(query.append(" WHERE ").append(kolumna).append(" LIKE '").append(wzorzec).append("%'"));
    }

    public QueryBuilder logowanie(String login, String haslo) {
        return new QueryBuilder(query.append("SELECT * FROM Dane WHERE (login = '").append(login).
                append("') AND (haslo = '").append(haslo).append("')"));
    }

    public QueryBuilder insert(String tabela, ArrayList<String> kolumny, ArrayList<String> wartosci) {
        QueryBuilder queryBuilder = new QueryBuilder(query.append("INSERT INTO ").append(tabela).append(" ("));
        for (int i = 0; i < kolumny.size() - 1; i++)
            queryBuilder.getQuery().append(kolumny.get(i)).append(", ");
        queryBuilder.getQuery().append(kolumny.get(kolumny.size() - 1)).append(") VALUES(");
        for (int i = 0; i < wartosci.size() - 1; i++)
            queryBuilder.getQuery().append(wartosci.get(i)).append(", ");
        queryBuilder.getQuery().append(wartosci.get(wartosci.size() - 1)).append(")");
        return queryBuilder;
    }

}

