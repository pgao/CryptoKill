(ns app.core)

(load "crypto")
(use '[ring.adapter.jetty :only [run-jetty]])

(defn app [req]
  {:status 200
   :headers {"Content-Type" "text/plain"}
   :body "Hello world!"})

(defn -main
  [& args]
  (defonce server (run-jetty app {:port 8000 :join? false}))
  (def secretkey "secret")
  (println "key: " secretkey)
  (def encrypted (encrypt "MESSAGE" secretkey))
  (println "encrypted: " encrypted)
  (def decrypted (decrypt encrypted secretkey))
  (println "decrypted: " decrypted)
  (println "HELLO WORLD"))
