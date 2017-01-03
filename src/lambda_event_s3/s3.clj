(ns lambda-event-s3.s3
  (:require [schema.core :as schema]
            [clojure.string :refer [split split-lines lower-case join] :as string]
            [clj-time.coerce :as c])
  (:import [com.amazonaws.services.s3 AmazonS3 AmazonS3Client]
           [com.amazonaws ClientConfiguration]
           [com.amazonaws.regions Regions]
           [com.amazonaws.services.s3.model PutObjectRequest]))

(defn create-client [& {:keys [max-connections max-error-retry endpoint region tcp-keep-alive]
                        :or {max-connections 50 max-error-retry 1 tcp-keep-alive false}}]
  (let [configuration (-> (ClientConfiguration.)
                          (.withMaxErrorRetry max-error-retry)
                          (.withMaxConnections max-connections)
                          (.withTcpKeepAlive tcp-keep-alive))
        client (AmazonS3Client. configuration)]
    (when endpoint
      (.setEndpoint client endpoint))
    (if region
      (.withRegion client (Regions/fromName region))
      client)))

(defn- fetch-s3-object [client bucket object-key]
  (.getObject client bucket object-key))

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

(def aws-regions
  (apply schema/enum (mapv #(.getName %1) (Regions/values))))

(schema/defschema AWSEventSchema
  {:event-name String ;(schema/enum "ObjectCreated:Put")
   :event-version (schema/enum "2.0")
   :event-source String ;"aws:s3"
   :response-elements ResponseElementsSchema
   :aws-region aws-regions
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
  [client :- AmazonS3Client target :- S3ObjectDetails]
  (let [s3-response (fetch-s3-object client (:bucket-name target) (:object-key target))
        s3-object-metadata (.getObjectMetadata s3-response)]
    (->S3Object (.getKey s3-response)
                (.getBucketName s3-response)
                (.getObjectContent s3-response)
                (.getContentLength s3-object-metadata)
                (.getContentType s3-object-metadata)
                (c/from-date (.getLastModified s3-object-metadata)))))

(schema/defn ^:always-validate event->s3-fetch :- S3ObjectDetails
  [event :- AWSEventSchema]
  (->S3ObjectDetails (-> event :s3 :object :key)
                     (-> event :s3 :bucket :name)))

(schema/defn ^:always-validate event->s3-object :- S3Object
  [client :- AmazonS3Client event :- AWSEventSchema]
  (let [s3-fetch (event->s3-fetch event)]
    (get-object client s3-fetch)))


