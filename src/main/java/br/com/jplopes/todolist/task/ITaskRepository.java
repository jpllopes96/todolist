package br.com.jplopes.todolist.task;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ITaskRepository extends JpaRepository<TaskModel, String>{
   List<TaskModel> findByIdUser(String idUser);
   TaskModel findByIdAndIdUser(String id, String idUser);
    
}
