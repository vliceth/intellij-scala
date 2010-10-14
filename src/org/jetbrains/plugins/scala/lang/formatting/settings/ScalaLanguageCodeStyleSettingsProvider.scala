package org.jetbrains.plugins.scala.lang.formatting.settings

import java.lang.String
import com.intellij.psi.codeStyle.LanguageCodeStyleSettingsProvider.SettingsType
import com.intellij.lang.Language
import org.jetbrains.plugins.scala.ScalaFileType
import com.intellij.psi.codeStyle.{CommonCodeStyleSettings, CodeStyleSettingsCustomizable, LanguageCodeStyleSettingsProvider}
import collection.mutable.ArrayBuffer
import com.intellij.openapi.application.ApplicationBundle

/**
 * @author Alexander Podkhalyuzin
 */

class ScalaLanguageCodeStyleSettingsProvider extends LanguageCodeStyleSettingsProvider {
  def getCodeSample(settingsType: SettingsType): String = {
    settingsType match {
      case SettingsType.BLANK_LINES_SETTINGS => BLANK_LINES_CODE_SAMPLE
      case SettingsType.LANGUAGE_SPECIFIC => GENERAL_CODE_SAMPLE //todo:
      case SettingsType.SPACING_SETTINGS => GENERAL_CODE_SAMPLE //todo:
      case SettingsType.WRAPPING_AND_BRACES_SETTINGS => WRAPPING_AND_BRACES_SAMPLE
    }
  }

  def getLanguage: Language = ScalaFileType.SCALA_LANGUAGE

  override def customizeSettings(consumer: CodeStyleSettingsCustomizable, settingsType: SettingsType): Unit = {
    def showCustomOption(fieldName: String, title: String, groupName: String, options: AnyRef*) {
      consumer.showCustomOption(classOf[ScalaCodeStyleSettings], fieldName, title, groupName, options: _*)
    }

    val buffer: ArrayBuffer[String] = new ArrayBuffer
    //blank lines
    if (settingsType == SettingsType.BLANK_LINES_SETTINGS) {
      buffer ++= Seq("KEEP_BLANK_LINES_IN_CODE", "BLANK_LINES_AFTER_CLASS_HEADER",
        "KEEP_BLANK_LINES_BEFORE_RBRACE", "KEEP_BLANK_LINES_IN_DECLARATIONS", "BLANK_LINES_BEFORE_PACKAGE",
        "BLANK_LINES_AFTER_PACKAGE", "BLANK_LINES_BEFORE_IMPORTS", "BLANK_LINES_AFTER_IMPORTS",
        "BLANK_LINES_AROUND_CLASS", "BLANK_LINES_AFTER_ANONYMOUS_CLASS_HEADER", "BLANK_LINES_AROUND_FIELD_IN_INTERFACE",
        "BLANK_LINES_AROUND_FIELD", "BLANK_LINES_AROUND_METHOD_IN_INTERFACE", "BLANK_LINES_AROUND_METHOD",
        "BLANK_LINES_BEFORE_METHOD_BODY")
    }

    if (settingsType == SettingsType.WRAPPING_AND_BRACES_SETTINGS) {
      //Binary expression section
      buffer ++= Seq("BINARY_OPERATION_WRAP", "ALIGN_MULTILINE_BINARY_OPERATION",
        "ALIGN_MULTILINE_PARENTHESIZED_EXPRESSION", "PARENTHESES_EXPRESSION_LPAREN_WRAP",
        "PARENTHESES_EXPRESSION_RPAREN_WRAP")
      consumer.renameStandardOption("BINARY_OPERATION_WRAP", "Wrap infix expressions, patterns and types ")

      //Method calls section
      buffer ++= Seq("CALL_PARAMETERS_WRAP", "ALIGN_MULTILINE_PARAMETERS_IN_CALLS",
        "PREFER_PARAMETERS_WRAP", "CALL_PARAMETERS_LPAREN_ON_NEXT_LINE", "CALL_PARAMETERS_RPAREN_ON_NEXT_LINE")

      //align call parameters
      buffer ++= Seq("ALIGN_MULTILINE_METHOD_BRACKETS")

      //method call chain
      buffer ++= Seq("METHOD_CALL_CHAIN_WRAP", "ALIGN_MULTILINE_CHAINED_METHODS", "KEEP_LINE_BREAKS")

      //brace placement
      buffer ++= Seq("CLASS_BRACE_STYLE", "METHOD_BRACE_STYLE", "BRACE_STYLE")

      //extends list wrap
      buffer ++= Seq("EXTENDS_LIST_WRAP", "ALIGN_MULTILINE_EXTENDS_LIST", "EXTENDS_KEYWORD_WRAP")

      //method parameters
      buffer ++= Seq("METHOD_PARAMETERS_WRAP", "ALIGN_MULTILINE_PARAMETERS", "METHOD_PARAMETERS_LPAREN_ON_NEXT_LINE",
        "METHOD_PARAMETERS_RPAREN_ON_NEXT_LINE")

      //if statement
      buffer ++= Seq("IF_BRACE_FORCE", "ELSE_ON_NEW_LINE", "SPECIAL_ELSE_IF_TREATMENT")

      //brace forces
      buffer ++= Seq("FOR_BRACE_FORCE", "WHILE_BRACE_FORCE", "DOWHILE_BRACE_FORCE", "WHILE_ON_NEW_LINE",
        "INDENT_CASE_FROM_SWITCH", "CATCH_ON_NEW_LINE", "FINALLY_ON_NEW_LINE", "FOR_STATEMENT_WRAP",
        "ALIGN_MULTILINE_FOR", "FOR_STATEMENT_LPAREN_ON_NEXT_LINE", "FOR_STATEMENT_RPAREN_ON_NEXT_LINE")

      //modifier list wrap
      buffer ++= Seq("MODIFIER_LIST_WRAP")
    }

    consumer.showStandardOptions(buffer.toArray:_*)

    //Custom options
    if (settingsType == SettingsType.WRAPPING_AND_BRACES_SETTINGS) {
      showCustomOption("WRAP_BEFORE_WITH_KEYWORD", "Wrap before 'with' keyword",
        ApplicationBundle.message("wrapping.extends.implements.list"))
      showCustomOption("ALIGN_IF_ELSE", "Align if-else statements", ApplicationBundle.message("wrapping.if.statement"))
      showCustomOption("METHOD_BRACE_FORCE", "Force braces", METHOD_DEFINITION,
        CodeStyleSettingsCustomizable.BRACE_OPTIONS, CodeStyleSettingsCustomizable.BRACE_VALUES)
    }

  }

  //custom groups
  private val METHOD_DEFINITION = "Method definition"

  override def getDefaultCommonSettings: CommonCodeStyleSettings = null

  private val GENERAL_CODE_SAMPLE =
    "class A {\n" +
            "  def foo(): Int = 1\n" +
            "}"

  private val WRAPPING_AND_BRACES_SAMPLE =
    "class A {\n" +
            "  def foo {\n" +
            "    val infixExpr = 1 + 2 + (3 + 4) + 5 + 6 +\n" +
            "      7 + 8 + 9 + 10 + 11 + 12 + 13 + (14 +\n" +
            "      15) + 16 + 17 * 18 + 19 + 20\n" +
            "  }\n" +
            "\n" +
            "  class Foo {\n" +
            "    def foo(x: Int = 0, y: Int = 1, z: Int = 2) = new Foo\n" +
            "  }\n" +
            "  \n" +
            "  val goo = new Foo\n" +
            "\n" +
            "  goo.foo().foo(1, 2).foo(z = 1, y = 2).foo().foo(1, 2, 3).foo()\n" +
            "  \n" +
            "  def m(x: Int, y: Int, z: Int)(u: Int, f: Int, l: Int) {\n" +
            "    val zz = if (true) 1 else 3\n" +
            "    val uz = if (true)\n" +
            "               1\n" +
            "              else {\n" +
            "              }\n" +
            "    if (true) {\n" +
            "      false\n" +
            "    } else if (false) {\n" +
            "    } else true\n" +
            "    for (i <- 1 to 5) yield i + 1\n" +
            "    Some(3) match {\n" +
            "      case Some(a) if a != 2 => a\n" +
            "      case Some(a) => \n" +
            "        a + 1\n" +
            "      case _ =>\n" +
            "    }\n" +
            "    try a + 2\n" +
            "    catch {\n" +
            "      case e => (i: Int) => i + 1\n" +
            "    } finally \n" +
            "      doNothing\n" +
            "    while (true) \n" +
            "      true = false\n" +
            "  }\n" +
            "}"

  private val BLANK_LINES_CODE_SAMPLE =
    "//code\npackage A\n" +
            "\n" +
            "\n" +
            "import a.b\n" +
            "\n" +
            "import b.c\n" +
            "import c.d\n" +
            "\n" +
            "\n" +
            "class A {\n" +
            "  def foo = 1\n" +
            "  def goo = 2\n" +
            "  type S = String\n" +
            "  val a = 1\n" +
            "  \n" +
            "  val b = 2\n" +
            "  val c = 2\n" +
            "}\n" +
            "\n" +
            "trait B {\n" +
            "  \n" +
            "  def foo\n" +
            "  def goo\n" +
            "  def too = {\n" +
            "    \n" +
            "    \n" +
            "    val x = 2\n" +
            "    new J {\n" +
            "      def goo = 1\n" +
            "    }\n" +
            "  }\n" +
            "}"
}