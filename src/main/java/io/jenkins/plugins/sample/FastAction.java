package io.jenkins.plugins.sample;
import hudson.model.Run;
import jenkins.model.RunAction2;
import hudson.model.Action;

public class FastAction implements RunAction2 {

    private String name;
    private String orchestrationid;
    private String accessToken;
    private String urllink;
    private transient Run run;

    public FastAction(String name,String orchestrationid,String accessToken,String urllink) {
        this.name = name;
        this.orchestrationid = orchestrationid;
        this.accessToken = accessToken;
        this.urllink = urllink;
    }

    public String getName() {
        return name;
    }

    public String getOrchestrationid(){
        return orchestrationid;
    }

    public String getAccessToken(){
        return accessToken;
    }

    public String getUrllink(){
        return urllink;
    }



    @Override
    public void onAttached(Run<?, ?> run) {
        this.run = run; 
    }

    @Override
    public void onLoad(Run<?, ?> run) {
        this.run = run; 
    }

    public Run getRun() { 
        return run;
    }

    @Override
    public String getIconFileName() {
        return "document.png";
    }

    @Override
    public String getDisplayName() {
        return "CyFast";
    }

    @Override
    public String getUrlName() {
        return "cyfast";
    }
}