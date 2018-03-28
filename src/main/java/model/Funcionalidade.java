package model;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class Funcionalidade extends Test{

    private List<Cenario> listCenario = new ArrayList<Cenario>();

    public Funcionalidade(String funcionalidade){
        nome = funcionalidade;

        inicio = new Date(System.currentTimeMillis());
    }


    public List<Cenario> getCenarios() {
        return listCenario;
    }

    public void addCenario(Cenario cenario) {
        listCenario.add(cenario);
    }
}
