
# geor_photos_obliques_backend
Backend du module Photos Obliques de Rennes Métropole

## I Mise en oeuvre de la Base de données

L'application nécessite la création de deux schémas distincts dans la base de données. 
Le premier, **phototheque**, contient toutes les informations sur les photos. 
Le second, **backend_stats**, permet de stocker les différentes statistiques générées par le backend de l'application.

Ces 2 schémas utilisent respectivement des rôles nommés "photos_obliques" et "georchestra".
Si vous ne les posséder pas, vous pouvez :
- les créer dans votre base de données et leur donner tous les privilèges.
- sinon remplacer dans les scripts *[create.sql](resources/bdd/phototheque/create.sql)* et
*[create.sql](resources/bdd/statistiques/create.sql)* la ligne de création avec d'autres rôles comme ici
"CREATE SCHEMA le_schema AUTHORIZATION votre_nouveau_role;"

**/!\L'utilisation d'une base de données nommée "geochestra" avec les rôles propriétaires "photos_obliques" et "georchestra" est recommandée pour l'utilisation de plugin mais n'est pas obligatoire.**

Vous pouvez ultérieurement ajouter des rôles et leur céder des privilèges avec des GRANT comme ici:
- GRANT ALL ON TABLE photos_obliques_stats.statistiques TO votre_role;

Cependant soyez prudent car cela peut créer des failles de sécurité si les permissions ne sont pas attribuées avec précaution.

### I.1 Phototheque

Les ressources nécessaires à la création des éléments relatifs à **phototheque** se trouvent dans le dossier *resources/bdd/phototheque*.

Le script *[create.sql](resources/bdd/phototheque/create.sql)* permet de créer :

- Le schéma
- Un utilisateur propriétaire du schéma nommé *photos_obliques*, 
- Un utilisateur en lecture seule sur le schéma nommé *consult*. 

Vous pouvez modifier ce script pour changer les noms d'utilisateur et les mots de passe par défaut.

Le script *[initialisation.sql](resources/bdd/phototheque/initialisation.sql)* permet de créer la table *photo_oblique* dans le schéma précédemment créé.

### I.2 Statistiques

De la même façon que pour le schéma phototheque, éxécutez les scripts du dossier 'resources/bdd/phototheque' pour créer le schéma **backend_stats** 
puis initialiser la table *statiques*.

Les ressources nécessaires à la création des éléments relatifs à **backend_stats** se trouvent dans le dossier *resources/bdd/statistiques*.

Le script *[create.sql](resources/bdd/statistiques/create.sql)* permet de créer :

- Le schéma
- Un utilisateur nommé *stats*, 

Vous pouvez modifier ce script pour changer le nom d'utilisateur et le mot de passe par défaut.

Le user configuré pour accéder à la source de données des statistiques (spring.stats.datasource.username), doit être configuré de manière à ce que le schéma dans lequel se trouve la table statistique soit son schéma par défaut. Ceci peut-être réalisé comme suit :
- ALTER USER stats SET search_path TO backend_stats, public;

Vérifiez que le fichier *[create.sql](resources/bdd/statistiques/create.sql)* qui contient cette ligne est donc à jour vis-à-vis de votre infrastructure.

Le script *[initialisation.sql](resources/bdd/statistiques/initialisation.sql)* permet de créer la table *statistiques* 
dans le schéma précédemment créé.

## II Configuration de l'application

Un template complet de fichier de propriétés est présent dans le dossier **[docker/photos-obliques.properties](docker/photos-obliques.properties)**.

### II.1 Propriétés de connexion aux bases de données

Les propriétés suivantes permettent de configurer l'accès aux 2 schémas utilisés par l'application :

```properties
  spring.phototheque.datasource.jdbc-url=jdbc:postgresql://${pgsqlHost}:${pgsqlPort}/${pgsqlDatabase}?currentSchema=photos_obliques&ApplicationName=georchestra
  spring.phototheque.datasource.username=<username en consultation sur le schema photos_obliques>
  spring.phototheque.datasource.password=<à définir>

  spring.stats.datasource.jdbc-url=jdbc:postgresql://${pgsqlHost}:${pgsqlPort}/${pgsqlDatabase}?ApplicationName=georchestra
  spring.stats.datasource.username=<username en accès sur le schéma backend_stats>
  spring.stats.datasource.password=<à définir>

```

**Remarque :**

- Les variables *pgsqlHost*, *pgsqlPort*, *pgsqlDatabase* vont référence à des variables par défaut présentes dans le fichier de configuration *default.properties* de GeOrchestra.

### II.2 Propriétés d'application

Les propriétés suivantes permettent de configurer l'application Photos-Obliques :

```properties
  server.port=<port de l'application>
  
  photos-obliques.tolerance.angle=<tolerance de l'angle lors de la recherche (en degré)>
  photos-obliques.url.vignette=<url du serveur contenant les vignettes>
  photos-obliques.url.apercu=<url du serveur contenant les apercus>
  photos-obliques.acces.photohd=<chemin du dossier contenant les photos hautes qualités
  photos-obliques.panier.max.photos=<nombre maximum de photos dans le panier (200 par défaut)>

```

**Remarque :**

- La propriété *photos-obliques.acces.photohd* doit désigner un répertoire. Dans le cadre d'une installation dockerisée, le répertoire doit être visible depuis le container (sous forme d'un volume par exemple ou d'un répertoire dans un des volumes déjà monté).

### II.3 Configuration du certificat

Un script est lancé au déploiement de l'image docker de l'application qui ajoute un certificat donné au keystore.

Afin d'ajouter le bon certificat au bon keystore, il est nécessaire de remplir les informations adéquates dans le fichier *photos-obliques.properties* :

```yaml
# dossier contenant le certificat
server.trustcert.keystore.path=<chemin du certificat>
# filename du certificat
server.trustcert.keystore.cert=<nom du fichier contenant le certificat>
# nom de l'alias du certificat à insérer dans le keystore
server.trustcert.keystore.alias=<alias du certificat>
# chemin absolu du keystore dans le container docker
server.trustcert.keystore.store=<chemin absolu du keystore dans le container docker>
# mot de passe du keystore
server.trustcert.keystore.password=<mot de passe du keystore>
```

**Exemple :**

```properties
server.trustcert.keystore.path=/etc/georchestra/
server.trustcert.keystore.cert=photobliques.crt
server.trustcert.keystore.alias=certificat-photobliques
server.trustcert.keystore.store=/usr/local/openjdk-11/lib/security/cacerts
server.trustcert.keystore.password=changeit
```

**Remarques :**

- Les propriétés *server.trustcert.keystore.path* et *server.trustcert.keystore.store* doivent désigner un répertoire. Dans le cadre d'une installation dockerisée, le répertoire doit être visible depuis le container (sous forme d'un volume par exemple ou d'un répertoire dans un des volumes déjà monté).
- Si les variables ne sont pas remplies, le certificat n'est pas ajouté au keystore et l'application démarre normalement.

## III Démarrage du service

Le backend peut être mis en oeuvre :

- En tant qu'application java éxecutable,
- En tant que service dockerisé à partir de votre propre image,
- En tant que service dockerisé à partir d'une image publiée sur dockerhub.

### III.1 Application Java éxecutable 

Le démarrage de l'application peut se faire à l'aide de la ligne de commande suivante :

```
java --add-opens java.base/java.util=ALL-UNNAMED \
   -Duser.language=fr -Duser.country=FR \
   -Djava.io.tmpdir=/tmp/jetty \
   -Dgeorchestra.datadir=/etc/georchestra \
   -Dspring.config.additional-location=file:<répertoire de configuration>/config/ \
   ${ADD_JAVA_OPTS}                  \
   -Xmx${XMX:-1G} -Xms${XMX:-1G}           \
   -jar photoobliques.jar"
```

**Remarques :**

La propriété ```spring.config.additional-location``` doit désigner une répertoire contenant :
- Un fichier *./default.properties* correspondant au fichier de configuration par défaut de GeOrchestra
- Un fhchier *./photos-obliques/photos-obliques.properties* correspondant au fichier de configuration de l'application

### III.2 Création d'une image docker

Le répertoire *docker/template* contient les fichiers *Dockerfile* permettant de créer :

- une image pour le backend *[docker/template/photosobliques/Dockerfile](docker/template/photosobliques/Dockerfile)*,
- une image pour le proxy GeOrchestra *[docker/template/proxy/Dockerfile](docker/template/proxy/Dockerfile)*utilisé pour la gestion de l'authentificiation.

Il est possible de générer une image docker en utilisant la commande suivante :

```
mvn clean package -DskipTests -Pdocker package
```

## III.3 Service Docker compose

Lors de la mise à jour du repository git `https://github.com/sigrennesmetropole/geor_photos_obliques_backend`, des actions Gits sont déclenchées afin :

- De builder les différents composants
- De pousser sur `https://hub.docker.com/r/sigrennesmetropole/geor_photos_obliques_backend` les images dockers du backend et de l'application back-office


Deux options s'offrent à vous pour exécuter l'application avec Docker Compose :

1. Créer un nouveau fichier *docker-compose.yml*

Si vous souhaitez démarrer l'application sur un nouveau serveur, vous pouvez créer un nouveau fichier docker-compose.yml dédié à cette application. 
Ce fichier définira la configuration des conteneurs Docker nécessaires à l'exécution de l'application.

2. Intégrer l'application dans un fichier docker-compose.yml existant

Si vous souhaitez utiliser docker sur le même serveur que les autres application, vous pouvez intégrer cette application dans votre fichier docker-compose.yml existant.
Pour ce faire, il vous faudra ajouter une nouvelle section au fichier définissant les conteneurs et les volumes nécessaires à l'exécution de l'application.


Voici un example d'intégration de photos obliques dans un [docker-compose.yml](docker/docker-compose.yml)


## IV Intégration dans Georchestra

### IV.1 Modification du security proxy pour Photos Obliques

Ajouter dans le fichier *target-mapping.properties* une ligne permettant de déclarer le backend de photos oblique au security proxy. 

Déclarez le backend avec une ligne de la forme :

```properties
  photosobliques=http(s)://<hôte du backend>:<port>/photosobliques
```
**Example** : ```photosobliques=http://photosobliques:8080/photosobliques```


Ajouter dans le fichier *security-mapping.xml* une ligne permettant de configurer les roles qui auront l'accès au backend :

**Example** :

```xml
<intercept-url pattern="/photosobliques/.*" access=<Liste des rôles>/>
```

Le Swagger sera accessible sur ```https://<serveur_georchestra>:<port>/photosobliques/swagger-ui.html```. 
Il n'est pas possible de configurer cet url.



