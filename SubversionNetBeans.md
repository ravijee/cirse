# Descargar CIRSE mediante Subversion desde NetBeans #

Para descargar CIRSE desde NetBeans, es necesario instalar primero [Subversion](http://es.wikipedia.org/wiki/Subversion), esto lo hace automáticamente el IDE la primera vez que se desee utilizar. Para su instalación manual se pueden seguir los pasos marcados por su [página oficial](http://subversion.apache.org/packages.html).

---


La opción para obtener el proyecto desde el repositorio se encuentra en el menú _Team_ (Equipo), estando ahí se selecciona la opción **_Checkout…_** del submenú _Subversion_ (En este punto NetBeans ofrecerá descargar e instalar Subversion automáticamente en caso de no estar en el sistema).

![http://cirse.googlecode.com/files/checkout1.png](http://cirse.googlecode.com/files/checkout1.png)

En los cuadros de texto a llenar dentro del diálogo siguiente contienen la información referente a la ubicación del repositorio y el usuario que lo descarga. La ubicación de CIRSE se encuentra en la pestaña [Source](http://code.google.com/p/cirse/source/checkout) de ésta página, donde también se les da información de su cuenta y contraseña.

![http://cirse.googlecode.com/files/checkout2.png](http://cirse.googlecode.com/files/checkout2.png)

**Nota: para poder descargar con cuenta debes estar dado de alta previamente en el proyecto.**

Si no tienes cuenta o ésta aún no ha sido dado de alta, puede omitir introducir su nombre de usuario y contraseña, lo que hará que se descargue de forma anónima, pero sin permitir agregar los cambios hechos al repositorio.

Si los datos dados son correctos, al pasar al diálogo siguiente se ofrecerán las opciones para descargar el proyecto.

  * _Repository Folder(s)._  Indica desde que carpeta del repositorio se descargará el proyecto (siendo **trunk** la carpeta recomendada).

  * _Repository Revision._ Indica la revisión (actualización) desde la que se desee descargar el proyecto. _Head_ indica que se descargará la última actualización disponible.

  * _Export a clean directory from the repository._ Indica que una vez descargado, el proyecto no estará ligado con el repositorio, por lo que todo el proyecto será considerado como nuevo proyecto. (Recomendado: desactivado).

  * _Local Folder._ Carpeta local donde se descargará el proyecto.

Una vez terminados estos pasos, el proyecto debería estar descargado y listo para compilar.

Ya con el proyecto listo para usar, dentro del submenú subversion se activan distintas opciones para mantener actualizados el proyecto local y el repositorio. Estas opciones se activan siempre que se seleccione un proyecto (o archivo dentro de éste) ligado a un repositorio. Las opciones más utilizadas son Update, Commit y revert modifications.

  * **Update.** Actualiza los archivos nuevos del repositorio con los del proyecto local, si ambos se encuentran en la misma revisión no sucede nada.

  * **Commit.** Agrega los archivos nuevos del proyecto local al repositorio y aumenta a éste su número de revisión en 1. Debido a la arquitectura modular de la plataforma de NetBeans se debe realizar un commit por cada módulo modificado, ya que al realizar un commit desde la carpeta raíz (CIRSE) solo se agregarán los archivos de ésta carpeta sin entrar en los módulos.
**Solo las cuentas registradas en el proyecto pueden realizar Commit**

  * **Revert modifications** Permite deshacer cambios en el proyecto local, descargando de nuevo los archivos modificados desde el repositorio.

Subversion cuenta con muchas opciones más que no se cubren en esta página, [la Wiki de NetBeans](http://netbeans.org/kb/docs/ide/subversion.html) contiene una página que cubre a fondo cada opción y se encuentra un [libro](http://svnbook.red-bean.com/) con toda la información referente a Subversion.

Para información sobre el manejo de la página de este proyecto, se puede consultar la información en la [Wiki de google code](http://code.google.com/p/support/wiki/FAQ).