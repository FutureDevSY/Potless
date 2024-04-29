package com.potless.backend.project.service;


import com.potless.backend.damage.dto.controller.response.DamageResponseDTO;
import com.potless.backend.damage.entity.area.AreaEntity;
import com.potless.backend.damage.entity.enums.Status;
import com.potless.backend.damage.entity.road.DamageEntity;
import com.potless.backend.damage.repository.AreaRepository;
import com.potless.backend.damage.repository.DamageRepository;
import com.potless.backend.global.exception.member.ManagerNotFoundException;
import com.potless.backend.global.exception.pothole.PotholeNotFoundException;
import com.potless.backend.global.exception.project.AreaNotFoundException;
import com.potless.backend.global.exception.project.ProjectNotFoundException;
import com.potless.backend.member.entity.ManagerEntity;
import com.potless.backend.member.entity.TeamEntity;
import com.potless.backend.member.repository.manager.ManagerRepository;
import com.potless.backend.member.repository.member.MemberRepository;
import com.potless.backend.member.repository.team.TeamRepository;
import com.potless.backend.project.dto.request.ProjectListRequestDto;
import com.potless.backend.project.dto.request.ProjectSaveRequestDto;
import com.potless.backend.project.dto.request.TaskAddRequestDto;
import com.potless.backend.project.dto.response.ProjectDetailResponseDto;
import com.potless.backend.project.dto.response.ProjectListResponseDto;
import com.potless.backend.project.entity.ProjectEntity;
import com.potless.backend.project.entity.TaskEntity;
import com.potless.backend.project.repository.project.ProjectRepository;
import com.potless.backend.project.repository.task.TaskRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private final AreaRepository areaRepository;

    @Override
    public Page<ProjectListResponseDto> getProjectAll(ProjectListRequestDto projectListRequestDto, Pageable pageable) {
        return projectRepository.findProjectAll(projectListRequestDto, pageable);
    }

    @Override
    @Transactional
    public Long createProject(ProjectSaveRequestDto projectSaveRequestDto) {
        ManagerEntity managerEntity = managerRepository.findById(projectSaveRequestDto.getManagerId())
                .orElseThrow(ProjectNotFoundException::new);

        TeamEntity teamEntity = null;
        if(projectSaveRequestDto.getTeamId().isPresent()){
            teamEntity = teamRepository.findById(projectSaveRequestDto.getTeamId().get())
                    .orElseThrow(ManagerNotFoundException::new);
        }


        AreaEntity areaEntity = areaRepository.findById(projectSaveRequestDto.getAreaId())
                .orElseThrow(AreaNotFoundException::new);

        ProjectEntity projectEntity = ProjectEntity.builder()
                .projectName(projectSaveRequestDto.getTitle())
                .managerEntity(managerEntity)
                .teamEntity(teamEntity)
                .projectDate(projectSaveRequestDto.getProjectDate())
                .projectSize(projectSaveRequestDto.getDamageNums().size())
                .status(Status.작업전)
                .areaEntity(areaEntity)
                .build();

        ProjectEntity saveProjectEntity = projectRepository.save(projectEntity);

        if(projectSaveRequestDto.getDamageNums() != null && !projectSaveRequestDto.getDamageNums().isEmpty()){
            projectSaveRequestDto.getDamageNums().forEach(damageId -> {
                DamageEntity damageEntity = damageRepository.findById(damageId)
                        .orElseThrow(PotholeNotFoundException::new);

                TaskEntity taskEntity = TaskEntity.builder().
                        projectEntity(saveProjectEntity)
                        .damageEntity(damageEntity)
                        .build();
                taskRepository.save(taskEntity);
            });
        }

        return saveProjectEntity.getId();
    }

    @Override
    public ProjectDetailResponseDto getProjectDetail(Long projectId) {
        //프로젝트 정보
        ProjectEntity projectEntity = projectRepository.findById(projectId)
                .orElseThrow(ProjectNotFoundException::new);

        //매니저 정보
        String managerName = projectEntity.getManagerEntity().getMemberEntity().getMemberName();

        //damageId 정보추출
        List<Long> damageIds = taskRepository.findTasksByProjectId(projectId).stream()
                .map(task -> task.getDamageEntity().getId())
                .collect(Collectors.toList());

        //damage 정보
        List<DamageResponseDTO> damageResponseDTOS = damageRepository.findDamageDetailsByIds(damageIds);

        return ProjectDetailResponseDto.builder()
                .projectName(projectEntity.getProjectName())
                .managerName(managerName)
                .projectSize(projectEntity.getProjectSize())
                .damageResponseDTOS(damageResponseDTOS)
                .build();
    }

    @Override
    public void deleteProject(Long projectId) {
        ProjectEntity projectEntity = projectRepository.findById(projectId)
                .orElseThrow(ProjectNotFoundException::new);
        projectEntity.softDelet();
        projectRepository.save(projectEntity);
    }
}
