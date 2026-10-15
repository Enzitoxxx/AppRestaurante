**Los requerimientos para la parte del administrador que nosotros pensamos hasta ahora son estos:**



* **Cuenta y perfil**

Registrarse, iniciar y cerrar sesión, recuperar contraseña.

Editar datos personales (nombre, contacto, foto).

Editar datos del establecimiento (nombre, dirección, horarios).



* **Dashboard**

Ventas del día, pedidos abiertos, mesas ocupadas/libres, producto más vendido y alertas de stock bajo.



* **Gestión de la carta**

ABM de categorías (Entradas, Platos principales, Bebidas, Postres).

ABM de productos: nombre, descripción, precio, foto, área de preparación (cocina/barra) y disponibilidad (activar/pausar).



* **Gestión del salón y las mesas**

Crear, editar y eliminar mesas (número, capacidad, zona).

Ver el mapa gráfico con el estado de cada mesa.



* **Gestión del personal**

Alta, baja y edición de empleados con su rol (camarero, cocinero, bartender).

Asignar mesas a camareros y activar/desactivar empleados.



* **Pedidos y ventas**

Ver pedidos en curso e historial, con filtros por fecha, mesa y camarero.

Ver el detalle de un pedido y anularlo con motivo.



**Estos 4 si llegamos con el tiempo, los vamos a hacer:**

* **Caja:** apertura y cierre, ingresos y egresos, ventas por método de pago.
* **Stock:** insumos, cantidad actual, mínimo y alertas.
* **Reportes:** ventas por período, productos más vendidos, rendimiento por camarero, ticket promedio.
* **Notificaciones:** stock bajo, cierre de caja, pedidos anulados.



Pantallas que tenemos que maquetar:

* Splash (pantalla de inicio / bienvenida)
* Login
* Registro
* Recuperar contraseña
* Dashboard (Inicio)
* Carta: lista por categorías
* Alta/edición de producto
* Salón: mapa de mesas
* Alta/edición de mesa
* Personal: lista
* Alta/edición de empleado
* Detalle de empleado (mesas asignadas)
* Pedidos: lista con filtros
* Detalle de pedido
* Caja
* Cierre de caja
* Stock: lista
* Reportes
* Notificaciones
* Perfil y configuración del local.

Las pantallas las estamos haciendo con figma con el agente de IA que viene integrado.

—-----------------------

**REQUERIMIENTOS FUNCIONALES**



RF01. Inicio de sesión

&#x20;Descripción: El sistema deberá permitir ingresar a la aplicación mediante el correo electrónico y la contraseña.

&#x20;Datos de Entrada: Correo electrónico y contraseña.

&#x20;Descripción del Proceso: El sistema verificará que la cuenta exista, que la contraseña sea correcta y que la cuenta esté activa.

&#x20;Salida: Si los datos son correctos, se permitirá el ingreso a la aplicación. En caso contrario, se mostrará un mensaje de error.



RF02. Registro de cuenta

&#x20;Descripción: El sistema deberá permitir registrar una cuenta de administrador para acceder a la aplicación.

&#x20;Datos de Entrada: Nombre, correo electrónico, contraseña, teléfono y nombre del restaurante.

&#x20;Descripción del Proceso: El sistema verificará que los campos obligatorios estén completos, que el correo electrónico tenga un formato válido y no esté registrado anteriormente, y que la contraseña tenga al menos 8 caracteres, una letra y un número. Luego se creará la cuenta junto con el restaurante.

&#x20;Salida: Se creará la cuenta y se mostrará un mensaje de confirmación. Si existe algún error, se informará al usuario.



RF03. Recuperación de contraseña

&#x20;Descripción: El sistema deberá permitir recuperar el acceso a una cuenta cuando se olvide la contraseña.

&#x20;Datos de Entrada: Correo electrónico registrado.

&#x20;Descripción del Proceso: El sistema verificará si el correo electrónico pertenece a una cuenta registrada y permitirá iniciar el proceso de recuperación.

&#x20;Salida: Se mostrará un mensaje indicando cómo continuar con la recuperación de la contraseña.



RF04. Cierre de sesión

&#x20;Descripción: El sistema deberá permitir cerrar la sesión iniciada.

&#x20;Datos de Entrada: Solicitud de cierre de sesión.

&#x20;Descripción del Proceso: El sistema finalizará la sesión actual y volverá a la pantalla de inicio de sesión.

&#x20;Salida: La sesión quedará cerrada y no se podrá acceder a las funciones privadas hasta volver a ingresar.



RF05. Consulta del panel principal

&#x20;Descripción: El sistema deberá mostrar un resumen del estado actual del restaurante.

&#x20;Datos de Entrada: Ninguno. El sistema utiliza la información de ventas, pedidos, mesas, productos e insumos registrada.

&#x20;Descripción del Proceso: El sistema consultará la información disponible y calculará el total vendido en el día, la cantidad de pedidos cobrados, la cantidad de pedidos abiertos, las mesas ocupadas (incluyendo las que esperan la cuenta) y libres, el producto más vendido del día según las unidades vendidas y los insumos con stock bajo.

&#x20;Salida: Se mostrarán las ventas del día con la cantidad de pedidos cobrados, los pedidos abiertos, la cantidad de mesas ocupadas y libres, el producto más vendido y una lista de alertas de stock bajo.



RF06. Gestión de categorías

&#x20;Descripción: El sistema deberá permitir registrar, consultar, modificar y eliminar las categorías de los productos.

&#x20;Datos de Entrada: Nombre y descripción de la categoría.

&#x20;Descripción del Proceso: El sistema verificará que el nombre esté completo y no esté repetido, y guardará los datos de la categoría o realizará los cambios solicitados. Antes de eliminarla, verificará que no existan productos asociados; si los hay, no permitirá eliminarla e informará al usuario.

&#x20;Salida: Se mostrará la lista de categorías actualizada o un mensaje indicando el resultado de la operación.



RF07. Gestión de productos

&#x20;Descripción: El sistema deberá permitir registrar, consultar, modificar y eliminar productos de la carta del restaurante, y activar o pausar su disponibilidad.

&#x20;Datos de Entrada: Nombre del producto, descripción, precio, foto, categoría, área de preparación (cocina o barra) y disponibilidad.

&#x20;Descripción del Proceso: El sistema verificará que los datos obligatorios estén completos y que el precio sea mayor a cero. Luego guardará el producto o aplicará los cambios solicitados. Un producto pausado no podrá agregarse a nuevos pedidos. Antes de eliminar un producto se pedirá confirmación; si el producto figura en pedidos registrados, se pausará en lugar de eliminarse.

&#x20;Salida: Se mostrará la lista de productos actualizada o un mensaje indicando si la operación se realizó correctamente.



RF08. Consulta de la carta

&#x20;Descripción: El sistema deberá permitir consultar los productos de la carta organizados por categorías.

&#x20;Datos de Entrada: Solicitud de consulta, categoría seleccionada o texto de búsqueda, si corresponde.

&#x20;Descripción del Proceso: El sistema buscará los productos registrados y los organizará según su categoría, teniendo en cuenta su disponibilidad.

&#x20;Salida: Se mostrará la lista de productos con sus nombres, precios, fotos y disponibilidad.



RF09. Gestión de mesas

&#x20;Descripción: El sistema deberá permitir registrar, consultar, modificar y eliminar las mesas del restaurante.

&#x20;Datos de Entrada: Número de mesa, capacidad y zona.

&#x20;Descripción del Proceso: El sistema verificará que el número de mesa no esté repetido y que la capacidad sea mayor a cero, y guardará la información de cada mesa. Si la zona indicada no existe, se podrá crear al registrar la mesa. También permitirá modificar sus datos cuando sea necesario. Antes de eliminar una mesa se pedirá confirmación y no se permitirá eliminar una mesa que tenga un pedido abierto.



&#x20;Salida: Se mostrará la lista de mesas actualizada con su información correspondiente.

RF10. Consulta del estado de las mesas

&#x20;Descripción: El sistema deberá permitir consultar el estado de las mesas del restaurante.

&#x20;Datos de Entrada: Zona seleccionada, si se quiere filtrar. La información de las mesas y de los pedidos asociados la obtiene el sistema.

&#x20;Descripción del Proceso: El sistema consultará los datos disponibles para determinar si cada mesa está libre, ocupada o esperando la cuenta.

&#x20;Salida: Se mostrará un mapa gráfico con las mesas y su estado indicado por color: verde (libre), rojo (ocupada) y amarillo (esperando cuenta).



RF11. Gestión del personal

&#x20;Descripción: El sistema deberá permitir registrar, consultar, modificar y desactivar las cuentas del personal del restaurante.

&#x20;Datos de Entrada: Nombre, teléfono, correo electrónico, rol (camarero, cocinero o bartender) y estado del empleado.

&#x20;Descripción del Proceso: El sistema verificará que los datos obligatorios estén completos y que el correo electrónico no esté repetido. Al registrar un empleado, generará su usuario y una contraseña temporal, que se mostrarán al administrador para que se las entregue al empleado. Un empleado desactivado no podrá iniciar sesión, pero se conservará su historial.

&#x20;Salida: Se mostrará la lista del personal actualizada con los datos de cada empleado y, al registrar un empleado nuevo, sus datos de acceso.



RF12. Asignación de mesas al personal

&#x20;Descripción: El sistema deberá permitir asignar mesas a los camareros del restaurante.

&#x20;Datos de Entrada: Empleado seleccionado y mesas que se desean asignar.

&#x20;Descripción del Proceso: El sistema registrará las mesas asignadas a cada camarero y actualizará la información correspondiente. Una mesa solo podrá tener un camarero asignado a la vez. Los pedidos abiertos conservarán el camarero con el que se tomaron.

&#x20;Salida: Se mostrará la asignación de mesas de cada empleado.



RF13. Consulta de pedidos

&#x20;Descripción: El sistema deberá permitir consultar los pedidos en curso y el historial de pedidos del restaurante.

&#x20;Datos de Entrada: Criterios de búsqueda, como fecha, mesa, camarero o estado del pedido (en preparación, listo para servir, esperando cuenta, cobrado o anulado).

&#x20;Descripción del Proceso: El sistema buscará los pedidos que coincidan con los criterios ingresados y organizará los resultados. Los pedidos en preparación, listos para servir y esperando cuenta se consideran pedidos abiertos; los cobrados y anulados forman parte del historial.

&#x20;Salida: Se mostrará una lista de pedidos con su fecha, número de mesa, camarero, importe total y estado.



RF14. Consulta del detalle de un pedido

&#x20;Descripción: El sistema deberá permitir consultar el detalle de un pedido seleccionado.

&#x20;Datos de Entrada: Identificador del pedido.

&#x20;Descripción del Proceso: El sistema buscará el pedido y recuperará los productos incluidos, sus cantidades, precios, observaciones, cantidad de comensales, camarero, mesa, hora y estado.

&#x20;Salida: Se mostrará el detalle completo del pedido y su importe total.



RF15. Anulación de pedidos

&#x20;Descripción: El sistema deberá permitir anular un pedido abierto, indicando el motivo de la anulación.

&#x20;Datos de Entrada: Pedido seleccionado y motivo de la anulación.

&#x20;Descripción del Proceso: El sistema verificará que el pedido esté abierto (en preparación, listo para servir o esperando cuenta) y que se haya indicado un motivo. Luego registrará la anulación junto con su motivo, la fecha y el usuario que la realizó. Un pedido anulado no se sumará a las ventas.

&#x20;Salida: Se actualizará el estado del pedido y se mostrará un mensaje con el resultado de la operación.



RF16. Gestión de caja

&#x20;Descripción: El sistema deberá permitir registrar la apertura y el cierre de la caja del restaurante, los ingresos y egresos de dinero, y consultar las ventas por método de pago.

&#x20;Datos de Entrada: Monto inicial, concepto y monto de cada ingreso o egreso, y monto contado al cierre.

&#x20;Descripción del Proceso: El sistema registrará la apertura de la caja con la fecha, la hora y el administrador responsable (solo podrá haber una caja abierta a la vez) y los movimientos de dinero realizados durante la jornada. Sumará las ventas cobradas según el método de pago (efectivo, tarjeta o transferencia). Al cerrar, calculará el monto esperado (monto inicial más ventas en efectivo más ingresos menos egresos) y lo comparará con el monto contado.

&#x20;Salida: Se mostrará un resumen de los movimientos, el total por método de pago, el monto esperado, el monto contado y la diferencia del cierre de caja.



RF17. Control de stock

&#x20;Descripción: El sistema deberá permitir registrar y consultar los insumos utilizados en el restaurante, controlar sus cantidades disponibles y registrar ajustes de stock.

&#x20;Datos de Entrada: Nombre del insumo, cantidad actual, unidad de medida y cantidad mínima. Para un ajuste: tipo (compra, merma o corrección), cantidad y motivo.

&#x20;Descripción del Proceso: El sistema guardará los datos de los insumos y actualizará sus cantidades cuando se registre un ajuste. Un insumo se considerará con stock bajo cuando su cantidad actual sea menor o igual a la cantidad mínima.

&#x20;Salida: Se mostrará una lista de insumos con sus cantidades disponibles y se identificarán aquellos que tengan poco stock.



RF18. Consulta de reportes

&#x20;Descripción: El sistema deberá permitir consultar reportes sobre las ventas, los productos y los camareros del restaurante.

&#x20;Datos de Entrada: Período (hoy, semana o mes) y tipo de reporte solicitado: ventas por período, productos más vendidos, rendimiento por camarero o ticket promedio.

&#x20;Descripción del Proceso: El sistema consultará la información registrada y calculará los resultados correspondientes al período seleccionado, sin tener en cuenta los pedidos anulados. Los productos más vendidos se ordenarán por unidades vendidas. El ticket promedio se calculará dividiendo el total vendido por la cantidad de pedidos cobrados.

&#x20;Salida: Se mostrarán los reportes solicitados, con gráficos o listas según corresponda.



RF19. Notificaciones

&#x20;Descripción: El sistema deberá informar sobre situaciones importantes relacionadas con el funcionamiento del restaurante.

&#x20;Datos de Entrada: Eventos registrados, como stock bajo, pedidos anulados o cierre de caja. Para marcarlas como leídas: solicitud del administrador.

&#x20;Descripción del Proceso: El sistema detectará los eventos definidos y generará los avisos correspondientes. También permitirá marcar las notificaciones como leídas.

&#x20;Salida: Se mostrará una lista de notificaciones con la información de cada situación, distinguiendo las leídas de las no leídas.



RF20. Gestión del perfil y del restaurante

&#x20;Descripción: El sistema deberá permitir consultar y modificar los datos del perfil del administrador y del restaurante.

&#x20;Datos de Entrada: Nombre, teléfono, correo electrónico, foto, nombre del restaurante, dirección y horarios de atención.

&#x20;Descripción del Proceso: El sistema verificará que los datos obligatorios estén completos y guardará los cambios realizados.

&#x20;Salida: Se mostrarán los datos actualizados del perfil y del restaurante.

