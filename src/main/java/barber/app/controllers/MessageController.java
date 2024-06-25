package barber.app.controllers;

import barber.app.dto.MessageDto;
import barber.app.services.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/message")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;
    private final String HEADER_KEY = "authorization";

    @GetMapping
    public Collection<MessageDto> getAll(){
        log.info("getAll messages");
        return messageService.getAll();
    }

    @PostMapping("/send")
    public void create(@RequestBody MessageDto messageDto, @RequestHeader Map<String, String> headers){
        var token = headers.get(HEADER_KEY);
        log.info("create message ");
        messageService.create(messageDto, token);

    }

    @GetMapping("/chatID/{id}")
    public Collection<MessageDto> getMessagesByChatId(@PathVariable Integer id){
        log.info("get messages by chat id: " + id);
        return messageService.getMessagesByChatId(id);
    }

}
