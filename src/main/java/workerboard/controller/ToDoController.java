package workerboard.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import workerboard.exception.NotFound;
import workerboard.model.ToDoCard;
import workerboard.model.dto.ToDoCardCreateDto;
import workerboard.model.dto.ToDoCardToMoveDto;
import workerboard.model.dto.ToDoCardUpdateDto;
import workerboard.repository.ApplicationUserCustomRepository;
import workerboard.repository.ToDoCardHistoryRepository;
import workerboard.repository.ToDoRepository;
import workerboard.serivce.ToDoService;

import javax.validation.Valid;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/todo")
public class ToDoController {


    private ToDoRepository repository;
    private ApplicationUserCustomRepository applicationUserRepository;
    private ToDoCardHistoryRepository toDoCardHistoryRepository;
    private ToDoService toDoService;

    @Autowired
    public ToDoController(ToDoRepository repository, ApplicationUserCustomRepository applicationUserRepository,
                          ToDoCardHistoryRepository toDoCardHistoryRepository, ToDoService toDoService) {
        this.repository = repository;
        this.applicationUserRepository = applicationUserRepository;
        this.toDoCardHistoryRepository = toDoCardHistoryRepository;
        this.toDoService = toDoService;
    }

    @PostMapping
    ResponseEntity<ToDoCard> createToDoCard(@RequestBody @Valid ToDoCardCreateDto toDoCard) throws NotFound {
        return ResponseEntity.ok(toDoService.createToDoCard(toDoCard));
    }

    @GetMapping
    ResponseEntity<List<ToDoCard>> getAllToDoCards(){
        return ResponseEntity.ok(toDoService.getAllToDoCards());
    }

    @GetMapping(path = "/byUser/{userId}")
    ResponseEntity<List<ToDoCard>> getAllToDoCardsByUser(@PathVariable("userId") Long id) throws NotFound {
        return ResponseEntity.ok(toDoService.getAllToDoCardsByUser(id));
    }

    @PutMapping
    ResponseEntity<ToDoCard> updateToDoCard(@RequestBody  ToDoCardUpdateDto toDoCardUpdateDto) throws NotFound {

        System.out.print("\n"+toDoCardUpdateDto.toString() + "\n");
        return ResponseEntity.ok(toDoService.updateToDoCard(toDoCardUpdateDto));
    }

    @GetMapping(path = "/{id}")
    ResponseEntity<ToDoCard> getToDoCardById(@PathVariable("id") Long id) throws NotFound {
        return ResponseEntity.ok(toDoService.getToDoCardById(id));
    }

    @DeleteMapping(path = "/{id}")
    ResponseEntity<?> deleteToDoCardById(@PathVariable("id") Long id) throws NotFound {

        toDoService.deleteToDoCardById(id);
        return ResponseEntity.ok("ok");
    }

    @GetMapping(path = "/history/{id}")
    ResponseEntity<?> getHistoryToDoCard(@PathVariable Long id) throws NotFound {
        return ResponseEntity.ok( toDoCardHistoryRepository.findAllByCardId(id));
    }

    @PostMapping("/moveCard")
    ResponseEntity<?> moveCardToOtherUser(@RequestBody ToDoCardToMoveDto cardMoveDto ) throws NotFound {


        this.toDoService.moveCardToOtherUser(cardMoveDto);
        return ResponseEntity.ok(HttpStatus.OK);

    }

}
