package org.example;


import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.ollama.OllamaChatModel;

public class Chatbot {


    public static final String RESET = "\033[0m";  // Text Reset
    public static final String RED = "\033[0;31m";     // RED
    public static final String GREEN = "\033[0;32m";   // GREEN
    public static final String YELLOW = "\033[0;33m";  // YELLOW
    public static final String BLUE = "\033[0;34m";    // BLUE
    public static final String PURPLE = "\033[0;35m";  // PURPLE
    public static final String CYAN = "\033[0;36m"; //CYAN



    static String MODEL_NAME = "llama3.2";
    private static ChatLanguageModel chatModel = OllamaChatModel.builder()
            .baseUrl("http://localhost:11434")
            .modelName(MODEL_NAME)
            .build();

    // Method for asking a general doubt
    public static String askGeneralDoubt(String question) {
        return chatModel.generate(question);
    }

    // Method for asking a doubt about a PDF
    public static String askDoubtAboutPDF(String pdfPath, String question) {
        // Extract text from PDF
        pdfPath=pdfPath.replace("'", "");
        String context = PDFTextExtractor.extractTextFromPDF(pdfPath);

        // Format final query with context
        String finalQuery = String.format(
                "Answer the question according to the given context. Context: %s. Question: %s",
                context, question
        );

        // Get answer from chat model
        return chatModel.generate(finalQuery);
    }


}
