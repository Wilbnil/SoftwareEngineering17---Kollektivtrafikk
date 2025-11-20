package org.gruppe17.kollektivtrafikk.repository.interfaces;

import java.util.ArrayList;

public interface I_Repo<Object> {

    Object getById(int id) throws Exception;
    Object getByName(String name) throws Exception;
    ArrayList<Object> getAll() throws Exception;
    void insert(Object object) throws Exception;
    void update(Object object, Object newObject) throws Exception;
    void delete(Object object) throws Exception;
}
