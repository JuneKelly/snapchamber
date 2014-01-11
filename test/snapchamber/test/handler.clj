(ns snapchamber.test.handler
  (:use clojure.test
        peridot.core
        snapchamber.handler)
  (:require [snapchamber.test.test-util :as util]
            [cheshire.core :refer [generate-string
                                   parse-string]]))


(deftest test-routes
  (testing "main route"
    (let [response (:response
                     (-> (session app)
                         (request "/")))]
      (is (= (response :status) 200))))

  (testing "not-found route"
    (let [response (:response
                     (-> (session app)
                         (request "/invalid/route")))]
      (is (= (:status response) 404)))))


(deftest test-api
  (testing "get snap"
    (do
      ;; setup
      (util/drop-database!)
      (util/populate-snaps!)

      ;; assert

      ;; sucessful get
      (let [response (:response
                       (-> (session app)
                           (request "/api/snap/123"
                                    :request-method :get)))]
        (is (= "application/json;charset=UTF-8"
               (get (:headers response) "Content-Type")))

        (is (= (:status response) 200)))

      ;; failed get, resource does not exist
      (let [response (:response
                       (-> (session app)
                           (request "/api/snap/0014231"
                                    :request-method :get)))]
        (is (= "text/plain"
               (get (:headers response) "Content-Type")))
        (is (= (:status response) 404)))

      ;; cleanup
      (util/drop-database!)))

  (testing "save snap"
    (do
      ;; setup
      (util/drop-database!)
      (util/populate-snaps!)

      ;; assert

      ;; sucessful post
      (let [request-body "{\"imageData\": \"data:image/jpeg;base64,/9j/4AAQSkZJRgABAQAAAQABAX\"}"
            request (-> (session app)
                        (content-type "application/json;charset-UTF-8")
                        (request "/api/snap"
                                 :request-method :post
                                 :body request-body))
            response (:response request)]
        (is (= "application/json;charset=UTF-8"
               (get (:headers response) "Content-Type")))
        (is (= (:status response) 201)))


      ;; failed post, no imageData
      (let [request-body "{\"somethingElse\": \"lol\"}"
            request (-> (session app)
                        (content-type "application/json;charset-UTF-8")
                        (request "/api/snap"
                                 :request-method :post
                                 :body request-body))
            response (:response request)
            response-json (parse-string (response :body) true)]
        (is (= "application/json;charset=UTF-8"
               (get (:headers response) "Content-Type")))
        (is (not (= (:status response) 201)))
        (is (= (:status response) 400))
        (is (= "imageData required, and must be a string" (response-json :error))))


      ;; failed post, imageData too short
      (let [request-body "{\"imageData\": \"data:image\"}"
            request (-> (session app)
                        (content-type "application/json;charset-UTF-8")
                        (request "/api/snap"
                                 :request-method :post
                                 :body request-body))
            response (:response request)
            response-json (parse-string (response :body) true)]
        (is (= "application/json;charset=UTF-8"
               (get (:headers response) "Content-Type")))
        (is (not (= (:status response) 201)))
        (is (= (:status response) 400))
        (is (= "imageData too small" (response-json :error))))


      ;; failed post, imageData not bas64 jped
      (let [request-body "{\"imageData\": \"derpderpderpderpderpderpderpderpderpderp\"}"
            request (-> (session app)
                        (content-type "application/json;charset-UTF-8")
                        (request "/api/snap"
                                 :request-method :post
                                 :body request-body))
            response (:response request)
            response-json (parse-string (response :body) true)]
        (is (= "application/json;charset=UTF-8"
               (get (:headers response) "Content-Type")))
        (is (not (= (:status response) 201)))
        (is (= (:status response) 400))
        (is (= "imageData should be base64 jpeg" (response-json :error))))


      ;; failed post, imageData not a string
      (let [request-body "{\"imageData\": false}"
            request (-> (session app)
                        (content-type "application/json;charset-UTF-8")
                        (request "/api/snap"
                                 :request-method :post
                                 :body request-body))
            response (:response request)
            response-json (parse-string (response :body) true)]
        (is (= "application/json;charset=UTF-8"
               (get (:headers response) "Content-Type")))
        (is (not (= (:status response) 201)))
        (is (= (:status response) 400))
        (is (= "imageData required, and must be a string" (response-json :error))))

      ;; failed post, imageData matches existing image
      ;; cleanup
      (util/drop-database!))))
