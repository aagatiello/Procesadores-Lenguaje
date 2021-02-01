# Caso práctico de Procesadores del Lenguaje

Traductor predictivo, recursivo y descendente para un subconjunto de instrucciones de un lenguaje similar a C. Requisitos:

- El analizador léxico debe:
  - Leer los programas de prueba almacenados en ficheros de texto. Debe descartar los espacios, tabuladores y saltos de línea del fichero de entrada.

  - Reconocer las palabras reservadas, los operadores y los caracteres delimitadores del lenguaje. El fichero lexemas.txt define las parejas de componentes léxicos y de lexemas del lenguaje.

  - Reconocer como identificadores los lexemas que empiecen por una letra seguida de cero o más letras, dígitos o guiones bajos, siempre que el lexema no sea una palabra reservada.

  - Reconocer las secuencias de dígitos como números enteros o números reales.

  - Descartar los comentarios del programa. Un comentario de una línea comienza por los caracteres //. Para delimitar un comentario de una o más líneas se usan las secuencias de caracteres /* y */. No se admiten comentarios anidados, cuando se encuentra el delimitador de inicio de un comentario se ignora el resto de caracteres hasta encontrar el delimitador de fin.

- El analizador sintáctico debe:
  - Verificar que los programas de prueba cumplen la gramática del lenguaje y su semántica.

  - Indicar si el proceso de compilación ha sido correcto o si se han encontrado errores sintácticos o semánticos.
En caso de que se encuentre un error, debe mostrar una breve descripción del error y la línea del programa en la que se ha producido.

  - Comprobar que todos los identificadores del programa han sido declarados una sola vez y que se cumplen las reglas de verificación de tipos de datos para las instrucciones de asignación y para las expresiones del lenguaje.
