/* LanguageTool, a natural language style checker 
 * Copyright (C) 2021 Daniel Naber (http://www.danielnaber.de)
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
package org.languagetool.rules.nl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.languagetool.JLanguageTool;
import org.languagetool.TestTools;
import org.languagetool.language.Dutch;
import org.languagetool.rules.RuleMatch;

import java.io.IOException;

public class CheckCaseRuleTest {
  private CheckCaseRule rule;
  private JLanguageTool lt;

  @BeforeEach
  public void setUp() throws Exception {
    rule = new CheckCaseRule(TestTools.getMessages("nl"), new Dutch());
    lt = new JLanguageTool(new Dutch());
  }

  @Test
  public void testRule() throws IOException {

    // correct sentences:
    Assertions.assertEquals(0, rule.match(lt.getAnalyzedSentence("een bisschop")).length);
    Assertions.assertEquals(0, rule.match(lt.getAnalyzedSentence("Een bisschop")).length);

    // incorrect sentences:
    RuleMatch[] matches = rule.match(lt.getAnalyzedSentence("Een Bisschop"));
    Assertions.assertEquals(1, matches.length);
    Assertions.assertEquals("Een bisschop", matches[0].getSuggestedReplacements().get(0));

    matches = rule.match(lt.getAnalyzedSentence("Hij is een Bisschop."));
    Assertions.assertEquals(1, matches.length);
    Assertions.assertEquals("een bisschop", matches[0].getSuggestedReplacements().get(0));

  }
}
