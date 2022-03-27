(ns cheffy.recipe.routes
  (:require [cheffy.recipe.handlers :as recipes]
            [cheffy.recipe.responses :as responses]))

(defn routes
  [env]
  (let [db (:jdbc-url env)]
    ["/recipes" {:swagger {:tags ["recipes"]}}
     [""
      {:get {:summary   "List all recipes."
             :responses {200 {:body responses/recipes}}
             :handler   (recipes/list-all-recipes db)}}]
     ["/:recipe-id"
      {:get {:summary    "Retrieve recipe."
             :parameters {:path {:recipe-id string?}}
             :responses  {200 {:body responses/recipe}}
             :handler    (recipes/retrieve-recipe db)}}]]))
