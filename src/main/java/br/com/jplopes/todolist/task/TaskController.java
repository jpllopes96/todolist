package br.com.jplopes.todolist.task;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.jplopes.todolist.utils.Utils;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/tasks")
public class TaskController {
    

    @Autowired
    private ITaskRepository iTaskRepository;    

    @PostMapping
    public ResponseEntity create(@RequestBody TaskModel taskModel, HttpServletRequest request){
        taskModel.setIdUser((String) request.getAttribute("idUser"));
        var currentDate = LocalDateTime.now();

        if(currentDate.isAfter(taskModel.getStartAt()) || currentDate.isAfter(taskModel.getEndAt())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body("Data de inicio e data de termino deve ser menor que a data atual");
        }

         if(taskModel.getStartAt().isAfter(taskModel.getEndAt())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body("Data de inicio deve ser depois da data de termino");
        }
        var task = this.iTaskRepository.save(taskModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(task);

    }

    @GetMapping
    public List<TaskModel> list(HttpServletRequest request){
        var idUser = request.getAttribute("idUser");
        var tasks = this.iTaskRepository.findByIdUser((String) idUser);
        return tasks;
    }

    @PutMapping("/{id}")
    public ResponseEntity update(@RequestBody TaskModel taskModel,  @PathVariable String id, HttpServletRequest request){
        var task = this.iTaskRepository.findById(id).orElse(null);
        if(task == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Tarefa não encontrada");
        }
        if(!task.getIdUser().equals(request.getAttribute("idUser"))){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("usuario sem permissão");
        }
        Utils.copyNonNullProperties(taskModel, task);
        var taskUpdated = this.iTaskRepository.save(task);
        return ResponseEntity.ok().body(taskUpdated);
    }
}
