package org.languagetool.language;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.languagetool.DetectedLanguage;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Slf4j
public class NativeLanguageIdentifier {

    private static List<String> supportedLangs = Arrays.asList("ar,ast-ES,be-BY,br-FR,ca-ES,ca-ES-valencia,da-DK,de,de-AT,de-CH,de-DE,de-DE-x-simple-language,el-GR,en,en-AU,en-CA,en-GB,en-NZ,en-US,en-ZA,eo,es,es-AR,fa,fr,ga-IE,gl-ES,it,ja-JP,km-KH,nl,nl-BE,pl-PL,pt,pt-AO,pt-BR,pt-MZ,pt-PT,ro-RO,ru-RU,sk-SK,sl-SI,sv,ta-IN,tl-PH,uk-UA,zh-CN".split(","));

    private final File linguagaServiceBin;
    private Process linguagaServiceProcess;
    private BufferedReader nativeIn;
    private BufferedWriter nativeOut;

    public NativeLanguageIdentifier(File linguaServiceBin) throws IOException {
        this.linguagaServiceBin = linguaServiceBin;
        initProcess();
    }

    private void initProcess() throws IOException {
        this.linguagaServiceProcess = new ProcessBuilder(linguagaServiceBin.getPath()).start();
        this.nativeIn = new BufferedReader(new InputStreamReader(linguagaServiceProcess.getInputStream()));
        this.nativeOut = new BufferedWriter(new OutputStreamWriter(linguagaServiceProcess.getOutputStream()));
    }

    @SneakyThrows
    public Map<String, Double> detectLanguages(String text) throws IOException {
        if (!linguagaServiceProcess.isAlive()) {
            initProcess();
        }
        nativeOut.write(text);
        nativeOut.newLine();
        nativeOut.flush();

        String line = nativeIn.readLine();
        return parseBuffer(line);
    }

    private Map<String, Double> parseBuffer(String response) {
//        System.out.println(response);
        Map<String, Double> results = new HashMap<>();
        String[] values = response.trim()
                .replace("[", "")
                .replace("]", "")
                .replace("\"", "")
                .split(",");

        for (int i = 0; i < values.length; i++) {
            if (supportedLangs.contains(values[i])) {
                results.put(values[i], (double) (values.length + 1 - i));
            } else {
                results.put("zz", (double) (values.length - i));
            }
        }
        return results;
    }
}
