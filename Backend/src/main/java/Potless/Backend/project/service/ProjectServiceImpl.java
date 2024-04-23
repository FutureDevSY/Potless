package Potless.Backend.project.service;

import Potless.Backend.damage.entity.road.DamageEntity;
import Potless.Backend.damage.repository.DamageRepository;
import Potless.Backend.global.api.ApiResponse;
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
        ManagerEntity managerEntity = managerRepository.findById(projectSaveRequestDto.getManagerId()).orElseThrow(() -> {
            ApiResponse<Object> response = ApiResponse.of(HttpStatus.NOT_FOUND, "존재하지 않은 매니저: " + projectSaveRequestDto.getManagerId(), null);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, response.getMessage());
        });

        TeamEntity teamEntity = teamRepository.findById(projectSaveRequestDto.getTeamId()).orElseThrow(() -> {
            ApiResponse<Object> response = ApiResponse.of(HttpStatus.NOT_FOUND, "존재하지 않은 팀 : " + projectSaveRequestDto.getTeamId(), null);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, response.getMessage());
        });

        ProjectEntity projectEntity = ProjectEntity.builder()
                .projectName(projectSaveRequestDto.getTitle())
                .managerEntity(managerEntity)
                .teamEntity(teamEntity)
                .projectDate(projectSaveRequestDto.getProjectDate())
                .projectSize(projectSaveRequestDto.getDamageNums().size())
                .build();

        ProjectEntity saveProjectEntity = projectRepository.save(projectEntity);

        projectSaveRequestDto.getDamageNums().forEach(damageId -> {
            DamageEntity damageEntity = damageRepository.findById(damageId).orElseThrow(() -> {
                ApiResponse<Object> response = ApiResponse.of(HttpStatus.NOT_FOUND, "존재하지 않은 포트홀(군열) : " + damageId, null);
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, response.getMessage());
            });

            TaskEntity taskEntity = TaskEntity.builder().
            projectEntity(saveProjectEntity)
                    .damageEntity(damageEntity)
                    .build();

            taskRepository.save(taskEntity);
        });


        return saveProjectEntity.getId();
    }
}
