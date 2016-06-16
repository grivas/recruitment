package simplesearch;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static java.nio.file.Files.readAllBytes;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;

public class Main {

    private static final String WORD_SEPARATOR = "[\\W]";

    public static void main(String[] args) throws IOException {
        if (args.length==0){
            System.out.println("You must provide a valid path");
            System.exit(0);
        }
        String dir = args[0];
        Map<String, Set<String>> dictionary = buildWordDictionary(dir);
        BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));
        while (true){
            System.out.print("search> ");
            String line = keyboard.readLine();
            if(line!=null && !line.isEmpty()){
                Set<String> words = new HashSet<>(asList(line.split(WORD_SEPARATOR)));
                Map<String, Long> matches = countMatchingWordPerFile(dictionary, words);
                printMatchResults(words, matches);
            }
        }
    }

    /**
     * @param dir directory to evaluate
     * @return map containing all found words as key and a list of the files that contain those words as value
     */
    private static Map<String, Set<String>> buildWordDictionary(String dir){
        Map<String, Set<String>> dictionary = new HashMap<>();
        try {
            Long fileCounter = Files.walk(new File(dir).toPath())
                    .filter(p -> !p.toFile().isDirectory())
                    .map(FileWords::from)
                    .peek(fileWords -> fileWords.words.forEach(word -> {
                        if (!dictionary.containsKey(word))
                            dictionary.put(word, new HashSet<>());
                        dictionary.get(word).add(fileWords.path.toString());
                    })).collect(Collectors.counting());
            System.out.println(String.format("%d files read in directory %s",fileCounter, dir));
        } catch (IOException e) {
            System.out.println(e.toString());
            System.exit(0);
        }
        return dictionary;
    }

    /**
     *
     * @param dictionary containing all the words and files containing them
     * @param words to be matched
     * @return all matching files and the number of words matched
     */
    private static Map<String, Long> countMatchingWordPerFile(Map<String, Set<String>> dictionary, Set<String> words) {
        return words.stream()
                .filter(dictionary::containsKey)
                .flatMap(word -> dictionary.get(word).stream())
                .collect(groupingBy(file -> file, counting()));
    }

    private static void printMatchResults(Set<String> words, Map<String, Long> matches) {
        if(matches.isEmpty())
            System.out.println("no matches found");
        else
            matches.entrySet().forEach(entry ->{
                double matchPercentage = ((double) entry.getValue() / words.size()) * 100;
                System.out.printf("%s : %.0f%%\n", entry.getKey(), matchPercentage);
            });
    }

    static class FileWords {
        Path path;
        Set<String> words;

        static FileWords from(Path path){
            FileWords fileContents = new FileWords();
            fileContents.path = path;
            try {
                fileContents.words = new HashSet<>(asList(new String(readAllBytes(path)).split(WORD_SEPARATOR)));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return fileContents;
        }
    }
}
