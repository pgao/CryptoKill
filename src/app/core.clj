(ns app.core
  (:use ring.adapter.jetty)
  (:use compojure.core)
  (:require [compojure.route :as route])
  (:load "crypto"))

(defroutes app
  (GET "/" [] "OH HELLO THERE")
  (route/not-found "<h1>Page not found</h1>"))

(defn -main
  [& args]
  (defonce server (run-jetty app {:port 8000 :join? false}))
  (def secretkey "secret")
  (println "key:" secretkey)
  (def encrypted (encrypt "MESSAGE" secretkey))
  (println "encrypted:" encrypted)
  (def decrypted (decrypt encrypted secretkey))
  (println "decrypted:" decrypted)
  (println "HELLO WORLD"))
