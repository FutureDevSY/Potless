package com.potless.backend.project.service;


import com.potless.backend.damage.entity.area.AreaEntity;
import com.potless.backend.damage.entity.enums.Status;
import com.potless.backend.damage.entity.road.DamageEntity;
import com.potless.backend.damage.repository.AreaRepository;
import com.potless.backend.damage.repository.DamageRepository;
import com.potless.backend.global.exception.member.ManagerNotFoundException;
import com.potless.backend.global.exception.pothole.PotholeNotFoundException;
import com.potless.backend.global.exception.project.AreaNotFoundException;
import com.potless.backend.global.exception.project.ProjectNotFoundException;
import com.potless.backend.global.exception.project.TeamNotFoundException;
import com.potless.backend.member.entity.ManagerEntity;
import com.potless.backend.member.entity.MemberEntity;
import com.potless.backend.member.entity.TeamEntity;
import com.potless.backend.member.entity.WorkerEntity;
import com.potless.backend.member.repository.manager.ManagerRepository;
import com.potless.backend.member.repository.team.TeamRepository;
import com.potless.backend.member.repository.worker.WorkerRepository;
import com.potless.backend.member.service.MemberService;
import com.potless.backend.project.dto.request.*;
import com.potless.backend.project.dto.response.ProjectDetailResponseDto;
import com.potless.backend.project.dto.response.ProjectListResponseDto;
import com.potless.backend.project.entity.ProjectEntity;
import com.potless.backend.project.entity.TaskEntity;
import com.potless.backend.project.repository.project.ProjectRepository;
import com.potless.backend.project.repository.task.TaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Log4j2
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProjectServiceImpl implements ProjectService {
    private final ProjectRepository projectRepository;
    private final ManagerRepository managerRepository;
    private final TeamRepository teamRepository;
    private final DamageRepository damageRepository;
    private final TaskRepository taskRepository;
    private final AreaRepository areaRepository;
    private final MemberService memberService;
    private final WorkerRepository workerRepository;

    @Override
    public Page<ProjectListResponseDto> getProjectAll(ProjectListRequestDto projectListRequestDto, Pageable pageable) {
        return projectRepository.findProjectAll(projectListRequestDto, pageable);
    }

    @Override
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
        return projectRepository.getProjectDetail(projectId);
    }

    @Override
    public void deleteProject(Long projectId) {
        ProjectEntity projectEntity = projectRepository.findById(projectId)
                .orElseThrow(ProjectNotFoundException::new);
        projectEntity.softDelet();
        projectRepository.save(projectEntity);
    }

    @Override
    public Long createTeam(Authentication authentication, CreateTeamRequestDto createTeamRequestDto) {
        MemberEntity member = memberService.findMember(authentication.getName());

        TeamEntity newTeam =
                TeamEntity.builder()
                          .managerEntity(ManagerEntity.builder()
                                                      .areaEntity(areaRepository.findById(createTeamRequestDto.getAreaId())
                                                                                .orElseThrow(AreaNotFoundException::new))
                                                      .memberEntity(member)
                                                      .build())
                          .teamName(createTeamRequestDto.getTeamName())
                          .build();

        teamRepository.save(newTeam);

        // 팀 worker 정보 생성, memberNameList 없는경우 고려하기!!
        List<WorkerEntity> workerEntityList = createTeamRequestDto
                .getMemberNameList()
                .stream()
                .map(memberName -> WorkerEntity.builder()
                                               .teamEntity(newTeam)
                                               .workerName(memberName).build())
                .toList();

        workerRepository.saveAll(workerEntityList);

        return newTeam.getId();
    }

    @Override
    public Long addWorker(Authentication authentication, WorkerRequestDto createTeamRequestDto) {

        TeamEntity team = teamRepository.findById(createTeamRequestDto.getTeamId())
                                        .orElseThrow(TeamNotFoundException::new);

        // 팀 worker 정보 생성
        List<WorkerEntity> workerEntityList = createTeamRequestDto
                .getMemberNameList()
                .stream()
                .map(memberName -> WorkerEntity.builder()
                                               .teamEntity(team)
                                               .workerName(memberName).build())
                .toList();

        workerRepository.saveAll(workerEntityList);

        return team.getId();
    }

    @Override
    public Long deleteWorker(Authentication authentication, WorkerRequestDto deleteTeamRequestDto) {

        TeamEntity team = teamRepository.findById(deleteTeamRequestDto.getTeamId())
                                        .orElseThrow(TeamNotFoundException::new);

        //리스트에 해당되는 사용자 이름이 없을경우 예외 처리 로직 추가 필요

        return workerRepository.deleteByNameAtOnce(deleteTeamRequestDto.getMemberNameList());

    }
}
