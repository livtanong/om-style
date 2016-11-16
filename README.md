# om-style
A lightweight colocated style lib similar to om-css, focused on server-side prerendering of styles. It comes with a boot task and a protocol to be added to any om.next class.

[![Clojars Project](https://img.shields.io/clojars/v/om-style.svg)](https://clojars.org/om-style)

## Features
- Colocated styles, with an api similar to om.next's colocated queries
- Generate stylesheets you can serve

## Future
- namespaced, "locally scoped" classes

## Installation
```
[om-style "0.0.1"]
```

## Usage
```clj
;; some_component.cljc
(ns project.some-component
  (:require
    [om.next :as om :refer [defui]]
    [om-style.core :as os]
    [project.other :as other))

(defui SomeComponent
;; We keep the om-style interface in clj mode because:
;; 1. We only need the clj part to generate the stylesheet
;; 2. We want to keep the cljs file size low. 
  #?(:clj static)
  #?(:clj os/Style)
  #?(:clj
    (style [this]
      (list
        ;; Styles compose upwards to the root component just like queries.
        (os/get-style other/OtherComponent)
        [:.some-class {:color "red"}]
        [:.other-class {:font-weight 300}
          [:a {:text-decoration 'none}]]))

  Object
  (render [this]
    (dom/div {:className "some-class"}
      (dom/div {:className "other-class"}
        (dom/a {:href "#"} "How Stylish")))))

;; build.boot
...
(require
  ...
  '[om-style.boot-om-style :refer [om-style]]
  '[project.some-component])

(deftask dev []
  (comp
    (serve)
    (watch)
    (reload)
    (speak)
    (om-style 
      :root-class 'project.some-component/SomeComponent
      :root-style 'project.styles/base ;; global stuff like typography, resets, etc...
      :output-to "css/styles.css")
    (cljs :optimizations :none)))
```
