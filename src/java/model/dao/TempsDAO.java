package model.dao;

import java.time.LocalDate;
import model.mo.Lettura;

public interface TempsDAO {
    
    public Lettura[] Readtoday(LocalDate today);
    
    public Lettura[] Readmeds(int number_of_reads);
    
}
