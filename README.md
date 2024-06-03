# geor_photos_obliques_backend
Backend du module Photos Obliques de Rennes Métropole

# Configuration du certificat

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

