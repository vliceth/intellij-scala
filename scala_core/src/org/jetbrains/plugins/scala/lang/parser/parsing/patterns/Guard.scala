package org.jetbrains.plugins.scala.lang.parser.parsing.patterns

import com.intellij.lang.PsiBuilder, org.jetbrains.plugins.scala.lang.lexer.ScalaTokenTypes
import org.jetbrains.plugins.scala.lang.parser.ScalaElementTypes
import org.jetbrains.plugins.scala.lang.lexer.ScalaElementType
import org.jetbrains.plugins.scala.lang.parser.bnf.BNF
import com.intellij.psi.tree.TokenSet
import com.intellij.psi.tree.IElementType
import org.jetbrains.plugins.scala.lang.parser.util.ParserUtils
import org.jetbrains.plugins.scala.lang.parser.parsing.types._
import org.jetbrains.plugins.scala.lang.parser.parsing.top.template._
import org.jetbrains.plugins.scala.lang.parser.parsing.expressions._
import org.jetbrains.plugins.scala.lang.parser.bnf._

/** 
* @author Alexander Podkhalyuzin
* Date: 28.02.2008
*/

object Guard {
  def parse(builder: PsiBuilder): Boolean = parse(builder, false) //deprecated if true
  def parse(builder: PsiBuilder, noIf: Boolean): Boolean = {
    val guardMarker = builder.mark
    builder.getTokenType match {
      case ScalaTokenTypes.kIF => {
        builder.advanceLexer //Ate if
      }
      case _ => {
        if (!noIf) {
          guardMarker.drop()
          return false
        }
      }
    }
    if (!PostfixExpr.parse(builder)) {
      if (noIf) {
        guardMarker.drop()
        return false
      }
      builder error ErrMsg("wrong.postfix.expression")
    }
    guardMarker.done(ScalaElementTypes.GUARD)
    return true
  }
}