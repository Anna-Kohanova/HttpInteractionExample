package com.example.http.httpmanagers;

import java.util.List;

public interface CRUD<T1, T2> {

    void add(T1 t);

    T1 getById(int id);

    List<T2> getAllById(int id);

    List<T1> getAll();

    void deleteById(int id);

}
