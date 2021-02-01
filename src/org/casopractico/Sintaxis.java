package org.casopractico;

import java.util.Enumeration;
import java.util.Hashtable;

public class Sintaxis {

	private ComponenteLexico componenteLexico;
	private Lexico lexico;
	private int tamanio;
	private String tipo;
	private Hashtable<String, TipoDato> simbolos;

	public Sintaxis(Lexico lexico) {
		this.lexico = lexico;
		this.simbolos = new Hashtable<String, TipoDato>();
		this.componenteLexico = lexico.getComponenteLexico();
	}

	private void asignacionDeclaracion() {
		if (this.componenteLexico.getEtiqueta().equals("assignment")) {
			compara("assignment");
			if (tipo.equals("int")) {
				if (this.componenteLexico.getEtiqueta().equals("floatNum")
						|| this.componenteLexico.getEtiqueta().equals("boolean")) {
					System.out.println("Error en la linea " + this.componenteLexico.getLinea()
							+ " , incompatibilidad de tipos en la instrucción de asignación.");
				}
			} else if (tipo.equals("float")) {
				if (this.componenteLexico.getEtiqueta().equals("integer")
						|| this.componenteLexico.getEtiqueta().equals("boolean")) {
					System.out.println("Error en la linea " + this.componenteLexico.getLinea()
							+ " , incompatibilidad de tipos en la instrucción de asignación.");
				}
			} else if (tipo.equals("boolean")) {
				if (this.componenteLexico.getEtiqueta().equals("integer")
						|| this.componenteLexico.getEtiqueta().equals("floatNum")) {
					System.out.println("Error en la linea " + this.componenteLexico.getLinea()
							+ " , incompatibilidad de tipos en la instrucción de asignación.");
				}
			}
			expresionLogica();
		}
	}

	private boolean busqueda(String a) {
		Enumeration<String> e = simbolos.keys();
		Object key;
		boolean encontrado = false;
		while (e.hasMoreElements()) {
			key = e.nextElement();
			if (key.equals(a)) {
				return true;
			}
		}
		return false;
	}

	public void compara(String etiqueta) {
		if (this.componenteLexico.getEtiqueta().equals(etiqueta)) {
			this.componenteLexico = this.lexico.getComponenteLexico();
		} else {
			System.out.println("Error! Se esperaba " + etiqueta);
		}
	}

	private void declaracionVariable() {
		tipoPrimitivo();
		if (this.componenteLexico.getEtiqueta().equals("open_square_bracket")) {
			tipoVector();
			simbolos.put(this.componenteLexico.getValor(), new TipoArray(this.tamanio, this.tipo));
			compara("id");
			compara("semicolon");
		} else {
			listaIdentificadores();
			compara("semicolon");
		}
	}

	private void declaraciones() {
		if (this.componenteLexico.getEtiqueta().equals("int") || this.componenteLexico.getEtiqueta().equals("float")
				|| this.componenteLexico.getEtiqueta().equals("boolean")) {
			declaracionVariable();
			declaraciones();
		}
	}

	private void expresion() {
		termino();
		if (this.componenteLexico.getEtiqueta().equals("add")) {
			compara("add");
			expresion();
		} else if (this.componenteLexico.getEtiqueta().equals("subtract")) {
			compara("subtract");
			expresion();
		}
	}

	private void expresionLogica() {
		terminoLogico();
		if (this.componenteLexico.getEtiqueta().equals("or")) {
			compara("or");
			expresionLogica();
		}
	}

	private void expresionRelacional() {
		expresion();
		if (this.componenteLexico.getEtiqueta().equals("greater_than")
				|| this.componenteLexico.getEtiqueta().equals("greater_equals")
				|| this.componenteLexico.getEtiqueta().equals("less_than")
				|| this.componenteLexico.getEtiqueta().equals("less_equals")
				|| this.componenteLexico.getEtiqueta().equals("equals")
				|| this.componenteLexico.getEtiqueta().equals("not_equals")
				|| this.componenteLexico.getEtiqueta().equals("assignment")) {
			operadorRelacional();
			expresion();
		}
	}

	private void factor() {
		if (this.componenteLexico.getEtiqueta().equals("open_parenthesis")) {
			compara("open_parenthesis");
			expresion();
			compara("closed_parenthesis");
		} else if (this.componenteLexico.getEtiqueta().equals("id")) {
			variable();
		} else if (this.componenteLexico.getEtiqueta().equals("integer")) {
			compara("integer");
		} else if (this.componenteLexico.getEtiqueta().equals("floatNum")) {
			compara("floatNum");
		}
	}

	private void factorLogico() {
		if (this.componenteLexico.getEtiqueta().equals("not")) {
			compara("not");
			factorLogico();
		} else if (this.componenteLexico.getEtiqueta().equals("true")) {
			compara("true");
		} else if (this.componenteLexico.getEtiqueta().equals("false")) {
			compara("false");
		} else {
			expresionRelacional();
		}
	}

	private void instruccion() {
		if (this.componenteLexico.getEtiqueta().equals("int") || this.componenteLexico.getEtiqueta().equals("float")
				|| this.componenteLexico.getEtiqueta().equals("boolean")) {
			declaracionVariable();
		}
		if (this.componenteLexico.getEtiqueta().equals("id")) {
			variable();
			compara("assignment");
			expresionLogica();
			compara("semicolon");
		} else if (this.componenteLexico.getEtiqueta().equals("if")) {
			compara("if");
			compara("open_parenthesis");
			expresionLogica();
			compara("closed_parenthesis");
			instruccion();
			if (this.componenteLexico.getEtiqueta().equals("else")) {
				compara("else");
				instruccion();
			}
		} else if (this.componenteLexico.getEtiqueta().equals("while")) {
			compara("while");
			compara("open_parenthesis");
			expresionLogica();
			compara("closed_parenthesis");
			instruccion();
		} else if (this.componenteLexico.getEtiqueta().equals("do")) {
			compara("do");
			instruccion();
			compara("while");
			compara("open_parenthesis");
			expresionLogica();
			compara("closed_parenthesis");
			compara("semicolon");
		} else if (this.componenteLexico.getEtiqueta().equals("print")) {
			compara("print");
			compara("open_parenthesis");
			variable();
			compara("closed_parenthesis");
			compara("semicolon");
		} else if (this.componenteLexico.getEtiqueta().equals("open_bracket")) {
			compara("open_bracket");
			instrucciones();
			compara("closed_bracket");
		}
	}

	private void instrucciones() {
		instruccion();
		if (this.componenteLexico.getEtiqueta().equals("int") 
				|| this.componenteLexico.getEtiqueta().equals("float")
				|| this.componenteLexico.getEtiqueta().equals("boolean")
				|| this.componenteLexico.getEtiqueta().equals("id") 
				|| this.componenteLexico.getEtiqueta().equals("if")
				|| this.componenteLexico.getEtiqueta().equals("while")
				|| this.componenteLexico.getEtiqueta().equals("do")
				|| this.componenteLexico.getEtiqueta().equals("print")
				|| this.componenteLexico.getEtiqueta().equals("open_square_bracket")) {
			instrucciones();
		}
	}

	private void listaIdentificadores() {
		if (this.componenteLexico.getEtiqueta().equals("id")) {
			if (!busqueda(this.componenteLexico.getValor())) {
				simbolos.put(this.componenteLexico.getValor(), new TipoPrimitivo(this.tipo));
			} else {
				System.out.println("Error en la linea: " + this.componenteLexico.getLinea() + ", identificador '"
						+ this.componenteLexico.getValor() + "' ya declarado");
			}
			compara("id");
			asignacionDeclaracion();
			masIdentificadores();
		}
	}

	private void masIdentificadores() {
		if (this.componenteLexico.getEtiqueta().equals("comma")) {
			compara("comma");
			if (!busqueda(this.componenteLexico.getValor())) {
				simbolos.put(this.componenteLexico.getValor(), new TipoPrimitivo(this.tipo));
			} else {
				System.out.println("Error en la linea: " + this.componenteLexico.getLinea() + ", identificador '"
						+ this.componenteLexico.getValor() + "' ya declarado");
			}
			compara("id");
			asignacionDeclaracion();
			masIdentificadores();
		}
	}

	private void operadorRelacional() {
		if (this.componenteLexico.getEtiqueta().equals("greater_than")) {
			compara("greater_than");
		} else if (this.componenteLexico.getEtiqueta().equals("greater_equals")) {
			compara("greater_equals");
		} else if (this.componenteLexico.getEtiqueta().equals("less_than")) {
			compara("less_than");
		} else if (this.componenteLexico.getEtiqueta().equals("less_equals")) {
			compara("less_equals");
		} else if (this.componenteLexico.getEtiqueta().equals("equals")) {
			compara("equals");
		} else if (this.componenteLexico.getEtiqueta().equals("not_equals")) {
			compara("not_equals");
		} else if (this.componenteLexico.getEtiqueta().equals("assignment")) {
			compara("assignment");
		}
	}

	public void programa() {
		compara("void");
		compara("main");
		compara("open_bracket");
		declaraciones();
		instrucciones();
		compara("closed_bracket");
	}

	public void getTablaSimbolos() {
		Enumeration<String> e = simbolos.keys();
		Object variable;
		Object dato;
		while (e.hasMoreElements()) {
			variable = e.nextElement();
			dato = simbolos.get(variable);
			System.out.println("<'" + variable + "', " + dato + ">");
		}
	}

	private void termino() {
		factor();
		if (this.componenteLexico.getEtiqueta().equals("multiply")) {
			compara("multiply");
			termino();
		} else if (this.componenteLexico.getEtiqueta().equals("divide")) {
			compara("divide");
			termino();
		} else if (this.componenteLexico.getEtiqueta().equals("remainder")) {
			compara("remainder");
			termino();
		}
	}

	private void terminoLogico() {
		factorLogico();
		if (this.componenteLexico.getEtiqueta().equals("and")) {
			compara("and");
			terminoLogico();
		}
	}

	private void tipoPrimitivo() {
		if (this.componenteLexico.getEtiqueta().equals("int")) {
			this.tipo = "int";
			compara("int");
		} else if (this.componenteLexico.getEtiqueta().equals("float")) {
			this.tipo = "float";
			compara("float");
		} else {
			if (this.componenteLexico.getEtiqueta().equals("boolean")) {
				this.tipo = "boolean";
				compara("boolean");
			}
		}
	}

	private void tipoVector() {
		compara("open_square_bracket");
		this.tamanio = Integer.parseInt(this.componenteLexico.getValor());
		compara("integer");
		compara("closed_square_bracket");
	}

	private void variable() {
		if (!busqueda(this.componenteLexico.getValor())) {
			System.out.println("Error en la linea: " + this.componenteLexico.getLinea() + ", identificador '"
					+ this.componenteLexico.getValor() + "' no declarado");
		}
		compara("id");
		if (this.componenteLexico.getEtiqueta().equals("open_square_bracket")) {
			compara("open_square_bracket");
			expresion();
			compara("closed_square_bracket");
		}
	}

}
