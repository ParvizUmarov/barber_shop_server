package barber.app.services;

import barber.app.dto.ChatDto;
import barber.app.entity.Barber;
import barber.app.entity.Chat;
import barber.app.entity.Customer;
import barber.app.repositories.BarberRepository;
import barber.app.repositories.ChatRepository;
import barber.app.repositories.CustomerRepository;
import barber.app.repositories.RedisRepository;
import barber.app.restExceptionHandler.ResourceNotFoundException;
import barber.app.session.SessionUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class ChatService implements CRUDService<ChatDto>{

    private final ChatRepository chatRepository;
    private final BarberRepository barberRepository;
    private final CustomerRepository customerRepository;
    private final RedisRepository redisRepository;

    @Override
    public Collection<ChatDto> getAll() {
        return chatRepository.findAll()
                .stream()
                .map(ChatService::mapToDto)
                .toList();
    }

    @Override
    public void create(ChatDto chatDto, String token) {
       barberRepository.findById(chatDto.getBarberId()).orElseThrow(() -> new ResourceNotFoundException("Barber not found"));
       customerRepository.findById(chatDto.getCustomerId()).orElseThrow(() -> new ResourceNotFoundException("Customer not found"));
       chatRepository.save(mapToEntity(chatDto));
    }

    @Override
    public void update(ChatDto chatDto, String token) {

    }

    @Override
    public void delete(Integer id, String token) {
        chatRepository.deleteById(id);

    }

    @Override
    public ChatDto get(Integer id, String token) {
        Chat chat = chatRepository.findById(id).orElseThrow(() -> new RuntimeException("Chat not found"));
        return mapToDto(chat);
    }

    @Override
    public SessionUser checkToken(String token) {
        return  redisRepository.checkUserToken(token);
    }

    public Collection<ChatDto> getChatByBarberId(Integer id){
        Collection<Chat> chatByBarberId = chatRepository.findChatByBarberId(id);
        chatByBarberId.forEach((value) -> {
            System.out.println(value);
        });

        return chatRepository.findChatByBarberId(id)
                .stream()
                .map(ChatService::mapToDto)
                .toList();
    }


    public static Chat mapToEntity(ChatDto chatDto){
        Chat chat = new Chat();
        Barber barber = new Barber();
        Customer customer = new Customer();
        barber.setId(chatDto.getBarberId());
        customer.setId(chatDto.getCustomerId());
        chat.setId(chatDto.getId());
        chat.setBarber(barber);
        chat.setCustomer(customer);
        return chat;
    }

    public static ChatDto mapToDto(Chat chat){
        ChatDto chatDto = new ChatDto();
        chatDto.setId(chat.getId());
        chatDto.setCustomerId(chat.getCustomer().getId());
        chatDto.setBarberId(chat.getBarber().getId());
        return chatDto;
    }

}
