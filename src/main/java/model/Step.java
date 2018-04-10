package model;

import java.util.Date;

public class Step extends Test{
    private String img;

    public Step(String step){
        setNome( step);
        setInicio( new Date( ) );
        System.out.println( "Step: " + step  );
    }

    public void setImg(String img64) {
        this.img = img64;
    }

    public String getImg(){
        return img;
    }
}
