package net.bobdb.fakegpt;


import io.github.wimdeblauwe.htmx.spring.boot.mvc.HtmxResponse;
import io.github.wimdeblauwe.htmx.spring.boot.mvc.HxRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@CrossOrigin
public class FakeGPTController {

    private static final Logger log = LoggerFactory.getLogger(FakeGPTController.class);
    private final ChatClient client;

    public FakeGPTController(ChatClient.Builder builder) {
        this.client = builder
                .defaultAdvisors(new MessageChatMemoryAdvisor(new InMemoryChatMemory()))
            .build();
    }

    @GetMapping("")
    public String home() {
        return "index";
    }

    @HxRequest
    @PostMapping("/api/chat")
    public HtmxResponse generate(@RequestParam String message, Model model) {

      String response = client.prompt()
              .user(message)
              .call()
              .content();
      model.addAttribute("response",response);
      model.addAttribute("message",message);

      return HtmxResponse.builder()
            .view("response :: responseFragment")
            .view("recent-message-list :: messageFragment")
        .build();

    }

}
