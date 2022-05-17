package com.xhtech.tool.jenkins.util;

import com.google.common.annotations.VisibleForTesting;
import com.xhtech.tool.jenkins.service.UploadFileService;
import com.xhtech.tool.jenkins.steps.UploadFileStep;
import hudson.FilePath;
import hudson.model.TaskListener;

import java.io.IOException;
import java.util.UUID;

import jenkins.security.MasterToSlaveCallable;
import org.apache.log4j.MDC;

/**
 * Base Callable for all SSH Steps.
 *
 * @author Naresh Rayapati.
 */
public abstract class SSHMasterToSlaveCallable extends MasterToSlaveCallable<Object, IOException> {

    private final UploadFileStep step;
    private final TaskListener listener;
    private final FilePath workspace;

    private UploadFileService service;

    public SSHMasterToSlaveCallable(UploadFileStep step, TaskListener listener, FilePath workspace) {
        this.step = step;
        this.listener = listener;
        this.workspace = workspace;
    }

    @Override
    public Object call() {
        MDC.put("execution.id", UUID.randomUUID().toString());
        this.service = createService();
        return execute();
    }

    @VisibleForTesting
    public UploadFileService createService() {
        return UploadFileService
                .create(step.getOssType(), step.getBucketName(), step.getTargetPath(), step.isDebug());
    }

    protected abstract Object execute();

    public UploadFileStep getStep() {
        return step;
    }

    public FilePath getWorkspace() {
        return workspace;
    }

    public UploadFileService getService() {
        return service;
    }

    public TaskListener getListener() {
        return listener;
    }
}
