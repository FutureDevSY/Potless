package com.potless.backend.project.service;

import com.potless.backend.damage.entity.enums.Status;
import com.potless.backend.damage.entity.road.DamageEntity;
import com.potless.backend.damage.repository.DamageRepository;
import com.potless.backend.global.exception.member.MemberNotFoundException;
import com.potless.backend.global.exception.member.UnauthorizedRequestException;
import com.potless.backend.global.exception.pothole.PotholeNotFoundException;
import com.potless.backend.global.exception.project.ProjectNotFoundException;
import com.potless.backend.global.exception.task.TaskNotFoundException;
import com.potless.backend.member.entity.MemberEntity;
import com.potless.backend.member.entity.TeamEntity;
import com.potless.backend.member.repository.member.MemberRepository;
import com.potless.backend.member.repository.team.TeamRepository;
import com.potless.backend.project.dto.request.TaskAddRequestDto;
import com.potless.backend.project.dto.response.GetTaskResponseDto;
import com.potless.backend.project.entity.ProjectEntity;
import com.potless.backend.project.entity.TaskEntity;
import com.potless.backend.project.repository.project.ProjectRepository;
import com.potless.backend.project.repository.task.TaskRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final ProjectRepository projectRepository;
    private final DamageRepository damageRepository;
    private final TaskRepository taskRepository;
    private final MemberRepository memberRepository;
    private final TeamRepository teamRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public List<Long> addTaskToProject(TaskAddRequestDto taskAddRequestDto) {
        ProjectEntity project = projectRepository.findById(taskAddRequestDto.getProjectId())
                .orElseThrow(ProjectNotFoundException::new);

        List<Long> taskIds = new ArrayList<>();

        for (Long damageId : taskAddRequestDto.getDamageIds()) {
            DamageEntity damage = damageRepository.findById(damageId)
                    .orElseThrow(PotholeNotFoundException::new);

            damage.changeStatus(Status.작업중);

            TaskEntity task = TaskEntity.builder()
                    .projectEntity(project)
                    .damageEntity(damage)
                    .taskOrder(0)
                    .build();

            taskRepository.save(task);
            taskIds.add(task.getId());
        }

        project.setProjectSize(project.getProjectSize() + taskAddRequestDto.getDamageIds().size());
        projectRepository.save(project);

        return taskIds;
    }

    @Override
    @Transactional
    public Long deleteTask(Long taskId) {
        TaskEntity taskEntity = taskRepository.findById(taskId)
                .orElseThrow(TaskNotFoundException::new);
        ProjectEntity project = taskEntity.getProjectEntity();
        DamageEntity damage = taskEntity.getDamageEntity();

        damage.changeStatus(Status.작업전);
        damageRepository.save(damage);

        project.setProjectSize(project.getProjectSize() - 1);
        projectRepository.save(project);

        taskRepository.delete(taskEntity);

        return project.getId();
    }

    @Override
    public List<GetTaskResponseDto> getTaskList(Authentication authentication) {
        MemberEntity member = memberRepository.searchByEmail(authentication.getName())
                .orElseThrow(MemberNotFoundException::new);

        // 작업자 계정으로 접속한건지 검증
        if (member.getRole() != 1) throw new UnauthorizedRequestException();

        // 본인이 어떤 팀에 속해있는지 확인
        List<TeamEntity> teamList = teamRepository.findByMemberId(member.getId());
        // 속해있는 팀이 없을경우 빈값 반환
        if (teamList.isEmpty()) {
            return new ArrayList<GetTaskResponseDto>();

        } else {
            List<Long> teamIdList = teamList.stream()
                    .map(TeamEntity::getId)
                    .toList();

            // 해당 팀이 할당받은 프로젝트와 작업정보 조회
            return projectRepository.findProjectAndTaskByTeamId(teamIdList);
        }

    }

}
