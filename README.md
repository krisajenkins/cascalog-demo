## About

A sketch. Hadoop->Cascalog->Clojure atom->Websocket->ClojureScript
Atom->D3->Your eyes. Buzzword-tastic, but also kind of cool. Edit a query and
watch the results unfold graphically before you. That's the idea. This is a
prototype.

## Usage

You want to try it out. Wow. Okay, you'll need to:

```sh
$ lein cljsbuild once                              # Build the Javascript
$ lein server                                      # Run a websocket server.
$ cd resources/public ; python -m SimpleHTTPServer # Run a web server.
```

...and then pray.
