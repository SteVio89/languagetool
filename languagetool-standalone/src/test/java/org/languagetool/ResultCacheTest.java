/* LanguageTool, a natural language style checker
 * Copyright (C) 2017 Daniel Naber (http://www.danielnaber.de)
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
package org.languagetool;

import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import static org.hamcrest.core.Is.is;

public class ResultCacheTest {
  
  @Test
  public void testSimpleInputSentenceCache() {
    ResultCache cache = new ResultCache(100);
    MatcherAssert.assertThat(cache.hitCount(), is(0L));
    MatcherAssert.assertThat(cache.hitRate(), is(1.0));
    cache.put(new SimpleInputSentence("foo", Languages.getLanguageForShortCode("de")), new AnalyzedSentence(new AnalyzedTokenReadings[]{}));
    Assertions.assertNotNull(cache.getIfPresent(new SimpleInputSentence("foo", Languages.getLanguageForShortCode("de"))));
    Assertions.assertNull(cache.getIfPresent(new SimpleInputSentence("foo", Languages.getLanguageForShortCode("de-DE"))));
    Assertions.assertNull(cache.getIfPresent(new SimpleInputSentence("foo", Languages.getLanguageForShortCode("en"))));
    Assertions.assertNull(cache.getIfPresent(new SimpleInputSentence("foo bar", Languages.getLanguageForShortCode("de"))));
  }

  @Test
  public void testInputSentenceCache() {
    ResultCache cache = new ResultCache(100);
    MatcherAssert.assertThat(cache.hitCount(), is(0L));
    MatcherAssert.assertThat(cache.hitRate(), is(1.0));
    UserConfig userConfig1 = new UserConfig(Arrays.asList("word1"));
    JLanguageTool.Mode mode = JLanguageTool.Mode.ALL;
    JLanguageTool.Level level = JLanguageTool.Level.DEFAULT;
    List<Language> el = Collections.emptyList();
    InputSentence input1a = new InputSentence("foo", Languages.getLanguageForShortCode("de"), null, new HashSet<>(), new HashSet<>(), new HashSet<>(), new HashSet<>(), userConfig1, el, mode, level);
    InputSentence input1b = new InputSentence("foo", Languages.getLanguageForShortCode("de"), null, new HashSet<>(), new HashSet<>(), new HashSet<>(), new HashSet<>(), userConfig1, el, mode, level);
    cache.put(input1a, Arrays.asList());
    Assertions.assertNotNull(cache.getIfPresent(input1a));
    Assertions.assertNotNull(cache.getIfPresent(input1b));
    InputSentence input2a = new InputSentence("foo bar", Languages.getLanguageForShortCode("de"), null, new HashSet<>(), new HashSet<>(), new HashSet<>(), new HashSet<>(), userConfig1, el, mode, level);
    InputSentence input2b = new InputSentence("foo", Languages.getLanguageForShortCode("en"), null, new HashSet<>(), new HashSet<>(), new HashSet<>(), new HashSet<>(), userConfig1, el, mode, level);
    InputSentence input2c = new InputSentence("foo", Languages.getLanguageForShortCode("de"), Languages.getLanguageForShortCode("en"), new HashSet<>(), new HashSet<>(), new HashSet<>(), new HashSet<>(), userConfig1, el, mode, level);
    InputSentence input2d = new InputSentence("foo", Languages.getLanguageForShortCode("de"), null, new HashSet<>(Arrays.asList("ID1")), new HashSet<>(), new HashSet<>(), new HashSet<>(), userConfig1, el, mode, level);
    Assertions.assertNull(cache.getIfPresent(input2a));
    Assertions.assertNull(cache.getIfPresent(input2b));
    Assertions.assertNull(cache.getIfPresent(input2c));
    Assertions.assertNull(cache.getIfPresent(input2d));
    
    UserConfig userConfig2 = new UserConfig(Arrays.asList("word2"));
    InputSentence input1aUc1 = new InputSentence("foo", Languages.getLanguageForShortCode("de"), null, new HashSet<>(), new HashSet<>(), new HashSet<>(), new HashSet<>(), userConfig1, el, mode, level);
    Assertions.assertNotNull(cache.getIfPresent(input1aUc1));
    InputSentence input1aUc2 = new InputSentence("foo", Languages.getLanguageForShortCode("de"), null, new HashSet<>(), new HashSet<>(), new HashSet<>(), new HashSet<>(), userConfig2, el, mode, level);
    Assertions.assertNull(cache.getIfPresent(input1aUc2));

    InputSentence input1aUc2Alt = new InputSentence("foo", Languages.getLanguageForShortCode("de"), null, new HashSet<>(),
            new HashSet<>(), new HashSet<>(), new HashSet<>(), userConfig2, Arrays.asList(Languages.getLanguageForShortCode("en")), mode, level);
    Assertions.assertNull(cache.getIfPresent(input1aUc2Alt));
  }

}
