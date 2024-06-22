package barber.app.services;

import barber.app.dto.MessageDto;
import barber.app.entity.Chat;
import barber.app.entity.Message;
import barber.app.repositories.MessageRepository;
import barber.app.repositories.RedisRepository;
import barber.app.restExceptionHandler.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Collection;

@Service
@RequiredArgsConstructor
public class MessageService implements CRUDService<MessageDto> {

    private final MessageRepository messageRepository;
    private final RedisRepository redisRepository;

    @Override
    public Collection<MessageDto> getAll() {
        return messageRepository.findAll()
                .stream()
                .map(MessageService::mapToDto)
                .toList();
    }

    @Override
    public void create(MessageDto object) {
        messageRepository.save(mapToEntity(object));

    }

    @Override
    public void update(MessageDto object) {

    }

    @Override
    public void delete(Integer id) {
        messageRepository.deleteById(id);
    }

    @Override
    public MessageDto get(Integer id) {
        Message message = messageRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Message not found"));
        return mapToDto(message);
    }

    @Override
    public String checkToken(String mail, String token) {
        return redisRepository.checkUserToken(mail, token);
    }

    public Collection<MessageDto> getMessagesByChatId(Integer id){
        return messageRepository.getMessagesByChatId(id)
                .stream()
                .map(MessageService::mapToDto)
                .toList();
    }

    public static Message mapToEntity(MessageDto messageDto){
        Message message = new Message();
        Chat chat = new Chat();
        chat.setId(messageDto.getChatId());
        message.setId(messageDto.getId());
        message.setChat(chat);
        message.setMessage(messageDto.getMessage());
        message.setTime(messageDto.getTime());
        return message;
    }

    public static MessageDto mapToDto(Message message){
        MessageDto messageDto = new MessageDto();
        messageDto.setId(message.getId());
        if (message.getChat() != null) {
            messageDto.setChatId(message.getChat().getId());
        }
        messageDto.setMessage(message.getMessage());
        messageDto.setTime(message.getTime());
        return messageDto;
    }

}
