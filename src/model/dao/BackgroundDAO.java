package model.dao;

import backgrounder.Variabili;
import model.dao.exception.DuplicatedObjectException;

import java.time.LocalDate;

public interface BackgroundDAO {
    
    boolean insert(Variabili var) throws DuplicatedObjectException ;

    boolean medify(LocalDate day);

}
