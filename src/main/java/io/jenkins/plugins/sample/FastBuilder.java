package io.jenkins.plugins.sample;

import hudson.model.Action;
import hudson.Launcher;
import hudson.EnvVars;
import hudson.Extension;
import hudson.FilePath;
import hudson.util.FormValidation;
import hudson.model.AbstractProject;
import hudson.model.Run;
import hudson.model.TaskListener;
import hudson.tasks.Builder;
import hudson.tasks.BuildStepDescriptor;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.QueryParameter;

import javax.servlet.ServletException;
import java.io.IOException;
import jenkins.tasks.SimpleBuildStep;
import org.jenkinsci.Symbol;
import org.kohsuke.stapler.DataBoundSetter;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.*;
import java.io.*;
import java.util.*;




public class FastBuilder extends Builder implements SimpleBuildStep {

    private final String name;
    private final String password;
    private final String projectid;
    private final String orchestrationid;
    private final String repourl;
    private final String branch;
    private final String urllink;
    
    private boolean useFrench;

    @DataBoundConstructor
    public FastBuilder(String name,String password,String projectid,String orchestrationid,String branch,String repourl,String urllink) {
        this.name = name;
        this.password = password;
        this.projectid = projectid;
        this.orchestrationid = orchestrationid;
        this.branch = branch;
        this.repourl = repourl;
        this.urllink = urllink;
        
    }

    public String getUserName() {
        return name;
    }

    public String getPassword(){
        return password;
    }

    public String getProjectId(){
        return projectid;
    }

    public String getOrchestrationId(){
        return orchestrationid;
    }

    public String getBranchName(){
        return branch;
    }

    public String getRepoUrl(){
        return repourl;
    }

    public String getUrl(){
        return urllink;
    }

    public boolean isUseFrench() {
        return useFrench;
    }

    @DataBoundSetter
    public void setUseFrench(boolean useFrench) {
        this.useFrench = useFrench;
    }

    @Override
    public void perform(Run<?, ?> run, FilePath workspace, EnvVars env, Launcher launcher, TaskListener listener) throws InterruptedException, IOException {
        Optional<String> optionalName = Optional.ofNullable(name);
        Optional<String> optionalPassword = Optional.ofNullable(password);
        Optional<String> optionalProjectId = Optional.ofNullable(projectid);
        Optional<String> optionalOrchestrationId = Optional.ofNullable(orchestrationid);
        Optional<String> optionalBranch = Optional.ofNullable(branch);
        Optional<String> optionalRepoUrl = Optional.ofNullable(repourl);
        Optional<String> optionalUrlLink = Optional.ofNullable(urllink);
        if (optionalName.isPresent() && optionalPassword.isPresent() && optionalProjectId.isPresent() && optionalOrchestrationId.isPresent() && optionalBranch.isPresent() && optionalRepoUrl.isPresent() && optionalUrlLink.isPresent()) {
        String url = urllink+"/user/authenticate";
            String params = "{\"userName\": \"" + name + "\", \"password\": \"" + password + "\"}";
            
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(params))
                    .build(); 
            
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            String responseBody = response.body();
            ObjectMapper mapper = new ObjectMapper();
            JsonParser jsonParser = mapper.getFactory().createParser(responseBody);

            JsonNode jsonNode = mapper.readTree(jsonParser);
            String accessToken = jsonNode.get("AccessToken").asText();
        run.addAction(new FastAction(name,orchestrationid,accessToken,urllink)); 
        if (useFrench) {
            listener.getLogger().println("Bonjour, " + name + "!");
        } else {
            //api for running orch 
            String apiUrl = urllink+"/OrchestrationTestCase/ExecuteOrchestrationTestCases";
            String apiParams = "{\"RepoURL\": \"" + repourl + "\",\"Branch\": \"" + branch + "\",\"ProjectID\": \"" + projectid + "\",\"OrchestrationID\": \"" + orchestrationid + "\",\"OrchestrationName\": \"" + "" + "\",\"UserID\": \"" + name + "\"}";
            
            HttpClient clientTwo = HttpClient.newHttpClient();
            HttpRequest requestTwo = HttpRequest.newBuilder()
                    .uri(URI.create(apiUrl))
                    .header("Authorization", "Bearer " + accessToken)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(apiParams))
                    .build();

            HttpResponse<String> responseOrch = clientTwo.send(requestTwo, HttpResponse.BodyHandlers.ofString());
            String responseBodyOrchestration = responseOrch.body();
            

            //while loop for console output for orchestration
            while (true) {
                try {
                    URL urlLog = new URL(urllink+"/LogStream/GetByOrchestration?UserID="+name+ "&OrchestrationID=" + orchestrationid);
                    HttpURLConnection conn = (HttpURLConnection) urlLog.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setRequestProperty("Authorization", "Bearer " + accessToken);
                    conn.setRequestProperty("Content-Type", "text/plain");
                    // Reading  the response
                    BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String line;
                    StringBuilder responseLog = new StringBuilder();
                    while ((line = rd.readLine()) != null) {
                        responseLog.append(line);
                        listener.getLogger().println(  "log response"+responseLog.toString());
                    }
                    rd.close();

                    
                    String inputstr = responseLog.toString();
                    // int startindex = inputstr.length() - 14;
                    // String outputtendigitstr = inputstr.substring(startindex);
                    if (inputstr.contains("Execution Done")) {
                        break;
                    }

                    
                    Thread.sleep(2000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    } else {
         String errorMsg = "Required fields are missing: ";
    }
         
    }

    @Symbol("greet")
    @Extension
    public static final class DescriptorImpl extends BuildStepDescriptor<Builder> {

        public FormValidation doCheckName(@QueryParameter String value, @QueryParameter boolean useFrench)
                throws IOException, ServletException {
            if (value.length() == 0)
                return FormValidation.error(Messages.FastBuilder_DescriptorImpl_errors_missingName());
            if (value.length() < 4)
                return FormValidation.warning(Messages.FastBuilder_DescriptorImpl_warnings_tooShort());
            
            return FormValidation.ok();
        }

        @Override
        public boolean isApplicable(Class<? extends AbstractProject> aClass) {
            return true;
        }

        @Override
        public String getDisplayName() {
            return Messages.FastBuilder_DescriptorImpl_DisplayName();
        }

    }

}
