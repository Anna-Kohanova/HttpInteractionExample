package com.example.http.httpmanagers;

import java.util.List;

public interface CRUD<T> {

    void add(T t);

    T getById();

    List<T> getAll();

    void deleteById(int id);

}
