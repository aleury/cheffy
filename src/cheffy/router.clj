(ns cheffy.router
  (:require [reitit.ring :as ring]
            [cheffy.recipe.routes :as recipe]
            [reitit.swagger :as swagger]
            [reitit.swagger-ui :as swagger-ui]
            [reitit.coercion.spec :as coercion-spec]
            [reitit.ring.coercion :as coercion]
            [reitit.ring.middleware.exception :as exception]
            [reitit.ring.middleware.muuntaja :as muuntaja]
            [muuntaja.core :as m]))

(def swagger-docs
  ["/swagger.json"
   {:get
    {:no-doc  true
     :swagger {:basePath "/"
               :info     {:title       "Cheffy API Reference"
                          :description "The Cheffy API is organized around REST. Returns JSON, Transit (msgpack, json), or EDN encoded responses."
                          :version     "1.0.0"}}
     :handler (swagger/create-swagger-handler)}}])

(defn v1 [env]
  ["/v1"
   (recipe/routes env)])

(def router-config
  {:data {:muuntaja   m/instance
          :coercion   coercion-spec/coercion
          :middleware [swagger/swagger-feature
                       muuntaja/format-middleware
                       exception/exception-middleware
                       coercion/coerce-request-middleware
                       coercion/coerce-response-middleware]}})

(defn routes
  [env]
  (ring/ring-handler
    (ring/router
      [swagger-docs
       (v1 env)]
      router-config)
    (ring/routes
      (swagger-ui/create-swagger-ui-handler {:path "/"}))))
