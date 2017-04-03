/*
 * #%L
 * %%
 * Copyright (C) 2017 Adobe
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package com.adobe.people.jedelson.rugsinlambda.helpers;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.S3Object;
import com.atomist.project.archive.Rugs;
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


public class RugWrapper {

    private static final Logger log = LoggerFactory.getLogger(RugWrapper.class);

    private final String rugBucketName = System.getenv("RUG_BUCKET_NAME");
    private final String rugObjectKey = System.getenv("RUG_OBJECT_KEY");

    private final String groupId = System.getenv("RUG_GROUP_ID");
    private final String artifactId = System.getenv("RUG_ARTIFACT_ID");
    private final String version = System.getenv("RUG_VERSION");

    private final File tmpRoot;
    private final File repo;
    private final File archive;

    public RugWrapper(String requestId) throws IOException {
        tmpRoot = new File(FileUtils.getTempDirectory().getAbsoluteFile(), requestId);
        tmpRoot.mkdirs();
        repo = new File(tmpRoot, "rugs");
        File archiveFolder = new File(repo, groupId + "/" + artifactId + "/" + version);
        archiveFolder.mkdirs();
        archive = new File(archiveFolder, artifactId + "-" + version + ".zip");

        AmazonS3Client s3Client = new AmazonS3Client();
        S3Object rugFile = s3Client.getObject(rugBucketName, rugObjectKey);

        FileUtils.copyToFile(rugFile.getObjectContent(), archive);
        log.info("Saved rug zip in {}", archive);
    }

    public Rugs getRugs() throws Exception {
        URI uri = archive.toURI();
        ArtifactDescriptor artifact = new DefaultArtifactDescriptor(groupId, artifactId, version, ArtifactDescriptor.Extension.ZIP, ArtifactDescriptor.Scope.COMPILE, uri);
        ArtifactSource source = ZipFileArtifactSourceReader.fromZipSource(new ZipFileInput(new FileInputStream(archive)));

        DependencyResolver resolver = new UriBasedDependencyResolver(new URI[] { uri }, repo.getAbsolutePath());
        RugLoader loader = new ProvenanceAddingRugLoader(resolver);

        Rugs rugs = loader.load(artifact, source);

        return rugs;
    }

    public void cleanup() {
        FileUtils.deleteQuietly(tmpRoot);
    }
}
