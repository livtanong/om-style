(ns om-style.core
  (:require
   [om.next :as om]))

(defprotocol Style
  (style [this]))

(defn get-style
  "Gets the colocated style of either a component or a class.
  An analogue to om/get-query. Used for composing styles upwards."
  [x]
  (let [ctor (if (om/component? x)
               (om/react-type x) x)]
    (when-let [style (:style (meta ctor))]
      (with-meta (style ctor) {:component ctor}))))
