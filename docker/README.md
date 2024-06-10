# Pour les utilisateurs dockers :

Il est possible de générer une image docker en utilisant la commande suivante :

```
$ mvn clean package -DskipTests -Pdocker package
```

Si vous utilisez l'image docker vaut devez prendre en compte 2 éléments :

# Servlet container

L'image docker est basée sur une application Spring Boot 

# Configuration

Il faut configurer l'application correctement. Voici un extrait du fichier de configuration a modifier en fonction de l'installation :

```
## Version de l'application
application.version=@project.version@
application.comment=Photos Obliques @project.version@
application.role.administrateur.code=ADMINISTRATOR
application.role.user.code=USER


photos-obliques.tolerance.angle=20
photos-obliques.url.vignette=https://public.sig.rennesmetropole.fr/ressources/donnees/photosobliques/vignettes2/
photos-obliques.url.apercu=https://public.sig.rennesmetropole.fr/ressources/donnees/photosobliques/apercus2/

photos-obliques.acces.photohd=C:/projets/RennesMetropole/Photos_Obliques/HD
photos-obliques.panier.max.photos=200

photos-obliques.geometries.srid=EPSG:3948

# SERVER
server.port=8082

# TEMPORARY DIRECTORY
temporary.directory=${java.io.tmpdir}/photos-obliques

# LOG
logging.level.org.springframework=DEBUG
logging.level.org.georchestra=DEBUG

# Configuration BDD
spring.datasource.url=jdbc:postgresql://${pgsqlHost}:${pgsqlPort}/${pgsqlDatabase}?currentSchema=phototheque&ApplicationName=georchestra
spring.datasource.username=consult
spring.datasource.password=consult
spring.datasource.driver-class-name=org.postgresql.Driver
spring.jpa.properties.hibernate.dialect = org.hibernate.spatial.dialect.postgis.PostgisPG95Dialect
#spring.jpa.properties.hibernate.jdbc.lob.non_contextual_creation=true
spring.jpa.properties.hibernate.jdbc.lob.non_contextual_creation=true
spring.jpa.properties.hibernate.temp.use_jdbc_metadata_defaults = false
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=false


# Gï¿½nï¿½ration du script par hibernate + jpa
spring.flyway.enabled=false
#spring.flyway.enabled=false
spring.jpa.hibernate.ddl-auto=none
#spring.jpa.hibernate.ddl-auto=create

# SECURITY
server.servlet.session.cookie.secure=true
#server.servlet.session.cookie.http-only=true
#server.servlet.session.tracking-modes=cookie

spring.security.user.name=admin
spring.security.user.password={noop}4dM1nApp!
spring.security.user.roles=USER

georchestra.role.administrator=ROLE_ADMINISTRATOR


```