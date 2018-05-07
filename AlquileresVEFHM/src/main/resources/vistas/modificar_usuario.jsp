<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
   
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Modificar Usuario</title>
</head>
<body>
	<form:form method="POST" action="/modificar" modelAttribute="usuario">
		<table>
			<tr><td colspan=2><h2>Modificar usuario</h2></td></tr>
			<tr>
				<td><form:hidden path="id"/></td>
			</tr>
			<tr>
				<td><form:label path="email">Correo electronico</form:label></td>
				<td><form:input path="email"/></td>
			</tr>
			<tr>
				<td><form:label path="dni">DNI</form:label></td>
				<td><form:input path="dni"/></td>
			</tr>
			<tr>
				<td><form:label path="nombre">Nombre</form:label></td>
				<td><form:input path="nombre"/></td>
			</tr>
			<tr>
				<td><form:label path="apellidos">Apellidos</form:label></td>
				<td><form:input path="apellidos"/></td>
			</tr>
			<tr>
				<td><form:label path="f_nacimiento">Fecha nacimiento</form:label></td>
				<td><form:input type="date" path="f_nacimiento"/></td>
			</tr>
			<tr>
				<td><form:label path="sexo">Fecha nacimiento</form:label></td>
				<td><form:radiobutton path="sexo" value="0"/>Hombre
					<form:radiobutton path="sexo" value="1"/>Mujer</td>
			</tr>
			<tr>
				<td><form:label path="direccion">Direccion</form:label></td>
				<td><form:input path="direccion"/></td>
			</tr>
			<tr>
				<td><form:label path="telefono">Telefono</form:label></td>
				<td><form:input path="telefono"/></td>
			</tr>
			<tr>
				<td><form:label path="rol">Rol</form:label></td>
				<td><form:select path="rol">
					<form:option value="1" label="Huesped"/>
					<form:option value="2" label="Anfitrion"/>
				</form:select></td>
			</tr>
			<tr>
				<td colspan=2><input type="submit" value="Modificar"></td>
			</tr>
		</table>
	</form:form>
</body>
</html>