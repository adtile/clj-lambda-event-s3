(defproject clj-lambda-event-s3 "1.0.1"
  :description "A Clojure library designed for parsing AWS Lambda S3 events"
  :url "https://github.com/adtile/clj-lambda-event-s3"
  :license {:name "MIT"}
  :signing {:gpg-key "webmaster@adtile.me"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [com.amazonaws/aws-java-sdk-s3 "1.11.15"]
                 [clj-time "0.13.0"]
                 [prismatic/schema "1.1.3"]]
  :profiles {:dev {:dependencies [[clj-containment-matchers "1.0.1"]]
                   :resource-paths ["dev-resources"]}})
