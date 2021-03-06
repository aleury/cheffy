(ns cheffy.recipe.db
  (:require [next.jdbc.sql :as sql]
            [next.jdbc :as jdbc]))

(defn find-all-recipes
  [db uid]
  (with-open [conn (jdbc/get-connection db)]
    (let [public (sql/find-by-keys conn :recipe {:public true})]
      (if uid
        (let [drafts (sql/find-by-keys conn :recipe {:public false :uid uid})]
          {:public public
           :drafts drafts})
        {:public public}))))

(defn find-recipe-by-id
  [db recipe-id]
  (with-open [conn (jdbc/get-connection db)]
    (let [recipe (sql/get-by-id conn :recipe recipe-id :recipe_id {})
          steps (sql/find-by-keys conn :step {:recipe_id recipe-id})
          ingredients (sql/find-by-keys conn :ingredient {:recipe_id recipe-id})]
      (when recipe
        (assoc recipe
          :recipe/steps steps
          :recipe/ingredients ingredients)))))

(defn insert-recipe!
  [db recipe]
  (sql/insert! db :recipe (assoc recipe :public false
                                        :favorite-count 0)))

(defn update-recipe!
  [db recipe]
  (->> (select-keys recipe [:recipe-id])
       (sql/update! db :recipe recipe)
       :next.jdbc/update-count
       (pos?)))


(defn delete-recipe!
  [db recipe-id]
  (->> (sql/delete! db :recipe {:recipe-id recipe-id})
       :next.jdbc/update-count
       (pos?)))