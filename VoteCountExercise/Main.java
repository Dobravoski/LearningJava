package VoteCountExercise;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class Main {
    static void main(String[] args) {
        Map<String, Integer> candidates = new HashMap<>();
        Path path = Path.of("src", "VoteCountExercise", "votes.txt");
        try(var br = Files.newBufferedReader(path)) {
            String line;
            while((line = br.readLine()) != null){
                String[] data  = line.split(",");
                if (!candidates.containsKey(data[0])) {
                    candidates.put(data[0], Integer.parseInt(data[1]));
                } else {
                    candidates.put(data[0], (candidates.get(data[0]) + Integer.parseInt(data[1])));
                }
            }
        } catch (IOException e) {
            System.out.println("Error opening file: "  + e.getMessage());
        }

        System.out.println("Candidates:");
        for(String key : candidates.keySet()) {
            System.out.printf("%s: %d\n", key, candidates.get(key));
        }
    }
}
