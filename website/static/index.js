import { Elm } from "../src/Main.elm";

const app = Elm.Main.init({
  node: document.querySelector("main"),
  flags: {}
});
