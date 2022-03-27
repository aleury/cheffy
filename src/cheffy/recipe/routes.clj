(ns cheffy.recipe.routes
  (:require [cheffy.recipe.handlers :as recipes]
            [cheffy.recipe.responses :as responses]))

(defn routes
  [env]
  (let [db (:jdbc-url env)]
    ["/recipes" {:swagger {:tags ["recipes"]}}
     [""
      {:get  {:summary   "List all recipes."
              :responses {200 {:body responses/recipes}}
              :handler   (recipes/list-all-recipes db)}
       :post {:summary    "Create a recipe."
              :parameters {:body {:name      string?
                                  :prep-time number?
                                  :img       string?}}
              :responses  {201 {:body {:recipe-id string?}}}
              :handler    (recipes/create-recipe! db)}}]
     ["/:recipe-id"
      {:get {:summary    "Retrieve a recipe."
             :parameters {:path {:recipe-id string?}}
             :responses  {200 {:body responses/recipe}}
             :handler    (recipes/retrieve-recipe db)}
       :put {:summary    "Update a recipe."
             :parameters {:path {:recipe-id string?}
                          :body {:name      string?
                                 :prep-time int?
                                 :public    boolean?
                                 :img       string?}}
             :responses  {204 {:body nil?}}
             :handler    (recipes/update-recipe! db)}}]]))
