package model;

import report.Report;

import java.util.*;

public class Funcionalidade extends Test{

    private List<Cenario> listCenario = new ArrayList<Cenario>();

    public Funcionalidade(String funcionalidade){
        setNome( funcionalidade);
        setInicio( new Date(System.currentTimeMillis()));
        Report.addFuncionalidade( this );
    }


    public List<Cenario> getCenarios() {
        return listCenario;
    }

    public void addCenario(Cenario cenario) {
        listCenario.add(cenario);
    }
}
