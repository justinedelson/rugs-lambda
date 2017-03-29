package com.adobe.people.jedelson.aemrugslambda;

import com.atomist.param.ParameterValue;
import com.atomist.param.SimpleParameterValue;
import com.atomist.param.SimpleParameterValues;
import com.atomist.project.archive.Rugs;
import com.atomist.project.generate.ProjectGenerator;
import com.atomist.rug.resolver.ArtifactDescriptor;
import com.atomist.rug.resolver.DefaultArtifactDescriptor;
import com.atomist.rug.resolver.DependencyResolver;
import com.atomist.rug.resolver.UriBasedDependencyResolver;
import com.atomist.rug.resolver.loader.ProvenanceAddingRugLoader;
import com.atomist.rug.resolver.loader.RugLoader;
import com.atomist.source.ArtifactSource;
import com.atomist.source.file.ZipFileArtifactSourceReader;
import com.atomist.source.file.ZipFileInput;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

import static scala.collection.JavaConversions.asJavaCollection;
import static scala.collection.JavaConversions.asScalaBuffer;


public class RugWrapper {

    private static final Logger log = LoggerFactory.getLogger(RugWrapper.class);

    private final String groupId;
    private final String artifactId;
    private final String version;

    private final File tmpRoot;
    private final File repo;
    private final File archive;

    public RugWrapper(String requestId, String groupId, String artifactId, String version) throws IOException {
        this.groupId = groupId;
        this.artifactId = artifactId;
        this.version = version;
        tmpRoot = new File(FileUtils.getTempDirectory().getAbsoluteFile(), requestId);
        tmpRoot.mkdirs();
        repo = new File(tmpRoot, "rugs");
        File archiveFolder = new File(repo, groupId + "/" + artifactId + "/" + version);
        archiveFolder.mkdirs();
        archive = new File(archiveFolder, artifactId + "-" + version + ".zip");
        FileUtils.copyToFile(getClass().getResourceAsStream("/aem-rugs-0.2.2.zip"), archive);
        log.info("Saved rug zip in {}", archive);
    }

    public File getArchive() {
        return archive;
    }

    Rugs getRugs() throws Exception {
        URI uri = archive.toURI();
        ArtifactDescriptor artifact = new DefaultArtifactDescriptor(groupId, artifactId, version, ArtifactDescriptor.Extension.ZIP, ArtifactDescriptor.Scope.COMPILE, uri);
        ArtifactSource source = ZipFileArtifactSourceReader.fromZipSource(new ZipFileInput(new FileInputStream(archive)));

        DependencyResolver resolver = new UriBasedDependencyResolver(new URI[] { uri }, repo.getAbsolutePath());
        RugLoader loader = new ProvenanceAddingRugLoader(resolver);

        Rugs rugs = loader.load(artifact, source);

        return rugs;
    }

    void cleanup() {
        FileUtils.deleteQuietly(tmpRoot);
    }
}
