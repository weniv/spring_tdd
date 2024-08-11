package com.example.tdddemo.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.tdddemo.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.TestPropertySource;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = "classpath:application.yml")
public class UserRepositoryTest {

  @Autowired private TestEntityManager entityManager;

  @Autowired private UserRepository userRepository;

  @Test
  public void testFindByEmail() {
    User newUser = new User("test@example.com", "password", "Test User");
    entityManager.persist(newUser);
    entityManager.flush();

    User found = userRepository.findByEmail(newUser.getEmail());

    assertThat(found.getName()).isEqualTo(newUser.getName());
    assertThat(found.getEmail()).isEqualTo(newUser.getEmail());
  }

  @Test
  public void testSaveUser() {
    User user = new User("save@example.com", "password", "Save User");
    User savedUser = userRepository.save(user);

    assertThat(savedUser).hasFieldOrPropertyWithValue("email", "save@example.com");
    assertThat(savedUser).hasFieldOrPropertyWithValue("name", "Save User");
  }

  @Test
  public void testFindUserById() {
    User user = new User("find@example.com", "password", "Find User");
    entityManager.persist(user);
    entityManager.flush();

    User found = userRepository.findById(user.getId()).orElse(null);
    assertThat(found).isNotNull();
    assertThat(found.getEmail()).isEqualTo(user.getEmail());
    assertThat(found.getName()).isEqualTo(user.getName());
  }

  @Test
  public void testUpdateUser() {
    // 1. 새 사용자를 생성하고 저장합니다.
    User user = new User("update@example.com", "password", "Update User");
    entityManager.persist(user);
    entityManager.flush();

    // 2. 저장된 사용자의 정보를 변경합니다.
    User savedUser = userRepository.findByEmail("update@example.com");
    savedUser.setName("Updated Name");
    userRepository.save(savedUser);

    // 3. 변경된 정보로 사용자를 다시 조회합니다.
    User updatedUser = userRepository.findByEmail("update@example.com");

    // 4. 변경된 정보가 정확히 저장되었는지 확인합니다.
    assertThat(updatedUser.getName()).isEqualTo("Updated Name");
  }

  @Test
  public void testDeleteUser() {
    // 1. 새 사용자를 생성하고 저장합니다.
    User user = new User("delete@example.com", "password", "Delete User");
    entityManager.persist(user);
    entityManager.flush();

    // 2. 저장된 사용자를 삭제합니다.
    User savedUser = userRepository.findByEmail("delete@example.com");
    userRepository.delete(savedUser);

    // 3. 삭제된 사용자를 조회합니다.
    User deletedUser = userRepository.findByEmail("delete@example.com");

    // 4. 삭제된 사용자가 더 이상 존재하지 않는지 확인합니다.
    assertThat(deletedUser).isNull();
  }

  @Test
  public void testExistsByEmail() {
    // 1. 새 사용자를 생성하고 저장합니다.
    User user = new User("exists@example.com", "password", "Exists User");
    entityManager.persist(user);
    entityManager.flush();

    // 2. 존재하는 이메일로 검사합니다.
    boolean existsUser = userRepository.existsByEmail("exists@example.com");

    // 3. 존재하지 않는 이메일로 검사합니다.
    boolean notExistsUser = userRepository.existsByEmail("notexists@example.com");

    // 4. 각각의 결과가 예상과 일치하는지 확인합니다.
    assertThat(existsUser).isTrue();
    assertThat(notExistsUser).isFalse();
  }

}