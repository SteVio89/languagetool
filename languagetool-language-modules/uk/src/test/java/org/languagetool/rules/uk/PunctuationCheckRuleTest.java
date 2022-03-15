/* LanguageTool, a natural language style checker 
 * Copyright (C) 2005 Daniel Naber (http://www.danielnaber.de)
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301
 * USA
 */

package org.languagetool.rules.uk;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.languagetool.JLanguageTool;
import org.languagetool.TestTools;
import org.languagetool.language.Ukrainian;
import org.languagetool.rules.RuleMatch;

import java.io.IOException;

public class PunctuationCheckRuleTest {

  @Test
  public void testRule() throws IOException {
    PunctuationCheckRule rule = new PunctuationCheckRule(TestTools.getEnglishMessages());

    RuleMatch[] matches;
    JLanguageTool lt = new JLanguageTool(new Ukrainian());
    
    // correct sentences:
    matches = rule.match(lt.getAnalyzedSentence("Дві, коми. Ось: дві!!!"));
    Assertions.assertEquals(0, matches.length);

    // correct sentences:
    matches = rule.match(lt.getAnalyzedSentence("- Це ваша пряма мова?!!"));
    Assertions.assertEquals(0, matches.length);

    // correct sentences:
    matches = rule.match(lt.getAnalyzedSentence("Дві,- коми!.."));
    Assertions.assertEquals(0, matches.length);

    // correct sentences:
    matches = rule.match(lt.getAnalyzedSentence("Таке питання?.."));
    Assertions.assertEquals(0, matches.length);

    // correct sentences:
    matches = rule.match(lt.getAnalyzedSentence("Два  пробіли."));  // поки що ігноруємо - не царська це справа :)
    Assertions.assertEquals(0, matches.length);

    // incorrect sentences:
    matches = rule.match(lt.getAnalyzedSentence("Дві крапки.."));
    Assertions.assertEquals(1, matches.length);
    Assertions.assertEquals(1, matches[0].getSuggestedReplacements().size());
    Assertions.assertEquals(".", matches[0].getSuggestedReplacements().get(0));

    // incorrect sentences:
    matches = rule.match(lt.getAnalyzedSentence("Дві,, коми."));
    Assertions.assertEquals(1, matches.length);

    // incorrect sentences:
    matches = rule.match(lt.getAnalyzedSentence("Не там ,кома."));
    Assertions.assertEquals(1, matches.length);

    // incorrect sentences:
    matches = rule.match(lt.getAnalyzedSentence("Двокрапка:- з тире."));
    Assertions.assertEquals(1, matches.length);
  }
}
