(ns user
  (:require [integrant.repl :as ig-repl]
            [integrant.core :as ig]
            [integrant.repl.state :as state]
            [cheffy.server]
            [next.jdbc :as jdbc]
            [next.jdbc.sql :as sql]
            [cheffy.recipe.db :as recipes]))

(ig-repl/set-prep!
  (fn [] (-> "resources/config.edn" slurp ig/read-string)))

(def go ig-repl/go)
(def halt ig-repl/halt)
(def reset ig-repl/reset)
(def reset-all ig-repl/reset-all)

(def app (-> state/system :cheffy/app))
(def db (-> state/system :db/postgres))

(comment
  (recipes/update-recipe! db {:recipe-id "7e6acead-8b62-4c7a-9ab7-02881d33ad6d"
                              :name      "my-recipe-name"})

  (recipes/delete-recipe! db "285a939a-3e95-4917-8b2b-d1a5a4853db4")

  (sql/delete! db :recipe {:recipe-id "0cafb320-a21c-4793-932e-e4a07f2a09eb"})

  (let [where-params {:recipe-id "7e6acead-8b62-4c7a-9ab7-02881d33ad6d"}
        result (sql/update! db :recipe {:name "my-updated-recipe-again-again"}
                            where-params)]
    (= (:next.jdbc/update-count result) 1))

  (-> (app {:request-method :get
            :uri            "/v1/recipes/65a3e960-0134-4136-8cfa-a7cd2bbee0c5"})
      :body
      (slurp))
  (-> (app {:request-method :post
            :uri            "/v1/recipes"
            :body-params    {:name      "my recipe"
                             :prep-time 49
                             :img       "img-url"}})
      :body
      (slurp))
  (go)
  (halt)
  (reset))