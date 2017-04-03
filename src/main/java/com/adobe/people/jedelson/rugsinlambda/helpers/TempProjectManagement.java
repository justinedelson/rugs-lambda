package com.adobe.people.jedelson.rugsinlambda.helpers;

import com.atomist.param.ParameterValues;
import com.atomist.project.common.InvalidParametersException;
import com.atomist.project.edit.ModificationAttempt;
import com.atomist.project.edit.ProjectEditor;
import com.atomist.project.edit.SuccessfulModification;
import com.atomist.project.generate.ProjectGenerator;
import com.atomist.project.review.ProjectReviewer;
import com.atomist.project.review.ReviewResult;
import com.atomist.rug.resolver.project.ProvenanceInfoWriter;
import com.atomist.rug.runtime.plans.ProjectManagement;
import com.atomist.source.ArtifactSource;
import com.atomist.source.FileAdditionDelta;
import com.atomist.source.FileDeletionDelta;
import com.atomist.source.FileUpdateDelta;
import com.atomist.source.SimpleSourceUpdateInfo;
import com.atomist.source.file.FileSystemArtifactSource;
import com.atomist.source.file.FileSystemArtifactSourceIdentifier;
import com.atomist.source.file.FileSystemArtifactSourceWriter;
import com.atomist.source.file.SimpleFileSystemArtifactSourceIdentifier;
import org.apache.commons.io.FileUtils;
import org.zeroturnaround.zip.ZipUtil;
import scala.collection.JavaConversions;

import java.io.File;

public class TempProjectManagement implements ProjectManagement {

    private final File projectDirectory;
    private final FileSystemArtifactSourceIdentifier fileSystemIdentifier;


    public TempProjectManagement(String requestId) {
        projectDirectory = new File(FileUtils.getTempDirectory(), "output/" + requestId + "/project");
        projectDirectory.mkdirs();
        fileSystemIdentifier = new SimpleFileSystemArtifactSourceIdentifier(projectDirectory);
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
            new FileSystemArtifactSourceWriter().write(result, fileSystemIdentifier, new SimpleSourceUpdateInfo(projectGenerator.name()));
            return new FileSystemArtifactSource(fileSystemIdentifier);
        } catch (InvalidParametersException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ModificationAttempt edit(ProjectEditor projectEditor, ParameterValues parameterValues, String projectName) {
        try {
            ModificationAttempt result = projectEditor.modify(new FileSystemArtifactSource(fileSystemIdentifier), parameterValues);
            if (result instanceof SuccessfulModification) {
                ArtifactSource resultArtifactSource = new ProvenanceInfoWriter().write(((SuccessfulModification)result).result(), projectEditor, parameterValues, "Lambda Client");

                final FileSystemArtifactSourceWriter writer = new FileSystemArtifactSourceWriter();

                JavaConversions.asJavaCollection(resultArtifactSource.cachedDeltas()).stream().forEach(d -> {
                    if (d instanceof FileAdditionDelta) {
                        writer.write(((FileAdditionDelta) d).newFile(), projectDirectory);
                    } else if (d instanceof FileUpdateDelta) {
                        new File(projectDirectory, ((FileUpdateDelta) d).oldFile().path()).delete();
                        writer.write(((FileUpdateDelta) d).updatedFile(), projectDirectory);
                    } else if (d instanceof FileDeletionDelta) {
                        new File(projectDirectory, ((FileDeletionDelta) d).oldFile().path()).delete();
                    }
                });
            }
            return result;
        } catch (InvalidParametersException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ReviewResult review(ProjectReviewer projectReviewer, ParameterValues parameterValues, String s) {
        throw new UnsupportedOperationException();
    }
}
