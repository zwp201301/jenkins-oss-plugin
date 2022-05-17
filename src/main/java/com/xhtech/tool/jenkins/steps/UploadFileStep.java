package com.xhtech.tool.jenkins.steps;

import com.xhtech.tool.jenkins.util.SSHMasterToSlaveCallable;
import com.xhtech.tool.jenkins.util.SSHStepDescriptorImpl;
import com.xhtech.tool.jenkins.util.SSHStepExecution;
import hudson.Extension;
import hudson.FilePath;
import hudson.model.TaskListener;
import lombok.Getter;
import org.jenkinsci.plugins.workflow.steps.Step;
import org.jenkinsci.plugins.workflow.steps.StepContext;
import org.jenkinsci.plugins.workflow.steps.StepExecution;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;

import java.io.IOException;
import java.io.Serializable;

/**
 * Step to execute a command on remote node.
 *
 * @author Naresh Rayapati
 */
public class UploadFileStep extends Step implements Serializable {

    private static final long serialVersionUID = 791486302260458002L;

    @Getter
    @DataBoundSetter
    private boolean debug = false;


    /**
     * 类型包括：aliyun/minio
     */
    @Getter
    @DataBoundSetter
    private String ossType;

    @Getter
    @DataBoundSetter
    private String bucketName;

    /**
     * 目标文件目录
     */
    @Getter
    @DataBoundSetter
    private String targetPath;

    @DataBoundConstructor
    public UploadFileStep(boolean debug, String ossType, String bucketName, String targetPath) {
        this.debug = debug;
        this.ossType = ossType;
        this.bucketName = bucketName;
        this.targetPath = targetPath;
    }

    @Override
    public StepExecution start(StepContext context) throws Exception {
        return new Execution(this, context);
    }

    @Extension
    public static class DescriptorImpl extends SSHStepDescriptorImpl {

        @Override
        public String getFunctionName() {
            return "uploadFile";
        }

        @Override
        public String getDisplayName() {
            return getPrefix() + getFunctionName();
        }
    }

    public static class Execution extends SSHStepExecution {

        private static final long serialVersionUID = -5293959904328128L;

        protected Execution(UploadFileStep step, StepContext context)
                throws IOException, InterruptedException {
            super(step, context);
        }

        @Override
        protected Object run() throws Exception {
            UploadFileStep step = (UploadFileStep) getStep();
            return getChannel().call(new CommandCallable(step, getListener(), getWorkspace()));
        }

        private static class CommandCallable extends SSHMasterToSlaveCallable {

            public CommandCallable(UploadFileStep step, TaskListener listener, FilePath workspace) {
                super(step, listener, workspace);
            }

            @Override
            public Object execute() {
                return getService().upload(getListener(), getWorkspace());
            }
        }
    }
}
