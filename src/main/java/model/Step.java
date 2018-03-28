package model;

import java.util.Date;

public class Step extends Test{
    public Step(String step){
        setNome( step);
        setInicio( new Date( ) );
    }
}
