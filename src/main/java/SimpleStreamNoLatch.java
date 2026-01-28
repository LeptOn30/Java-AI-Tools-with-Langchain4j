import java.time.Duration;
import static java.util.Arrays.asList;

import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.StreamingChatModel;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.chat.response.StreamingChatResponseHandler;
import dev.langchain4j.model.openai.OpenAiStreamingChatModel;

public class SimpleStreamNoLatch {
    public static void main(String[] args) throws InterruptedException {
        StreamingChatModel model = OpenAiStreamingChatModel.builder()
                .apiKey(System.getenv("OPENAI_API_KEY"))
                .modelName("gpt-4o-mini")
                .timeout(Duration.ofSeconds(60))
                .build();

        var messages = asList(
                SystemMessage.from("You are a senior Java software engineer"),
                UserMessage.from("Explain why Java is useful for creating GenAI applications.")
        );

        StreamingChatResponseHandler handler = new StreamingChatResponseHandler() {
            @Override
            public void onPartialResponse(String partial) {
                System.out.print(partial);      // tokens arrive here
            }
            @Override
            public void onCompleteResponse(ChatResponse complete) {
                System.out.println("Complete!");
            }
            @Override
            @SuppressWarnings("CallToPrintStackTrace")
            public void onError(Throwable error) {
                error.printStackTrace();
            }
        };

        model.chat(messages, handler);
    }
}

