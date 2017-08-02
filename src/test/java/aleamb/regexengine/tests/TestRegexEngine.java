package es.chathispano.canalprogramacion.regexengine.tests;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.junit.BeforeClass;
import org.junit.Test;

import es.chathispano.canalprogramacion.regexengine.Regex;
import es.chathispano.canalprogramacion.regexengine.RegexEngine;
import es.chathispano.canalprogramacion.regexengine.RegexMatchResult;

public class TestRegexEngine {

	private static String data = null;

	static class TestResult {

		boolean match;
		int startPosition;
		int length;
		String text;

		public int getLength() {
			return length;
		}

		public void setLength(int length) {
			this.length = length;
		}

		public String getText() {
			return text;
		}

		public void setText(String text) {
			this.text = text;
		}

		public boolean isMatch() {
			return match;
		}

		public void setMatch(boolean match) {
			this.match = match;
		}

		public int getStartPosition() {
			return startPosition;
		}

		public void setStartPosition(int startPosition) {
			this.startPosition = startPosition;
		}

	}

	@BeforeClass
	public static void loadDataFile() throws IOException {

		data = FileUtils.readFileToString(new File("text.txt"));

	}

	@Test
	public void test1() {
		compareMatchExecution("a");
	}

	@Test
	public void test2() {
		compareMatchExecution("b");
	}

	@Test
	public void test3() {
		compareMatchExecution("c");
	}

	@Test
	public void test4() {
		compareMatchExecution("aa");
	}

	@Test
	public void test5() {
		compareMatchExecution("bb");
	}

	@Test
	public void test6() {
		compareMatchExecution("lorem");
	}

	@Test
	public void test7() {
		compareMatchExecution("(lorem)");
	}

	@Test
	public void test8() {
		compareMatchExecution("(lo)");
	}

	@Test
	public void test9() {
		compareMatchExecution("lo*");
	}

	@Test
	public void test10() {
		compareMatchExecution("lo.*");
	}

	@Test
	public void test11() {
		compareMatchExecution("[a-z]");
	}

	@Test
	public void test12() {
		compareMatchExecution("[a-z][a-z]");
	}

	@Test
	public void test13() {
		compareMatchExecution("[a-zA-Z0-9]");
	}

	@Test
	public void test14() {
		compareMatchExecution("[a-zA-Z0-9][0-9]");
	}

	@Test
	public void test15() {
		compareMatchExecution("[a-zA-Z0-9][a-z]");
	}

	@Test
	public void test16() {
		compareMatchExecution("[a-z]*");
	}

	@Test
	public void test17() {
		compareMatchExecution("[a-z]+");
	}

	@Test
	public void test18() {
		compareMatchExecution("[a-zA-Z0-9]+");
	}

	@Test
	public void test19() {
		compareMatchExecution("[^abc]+");
	}

	@Test
	public void test20() {
		compareMatchExecution("[\\[-z]");
	}

	@Test
	public void test21() {
		compareMatchExecution("[A-Z][a-z]+");
	}

	@Test
	public void test22() {
		compareMatchExecution("B*");
	}

	@Test
	public void test23() {
		compareMatchExecution("(aa|b)+");
	}

	@Test
	public void test24() {
		compareMatchExecution("(aa(b+)*)");
	}

	@Test
	public void test25() {
		compareMatchExecution("(a)(a)*.+");
	}

	@Test
	public void test26() {
		compareMatchExecution(".");
	}

	@Test
	public void test27() {
		compareMatchExecution(".+");
	}

	@Test
	public void test28() {
		compareMatchExecution(".*");
	}

	@Test
	public void test29() {
		compareMatchExecution("([a-z]|[A-Z])|0+");
	}

	@Test
	public void test30() {
		compareMatchExecution("[0-9]");
	}

	@Test
	public void test31() {
		compareMatchExecution("[0-9]*lorem");
	}

	@Test
	public void test32() {
		compareMatchExecution("(a|b)c");
	}

	private void compareMatchExecution(String regexExpression) {

		TestResult result1 = executeJavaRegex(regexExpression);
		TestResult result2 = executeMyRegexEngine(regexExpression);

		assertEquals("Match no coincide", result1.isMatch(), result2.isMatch());
		assertEquals("No coincide la posicion de comienzo del match",
				result1.getStartPosition(), result2.getStartPosition());
		assertEquals("No coincide longitud de match", result1.getLength(),
				result2.getLength());

	}

	private TestResult executeJavaRegex(String pRegexExpression) {

		Pattern pattern = Pattern.compile(pRegexExpression);

		Matcher matcher = pattern.matcher(data);

		TestResult result = new TestResult();

		boolean match = matcher.find(0);

		result.setMatch(match);
		if (match) {
			result.setStartPosition(matcher.start());
			result.setLength(matcher.end() - matcher.start());
		} else {

			result.setStartPosition(-1);
			result.setLength(0);
		}

		return result;

	}

	private TestResult executeMyRegexEngine(String pRegexExpression) {

		Regex regex = RegexEngine.compile(pRegexExpression);

		RegexMatchResult regexResult = new RegexMatchResult();

		boolean match = regex.match(data.toCharArray(), regexResult);

		TestResult result = new TestResult();

		result.setMatch(match);
		result.setStartPosition(regexResult.getMatchStartPosition());
		result.setLength(regexResult.getMatchLength());

		return result;

	}
}
