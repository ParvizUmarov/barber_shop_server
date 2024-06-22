package barber.app.controllers;


import barber.app.dto.ChatDto;
import barber.app.services.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @GetMapping
    public Collection<ChatDto> getAll(){
        log.info("getAll chats");
        return chatService.getAll();
    }



    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public void create(@RequestBody ChatDto chatDto){
        log.info("create chat");
        chatService.create(chatDto);

    }

    @GetMapping("/barberID/{id}")
    public Collection<ChatDto> getChatByBarberId(@PathVariable Integer id){
        log.info("get chat by barber id: " + id);
        return chatService.getChatByBarberId(id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id){
        log.info("delete chat by id: {}",id);
        chatService.delete(id);
    }

}
