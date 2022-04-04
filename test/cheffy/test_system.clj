(ns cheffy.test-system
  (:require [clojure.test :refer :all]
            [integrant.repl.state :as state]
            [ring.mock.request :as mock]
            [muuntaja.core :as m]))

(defn test-endpoint
  ([method uri]
   (test-endpoint method uri nil))
  ([method uri opts]
   (let [app (-> state/system :cheffy/app)
         response (app (-> (mock/request method uri)
                           (cond-> (:body opts) (mock/json-body {:body opts}))))]
     (update response :body (partial m/decode "application/json")))))

(comment
  (test-endpoint :get "/v1/recipes")
  (test-endpoint :post "/v1/recipes" {:img "img-url"
                                      :name "recipe name"
                                      :prep-time 30}))