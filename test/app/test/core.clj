(ns app.test.core
  (:use [app.core])
  (:use [clojure.test]))

(deftest replace-me ;; FIXME: write
  (def secretkey "secret")
  (println "key:" secretkey)
  (def encrypted (encrypt "MESSAGE" secretkey))
  (println "encrypted:" encrypted)
  (def decrypted (decrypt (:encrypted encrypted) secretkey))
  (println "decrypted:" decrypted)
  (println "HELLO WORLD")

  (def encrypted-random (encrypt-random "MESSAGE"))
  (println "encrypted-random:" encrypted-random)
  (def secretkey-random (:key-text encrypted-random))
  (println "secretkey-random:" secretkey-random)
  (def decrypted-random (decrypt (:encrypted encrypted-random) secretkey-random))
  (println "decrypted-random:" decrypted-random)
  (println "HELLO WORLD"))
