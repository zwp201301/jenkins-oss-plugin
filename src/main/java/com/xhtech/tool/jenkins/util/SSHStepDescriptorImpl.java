package com.xhtech.tool.jenkins.util;

import com.google.common.collect.ImmutableSet;
import hudson.EnvVars;
import hudson.FilePath;
import hudson.Launcher;
import hudson.model.Run;
import hudson.model.TaskListener;
import org.jenkinsci.plugins.workflow.steps.StepDescriptor;

import java.util.Set;

/**
 * Default StepDescriptorImpl for all SSH steps.
 *
 * @author Naresh Rayapati
 */
public abstract class SSHStepDescriptorImpl extends StepDescriptor {

  protected String getPrefix() {
    return "jenkins plugin - ";
  }

  @Override
  public Set<? extends Class<?>> getRequiredContext() {
    return ImmutableSet
        .of(Launcher.class, FilePath.class, Run.class, TaskListener.class, EnvVars.class);
  }
}
