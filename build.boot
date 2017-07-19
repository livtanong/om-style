(def +version+ "0.1.1")

(set-env!
  :source-paths #{"src"}
  :dependencies
  '[[org.clojure/clojure         "1.9.0-alpha17"  :scope "provided"]
    [org.omcljs/om               "1.0.0-beta1"  #_:scope #_"provided"
     :exclusions [cljsjs/react]]

    [com.cemerick/piggieback     "0.2.1"          :scope "test"]
    [pandeiro/boot-http          "0.7.3"          :scope "test"]
    [adzerk/boot-cljs            "1.7.228-1"      :scope "test"]
    [adzerk/boot-cljs-repl       "0.3.3"          :scope "test"]
    [adzerk/boot-test            "1.1.2"          :scope "test"]
    [crisptrutski/boot-cljs-test "0.2.2-SNAPSHOT" :scope "test"]
    [adzerk/boot-reload          "0.4.12"         :scope "test"]
    [adzerk/bootlaces            "0.1.13"         :scope "test"]
    [org.clojure/tools.nrepl     "0.2.12"         :scope "test"]
    [org.clojure/tools.namespace "0.3.0-alpha3"   :scope "test"]
    [weasel                      "0.7.0"          :scope "test"]
    [boot-codox                  "0.10.0"         :scope "test"]

    [devcards "0.2.2" :scope "test"
     :exclusions [cljsjs/react cljsjs/react-dom]]])

(require
  '[adzerk.boot-cljs      :refer [cljs]]
  '[adzerk.boot-cljs-repl :as cr :refer [cljs-repl start-repl]]
  '[adzerk.boot-reload    :refer [reload]]
  '[adzerk.boot-test :as bt-clj]
  '[adzerk.bootlaces :as bootlaces :refer [bootlaces! push-release push-snapshot]]
  '[clojure.tools.namespace.repl :as repl]
  '[crisptrutski.boot-cljs-test :as bt-cljs]
  '[pandeiro.boot-http :refer [serve]]
  '[codox.boot :refer [codox]])

(bootlaces! +version+)

(task-options!
  pom {:project 'com.levitanong/om-style
       :version +version+
       :description "Prerendered stylesheets from colocated styles for om.next"
       :url "https://github.com/levitanong/om-style"
       :scm {:url "https://github.com/levitanong/om-style"}
       :license {"name" "MIT License"
                 "url" "https://opensource.org/licenses/MIT"}})

(deftask build-jar []
  (bootlaces/build-jar))

(deftask devcards []
  (set-env! :source-paths #(conj % "src/devcards"))
  (comp
    (serve)
    (watch)
    (cljs-repl)
    (reload)
    (speak)
    (cljs :source-map true
      :compiler-options {:devcards true
                         :parallel-build true}
      :ids #{"js/devcards"})
    (target :dir #{"target"})))
