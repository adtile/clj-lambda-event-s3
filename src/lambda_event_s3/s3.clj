(ns lambda-event-s3.s3
  (:require [schema.core :as schema]
            [amazonica.aws.s3 :as s3]
            [amazonica.aws.s3transfer :as s3transfer]
            [clojure.string :refer [split split-lines lower-case join] :as string]))

(schema/defschema ResponseElementsSchema
  {:x-amz-id-2 String
   :x-amz-request-id String})

(schema/defschema UserIdentitySchema
  {:principal-id String})

(schema/defschema S3ObjectSchema
  {:e-tag String
   :sequencer String
   :key String
   :size schema/Num })

(schema/defschema S3BucketSchema
  {:arn String
   :name String
   :owner-identity UserIdentitySchema})

(schema/defschema S3Schema
  {:configuration-id String
   :object S3ObjectSchema
   :bucket S3BucketSchema
   :s3schema-version (schema/enum "1.0")})

(schema/defschema RequestParametersSchema
  {:source-ip-address String})

(schema/defschema AWSEventSchema
  {:event-name String ;(schema/enum "ObjectCreated:Put")
   :event-version (schema/enum "2.0")
   :event-source String ;"aws:s3"
   :response-elements ResponseElementsSchema
   :aws-region (schema/enum "ap-northeast-1" "ap-southeast-1" "ap-southeast-2" "eu-central-1" "eu-west-1" "sa-east-1" "us-east-1" "us-west-1" "us-west-2")
   :event-time String; "1970-01-01T00:00:00.000Z"
   :user-identity UserIdentitySchema
   :s3 S3Schema
   :request-parameters RequestParametersSchema})

(schema/defrecord S3ObjectDetails
  [object-key :- String
   bucket-name :- String])

(schema/defrecord S3Object
  [object-key :- String
   bucket-name :- String
   content :- com.amazonaws.services.s3.model.S3ObjectInputStream
   content-length :- Long
   content-type :- String
   last-modified :- org.joda.time.DateTime])

(schema/defn ^:always-validate get-object :- S3Object
  [target :- S3ObjectDetails]
  (let [s3-response (s3/get-object (:bucket-name target) (:object-key target))]
    (->S3Object (-> s3-response :key)
                (-> s3-response :bucket-name)
                (-> s3-response :object-content)
                (-> s3-response :object-metadata :content-length)
                (-> s3-response :object-metadata :content-type)
                (-> s3-response :object-metadata :last-modified))))

(schema/defn ^:always-validate event->s3-fetch :- S3ObjectDetails
  [event :- AWSEventSchema]
  (->S3ObjectDetails (-> event :s3 :object :key)
                     (-> event :s3 :bucket :name)))

(schema/defn ^:always-validate event->s3-object :- S3Object
  [event :- AWSEventSchema]
  (-> event
    (event->s3-fetch)
    (get-object)))

