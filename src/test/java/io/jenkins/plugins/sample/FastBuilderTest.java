package io.jenkins.plugins.sample;

import hudson.model.FreeStyleBuild;
import hudson.model.FreeStyleProject;
import hudson.model.Label;
import org.jenkinsci.plugins.workflow.cps.CpsFlowDefinition;
import org.jenkinsci.plugins.workflow.job.WorkflowJob;
import org.jenkinsci.plugins.workflow.job.WorkflowRun;
import org.junit.Rule;
import org.junit.Test;
import org.jvnet.hudson.test.JenkinsRule;

public class FastBuilderTest {

    @Rule
    public JenkinsRule jenkins = new JenkinsRule();

    final String name = "admin";
    final String password = "Cyfast@123";
    final String projectid = "1";
    final String orchestrationid = "2";
    final String branch = "TestCases_WebApplication";
    final String repourl = "https://smartsols@dev.azure.com/smartsols/MTH/_git/FAST";
    final String urllink = "https://cyfast.platformcyient.com/gateway";


    // @Test
    // public void testConfigRoundtrip() throws Exception {
    //     FreeStyleProject project = jenkins.createFreeStyleProject();
    //     project.getBuildersList().add(new FastBuilder(name,password,projectid,orchestrationid,branch,repourl,urllink));
    //     project = jenkins.configRoundtrip(project);
    //     jenkins.assertEqualDataBoundBeans(new FastBuilder(name,password,projectid,orchestrationid,branch,repourl,urllink), project.getBuildersList().get(0));
    // }

    // @Test
    // public void testConfigRoundtripFrench() throws Exception {
    //     FreeStyleProject project = jenkins.createFreeStyleProject();
    //     FastBuilder builder = new FastBuilder(name,password,projectid,orchestrationid,branch,repourl,urllink);
    //     builder.setUseFrench(true);
    //     project.getBuildersList().add(builder);
    //     project = jenkins.configRoundtrip(project);

    //     FastBuilder lhs = new FastBuilder(name,password,projectid,orchestrationid,branch,repourl,urllink);
    //     lhs.setUseFrench(true);
    //     jenkins.assertEqualDataBoundBeans(lhs, project.getBuildersList().get(0));
    // }

    // @Test
    // public void testBuild() throws Exception {
    //     FreeStyleProject project = jenkins.createFreeStyleProject();
    //     FastBuilder builder = new FastBuilder(name,password,projectid,orchestrationid,branch,repourl,urllink);
    //     project.getBuildersList().add(builder);

    //     FreeStyleBuild build = jenkins.buildAndAssertSuccess(project);
    //     jenkins.assertLogContains("Hello, " + name, build);
    // }

    // @Test
    // public void testBuildFrench() throws Exception {

    //     FreeStyleProject project = jenkins.createFreeStyleProject();
    //     FastBuilder builder = new FastBuilder(name,password,projectid,orchestrationid,branch,repourl,urllink);
    //     builder.setUseFrench(true);
    //     project.getBuildersList().add(builder);

    //     FreeStyleBuild build = jenkins.buildAndAssertSuccess(project);
    //     jenkins.assertLogContains("Bonjour, " + name, build);
    // }

    // @Test
    // public void testScriptedPipeline() throws Exception {
    //     String agentLabel = "my-agent";
    //     jenkins.createOnlineSlave(Label.get(agentLabel));
    //     WorkflowJob job = jenkins.createProject(WorkflowJob.class, "test-scripted-pipeline");
    //     String pipelineScript
    //             = "node {\n"
    //             + "  greet '" + name + "'\n"
    //             + "}";
    //     job.setDefinition(new CpsFlowDefinition(pipelineScript, true));
    //     WorkflowRun completedBuild = jenkins.assertBuildStatusSuccess(job.scheduleBuild2(0));
    //     String expectedString = "Hello, " + name + "!";
    //     jenkins.assertLogContains(expectedString, completedBuild);
    // }

}