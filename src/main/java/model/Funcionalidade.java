package model;

import java.util.*;

public class Funcionalidade extends Test{

    private List<Cenario> listCenario = new ArrayList<Cenario>();

    public Funcionalidade(String funcionalidade){
        setNome( funcionalidade);
        setInicio( new Date(System.currentTimeMillis()));
    }


    public List<Cenario> getCenarios() {
        return listCenario;
    }

    public void addCenario(Cenario cenario) {
        listCenario.add(cenario);
    }
}
