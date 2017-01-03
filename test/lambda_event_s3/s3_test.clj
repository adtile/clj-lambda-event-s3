(ns lambda-event-s3.s3-test
  (:require [clojure.test :refer :all]
            [clj-containment-matchers.clojure-test :refer :all]
            [clojure.java.io :as io]
            [lambda-event-s3.s3 :as s3])
  (:import [java.util UUID]))

(defonce client (s3/create-client))
(def bucket "adtile-ci-aws-sandbox-development")
(def file-key (str "lambda-event-s3/" (UUID/randomUUID) ".txt"))
(defonce sample-file (delay (io/file (io/resource "sample.txt"))))

(defn- prepare-s3 [suite]
  (.putObject client bucket file-key @sample-file)
  (suite)
  (.deleteObject client bucket file-key))

(use-fixtures :once prepare-s3)

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
   :content-type "text/plain"
   :last-modified #(instance? org.joda.time.DateTime %1)
   :object-key file-key})

(deftest lambda-s3-events
  (testing "event->s3-object"
    (let [s3-object (s3/event->s3-object client sample-source-event)]
      (is (equal? s3-object expected-s3-object)
      (is (equal? (slurp (-> s3-object :content)) "lolbar"))))))

