package com.potless.backend.project.service;

import com.potless.backend.damage.entity.road.DamageEntity;
import com.potless.backend.damage.repository.DamageRepository;
import com.potless.backend.global.exception.pothole.PotholeNotFoundException;
import com.potless.backend.global.exception.project.ProjectNotFoundException;
import com.potless.backend.global.exception.task.TaskNotFoundException;
import com.potless.backend.project.dto.request.TaskAddRequestDto;
import com.potless.backend.project.entity.ProjectEntity;
import com.potless.backend.project.entity.TaskEntity;
import com.potless.backend.project.repository.project.ProjectRepository;
import com.potless.backend.project.repository.project.custom.ProjectRepositoryCustomImpl;
import com.potless.backend.project.repository.task.TaskRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Log4j2
@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final ProjectRepository projectRepository;
    private final DamageRepository damageRepository;
    private final TaskRepository taskRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public List<Long> addTaskToProject(TaskAddRequestDto taskAddRequestDto) {
        ProjectEntity project = projectRepository.findById(taskAddRequestDto.getProjectId())
                .orElseThrow(ProjectNotFoundException::new);
        log.info("project.getId() = {}", project.getId());

        List<Long> taskIds = new ArrayList<>();

        for (Long damageId : taskAddRequestDto.getDamageIds()) {
            DamageEntity damage = damageRepository.findById(damageId)
                    .orElseThrow(PotholeNotFoundException::new);

            TaskEntity task = TaskEntity.builder()
                    .projectEntity(project)
                    .damageEntity(damage)
                    .build();

            taskRepository.save(task);
            taskIds.add(task.getId());
        }

        return taskIds;
    }

    @Override
    public void deleteTask(Long taskId) {
        TaskEntity taskEntity = taskRepository.findById(taskId)
                .orElseThrow(TaskNotFoundException::new);
        taskEntity.softDelet();
        taskRepository.save(taskEntity);
    }

}
