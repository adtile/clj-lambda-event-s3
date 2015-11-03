(defproject clj-lambda-event-s3 "0.1.3"
  :description "A Clojure library designed for parsing AWS Lambda S3 events"
  :url "https://github.com/adtile/clj-lambda-event-s3"
  :license {:name "MIT"}
  :signing {:gpg-key "webmaster@adtile.me"}
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [amazonica/amazonica "0.3.39"
                  :exclusions [com.amazonaws/amazon-kinesis-client
                               com.amazonaws/aws-java-sdk-autoscaling
                               com.amazonaws/aws-java-sdk-cloudformation
                               com.amazonaws/aws-java-sdk-cloudfront
                               com.amazonaws/aws-java-sdk-cloudhsm
                               com.amazonaws/aws-java-sdk-cloudtrail
                               com.amazonaws/aws-java-sdk-cloudwatch
                               com.amazonaws/aws-java-sdk-cloudwatchmetrics
                               com.amazonaws/aws-java-sdk-codecommit
                               com.amazonaws/aws-java-sdk-codedeploy
                               com.amazonaws/aws-java-sdk-codepipeline
                               com.amazonaws/aws-java-sdk-cognitoidentity
                               com.amazonaws/aws-java-sdk-cognitosync
                               com.amazonaws/aws-java-sdk-config
                               com.amazonaws/aws-java-sdk-datapipeline
                               com.amazonaws/aws-java-sdk-devicefarm
                               com.amazonaws/aws-java-sdk-directconnect
                               com.amazonaws/aws-java-sdk-directory
                               com.amazonaws/aws-java-sdk-dynamodb
                               com.amazonaws/aws-java-sdk-ec2
                               com.amazonaws/aws-java-sdk-ecs
                               com.amazonaws/aws-java-sdk-efs
                               com.amazonaws/aws-java-sdk-elasticache
                               com.amazonaws/aws-java-sdk-elasticbeanstalk
                               com.amazonaws/aws-java-sdk-elasticloadbalancing
                               com.amazonaws/aws-java-sdk-elasticsearch
                               com.amazonaws/aws-java-sdk-elastictranscoder
                               com.amazonaws/aws-java-sdk-emr
                               com.amazonaws/aws-java-sdk-glacier
                               com.amazonaws/aws-java-sdk-iam
                               com.amazonaws/aws-java-sdk-importexport
                               com.amazonaws/aws-java-sdk-inspector
                               com.amazonaws/aws-java-sdk-iot
                               com.amazonaws/aws-java-sdk-kinesis
                               com.amazonaws/aws-java-sdk-logs
                               com.amazonaws/aws-java-sdk-machinelearning
                               com.amazonaws/aws-java-sdk-marketplacecommerceanalytics
                               com.amazonaws/aws-java-sdk-opsworks
                               com.amazonaws/aws-java-sdk-rds
                               com.amazonaws/aws-java-sdk-redshift
                               com.amazonaws/aws-java-sdk-route53
                               com.amazonaws/aws-java-sdk-ses
                               com.amazonaws/aws-java-sdk-simpledb
                               com.amazonaws/aws-java-sdk-simpleworkflow
                               com.amazonaws/aws-java-sdk-sns
                               com.amazonaws/aws-java-sdk-sqs
                               com.amazonaws/aws-java-sdk-ssm
                               com.amazonaws/aws-java-sdk-storagegateway
                               com.amazonaws/aws-java-sdk-sts
                               com.amazonaws/aws-java-sdk-support
                               com.amazonaws/aws-java-sdk-swf-libraries
                               com.amazonaws/aws-java-sdk-waf
                               com.amazonaws/aws-java-sdk-workspaces]]
                 [prismatic/schema "0.4.4"]])
