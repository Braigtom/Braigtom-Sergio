.PHONY: clean consumidor reportador productor all

default: all

all: consumidor reportador productor

reportador: reportador.jar

reportador.jar: manifest_reportador.mf build/tcp/Reportador.class
	jar cmf manifest_reportador.mf reportador.jar -C build tcp/Reportador.class
	rm -rf manifest_reportador.mf

manifest_reportador.mf:
	echo "Main-Class: tcp.Reportador" > manifest_reportador.mf

build/tcp/Reportador.class: src/tcp/Reportador.java
	mkdir -p build
	javac src/tcp/Reportador.java -d build

productor: productor.jar

productor.jar: manifest_productor.mf build/queue/Productor.class build/files/Archivo.class build/com build/org
	jar cmf manifest_productor.mf productor.jar -C build com -C build queue -C build files -C build org -C build rabbitmq-amqp-client.properties -C build version.properties
	rm -rf manifest_productor.mf

build/com: lib/amqp-client-5.7.1
	cp -rf lib/amqp-client-5.7.1/* build/

build/org: lib/slf4j-api-1.7.26 lib/slf4j-simple-1.7.26
	cp -rf lib/slf4j-api-1.7.26/* build/
	cp -rf lib/slf4j-simple-1.7.26/* build/

manifest_productor.mf:
	echo "Main-Class: queue.Productor" > manifest_productor.mf

build/queue/Productor.class: src/queue/Productor.java build/files/Archivo.class build/com
	mkdir -p build	
	javac -cp build/ src/queue/Productor.java -d build

build/files/Archivo.class: src/files/Archivo.java
	mkdir -p build
	javac src/files/Archivo.java -d build

consumidor: consumidor.jar

consumidor.jar: manifest_consumidor.mf build/queue/Consumidor.class build/tcp/Mensajero.class build/cryptography/Crypt.class build/com build/org
	jar cmf manifest_consumidor.mf consumidor.jar -C build com -C build queue -C build cryptography -C build org -C build tcp -C build rabbitmq-amqp-client.properties -C build version.properties
	rm -rf manifest_consumidor.mf

manifest_consumidor.mf:
	echo "Main-Class: queue.Consumidor" > manifest_consumidor.mf

build/tcp/Mensajero.class: src/tcp/Mensajero.java
	mkdir -p build
	javac src/tcp/Mensajero.java -d build

build/cryptography/Crypt.class: src/cryptography/Crypt.java
	mkdir -p build
	javac src/cryptography/Crypt.java -d build

build/queue/Consumidor.class: build/tcp/Mensajero.class build/cryptography/Crypt.class build/com
	mkdir -p build	
	javac -cp build/ src/queue/Consumidor.java -d build

clean:
	rm -rf build *.jar