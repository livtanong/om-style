(ns om-style.core
  (:require
   [om.next :as om]))

(defprotocol Style
  (style [this]))

(defn get-style
  "Gets the colocated style of either a component or a class
  Adds the class as meta"
  [x]
  (let [ctor (if (om/component? x)
               (om/react-type x) x)]
    (when-let [style (:style (meta ctor))]
      (with-meta (style ctor) {:component ctor}))))

;; keeping comments below for future use
#_(defn gen-css-namespace
    "Generate css namespace from om.next class or component.
  Replaces all dots with underscores. "
    [x]
    (let [class (if (om/component? x)
                  (om/react-type x) x)
          {:keys [component-name
                  component-ns]} (meta class)]
      (str
        (clojure.string/replace
          component-ns "." "_")
        "_"
        component-name)))

#_(defn qualify-selector
    "Given a class and keyword, qualify the keyword
  with the appropriate class namespace."
    [component-class css-classname]
    (str (if (keyword? css-classname) ".")
      (gen-css-namespace component-class)
      "_" (name css-classname)))
