package com.potless.backend.project.service;


import com.potless.backend.damage.entity.area.AreaEntity;
import com.potless.backend.damage.entity.enums.Status;
import com.potless.backend.damage.entity.road.DamageEntity;
import com.potless.backend.damage.repository.AreaRepository;
import com.potless.backend.damage.repository.DamageRepository;
import com.potless.backend.global.exception.member.ManagerNotFoundException;
import com.potless.backend.global.exception.member.MemberNotFoundException;
import com.potless.backend.global.exception.pothole.PotholeNotFoundException;
import com.potless.backend.global.exception.project.AreaNotFoundException;
import com.potless.backend.global.exception.project.ProjectDeleteFailException;
import com.potless.backend.global.exception.project.ProjectNotFoundException;
import com.potless.backend.member.entity.ManagerEntity;
import com.potless.backend.member.entity.TeamEntity;
import com.potless.backend.member.repository.manager.ManagerRepository;
import com.potless.backend.member.repository.member.MemberRepository;
import com.potless.backend.member.repository.team.TeamRepository;
import com.potless.backend.project.dto.request.ProjectListRequestDto;
import com.potless.backend.project.dto.request.ProjectSaveRequestDto;
import com.potless.backend.project.dto.response.ProjectDetailResponseDto;
import com.potless.backend.project.dto.response.ProjectListResponseDto;
import com.potless.backend.project.entity.ProjectEntity;
import com.potless.backend.project.entity.TaskEntity;
import com.potless.backend.project.repository.project.ProjectRepository;
import com.potless.backend.project.repository.task.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ProjectServiceImpl implements ProjectService {
    private final ProjectRepository projectRepository;
    private final ManagerRepository managerRepository;
    private final TeamRepository teamRepository;
    private final DamageRepository damageRepository;
    private final TaskRepository taskRepository;
    private final AreaRepository areaRepository;
    private final MemberRepository memberRepository;

    @Override
    public Page<ProjectListResponseDto> getProjectAll(String email, ProjectListRequestDto projectListRequestDto, Pageable pageable) {
        return projectRepository.findProjectAll(projectListRequestDto, pageable);
    }

    @Override
    public Long createProject(String email, ProjectSaveRequestDto projectSaveRequestDto, int[] order) {
        Long memberId = memberRepository.searchByEmail(email)
                .orElseThrow(MemberNotFoundException::new).getId();
        ManagerEntity managerEntity = managerRepository.findByMemberId(memberId)
                .orElseThrow(ProjectNotFoundException::new);

        TeamEntity teamEntity = null;

        if (projectSaveRequestDto.getTeamId().isPresent()) {
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

        if (projectSaveRequestDto.getDamageNums() != null && !projectSaveRequestDto.getDamageNums().isEmpty()) {
            for (int i = 1; i < order.length - 1; i++) {
                long damageId = projectSaveRequestDto.getDamageNums().get(order[i] - 1);
                DamageEntity damageEntity = damageRepository.findById(damageId)
                        .orElseThrow(PotholeNotFoundException::new);
                damageEntity.changeStatus(Status.작업중);
                damageRepository.save(damageEntity);

                TaskEntity taskEntity = TaskEntity.builder()
                        .projectEntity(saveProjectEntity)
                        .damageEntity(damageEntity)
                        .taskOrder(i)
                        .build();
                taskRepository.save(taskEntity);
            }
        }

        return saveProjectEntity.getId();
    }

    @Override
    public ProjectDetailResponseDto getProjectDetail(Long projectId) {
        return projectRepository.getProjectDetail(projectId);
    }

    @Override
    public void deleteProject(Long projectId) {
        ProjectEntity projectEntity = projectRepository.findById(projectId)
                .orElseThrow(ProjectNotFoundException::new);

        List<TaskEntity> taskEntities = damageRepository.findTasksByProjectIdAndDamageStatus(projectId, Status.작업중);

        if (!taskEntities.isEmpty()) {
            throw new ProjectDeleteFailException();
        }

        projectEntity.softDelet();
        projectRepository.save(projectEntity);
    }

    @Override
    public void changeProjectStatus(Long projectId) {
        ProjectEntity projectEntity = projectRepository.findById(projectId)
                .orElseThrow(ProjectNotFoundException::new);

        List<TaskEntity> taskEntities = taskRepository.findTasksByProjectId(projectId);

        for (TaskEntity taskEntity : taskEntities) {
            DamageEntity damageEntity = taskEntity.getDamageEntity();
            if (damageEntity.getStatus() == Status.작업중) {
                damageEntity.changeStatus(Status.작업전);
                damageRepository.save(damageEntity);
                taskRepository.delete(taskEntity);
            }
        }

        projectEntity.changeStatus(Status.작업완료);
        projectRepository.save(projectEntity);
    }

}
