(ns cheffy.recipe.handlers
  (:require [cheffy.recipe.db :as recipe-db]
            [ring.util.response :as rr]
            [cheffy.recipe.responses :as responses])
  (:import (java.util UUID)))

(defn list-all-recipes
  [db]
  (fn [_request]
    (let [uid "auth0|5ef440986e8fbb001355fd9c"
          recipes (recipe-db/find-all-recipes db uid)]
      (rr/response recipes))))

(defn retrieve-recipe
  [db]
  (fn [request]
    (let [recipe-id (get-in request [:parameters :path :recipe-id])
          recipe (recipe-db/find-recipe-by-id db recipe-id)]
      (if recipe
        (rr/response recipe)
        (rr/not-found {:type    "recipe-not-found"
                       :message "Recipe not found"
                       :data    (str "recipe-id " recipe-id)})))))

(defn create-recipe!
  [db]
  (fn [request]
    (let [recipe-id (str (UUID/randomUUID))
          uid "auth0|5ef440986e8fbb001355fd9c"
          recipe (get-in request [:parameters :body])]
      (recipe-db/insert-recipe! db (assoc recipe :recipe-id recipe-id :uid uid))
      (rr/created (str responses/base-url "/recipes/" recipe-id)
                  {:recipe-id recipe-id}))))

(defn update-recipe!
  [db]
  (fn [request]
    (let [recipe-id (get-in request [:parameters :path :recipe-id])
          recipe (get-in request [:parameters :body])
          updated? (recipe-db/update-recipe! db (assoc recipe :recipe-id recipe-id))]
      (if updated?
        (rr/status 204)
        (rr/not-found {:recipe-id recipe-id})))))


(defn delete-recipe!
  [db]
  (fn [request]
    (let [recipe-id (get-in request [:parameters :path :recipe-id])
          deleted? (recipe-db/delete-recipe! db recipe-id)]
      (if deleted?
        (rr/status 204)
        (rr/not-found {:recipe-id recipe-id})))))