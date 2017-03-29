This project is a way to execute Atomist Rugs inside AWS Lambda using API Gateway.

To use it, you must first deploy your Rug Archive to S3.

Then, update serverless.yml to change the `custom` section to meet your specific needs:

* accountId - your AWS Account ID
* outputBucketName - the S3 bucket into which generated projects will be created (cannot already exist)
* inputBucketName - the S3 bucket which contains your Rug Archive (must already exist)
* rugObjectKey - the key in the inputBucketName at which your Rug Archive is located
* rugGroupId - Rug Archive group ID
* rugArtifactId - Rug Archive artifact ID
* rugVersion - Rug Archive version