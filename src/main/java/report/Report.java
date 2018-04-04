package report;

import model.Funcionalidade;
import model.ReportLog;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class Report extends ReportLog{
    private String reportName;
    private String userName;
    private String OS;
    private String browser;
    private String HostName;
    private Date dateCreation;
    private Date dateFinish;
    private String version;
    File htmlTemplateFile;
    String htmlString;
    DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    public Report(String src) throws IOException {
        reportName = "REPORT ALEX"     ;
        userName   = System.getProperty( "user.name" )     ;
        OS         = System.getProperty( "os.name" )     ;
        browser    = "Chrome"    ;
        dateCreation       = new Date( );
        version    = "1.0-SNAPSHOT" ;
        String sprint = "Sprint 15";
        try {
            HostName   = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        htmlTemplateFile = new File("./src/main/java/report/template.html");
        htmlString = FileUtils.readFileToString(htmlTemplateFile);
        htmlString = htmlString.replace("$reportName", reportName);
        htmlString = htmlString.replace("$reportDescription", sprint);
        htmlString = htmlString.replace("$reportDate", df.format(dateCreation));
        htmlString = htmlString.replace("$version", version);





    }

    public static void addFuncionalidade(Funcionalidade funcionalidade) {
       addFunc( funcionalidade );
    }

    public String getVersion() {
        return version;
    }

    public void flush() throws IOException {
        /*Statics
        $featureNumber
        $cenarioNumber
        $duracaoTotal
        $featureStart //dateStart
        $featureEnd     //dateEnd
        $totalFeaturesPassed
        $totalFeaturesFailed
        $totalFeaturesOthers
        $totalCenariosPassed
        $totalCenariosFailed
        $totalCenariosOthers
        $passPercent
        $userName
        $OS
        $browser
        $hostName
        $categoriesList (apenas tag)
        $categories[i] /para dropdown fazer loop no li
        $featureName li loop
        $featureStatus
        $featureDescription
        $featureCategory
        $cenarioName
        $cenarioDescription
        $stepTime
        $stepName

        $categoriName (fazer loop)
                        <div class="category-status-counts">
                            <span class="fail label dot">Fail: $failCategoryCount</span>
                            <span class="pass label dot">Pass> $passCategoryCount</span>
                            <span class="other label dot">Pass> $otherCategoryCount</span>
                         </div>
        $totalPass
        $totalFail
        $totalOther
        $featureStart
        $featureName
        $featureStatus
        */
        dateFinish = new Date();
        String duracao = getDuracao( dateFinish.getTime() - dateCreation.getTime() );
        htmlString = htmlString.replace("$duracaoTotal", duracao);
        htmlString = htmlString.replace("$numeroTestes", getTotalFunc().toString());
        htmlString = htmlString.replace("$dateCreation", df.format( dateCreation ));
        htmlString = htmlString.replace("$dateFinish", df.format( dateFinish ));
        htmlString = htmlString.replace( "$totalFeaturesPassed", getPassedsFeatures().toString() );

        File newHtmlFile = new File("./src/main/resources/new.html");
        FileUtils.writeStringToFile(newHtmlFile, htmlString);
    }


    private String getDuracao(long duracao) {
        if (duracao < 0) {
            throw new IllegalArgumentException("Duration must be greater than zero!");
        }

        long hours = TimeUnit.MILLISECONDS.toHours(duracao) % 24;
        long minutes = TimeUnit.MILLISECONDS.toMinutes(duracao) % 60;
        long seconds = TimeUnit.MILLISECONDS.toSeconds(duracao) % 60;
        long milliseconds = duracao % 1000;
        return String.format("%dh %dm %ds+%dms",
                hours, minutes, seconds, milliseconds);
    }

}
