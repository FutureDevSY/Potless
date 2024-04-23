package Potless.Backend.project.service;

import Potless.Backend.damage.entity.road.DamageEntity;
import Potless.Backend.damage.repository.DamageRepository;
import Potless.Backend.global.exception.project.ProjectNotFoundException;
import Potless.Backend.global.format.code.ApiResponse;
import Potless.Backend.member.entity.ManagerEntity;
import Potless.Backend.member.entity.TeamEntity;
import Potless.Backend.member.repository.manager.ManagerRepository;
import Potless.Backend.member.repository.team.TeamRepository;
import Potless.Backend.project.dto.request.ProjectSaveRequestDto;
import Potless.Backend.project.entity.ProjectEntity;
import Potless.Backend.project.entity.TaskEntity;
import Potless.Backend.project.repository.project.ProjectRepository;
import Potless.Backend.project.repository.task.TaskRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Log4j2
@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {
    private final ProjectRepository projectRepository;
    private final ManagerRepository managerRepository;
    private final TeamRepository teamRepository;
    private final DamageRepository damageRepository;
    private final TaskRepository taskRepository;

    @Override
    @Transactional
    public Long createProject(ProjectSaveRequestDto projectSaveRequestDto) {
        ManagerEntity managerEntity = managerRepository.findById(projectSaveRequestDto.getManagerId())
                .orElseThrow(ProjectNotFoundException::new);

        TeamEntity teamEntity = teamRepository.findById(projectSaveRequestDto.getTeamId())
                .orElseThrow(ProjectNotFoundException::new);

        ProjectEntity projectEntity = ProjectEntity.builder()
                .projectName(projectSaveRequestDto.getTitle())
                .managerEntity(managerEntity)
                .teamEntity(teamEntity)
                .projectDate(projectSaveRequestDto.getProjectDate())
                .projectSize(projectSaveRequestDto.getDamageNums().size())
                .build();

        ProjectEntity saveProjectEntity = projectRepository.save(projectEntity);

        projectSaveRequestDto.getDamageNums().forEach(damageId -> {
            DamageEntity damageEntity = damageRepository.findById(damageId)
                    .orElseThrow(ProjectNotFoundException::new);

            TaskEntity taskEntity = TaskEntity.builder().
            projectEntity(saveProjectEntity)
                    .damageEntity(damageEntity)
                    .build();

            taskRepository.save(taskEntity);
        });


        return saveProjectEntity.getId();
    }
}
