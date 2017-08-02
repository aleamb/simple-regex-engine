package es.chathispano.canalprogramacion.regexengine.tests;

import java.text.MessageFormat;

import es.chathispano.canalprogramacion.regexengine.Regex;
import es.chathispano.canalprogramacion.regexengine.RegexEngine;
import es.chathispano.canalprogramacion.regexengine.RegexMatchResult;

public class Example {

	public static void main(String[] args) {

		// texto a comprobar
		String text = "kjdsk65sdksdk78odla98dasdf90dsakdsj";

		// buscar números en el texo
		Regex regex = RegexEngine.compile("[0-9]+");

		// almacenar resultados de cada búsqueda
		RegexMatchResult regexMatchResult = new RegexMatchResult();

		// preparar input
		char buffer[] = text.toCharArray();

		// meintras encuentre números...
		while (regex.match(buffer, regexMatchResult)) {
			int start = regexMatchResult.getMatchStartPosition();
			int length = regexMatchResult.getMatchLength();
			System.out
					.println(MessageFormat
							.format("Encontrada coincidencia en posicion {0}. Texto que coincide: {1}",
									start,
									text.substring(start, start + length)));

			// comenzar desde la última coincidencia
			regexMatchResult.setPosition(start + length);
		}
	}
}
