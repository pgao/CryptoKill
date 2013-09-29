(import (javax.crypto Cipher KeyGenerator SecretKey)
        (javax.crypto.spec SecretKeySpec)
        (java.security SecureRandom)
        (org.apache.commons.codec.binary Base64)
        (java.net URLEncoder)
        (java.net URLDecoder))

(defn bytes [s]
  (.getBytes s "UTF-8"))

(defn base64 [b]
  (Base64/encodeBase64String b))

(defn debase64 [s]
  (Base64/decodeBase64 (bytes s)))

; GET WITH PASSPHRASE

(defn get-key [passphrase]
  (let [keygen (KeyGenerator/getInstance "AES")
        sr (SecureRandom/getInstance "SHA1PRNG")]
    (.setSeed sr (bytes passphrase))
    (.init keygen 128 sr)
    (def x 
      (.. keygen generateKey getEncoded))
    ; (println x)
    x))

(defn get-cipher [mode key-text]
  (let [key-spec (SecretKeySpec. (get-key key-text) "AES")
        cipher (Cipher/getInstance "AES")]
    (.init cipher mode key-spec)
    cipher))

; GET RANDOM

(defn get-random-bytes [size]
  (let [seed (byte-array size)]
    (.nextBytes (SecureRandom/getInstance "SHA1PRNG") seed)
    seed))

(defn get-random-base64 [size]
  (String. (base64 (get-random-bytes size))))

; USER FUNCTIONS

(defn encrypt [text key-text]
  (let [bytes (bytes text)
        cipher (get-cipher Cipher/ENCRYPT_MODE key-text)]
    (println "ENCRYPT" text key-text)
    (def result {:encrypted (URLEncoder/encode (base64 (.doFinal cipher bytes)))})
    (println "ENCRYPT-RESULT" result)
    result))

(defn encrypt-random [text]
  (let [bytes (bytes text)
        key-text (get-random-base64 16)
        cipher (get-cipher Cipher/ENCRYPT_MODE key-text)]
    (println "ENCRYPT-RANDOM" text)
    (def result {:encrypted (URLEncoder/encode (base64 (.doFinal cipher bytes))), :keyText (URLEncoder/encode key-text)})
    (println "ENCRYPT-RANDOM-RESULT" result)
    result))

(defn decrypt [text key-text]
  (let [cipher (get-cipher Cipher/DECRYPT_MODE (URLDecoder/decode key-text))]
    (println "DECRYPT" text key-text)
    (def result {:decrypted (String. (.doFinal cipher (debase64 (URLDecoder/decode text))))})
    (println "DECRYPT-RESULT" result)
    result))
