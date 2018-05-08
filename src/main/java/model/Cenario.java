package model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Cenario extends Test{
    private List<Step> listStep = new ArrayList<Step>();

    public Cenario(String cenario){
        setNome( cenario);
        setInicio( new Date(System.currentTimeMillis()));
    }

    public void addStep(Step step){
        listStep.add(step);
    }

    public List<Step> getListStep() {
        return listStep;
    }
}
