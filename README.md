# clj-lambda-event-s3

https://travis-ci.org/adtile/clj-lambda-event-s3.svg?branch=master

A Clojure library designed for parsing AWS Lambda S3 events.

## Usage

```clojure
(use 'lambda-event-s3.s3)

(let [client (create-client)]
  (event->s3-object client {:aws-region "ap-southeast-2"
                            :event-name "ObjectCreated:Put"
                            :event-source "fake"
                            :event-time "1970-01-01T00:00:00.000Z"
                            :event-version "2.0"
                            :request-parameters {:source-ip-address "0.0.0.0"}
                            :response-elements {:x-amz-id-2 "foobar" :x-amz-request-id "barfoo"}
                            :s3 {:bucket {:arn "arn:aws:s3:::bucket/" :name "bucket" :owner-identity {:principal-id "1234567"}}
                                  :configuration-id "xx"
                                  :object {:e-tag "xx" :key "exampleobject.png" :sequencer "" :size 1302}
                                  :s3schema-version "1.0"}
                            :user-identity {:principal-id "1234567"}})
```

## License

Copyright © 2015 Adtile Technologies Inc.

Distributed under the MIT License
