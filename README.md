# geor_photos_obliques_backend
Backend du module Photos Obliques de Rennes Métropole

# Configuration du certificat

Un script est lancé au déploiement de l'image docker de l'application qui ajoute un certificat donné au keystore.  
Afin d'ajouter le bon certificat au bon keystore, il est nécessaire de remplir les informations adéquates dans le fichier `properties` de l'application :

```
# filename du certificat (à déposer dans <...>config/photos-obliques/)
server.trustcert.keystore.cert=
# nom de l'alias du certificat à insérer dans le keystore
server.trustcert.keystore.alias=
# chemin absolu du keystore dans le container docker
server.trustcert.keystore.store=
# mot de passe du keystore
server.trustcert.keystore.password=

```

Il est important de noter que la variable `server.trustcert.keystore.cert` ne doit contenir que le _nom du fichier_, pas son chemin.  
Si les variables ne sont pas remplies, le certificat n'est pas ajouté au keystore et l'application démarre normalement.

Le certificat dont le nom est renseigné doit être déposé dans `<...>/config/photos-obliques/` (le répertoire <...>/config/ qui est monté sur le répertoire /etc/georchestra dans le container)

