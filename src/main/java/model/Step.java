package model;

import java.util.Date;

public class Step extends Test{
    private String img;

    public Step(String step){
        setNome( step);
        setInicio( new Date( ) );
    }

    public void setImg(String img) {
        this.img = img;
    }
}
