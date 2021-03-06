(ns cheffy.recipes-test
  (:require [clojure.test :refer :all]
            [cheffy.server :refer :all]
            [cheffy.test-system :as ts]))

(deftest recipes-tests
  (testing "List recipes"
    (testing "with auth -- public and drafts"
      (let [{:keys [status body]} (ts/test-endpoint :get "/v1/recipes")]
        (is (= 200 status))
        (is (vector? (:public body)))
        (is (vector? (:drafts body)))))

    #_(testing "without auth -- public"
      (let [{:keys [status body]} (ts/test-endpoint :get "/v1/recipes")]
        (is (= 200 status))
        (is (vector? (:public body)))
        (is (nil? (:drafts body)))))))