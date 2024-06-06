# geor_photos_obliques_backend
Backend du module Photos Obliques de Rennes Métropole

## I Mise en oeuvre de la Base de données

L'application nécessite la création de deux schémas distincts dans la base de données. 
Le premier, **_phototheque_**, contient toutes les informations sur les photos. 
Le second, **_photos_obliques_stats_**, permet de stocker les différentes statistiques générées par le backend de l'application.

### I.1 Phototheque

Si le schéma Phototheque n'existe pas, vous pouvez le créer en exécutant le script _create.sql_ présent dans le dossier `resources/bdd/phototheque`.
Ce script va créer le schéma ainsi que 2 utilisateurs. Un utilisateur admin, et un second qui pourra simplement y accéder. 
Vous pouvez modifier ce script pour changer les noms d'utilisateur et mots de passe défaut.

Vous pouvez ensuite execute le script initialisation.sql qui créera la table _photo_oblique_.

### I.2 Statistiques

De la même façon que pour le schéma phototheque, éxécutez les scripts du dossier 'resources/bdd/phototheque' pour créer le schéma **_photos_obliques_stats_** 
puis initialiser la table _statiques_.

## II Configuration de l'application

### II.1 Propriétés de connexion 

Une fois les schémas créés, configurez les propriétés de connexion à la base de données dans le fichier de configuration de l'application Spring **photos-obliques.properties**.

Pour chaque scéma indiquer l'url d'accès sur les propriétés suivante :

```properties
    spring.phototheque.datasource.jdbc-url
    spring.stats.datasource.jdbc-url
```

Si vous avez modifié les noms d'utilisateur et mots de passe, modifiez également ces propriétés :

```properties
    spring.phototheque.datasource.username
    spring.phototheque.datasource.password
    spring.stats.datasource.username
    spring.stats.datasource.password
```

### II.2 Propriétés d'application

Toujours dans le fichier **photos-obliques.properties**, c'est également ici que vous pourrez configurer les différents paramètres de l'application comme suit:
```properties
    photos-obliques.tolerance.angle= tolerance de l'angle lors de la recherche (en degré)
    photos-obliques.url.vignette= url du serveur contenant les vignettes
    photos-obliques.url.apercu= url du serveur contenant les apercus
    photos-obliques.acces.photohd= Path du dossier contenant les photos hautes qualités
    photos-obliques.panier.max.photos= 200 par défaut
    server.port= port de l'application 
```

### II.3  Configuration du certificat

Un script est lancé au déploiement de l'image docker de l'application qui ajoute un certificat donné au keystore.  
Afin d'ajouter le bon certificat au bon keystore, il est nécessaire de remplir les informations adéquates dans le fichier `properties` de l'application :

```yaml
# dossier contenant le certificat
server.trustcert.keystore.path=
# filename du certificat
server.trustcert.keystore.cert=
# nom de l'alias du certificat à insérer dans le keystore
server.trustcert.keystore.alias=
# chemin absolu du keystore dans le container docker
server.trustcert.keystore.store=
# mot de passe du keystore
server.trustcert.keystore.password=
```

Par exemple :
```
server.trustcert.keystore.path=/etc/georchestra/
server.trustcert.keystore.cert=photobliques.crt
server.trustcert.keystore.alias=certificat-photobliques
server.trustcert.keystore.store=/usr/local/openjdk-11/lib/security/cacerts
server.trustcert.keystore.password=changeit
```

Si les variables ne sont pas remplies, le certificat n'est pas ajouté au keystore et l'application démarre normalement.

Le certificat dont le nom est renseigné doit être déposé dans `<...>/config/photos-obliques/` (le répertoire <...>/config/ qui est monté sur le répertoire /etc/georchestra dans le container)

## III Intégration dans Georchestra

### III.1 Modification du security proxy pour Photos Obliques

Ajouter dans le fichier *target-mapping.properties* une ligne permettant de déclarer le backend de photos oblique au security proxy. 

Déclarez le backend avec une ligne de la forme :

```properties
    photosobliques=http(s)://<hôte du backend>:<port>/photosobliques
```
**Example** : ```photosobliques=http://signalement:8080/signalement```


Ajouter dans le fichier *security-mapping.xml* une ligne permettant de configurer les roles qui auront l'accès au backend :
```xml
<intercept-url pattern="/photosobliques/.*" access=<Liste des rôles>/>
```

Le Swagger sera accessible sur ```https://<serveur_georchestra>:<port>/photosobliques/swagger-ui.html"```. Il n'est pas possible de configurer cet url.

## IV Démarrage du service

### IV.1 - Intégration *gitHub Rennes métropole*

Lors de la mise à jour du repository git `https://github.com/sigrennesmetropole/geor_photos_obliques_backend`, des actions Gits sont déclenchées afin :

- De builder les différents composants
- De pousser sur `https://hub.docker.com/r/sigrennesmetropole/geor_photos_obliques_backend` les images dockers du backend et de l'application back-office


### IV.2 Docker compose

Deux options s'offrent à vous pour exécuter l'application avec Docker Compose :

1. Créer un nouveau fichier docker-compose.yml

Si vous souhaitez démarrer l'application sur un nouveau serveur, vous pouvez créer un nouveau fichier docker-compose.yml dédié à cette application. 
Ce fichier définira la configuration des conteneurs Docker nécessaires à l'exécution de l'application.

2. Intégrer l'application dans un fichier docker-compose.yml existant

Si vous souhaitez utiliser docker sur le même serveur que les autres application, vous pouvez intégrer cette application dans votre fichier docker-compose.yml existant.
Pour ce faire, il vous faudra ajouter une nouvelle section au fichier définissant les conteneurs et les volumes nécessaires à l'exécution de l'application.


Voici un example d'intégration de photos obliques dans un docker-compose.yml

```yaml
volumes:
    photosobliques_data:

services:
    photosobliques:
        image: sigrennesmetropole/geor_photos_obliques_backend:<version de l'image, example :v1.0.0>
        ports:
            - "<photosobliques_server_expose_port>:<photosobliques_server_port>"      
        volumes:
            - ./config:/etc/georchestra
            - ./photosobliques_data:/opt/photosobliques/data
        environment:
            - JAVA_OPTIONS=-Dorg.eclipse.jetty.annotations.AnnotationParser.LEVEL=OFF
            - ADD_JAVA_OPTS=
            - XMX=1G
```

### IV.3 configuration

Pour finaliser la configuration du projet et permettre le lancement de l'application, il est nécessaire d'ajouter le fichier photos-obliques.properties. 
Ce fichier contient les informations de configuration spécifiques à l'application. Pour plus d'informations sur la configuration de ce fichier, veuillez vous référer à la première partie du README.

Un exemple complet de configuration du fichier photos-obliques.properties est disponible dans le répertoire resources/example. 
Cet exemple peut vous servir de guide pour configurer votre propre fichier.
