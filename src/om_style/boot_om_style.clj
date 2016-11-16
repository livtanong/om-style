(ns om-style.boot-om-style
  {:boot/export-tasks true}
  (:require
   [boot.core :as boot :refer [deftask]]
   [boot.pod :as pod]
   [boot.util :as util]
   [clojure.java.io :as io]
   [om-style.core :as os]))

(defn add-dep [env dep]
  (update-in env [:dependencies] (fnil conj []) dep))

(defn ns-tracker-pod []
  (->> '[[ns-tracker "0.3.0"] [org.clojure/tools.namespace "0.2.11"]]
    (assoc (boot/get-env) :dependencies)
    pod/make-pod))

(defonce garden-pods
  (pod/pod-pool
    (add-dep (boot/get-env) '[garden "1.3.0"])
    :init (fn [pod] (pod/require-in pod 'garden.core))))

(deftask om-style
  "Generate a stylesheet based on the
colocated styles of a root om.next class"
  [r root SYM sym "The root om.next class whose styles are to be generated"
   b base-style SYM sym "A base style to be included alongside the styles defined by root-class"
   o output-to  PATH str "The output css file path relative to docroot."
   p pretty-print bool "Pretty print compiled CSS"]
  (let [tmp (boot/tmp-dir!)
        output-path (or output-to "main.css")
        out-file (io/file tmp output-path)
        src-paths (vec (boot/get-env :source-paths))
        ns-sym (symbol (namespace root))
        ns-pod (ns-tracker-pod)]
    (pod/with-eval-in ns-pod
      (require 'ns-tracker.core)
      (def cns (ns-tracker.core/ns-tracker ~src-paths)))
    (boot/with-pre-wrap fileset
      (boot/empty-dir! tmp)
      (let [changed-ns (pod/with-eval-in ns-pod (cns))
            garden-pod (garden-pods :refresh)]
        (doseq [n changed-ns]
          (require n :reload))
        (util/info "Compiling %s...\n" (.getName out-file))
        (io/make-parents out-file)
        (let [out-style (when (resolve root)
                          (->> root
                            resolve
                            var-get
                            os/get-style))
              base-style (when (resolve base-style)
                           (-> base-style resolve var-get))]
          (pod/with-eval-in garden-pod
            (garden.core/css
              {:pretty-print? ~pretty-print
               :output-to ~(.getPath out-file)}
              '~base-style
              '~out-style))))
      (-> fileset
        (boot/add-resource tmp)
        boot/commit!))))
