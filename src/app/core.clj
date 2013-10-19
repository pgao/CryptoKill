(ns app.core
  (:use compojure.core
        ring.adapter.jetty)
  (:require [compojure.handler :as handler]
            [compojure.route :as route]
            [ring.util.response :as response]
            [clojure.data.json :as json])
  (:load "crypto"))

(defroutes app-routes
  (GET "/" [] (response/file-response "index.html" {:root "resources/public"}))
  (GET "/encrypt" {params :params} 
    (if (contains? params :keyText) 
      (json/write-str (encrypt (:text params) (:keyText params)))
      (json/write-str (encrypt-random (:text params)))))
  (GET "/encrypt-random" {params :params} (json/write-str (encrypt-random (:text params))))
  (GET "/decrypt" {params :params} (json/write-str (decrypt (:text params) (:keyText params))))
  (route/resources "/")
  (route/not-found "Page not found"))

(defn -main [& args]
  (run-jetty (handler/site app-routes) {:port 5000}))
