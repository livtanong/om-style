# om-style
Prerendered stylesheets from colocated styles for om.next

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
    [om-style :as os]))

(defui SomeComponent
  #?(:clj static)
  #?(:clj os/Style)
  #?(:clj
    (style [this]
      (list
        [:.some-class {:color "red"}]
        [:.other-class {:font-weight 300}
          [:a {:text-decoration 'none}]]))

  Object
  (render [this]
    (dom/div {:className "some-class"}
      (dom/div {:className "other-class"}
        (dom/a {:href "#"} "How Stylish")))))
```
