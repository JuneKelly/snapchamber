(ns snapchamber.test.handler
  (:use clojure.test
        peridot.core
        snapchamber.handler)
  (:require [snapchamber.test.test-util :as util]))


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
;
;  (testing "save snap"
;    (do
;      ;; setup
;      (util/drop-database!)
;      (util/populate-snaps!)
;
;      ;; assert
;
;      ;; sucessful post
;      (let [response
;            (app (header
;                   (request :post "/api/snap"
;                            {:imageData "data:image/jpeg;base64,/9j/4AAQSkZJRgABAQAAAQABAD"})
;                   "Content-Type" "application-json"))]
;        (is (= "application/json;charset=UTF-8"
;               (get (:headers response) "Content-Type")))
;        (is (= (:status response) 200))
;        (is (= [:snapId] (keys response))))
;
;
;      ;; failed post, no imageData
;      ;; failed post, imageData too short
;      ;; failed post, imageData not bas64 jped
;      ;; failed post, imageData not a string
;
;      ;; failed post, imageData matches existing image
;      ;; cleanup
;      (util/drop-database!)))

)
