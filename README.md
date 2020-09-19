# Todos

A traditional TodoMVC application to be used during Clojurescript, Reagent and
Re-frame training sessions.

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
