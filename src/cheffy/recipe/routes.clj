(ns cheffy.recipe.routes
  (:require [cheffy.recipe.handlers :as recipes]))

(defn routes
  [env]
  (let [db (:jdbc-url env)]
    ["/recipes" {:swagger {:tags ["recipes"]}}
     [""
      {:get {:summary "List all recipes."
             :handler (recipes/list-all-recipes db)}}]
     ["/:recipe-id"
      {:get {:summary "Retrieve recipe."
             :parameters {:path {:recipe-id string?}}
             :handler (recipes/retrieve-recipe db)}}]]))
