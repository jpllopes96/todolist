package br.com.jplopes.todolist.user;

import org.springframework.data.jpa.repository.JpaRepository;



public interface IUserRepository extends JpaRepository<UserModel, String>{
    UserModel findByUsername(String username);
    
}
