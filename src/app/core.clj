(ns app.core)

(import (javax.crypto KeyGenerator SecretKey Cipher))
(import (javax.crypto.spec SecretKeySpec))
(import (java.io File FileOutputStream DataInputStream FileInputStream))
(import (java.util Properties))
(import (org.apache.commons.codec.binary Base64))
 
(def msg "Hello encryption world!")
(defn encode-base64 [raw] (. (new Base64) encode raw))
(defn decode-base64 [coded] (. (new Base64) decode coded))
 
(def aes (. KeyGenerator getInstance "AES"))
(def cipher (. Cipher getInstance "AES"))
(def encrypt (. Cipher ENCRYPT_MODE))
(def decrypt (. Cipher DECRYPT_MODE))
 
(defn writekey [rawkey filename]    
    ( let [out (new FileOutputStream (new File filename))]
          (do (. out write rawkey)
              (. out close))))
     
(defn readkey [filename]
    (let [file (new File filename)
          rawkey (byte-array (. file length))
          in  (new DataInputStream (new FileInputStream file))]
          (do (. in readFully rawkey)
              (. in close)
              rawkey
          )))
           
(defn get-propfile [filename]  
    (let [prop (new Properties)]
        (do (. prop load (new FileInputStream filename)))
        prop))
     
 
(defn genkey [keygen]
    (do (. keygen init  128)
        (. (. keygen generateKey ) getEncoded)
    )
)   
     
(defn do-encrypt [rawkey plaintext]
    (let [cipher (. Cipher getInstance "AES")]
        (do (. cipher init encrypt (new SecretKeySpec rawkey "AES"))
            (. cipher doFinal (. plaintext getBytes)))))
     
(defn do-decrypt [rawkey ciphertext]
    (let [cipher (. Cipher getInstance "AES")]
        (do (. cipher init  decrypt (new SecretKeySpec rawkey "AES"))
            (new String(. cipher doFinal ciphertext)))))
             
             
(defn get-password [key rawkey filename]
    (let [ props (get-propfile filename)
           coded (. props getProperty key)
           cipher (decode-base64 coded)]
         (do (do-decrypt rawkey cipher))))

(defn -main
    [& args]
    (println "HELLO WORLD"))
