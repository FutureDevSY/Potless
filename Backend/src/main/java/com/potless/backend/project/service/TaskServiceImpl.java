package com.potless.backend.project.service;

import com.potless.backend.damage.entity.road.DamageEntity;
import com.potless.backend.damage.repository.DamageRepository;
import com.potless.backend.global.exception.pothole.PotholeNotFoundException;
import com.potless.backend.global.exception.project.ProjectNotFoundException;
import com.potless.backend.project.dto.request.TaskAddRequestDto;
import com.potless.backend.project.entity.ProjectEntity;
import com.potless.backend.project.entity.TaskEntity;
import com.potless.backend.project.repository.project.ProjectRepository;
import com.potless.backend.project.repository.project.custom.ProjectRepositoryCustomImpl;
import com.potless.backend.project.repository.task.TaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService{

    private ProjectRepository projectRepository;
    private DamageRepository damageRepository;
    private TaskRepository taskRepository;

    @Override
    public Long addTaskToProject(TaskAddRequestDto taskAddRequestDto) {
        ProjectEntity project = projectRepository.findById(taskAddRequestDto.getProjectId())
                .orElseThrow(ProjectNotFoundException::new);
        DamageEntity damage = damageRepository.findById(taskAddRequestDto.getDamageId())
                .orElseThrow(PotholeNotFoundException::new);

        TaskEntity task = TaskEntity.builder()
                .projectEntity(project)
                .damageEntity(damage)
                .build();

        taskRepository.save(task);
        return task.getId();
    }

}
