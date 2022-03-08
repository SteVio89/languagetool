/* LanguageTool, a natural language style checker 
 * Copyright (C) 2009 Marcin Miłkowski (http://www.languagetool.org)
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
package org.languagetool.gui;

import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.Assertions;


public class ToolsTest {

  @Test
  public void testShortenComment() {
    String testString = "Lorem ipsum dolor sit amet, consectetur (adipisici elit), sed eiusmod tempor incidunt.";
    String testStringShortened = "Lorem ipsum dolor sit amet, consectetur (adipisici elit), sed eiusmod tempor incidunt.";
    String testLongString = "Lorem ipsum dolor sit amet, consectetur (adipisici elit), sed eiusmod tempor incidunt ut labore (et dolore magna aliqua).";
    String testLongStringShortened = "Lorem ipsum dolor sit amet, consectetur (adipisici elit), sed eiusmod tempor incidunt ut labore.";
    String testVeryLongString = "Lorem ipsum dolor sit amet, consectetur (adipisici elit), sed eiusmod (tempor incidunt [ut labore et dolore magna aliqua]).";
    String testVeryLongStringShortened = "Lorem ipsum dolor sit amet, consectetur (adipisici elit), sed eiusmod (tempor incidunt).";
    String shortenedString = Tools.shortenComment(testString);
    Assertions.assertEquals(testStringShortened, shortenedString);
    String shortenedLongString = Tools.shortenComment(testLongString);
    Assertions.assertEquals(testLongStringShortened, shortenedLongString);
    String shortenedVeryLongString = Tools.shortenComment(testVeryLongString);
    Assertions.assertEquals(testVeryLongStringShortened, shortenedVeryLongString);
  }

  @Test
  public void testGetLabel() {
    Assertions.assertEquals("This is a Label", Tools.getLabel("This is a &Label"));
    Assertions.assertEquals("Bits & Pieces", Tools.getLabel("Bits && Pieces"));
  }

  @Test
  public void testGetOOoLabel() {
    Assertions.assertEquals("Bits & Pieces", Tools.getLabel("Bits && Pieces"));
  }

  @Test
  public void testGetMnemonic() {
    Assertions.assertEquals('F', Tools.getMnemonic("&File"));
    Assertions.assertEquals('O', Tools.getMnemonic("&OK"));
    Assertions.assertEquals('\u0000', Tools.getMnemonic("File && String operations"));
    Assertions.assertEquals('O', Tools.getMnemonic("File && String &Operations"));
  }

}
