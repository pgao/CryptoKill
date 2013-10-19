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
    (println "passphrase" passphrase)
    (println "GETKEY RESULT" (String. x))
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
    (println "decoded key-text" (URLDecoder/decode key-text))

    (def encryptedBytes (.doFinal cipher bytes))
    (def base64d (base64 encryptedBytes))

    (println "base64d" base64d)

    (def result {:encrypted base64d, :keyText key-text})

    (println "ENCRYPT-RESULT" result "\n")
    result))

(defn encrypt-random [text]
  (let [bytes (bytes text)
        key-text (get-random-base64 16)
        cipher (get-cipher Cipher/ENCRYPT_MODE key-text)]
    
    (println "ENCRYPT-RANDOM" text)
    
    (def result {:encrypted (base64 (.doFinal cipher bytes)), :keyText key-text})
    
    (println "ENCRYPT-RANDOM-RESULT" result "\n")
    result))

(defn decrypt [text key-text]
  (let [bytes (bytes text)
        cipher (get-cipher Cipher/DECRYPT_MODE (URLDecoder/decode key-text))]

    (println "DECRYPT" text key-text)
    (println "decoded key-text" (URLDecoder/decode key-text))
    (println "debase64d" (debase64 (URLDecoder/decode text)))

    (def result {:decrypted (String. (.doFinal cipher (debase64 text))), :keyText key-text})
    
    (println "DECRYPT-RESULT" result "\n")
    result))
