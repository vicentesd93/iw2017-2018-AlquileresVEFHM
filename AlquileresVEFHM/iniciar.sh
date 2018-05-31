#!/bin/bash

echo "REQUISITOS PREVIOS"
echo "Java JRE 1.8"
echo "MySql"

u="inmouser"
p="6eEFc5^T3:1WAm51-nl*"
if ! mysql -u$u -p$p -e "show databases;" &> /dev/null
then
	read -p "Usuario mysql con privilegios de administracion: " user
	read -p "Clave: " pass
	if ! mysql -u$user -p$pass -e "CREATE SCHEMA IF NOT EXISTS db_inmobiliaria DEFAULT CHARACTER SET utf8 COLLATE utf8_spanish_ci;" &> /dev/null
	then
		echo "Datos de sesion incorrectos"
		exit
	fi
	mysql -u$user -p$pass -e "CREATE USER 'inmouser'@'localhost' IDENTIFIED BY '6eEFc5^T3:1WAm51-nl*';" &> /dev/null
	mysql -u$user -p$pass -e "GRANT ALL PRIVILEGES ON db_inmobiliaria.* TO 'inmouser'@'localhost';" &> /dev/null
fi

echo "Iniciando aplicacion"
java -jar AlquileresVEFHM-0.0.1-SNAPSHOT.jar
