package aleamb.regexengine.tests;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import aleamb.regexengine.parser.ASTNode;
import aleamb.regexengine.parser.LexerException;
import aleamb.regexengine.parser.LexicalAnalyzer;
import aleamb.regexengine.parser.Parser;
import aleamb.regexengine.parser.SyntaxException;

public class TestParser {

	private final static Logger LOGGER = LoggerFactory
			.getLogger(TestParser.class);

	@Test(expected = SyntaxException.class)
	public void test1() throws Exception {
		testRegex("");
	}

	@Test(expected = SyntaxException.class)
	public void test2() throws Exception {
		testRegex("      ");
	}

	@Test
	public void test3() throws Exception {
		testRegex("a");
	}

	@Test
	public void test4() throws Exception {
		testRegex("#");
	}

	@Test
	public void test5() throws Exception {
		testRegex(".");
	}

	@Test
	public void test6() throws Exception {
		testRegex("dsadasdas");
	}

	@Test
	public void test7() throws Exception {
		testRegex("adasd asdsa");
	}

	@Test(expected = SyntaxException.class)
	public void test8() throws Exception {
		testRegex("()");
	}

	@Test(expected = SyntaxException.class)
	public void test9() throws Exception {
		testRegex("(");
	}

	@Test(expected = SyntaxException.class)
	public void test10() throws Exception {
		testRegex(")");
	}

	@Test(expected = SyntaxException.class)
	public void test11() throws Exception {
		testRegex("[");
	}

	@Test
	public void test12() throws Exception {
		testRegex("]");
	}

	@Test(expected = SyntaxException.class)
	public void test13() throws Exception {
		testRegex("[]");
	}

	@Test
	public void test14() throws Exception {
		testRegex("-");
	}

	@Test
	public void test15() throws Exception {
		testRegex("a-");
	}

	@Test
	public void test16() throws Exception {
		testRegex("[a-]");
	}

	@Test
	public void test17() throws Exception {
		testRegex("[a-.]");
	}

	@Test
	public void test18() throws Exception {
		testRegex("a-.");
	}

	@Test
	public void test19() throws Exception {
		testRegex("\\s");
	}

	@Test
	public void test20() throws Exception {
		testRegex("a\\s");
	}

	@Test
	public void test21() throws Exception {
		testRegex("a\\sa");
	}

	@Test
	public void test22() throws Exception {
		testRegex("a\\s]a");
	}

	@Test
	public void test23() throws Exception {
		testRegex("a    a	\\s");
	}

	@Test
	public void test24() throws Exception {
		testRegex("[a-z]");
	}

	@Test
	public void test25() throws Exception {
		testRegex("(a-z)");
	}

	@Test
	public void test26() throws Exception {
		testRegex("a|z");
	}

	@Test
	public void test27() throws Exception {
		testRegex("a\\|");
	}

	@Test(expected = SyntaxException.class)
	public void test28() throws Exception {
		testRegex("a|");
	}

	@Test
	public void test29() throws Exception {
		testRegex("(a)");
	}

	@Test
	public void test30() throws Exception {
		testRegex("(a)(a)");
	}

	@Test
	public void test31() throws Exception {
		testRegex("(a)+(a)");
	}

	@Test(expected = SyntaxException.class)
	public void test32() throws Exception {
		testRegex("(a)+()");
	}

	@Test
	public void test33() throws Exception {
		testRegex("(a)+ [a-z]*");
	}

	@Test
	public void test34() throws Exception {
		testRegex("[a-z][0-9][0-9]");
	}

	@Test
	public void test35() throws Exception {
		testRegex(".*=.*");
	}

	@Test
	public void test36() throws Exception {
		testRegex("[a-zA-Z0-9]=.*");
	}

	@Test
	public void test37() throws Exception {
		testRegex("[a-zA-Z0-9]=\\\\^=#\\\\");
	}

	@Test
	public void test38() throws Exception {
		testRegex("[a-zA-Z0-9]=[^=#]");

	}

	@Test
	public void test39() throws Exception {
		testRegex("[a-zA-Z0-9]+=[^=#]*");

	}

	@Test
	public void test40() throws Exception {
		testRegex("[a-zA-Z0-9a-z]+");

	}

	@Test
	public void test41() throws Exception {
		testRegex(".*");

	}

	@Test
	public void test42() throws Exception {
		testRegex(".[a-z]");

	}

	@Test
	public void test43() throws Exception {
		testRegex(".*[a-z]");

	}

	@Test
	public void test44() throws Exception {
		testRegex(".*.*.+");

	}

	@Test
	public void test45() throws Exception {
		testRegex("a+a+a+a+a*");

	}

	@Test
	public void test46() throws Exception {
		testRegex("[a-z][A-Z][0-9].*.+\\s\\s");

	}

	@Test
	public void test47() throws Exception {
		testRegex("^[a-z][A-Z][0-9].*.+\\s\\s");

	}

	@Test
	public void test48() throws Exception {
		testRegex("^[a-z][A-Z][0-9].*.+\\s\\s");

	}

	@Test
	public void test49() throws Exception {
		testRegex("[0-].*");

	}

	@Test
	public void test50() throws Exception {
		testRegex("[0-*]");

	}

	@Test
	public void test51() throws Exception {
		testRegex("[^abc]");

	}

	@Test
	public void test52() throws Exception {
		testRegex("^abc]");

	}

	@Test
	public void test53() throws Exception {
		testRegex("{[a-z]}");

	}

	@Test
	public void test54() throws Exception {
		testRegex("[\\s-z]");

	}

	@Test
	public void test55() throws Exception {
		testRegex("[\\s-\\t]");

	}

	@Test
	public void test56() throws Exception {
		testRegex("[\\s-\\\\]");

	}

	@Test(expected = LexerException.class)
	public void test57() throws Exception {
		testRegex("[\\s-\\");

	}

	@Test
	public void test58() throws Exception {
		testRegex("[.-*]");

	}

	@Test
	public void test59() throws Exception {
		testRegex("\\s*");

	}

	@Test
	public void test60() throws Exception {
		testRegex("\\s+");

	}

	@Test
	public void test61() throws Exception {
		testRegex("Interruptor (apagado|encendido)");

	}

	@Test
	public void test62() throws Exception {
		testRegex("J(avier|uan) Pérez");

	}

	@Test
	public void test63() throws Exception {
		testRegex("J(avier|uan) Perez");

	}

	@Test
	public void test64() throws Exception {
		testRegex("(aa|b)+");

	}

	@Test
	public void test65() throws Exception {
		testRegex("((a))");

	}

	@Test
	public void test66() throws Exception {
		testRegex("(a(a))(a)");

	}

	@Test
	public void test67() throws Exception {
		testRegex("aa (aa|bb)");

	}

	@Test
	public void test68() throws Exception {
		testRegex("(a|b)");

	}

	@Test
	public void test69() throws Exception {
		testRegex("(aa)+");
	}

	@Test
	public void test70() throws Exception {
		testRegex("(a)|-(b)|(a)|(b)|(a)|(b)|(a)|(b)|(a)|(b)|(a)|(b)|(a)|(b)|(a)|(b)");
	}

	@Test
	public void test71() throws Exception {
		testRegex("[a-z][a-z][a-z][a-z][a-z][a-z][a-z][a-z]");
	}

	@Test(expected = SyntaxException.class)
	public void test72() throws Exception {
		testRegex("|a");
	}

	@Test(expected = SyntaxException.class)
	public void test73() throws Exception {
		testRegex("[|a");
	}

	@Test
	public void test74() throws Exception {
		testRegex("]|a");
	}

	@Test(expected = SyntaxException.class)
	public void test75() throws Exception {
		testRegex("(a)|-(b)|(a)|(b)|(a)|(b)|(a)|b)|(a)|(b)|(a)|(b)|(a)|(b)|(a)|(b)");
	}

	@Test(expected = SyntaxException.class)
	public void test76() throws Exception {
		testRegex("(a)|b))");
	}

	@Test
	public void test77() throws Exception {
		testRegex("(a(a|b))|hy");
	}

	@Test
	public void test78() throws Exception {
		testRegex("h+y*(a)");
	}

	@Test
	public void test79() throws Exception {
		testRegex("[a\\--Z\\\\908druweriop]");
	}

	@Test
	public void test80() throws Exception {
		testRegex("[a\\-Z\\\\908druweriop]");
	}

	@Test(expected = SyntaxException.class)
	public void test81() throws Exception {
		testRegex("[[]");
	}

	@Test
	public void test82() throws Exception {
		testRegex("[\\[]");
	}

	@Test
	public void test83() throws Exception {
		testRegex("[.(+]");
	}

	@Test(expected = SyntaxException.class)
	public void test84() throws Exception {
		testRegex("b)");
	}

	@Test(expected = SyntaxException.class)
	public void test85() throws Exception {
		testRegex(")");
	}

	private ASTNode testRegex(String regex) throws Exception {

		LexicalAnalyzer lexicalAnalyzer;
		try {
			lexicalAnalyzer = new LexicalAnalyzer(regex);
			Parser syntaxAnalyzer = new Parser(lexicalAnalyzer);
			ASTNode ast = syntaxAnalyzer.analyze();
			LOGGER.debug(ast.toString());
			return ast;

		} catch (Exception e) {
			LOGGER.debug(e.getMessage());
			throw e;
		}

	}

}
