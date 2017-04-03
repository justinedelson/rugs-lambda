This project is a way to execute Atomist Rugs inside AWS Lambda using API Gateway.

To use it, you must first deploy your Rug Archive to S3.

Then, copy the file `config.yml.sample` to `config.yml` and set the following properties:

* `accountId` - your AWS Account ID
* `outputBucketName` - the S3 bucket into which generated projects will be created (cannot already exist)
* `inputBucketName` - the S3 bucket which contains your Rug Archive (must already exist)
* `rugObjectKey` - the key in the inputBucketName at which your Rug Archive is located
* `rugGroupId` - Rug Archive group ID
* `rugArtifactId` - Rug Archive artifact ID
* `rugVersion` - Rug Archive version

Once this is set, build the project with

    mvn clean package

and deploy to AWS with

    serverless deploy

You will now have three endpoints set up via API Gateway:

* `/{stage}/rugs` - describes the Rugs available
* `/{stage}/{generatorName}/validate` - validate a set of parameters to a generator
* `/{stage}/{generatorName}/generate` - generate a project. The result from a successful generation will contain a url (which expires in 1 hour) pointing to a ZIP of the generated project.

## Generating a Project

To generate a project, hit the `generate` endpoint passing a JSON body which contains the parameters to the generator:
 
    curl -H "Content-Type: application/json" -X POST -d '{"project_name":"foo","someOtherParam":"bar"}' https://XXXX.execute-api.us-east-1.amazonaws.com/dev/GeneratorName/generate
    
The response object will have a `url` key set to a presigned S3 URL (which expires after 1 hour) to a ZIP file of the generated project.
    
You can also pass editors to be executed after the generation is done:

    {
        "project_name" : "foo",
        "someOtherParam" : "bar",
        "editors" : [ {
            "name" : "EditorName",
            "params" : {
                "someEditorParam" : "value"
            }
        } ]
    }
    
> When using an editor like this, all of the generator parameters are passed to the editor as well. Editor-specific
parameters take precedence.