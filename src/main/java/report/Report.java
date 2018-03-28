package report;

import model.Funcionalidade;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Report {
    private String reportName;
    private String userName;
    private String OS;
    private String browser;
    private String HostName;
    private Date date;
    private String version;
    private List<Funcionalidade> listFuncionalidade = new ArrayList<Funcionalidade>(  );

    public Report(){
        reportName = "REPORT ALEX"     ;
        userName   = System.getProperty( "user.name" )     ;
        OS         = System.getProperty( "os.name" )     ;
        browser    = "Chrome"    ;
        try {
            HostName   = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        date       = new Date( );
        version    = "1.0-SNAPSHOT" ;
    }

    public void addFuncionalidade(Funcionalidade funcionalidade){
        listFuncionalidade.add( funcionalidade );
    }

    public List<Funcionalidade> getListFuncionalidade() {
        return listFuncionalidade;
    }

    public String getReportName() {
        return reportName;
    }

    public String getUserName() {
        return userName;
    }

    public String getOS() {
        return OS;
    }

    public String getBrowser() {
        return browser;
    }

    public String getHostName() {
        return HostName;
    }

    public Date getDate() {
        return date;
    }

    public String getVersion() {
        return version;
    }
}
