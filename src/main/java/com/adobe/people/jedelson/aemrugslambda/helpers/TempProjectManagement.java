package com.adobe.people.jedelson.aemrugslambda.helpers;

import com.atomist.param.ParameterValues;
import com.atomist.project.common.InvalidParametersException;
import com.atomist.project.edit.ModificationAttempt;
import com.atomist.project.edit.ProjectEditor;
import com.atomist.project.generate.ProjectGenerator;
import com.atomist.project.review.ProjectReviewer;
import com.atomist.project.review.ReviewResult;
import com.atomist.rug.runtime.plans.ProjectManagement;
import com.atomist.source.ArtifactSource;
import com.atomist.source.SimpleSourceUpdateInfo;
import com.atomist.source.file.FileSystemArtifactSource;
import com.atomist.source.file.FileSystemArtifactSourceIdentifier;
import com.atomist.source.file.FileSystemArtifactSourceWriter;
import com.atomist.source.file.SimpleFileSystemArtifactSourceIdentifier;
import org.apache.commons.io.FileUtils;
import org.zeroturnaround.zip.ZipUtil;

import java.io.File;

public class TempProjectManagement implements ProjectManagement {

    private final File projectDirectory;


    public TempProjectManagement() {
        projectDirectory = new File(FileUtils.getTempDirectory(), "output/" + System.currentTimeMillis() + "/project");
        projectDirectory.mkdirs();
    }

    public File createZipFile() {
        File zipFile = new File(projectDirectory.getParentFile(), "project.zip");
        ZipUtil.pack(projectDirectory, zipFile);
        return zipFile;
    }

    @Override
    public ArtifactSource generate(ProjectGenerator projectGenerator, ParameterValues parameterValues, String projectName) {
        try {
            ArtifactSource result = projectGenerator.generate(projectName, parameterValues);
            FileSystemArtifactSourceIdentifier fsid = new SimpleFileSystemArtifactSourceIdentifier(projectDirectory);
            File resultFile = new FileSystemArtifactSourceWriter().write(result, fsid, new SimpleSourceUpdateInfo(projectGenerator.name()));

            System.out.print(resultFile.getAbsolutePath());

            return new FileSystemArtifactSource(fsid);
        } catch (InvalidParametersException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ModificationAttempt edit(ProjectEditor projectEditor, ParameterValues parameterValues, String s) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ReviewResult review(ProjectReviewer projectReviewer, ParameterValues parameterValues, String s) {
        throw new UnsupportedOperationException();
    }
}
