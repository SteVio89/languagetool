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

package org.languagetool.rules.pt;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.languagetool.JLanguageTool;
import org.languagetool.TestTools;
import org.languagetool.language.Portuguese;
import org.languagetool.rules.RuleMatch;

import java.io.IOException;

/**
 *
 * @author Tiago F. Santos based on SimpleReplaceRule test
 * @since 3.6
 */
public class PortugueseWikipediaRuleTest {

  private PortugueseWikipediaRule rule;
  private JLanguageTool lt;

  @BeforeEach
  public void setUp() throws Exception {
    rule = new PortugueseWikipediaRule(TestTools.getMessages("pt"));
    lt = new JLanguageTool(new Portuguese());
  }

  @Test
  public void testRule() throws IOException {

    // correct sentences:
    Assertions.assertEquals(0, rule.match(lt.getAnalyzedSentence("Estas frase não tem erros comuns na Wikipédia.")).length);

    // incorrect sentences:

    // at the beginning of a sentence (Romanian replace rule is case-sensitive)
    checkSimpleReplaceRule("Isto é tecnologia do século 21.", "século XXI");
    // inside sentence
    checkSimpleReplaceRule("Isto é tecnologia do Século 21.", "Século XXI");
  }

  /**
   * Check if a specific replace rule applies.
   * @param sentence the sentence containing the incorrect/misspelled word.
   * @param word the word that is correct (the suggested replacement).
   */
  private void checkSimpleReplaceRule(String sentence, String word) throws IOException {
    RuleMatch[] matches = rule.match(lt.getAnalyzedSentence(sentence));
    Assertions.assertEquals(1, matches.length, "Invalid matches.length while checking sentence: "
            + sentence);
    Assertions.assertEquals(1, matches[0].getSuggestedReplacements().size(), "Invalid replacement count wile checking sentence: "
            + sentence);
    Assertions.assertEquals(word, matches[0].getSuggestedReplacements().get(0), "Invalid suggested replacement while checking sentence: "
            + sentence);
  }
}
