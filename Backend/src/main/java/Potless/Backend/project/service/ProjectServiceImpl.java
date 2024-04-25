package Potless.Backend.project.service;

import Potless.Backend.damage.dto.controller.response.DamageResponseDTO;
import Potless.Backend.damage.entity.road.DamageEntity;
import Potless.Backend.damage.repository.DamageRepository;
import Potless.Backend.global.exception.member.ManagerNotFoundException;
import Potless.Backend.global.exception.pothole.PotholeAreaNotFoundException;
import Potless.Backend.global.exception.pothole.PotholeNotFoundException;
import Potless.Backend.global.exception.project.ProjectNotFoundException;
import Potless.Backend.global.exception.member.TeamNotFoundException;
import Potless.Backend.member.entity.ManagerEntity;
import Potless.Backend.member.entity.TeamEntity;
import Potless.Backend.member.repository.manager.ManagerRepository;
import Potless.Backend.member.repository.member.MemberRepository;
import Potless.Backend.member.repository.team.TeamRepository;
import Potless.Backend.project.dto.request.ProjectListRequestDto;
import Potless.Backend.project.dto.request.ProjectSaveRequestDto;
import Potless.Backend.project.dto.response.ProjectDetailResponseDto;
import Potless.Backend.project.dto.response.ProjectListResponseDto;
import Potless.Backend.project.entity.ProjectEntity;
import Potless.Backend.project.entity.TaskEntity;
import Potless.Backend.project.repository.project.ProjectRepository;
import Potless.Backend.project.repository.task.TaskRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {
    private final ProjectRepository projectRepository;
    private final ManagerRepository managerRepository;
    private final TeamRepository teamRepository;
    private final DamageRepository damageRepository;
    private final TaskRepository taskRepository;
    private final MemberRepository memberRepository;

    @Override
    public Page<ProjectListResponseDto> getProjectAll(ProjectListRequestDto projectListRequestDto, Pageable pageable) {
        return projectRepository.findProjectAllByManagerId(projectListRequestDto, pageable);
    }

    @Override
    @Transactional
    public Long createProject(ProjectSaveRequestDto projectSaveRequestDto) {
        ManagerEntity managerEntity = managerRepository.findById(projectSaveRequestDto.getManagerId())
                .orElseThrow(ProjectNotFoundException::new);

        TeamEntity teamEntity = teamRepository.findById(projectSaveRequestDto.getTeamId())
                .orElseThrow(ManagerNotFoundException::new);

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
                    .orElseThrow(PotholeNotFoundException::new);

            TaskEntity taskEntity = TaskEntity.builder().
            projectEntity(saveProjectEntity)
                    .damageEntity(damageEntity)
                    .build();

            taskRepository.save(taskEntity);
        });
        return saveProjectEntity.getId();
    }

    @Override
    public ProjectDetailResponseDto getProjectDetail(Long projectId) {
        //프로젝트 정보
        ProjectEntity projectEntity = projectRepository.findById(projectId)
                .orElseThrow(ProjectNotFoundException::new);

        //매니저 정보
        ManagerEntity managerEntity = managerRepository.findById(projectEntity.getManagerEntity().getId())
                .orElseThrow(ManagerNotFoundException::new);

        //작업 정보
        List<TaskEntity> taskEntities = taskRepository.findTasksByProjectId(projectId);

        //damageId 정보
        List<Long> damageIds = taskEntities.stream()
                .map(task -> task.getDamageEntity().getId())
                        .collect(Collectors.toList());

        //damage 정보
        List<DamageResponseDTO> damageResponseDTOS  = damageRepository.findDamageDetailsByIds(damageIds);

        ProjectDetailResponseDto projectDetailResponseDto = ProjectDetailResponseDto.builder()
                .projectName(projectEntity.getProjectName())
                .managerName(managerEntity.getMemberEntity().getMemberName())
                .projectSize(projectEntity.getProjectSize())
                .damageResponseDTOS(damageResponseDTOS)
                .build();

        return projectDetailResponseDto;
    }

}
