package barber.app.controllers;


import barber.app.dto.ChatDto;
import barber.app.services.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;
    private final String HEADER_KEY = "authorization";

    @GetMapping
    public Collection<ChatDto> getAll(){
        log.info("getAll chats");
        return chatService.getAll();
    }



    @PostMapping()
    public void create(@RequestBody ChatDto chatDto, @RequestHeader Map<String, String> headers){
        var token = headers.get(HEADER_KEY);
        log.info("create chat");
        chatService.create(chatDto, token);

    }

    @GetMapping("/barberID/{id}")
    public Collection<ChatDto> getChatByBarberId(@PathVariable Integer id){
        log.info("get chat by barber id: " + id);
        return chatService.getChatByBarberId(id);
    }

    @GetMapping("/customer/{id}")
    public Collection<ChatDto> getChatByCustomerId(@PathVariable Integer id){
        log.info("get chat by customer id: " + id);
        return chatService.getChatByCustomerId(id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id, @RequestHeader Map<String, String> headers){
        var token = headers.get(HEADER_KEY);
        log.info("delete chat by id: {}",id);
        chatService.delete(id, token);
    }

}
