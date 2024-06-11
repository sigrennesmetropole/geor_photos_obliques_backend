# Pour les utilisateurs dockers :

Le présent répertoire comporte :

* Un fichier modèle de propriétés permettant de configurer l'application [photos-obliques.properties](photos-obliques.properties)
* Un fragment docker-compose permettant d'intégrer le backend dans une infrastructure docker [docker-compose.yml](docker-compose.yml)
* Un répertoire **template** contenant des modèles à adapter à chaque infrastructure pour la création d'image docker
* Un répertoire **rennes-metropoles** contenant les templates permettant la généréation d'image dans `https://hub.docker.com/r/sigrennesmetropole/geor_photos_obliques_backend`
