(ns cheffy.recipe.routes
  (:require [cheffy.recipe.handlers :as recipes]))

(defn routes
  [env]
  (let [db (:jdbc-url env)]
    ["/recipes" {:swagger {:tags ["recipes"]}}
     [""
      {:get (recipes/list-all-recipes db)
       :summary "List all recipes."}]
     ["/:recipe-id"
      {:get (recipes/retrieve-recipe db)
       :summary "Retrieve recipe."}]]))
