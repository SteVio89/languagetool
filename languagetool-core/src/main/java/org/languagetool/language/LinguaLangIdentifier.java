package org.languagetool.language;

import com.github.pemistahl.lingua.api.IsoCode639_1;
import com.github.pemistahl.lingua.api.Language;
import com.github.pemistahl.lingua.api.LanguageDetector;
import com.github.pemistahl.lingua.api.LanguageDetectorBuilder;
import org.languagetool.Languages;
import org.languagetool.noop.NoopLanguage;

import java.util.*;
import java.util.stream.Stream;

public class LinguaLangIdentifier {

    private final LanguageDetector detector;

    public LinguaLangIdentifier() {
        this(0.0, Collections.emptyList());
    }

    public LinguaLangIdentifier(double minimumRelativeDistance) {
        this(minimumRelativeDistance, Collections.emptyList());
    }

    public LinguaLangIdentifier(double minimumRelativeDistance, List<String> enabledLanguages) {
        List<IsoCode639_1> codes = new ArrayList<>();
        if (enabledLanguages.size() > 0) {
            for (String lang : enabledLanguages) {
                codes.add(IsoCode639_1.valueOf(lang));
            }
            this.detector = LanguageDetectorBuilder
                    .fromIsoCodes639_1(codes.toArray(new IsoCode639_1[0]))
                    .withMinimumRelativeDistance(minimumRelativeDistance)
                    .withPreloadedLanguageModels()
                    .build();
        } else {
            this.detector = LanguageDetectorBuilder
                    .fromAllLanguages()
                    .withMinimumRelativeDistance(minimumRelativeDistance)
                    .withPreloadedLanguageModels()
                    .build();
        }
    }
    
    public void release() {
        detector.destroy();
    }

    public Map<String, Double> detectLanguages(String text) {
        SortedMap<Language, Double> languageDoubleSortedMap = detector.computeLanguageConfidenceValues(text);
        if (languageDoubleSortedMap.isEmpty()) {
            Map<String, Double> result = new HashMap<>();
            result.put(NoopLanguage.SHORT_CODE, 1.0);
            return result;
        }
        Map<String, Double> results = new HashMap<>();
        languageDoubleSortedMap.forEach((language, aDouble) -> results.put(language.getIsoCode639_1().toString().toLowerCase(), aDouble));
        return results;
    }

    public String detectLanguage(String text) {
        Language language = detector.detectLanguageOf(text);
        if (language == Language.UNKNOWN) {
            return NoopLanguage.SHORT_CODE;
        } else {
            return language.getIsoCode639_1().toString();
        }
    }
}
