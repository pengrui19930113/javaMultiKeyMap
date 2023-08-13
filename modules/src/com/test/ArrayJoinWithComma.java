package com.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class ArrayJoinWithComma {
    public static void main(String[] args) throws IOException {
        final BufferedReader br = Files.newBufferedReader(Path.of("/tmp/caseids02.txt"));
        final List<String> lines = new ArrayList<>();
        String line;
        while(null!=(line=br.readLine())){
            line = line.trim();
            lines.add(line);
        }
        br.close();
        String join = String.join(",", lines);
        System.out.println(join);
    }
}
