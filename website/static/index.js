import { Elm } from "../src/Main.elm";

require("typeface-archivo");
require("fontsource-mulish");

const app = Elm.Main.init({
  node: document.querySelector("main"),
  flags: {}
});
