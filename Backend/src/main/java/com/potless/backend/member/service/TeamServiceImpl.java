package com.potless.backend.member.service;

import com.potless.backend.damage.repository.AreaRepository;
import com.potless.backend.global.exception.member.TeamNotFoundException;
import com.potless.backend.global.exception.project.AreaNotFoundException;
import com.potless.backend.global.exception.project.ProjectNotFoundException;
import com.potless.backend.member.dto.GetTeamResponseDto;
import com.potless.backend.member.dto.TeamAddRequestDto;
import com.potless.backend.member.entity.ManagerEntity;
import com.potless.backend.member.entity.MemberEntity;
import com.potless.backend.member.entity.TeamEntity;
import com.potless.backend.member.entity.WorkerEntity;
import com.potless.backend.member.repository.team.TeamRepository;
import com.potless.backend.member.repository.worker.WorkerRepository;
import com.potless.backend.project.dto.request.CreateTeamRequestDto;
import com.potless.backend.project.dto.request.WorkerRequestDto;
import com.potless.backend.project.entity.ProjectEntity;
import com.potless.backend.project.repository.project.ProjectRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Log4j2
@Service
@Transactional
@RequiredArgsConstructor
public class TeamServiceImpl implements TeamService{

    private final ProjectRepository projectRepository;
    private final TeamRepository teamRepository;
    private final WorkerRepository workerRepository;
    private final MemberService memberService;
    private final AreaRepository areaRepository;


    @Override
    public Long addTeamToProject(TeamAddRequestDto teamAddRequestDto) {
        ProjectEntity project = projectRepository.findById(teamAddRequestDto.getProjectId())
                .orElseThrow(ProjectNotFoundException::new);

        TeamEntity team = teamRepository.findById(teamAddRequestDto.getTeamId())
                .orElseThrow(TeamNotFoundException::new);

        ProjectEntity saveProject = project.builder()
                .teamEntity(team)
                .build();
        projectRepository.save(saveProject);
        return saveProject.getId();
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
                                        .orElseThrow(com.potless.backend.global.exception.project.TeamNotFoundException::new);

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
                                        .orElseThrow(com.potless.backend.global.exception.project.TeamNotFoundException::new);

        //리스트에 해당되는 사용자 이름이 없을경우 예외 처리 로직 추가 필요

        return workerRepository.deleteByNameAtOnce(deleteTeamRequestDto.getMemberNameList());

    }

    @Override
    public List<GetTeamResponseDto> getTeam(String area) {
        //예외처리 로직을 여기로 뺄까 고민중
        return teamRepository.getTeamListByArea(area);
    }
}
