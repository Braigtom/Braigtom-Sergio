# proyectosegundoparcial


Braigtom Toral

Sergio Chavez

Este proyecto tiene como objetivo ejercitar la creación de aplicaciones distribuidas que permitan la transferencia y procesamiento de información entre varios módulos, mediante el uso de un componente de middleware (work queue). Adicionalmente se ponen en práctica otros conceptos como serialización de datos, escalabilidad de aplicaciones, rendimiento, etc.




## descarga

Desde la terminal linux del nodo 1 se procede a clonar el repositorio del proyecto con el comando
git clone https://github.com/Braigtom/proyectosegundoparcial

lo mismo desde la terminal del nodo 2.



## compilar y Ejecutar proyecto

Una vez descargado el proyecto en ambas maquinas virtuales se procederá a compilar en el nodo 1 con el siguiente comando, donde dice <ip> se tiene que proceder a poner la direccion ip del nodo 2 para poder hacer la conexion.
   

```bash
Make consumidor
java -jar consumidor.jar <ip>

``` 

```bash
Make productor
java -jar productor.jar texto
``` 

En el nodo 2 se procede a ejecutar al reportador

```bash
 make reportador 
java -jar reportador.jar
``` 
El reportador estará a la escucha de las firmas que le tiene que mandar el consumidor, estas firmas son generadas a partir de líneas de texto que el consumidor recibe desde el productor a través de una cola de trabajo  
                   
