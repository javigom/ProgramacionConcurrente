Práctica 5. Realizada por Javier Gómez Moraleda.

Información básica del funcionamiento de la práctica:

Antes de empezar, si vamos a ejecutar el programa en distintas máquinas, debemos introducir  en la 
clase cliente la IP y puerto que tendrá el servidor para poder comunicarnos con él. 
(Hay una configuración por defecto).

Cada usuario, dispondrá de su propia carpeta. Se deberán almacenar los documentos en la siguiente 
ruta: RUTA_PROYECTO/usuarios/NOMBRE_USUARIO/ficheros/
Por ejemplo, si queremos almacenar el fichero1_pepe.txt del usuario pepe, debemos almacenarlo en:
RUTA_PROYECTO/usuarios/pepe/ficheros/fichero1_pepe.txt

Además, cada usuario dispondrá de un fichero con los nombres de todos los ficheros disponibles en
la carpeta de ficheros, que se utilizarán para obtener sus nombres.

Para realizar pruebas, se han añadido 4 usuarios al sistema: javi, laura, maria y pepe. Todos 
disponen de 3 ficheros cada uno y de sus respectivos ficheros de información. 

Para lanzar el programa, será necesario ejecutar la clase mainServidor, y posteriormente la clase 
mainCliente tantas veces como clientes queramos ejecutar. En cada cliente, el sistema buscará
todos sus ficheros disponibles con ese nombre de cliente, por lo que, si no existe el cliente, 
aparecerá un error.

Al descargar un fichero, se mostrará por pantalla al usuario que lo haya solicitado.

