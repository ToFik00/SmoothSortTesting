package org.tf;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.*;

public class Main {
    public static void main(String[] args) throws IOException {
        //createDataSet();
        results();
    }

    public static void createDataSet() throws IOException {
        File file = new File("dataset.json");
        JsonFactory jsonFactory = new JsonFactory();
        JsonGenerator jGenerator;
        jGenerator = jsonFactory.createGenerator(file, JsonEncoding.UTF8);
        jGenerator.setCodec(new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT));

        jGenerator.writeStartObject();
        int count = 0;
        for (int size = 100; size <= 10_000; size = size + 25) {
            int[][] arrays = ArrayGenerator.generateArrays(96, size, -100_000, 100_000);
            jGenerator.writeFieldName("data" + (++count));
            jGenerator.writeObject(arrays);
            System.out.println("matrix with size: " + size + " created");
        }
        jGenerator.writeEndObject();
        jGenerator.close();
    }

    public static void results() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        Reader reader = new FileReader("dataset.json");
        JsonNode jsonNode = objectMapper.readTree(reader);
        JsonNode jsonNodeData;
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("results.csv"));
        for (int i = 1; (jsonNodeData = jsonNode.findValue("data" + i)) != null; i++) {
            int[][] currentDataGroup = objectMapper.readValue(jsonNodeData.toString(), int[][].class);

            long averageTime1 = 0;
            long averageTime2 = 0;
            long averageTime3 = 0;
            long averageIterations1 = 0;
            long averageIterations2 = 0;
            long averageIterations3 = 0;
            for (int[] currentData : currentDataGroup) {
                int[] copy1 = new int[currentData.length];
                int[] copy2 = new int[currentData.length];
                System.arraycopy(currentData, 0, copy1, 0, currentData.length);
                new SmoothSortService().sort(copy1, copy1.length / 2);
                new SmoothSortService().sort(copy2);
                Testing testing1 = new Testing(currentData);
                Testing testing2 = new Testing(copy1);
                Testing testing3 = new Testing(copy2);
                //TODO: Добавить полностью отсортированный

                averageTime1 += testing1.getTime();
                averageIterations1 += testing1.getOperations();
                averageTime2 += testing2.getTime();
                averageIterations2 += testing2.getOperations();
                averageTime3 += testing3.getTime();
                averageIterations3 += testing3.getOperations();
            }
            averageTime1 /= currentDataGroup.length;
            averageIterations1 /= currentDataGroup.length;
            averageTime2 /= currentDataGroup.length;
            averageIterations2 /= currentDataGroup.length;
            averageTime3 /= currentDataGroup.length;
            averageIterations3 /= currentDataGroup.length;
            //TODO: Дописать
            System.out.println("time: " + averageTime1
                    + ", operations: " + averageIterations1
                    + ", time(partSorted): " + averageTime2
                    + ", operations(partSorted): " + averageIterations2
                    + ", time(fullSorted): " + averageTime3
                    + ", operations(fullSorted): " + averageIterations3);
            bufferedWriter.write(currentDataGroup[0].length + ";"
                    + averageTime1 + ";"
                    + averageIterations1 + ";"
                    + averageTime2 + ";"
                    + averageIterations2 + ";"
                    + averageTime3 + ";"
                    + averageIterations3);
            bufferedWriter.newLine();
        }
        bufferedWriter.close();
    }
}
