# Recipes sync

Recipes uses [Room](https://developer.android.com/training/data-storage/room) as an internal
source of truth. Fetches recipes are stored locally, as well as pending changes that still require
server sync.

## Tables

There are 3 main SQL tables to manage recipes :

- `RecipeEntity`, which stores server-issued recipes. These will generally be displayed in the main
  stack, and in recipe details when these are clicked.
- `PendingRateRecipeEntity` which stores pending like and dislike actions to the server. Like and
  dislike requests are therefore first stored in this temporary table, and then synced whenever the
  device eventually reaches the network.
- `PendingNewRecipeEntity`, which stores pending new recipe requests. These recipes are still on
  their way to the server, and eventually get delivered whenever the device syncs.
