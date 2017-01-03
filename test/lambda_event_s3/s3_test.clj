(ns lambda-event-s3.s3-test
  (:require [clojure.test :refer :all]
            [clj-containment-matchers.clojure-test :refer :all]
            [clojure.java.io :as io]
            [lambda-event-s3.s3 :as s3])
  (:import [com.amazonaws.auth BasicAWSCredentials]
           [com.amazonaws.services.s3 AmazonS3 AmazonS3Client S3ClientOptions]
           [java.util UUID]))

(defonce client
  (let [credentials (BasicAWSCredentials. "YOUR_ACCESS_KEY_ID" "YOUR_SECRET_ACCESS_KEY")
        client-options (-> (S3ClientOptions/builder)
                           (.setPathStyleAccess true)
                           (.build))
        client (-> (AmazonS3Client. credentials)
                   (.withEndpoint "http://localhost:4568"))]
    (.setS3ClientOptions client client-options)
    client))

(def bucket (str (UUID/randomUUID)))
(def file-key "sample.json")
(defonce sample-file (delay (io/file (io/resource "sample.json"))))

(defn- prepare-fake-s3 [suite]
  (.createBucket client bucket)
  (.putObject client bucket file-key @sample-file)
  (suite))

(use-fixtures :once prepare-fake-s3)

(def sample-source-event
  {:aws-region "ap-southeast-2"
   :event-name "ObjectCreated:Put"
   :event-source "fake"
   :event-time "1970-01-01T00:00:00.000Z"
   :event-version "2.0"
   :request-parameters {:source-ip-address "0.0.0.0"}
   :response-elements {:x-amz-id-2 "" :x-amz-request-id ""}
   :s3 {:bucket {:arn "" :name bucket :owner-identity {:principal-id ""}}
        :configuration-id ""
        :object {:e-tag "" :key file-key :sequencer "" :size 1302}
        :s3schema-version "1.0"}
   :user-identity {:principal-id ""}})

(def expected-s3-object
  {:bucket-name bucket
   :content #(instance? com.amazonaws.services.s3.model.S3ObjectInputStream %1)
   :content-length number?
   :content-type "application/octet-stream"
   :last-modified #(instance? org.joda.time.DateTime %1)
   :object-key file-key})

(deftest lambda-s3-events
  (testing "event->s3-object"
    (let [s3-object (s3/event->s3-object client sample-source-event)]
      (is (equal? s3-object expected-s3-object)))))

