# Clojurescript start for browser

A starting point for a Clojurescript project (browser). This repository can be
used as a template for a new project.

## Getting started with this template

Before forking this repository, it's best to update the template itself
(updating the dependencies is a good start) and test that everything still
works.

After forking it's best to do the following steps to transform this template
into a custom project:

 * Edit [public/index.html](public/index.html) (title, lang, viewport, etc).
 * Move and rename clojurescript-start-for-browser.core namespace to something
   more suitable.
 * Edit or remove the styles in [scss/style.scss](scss/style.scss).
 * Add [Re-frame](https://github.com/day8/re-frame) to project.
 * Add favicons.
 * Finally delete this section from this README :)

## Development

The only requirements for running the project is Node.js / npm and Java.
Latest stable / LTS versions or newer should be fine.

First install dependencies with:

```bash
npm install
```

To start the development workflow:

```bash
npm run dev
```

This will start the live-reload workflow for both cljs and scss files. Cljs
files will be compiled to public/js and scss files to public/css/style.css.

## Production

To build for production:

```bash
rm -rf public/js public/css
npm run prod
```

Then the contents of ```public/``` directory can be hosted in production.
