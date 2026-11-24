package pbo.dimsum.dao;

import java.sql.SQLException;
import java.util.List;

public interface OperasiCRUD<T> {
    List<T> getAll() throws SQLException;
    List<T> search(String keyword) throws SQLException;
    boolean insert(T data) throws SQLException;
    boolean update(T data) throws SQLException;
    boolean delete(int id) throws SQLException;
}
