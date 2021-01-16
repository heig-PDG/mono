# Video

## Produit (GUY-LAURENT PARLE)

- **Graphismes** : Mockup 3D de l'application, avec l'affichage de la pile de recettes.
+ Bienvenue sur Tupperdate.me. Tupperdate.me est une application permettant de partager des recettes et des moments avec d'autres personnes. Elle est disponible sur les smartphones Android.
- **MATTHIEU PARLE**
- **Graphismes** : Jump cut vers l'écran d'accueil
+ Pour utiliser l'application, il suffit de rentrer son numéro de téléphone. Ensuite, votre compte est vérifié par un captcha et par SMS. Ces étapes servent à tous nous protéger du spam. Vous rentrez ensuite votre nom, ajoutez une jolie image de profil, et c'est parti !
+ L'écran d'accueil affiche la liste des recettes entrées par les autres utilisateurs. Si une recette vous plaît, glissez-là sur la droite. Si elle ne vous plaît pas, glissez-là vers la gauche. Mais rassurez-vous - si vous venez de rater la recette avec laquelle vous souhaiteriez partager votre vie, un petit bouton vous permet de revenir en arrière.
+ Regardons maintenant quelles conversations nous avons. Oulà, pas grand-monde ! C'est normal, nous n'avons pas encore créé de recettes. Un match apparaît quand deux utilisateurs likent des recettes de manière réciproque.
+ Revenons donc à l'écran principal et créons une recette. Aujourd'hui, nous mangeons du canard en plastique, qui est donc froid et sans allergènes. Lorsque nous soumettrons la recette, elle apparaîtra automatiquement sur les appareils des autres utilisateurs.
+ Tiens, un utilisateur a aimé notre recette ! Nous pouvons commencer à discuter en cliquant sur "Start chatting".

+ **TODO** démonstration du chat

## Infrastructure (ALEXANDRE PARLE)

- **Graphismes** : Schémas de l'architecture, avec des plans de coupe quand on parle de certaines fonctionnalités.

+ Notre application mobile utilise Jetpack Compose, un nouveau framework réactive pour les interfaces graphiques sur Android. Jetpack Compose a eu sa première alpha release 3 semaines avant le début de notre projet. Aujourd'hui, le framework est toujours en alpha, avec une nouvelle release (et des des breaking changes) toutes les deux semaines. Nous utilisons aussi des ViewModel et les Android Architecture Components pour gérer la navigation à l'intérieur de l'application.
+ Un objectif principal du projet était de concevoir un système réactif, qui ne demande pas à l'utilisateur de manuellement rafraîchir les écrans pour voir de nouvelles données s'afficher. Pour ce faire, nous stockons toutes les données (profils, recettes, messages, conversations, matchs) dans une base de données SQLite, via la bibliothèque AndroidX Room.
+ La base de données est la seule source de vérité de notre client mobile ! Quand un utilisateur veut afficher la liste des recettes, il demande à notre système de synhronisation (la bibliothèque Store) les données déjà présentes dans la base de données locales. Ainsi, des résultats s'affichent immédiatement à l'écran. En parallèle, des requêtes peuvent être lancées à l'API REST, afin de télécharger les éventuels changements et les appliquer à notre base de données.
+ Cette architecture nous permet de supporter une fonctionnalité avancée : l'utilisation de l'application sans connectivité. On peut liker, disliker des recettes avec une connectivité médiocre, et ces changements seront persistés t appliqués localement puis synchronisé quand le serveur sera de nouveau disponible.

+ Notre serveur d'application expose une API REST, est écrit en Kotlin (comme l'application Android) et utilise Firestore comme un système de stockage NoSQL. Le processus d'authentification des utilisateurs se fait via les outils Firebase Auth, qui nous permettent d'authentifier des requêtes sur l'API via un token JWT.
+ Quand des données changent sur le serveur, les clients concernés sont notifiés via Firebase Cloud Messaging. Ainsi, ils peuvent lancer des tâches de synchronisation, mettant à jour le contenu si l'utilisateur est actuellement dans l'application. Si l'application n'est pas en premier-plan, une tâche de synchronisation du contenu est schedulée par l'OS. Ce scheduling est fait de manière intelligente et efficace pour la consommation de données réseau et d'énergie. Ainsi, quand l'utilisateur utilisera l'application (le lendemain, ou la semaine suivante), les données seront déjà à jour !

## Déploiement et CI (DAVID PARLE)

- **Graphismes** : Navigation sur GitHub, spec.tupperdate.me, le Wiki et Telegram

## Collaboration (GUY-LAURENT PARLE)

## Améliorations futures
