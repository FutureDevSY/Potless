package com.potless.backend.member.service;

import com.potless.backend.damage.entity.area.AreaEntity;
import com.potless.backend.damage.repository.AreaRepository;
import com.potless.backend.global.exception.member.InvalidCreateTeamAuthException;
import com.potless.backend.global.exception.member.MemberNotFoundException;
import com.potless.backend.global.exception.member.TeamNotFoundException;
import com.potless.backend.global.exception.project.AreaNotFoundException;
import com.potless.backend.global.exception.project.ProjectNotFoundException;
import com.potless.backend.member.dto.GetTeamResponseDto;
import com.potless.backend.member.dto.GetWorkerResponseDto;
import com.potless.backend.member.dto.TeamAddRequestDto;
import com.potless.backend.member.entity.ManagerEntity;
import com.potless.backend.member.entity.MemberEntity;
import com.potless.backend.member.entity.TeamEntity;
import com.potless.backend.member.entity.WorkerEntity;
import com.potless.backend.member.repository.manager.ManagerRepository;
import com.potless.backend.member.repository.member.MemberRepository;
import com.potless.backend.member.repository.team.TeamRepository;
import com.potless.backend.member.repository.worker.WorkerRepository;
import com.potless.backend.project.dto.request.CreateTeamRequestDto;
import com.potless.backend.project.dto.request.WorkerRequestDto;
import com.potless.backend.project.entity.ProjectEntity;
import com.potless.backend.project.repository.project.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Transactional
@RequiredArgsConstructor
public class TeamServiceImpl implements TeamService {

    private final ProjectRepository projectRepository;
    private final TeamRepository teamRepository;
    private final WorkerRepository workerRepository;
    private final MemberService memberService;
    private final AreaRepository areaRepository;
    private final MemberRepository memberRepository;
    private final ManagerRepository managerRepository;


    @Override
    public Long addTeamToProject(TeamAddRequestDto teamAddRequestDto) {
        ProjectEntity project = projectRepository.findById(teamAddRequestDto.getProjectId())
                .orElseThrow(ProjectNotFoundException::new);

        TeamEntity team = teamRepository.findById(teamAddRequestDto.getTeamId())
                .orElseThrow(TeamNotFoundException::new);

        project.setTeam(team);

        return project.getId();
    }

    @Override
    public Long createTeam(Authentication authentication, CreateTeamRequestDto createTeamRequestDto) {
        MemberEntity member = memberService.findMember(authentication.getName());
        AreaEntity area = areaRepository.findByAreaGu(createTeamRequestDto.getArea()).orElseThrow(AreaNotFoundException::new);
        ManagerEntity manager = managerRepository.findByMemberId(member.getId()).orElse(null);

        //매니저 아이디가 이미 있는데 매니저의 지역구과 생성하려는 팀의 지역구가 일치하지 않는경우
        if (manager != null) {
            if (!area.getId().equals(manager.getAreaEntity().getId())) {
                throw new InvalidCreateTeamAuthException();
            }
        } else {
            // 기존에 매니저 정보가 없는경우 새로 생성
            manager = ManagerEntity.builder()
                    .areaEntity(area)
                    .memberEntity(member)
                    .build();
        }

        TeamEntity newTeam =
                TeamEntity.builder()
                        .managerEntity(manager)
                        .teamName(createTeamRequestDto.getTeamName())
                        .build();

        teamRepository.save(newTeam);

        // 팀 worker 정보 생성
        // 팀 정보를 바꾸면 안되고 새로 생성해야함
        List<WorkerEntity> workerEntityList = createTeamRequestDto
                .getWorkerList()
                .stream()
                .flatMap(workerInfoDto -> {
                    MemberEntity workerMember = null;
                    WorkerEntity emptyTeamWorker = null;
                    Long workerMemberId = null;

                    if (workerInfoDto.getMemberId() != null) {
                        workerMemberId = workerInfoDto.getMemberId();
                        emptyTeamWorker = workerRepository.findByMemberIdAndAreaWhereTeamIsEmpty(workerMemberId, area.getId())
                                .orElse(null);
                    }

                    // 작업자의 memberId가 존재하는경우
                    if (workerMemberId != null) {
                        workerMember = memberRepository.findById(workerMemberId)
                                .orElseThrow(MemberNotFoundException::new);

                        // worker로 등록되어있지만 team이 할당되지 않은경우 수정, team 할당
                        if (emptyTeamWorker != null) {
                            emptyTeamWorker.changeTeam(newTeam);
                            return Stream.of(emptyTeamWorker);

                        }
                        // worker로 등록되어있지만 team이 할당되어 있는 경우 새로 생성
                        else {
                            WorkerEntity newWorker = WorkerEntity.builder()
                                    .teamEntity(newTeam)
                                    .workerName(workerInfoDto.getWorkerName())
                                    // null 혹은 memberId 참조
                                    .memberEntity(workerMember)
                                    .areaEntity(area)
                                    .build();
                            return Stream.of(newWorker);
                        }

                        // 작업자의 memberId값이 비어있는경우는 새로 생성
                    } else {
                        WorkerEntity newWorker = WorkerEntity.builder()
                                .teamEntity(newTeam)
                                .workerName(workerInfoDto.getWorkerName())
                                // null 혹은 memberId 참조
                                .memberEntity(workerMember)
                                .areaEntity(area)
                                .build();
                        return Stream.of(newWorker);
                    }

                }).collect(Collectors.toList());

        workerRepository.saveAll(workerEntityList);

        return newTeam.getId();
    }

    @Override
    public Long addWorker(Authentication authentication, WorkerRequestDto createTeamRequestDto) {

        TeamEntity team = teamRepository.findById(createTeamRequestDto.getTeamId())
                .orElseThrow(TeamNotFoundException::new);

        AreaEntity area = team.getManagerEntity().getAreaEntity();

        // 팀 worker 정보 생성
        List<WorkerEntity> workerEntityList = createTeamRequestDto
                .getWorkerList()
                .stream()
                .map(workerInfoDto -> WorkerEntity.builder()
                        .teamEntity(team)
                        .workerName(workerInfoDto.getWorkerName())
                        //memberId가 없는경우 가입된 작업자가 아님, null할당
                        .memberEntity(workerInfoDto.getMemberId() != null ? memberRepository.findById(workerInfoDto.getMemberId())
                                .orElseThrow(MemberNotFoundException::new) : null)
                        .areaEntity(area)
                        .build())

                .toList();

        workerRepository.saveAll(workerEntityList);

        return team.getId();
    }

    @Override
    public Long deleteWorker(Authentication authentication, WorkerRequestDto deleteTeamRequestDto) {

        TeamEntity team = teamRepository.findById(deleteTeamRequestDto.getTeamId())
                .orElseThrow(TeamNotFoundException::new);

        //리스트에 해당되는 사용자 이름이 없을경우 예외 처리 로직 추가 필요

        return workerRepository.deleteByNameAtOnce(deleteTeamRequestDto.getWorkerList());

    }

    @Override
    public List<GetTeamResponseDto> getTeam(String area) {
        Long areaId = teamRepository.getAreaIdByAreaGu(area).orElseThrow(AreaNotFoundException::new);
        List<GetTeamResponseDto> teamList = teamRepository.getTeamListByArea(areaId);

        for (GetTeamResponseDto dto : teamList) {
            dto.setWorkerList(teamRepository.getWorkerListByTeamId(dto.getTeamId()));
        }

        return teamList;
    }

    @Override
    public List<GetWorkerResponseDto> getWorker(String area) {
        AreaEntity areaEntity = areaRepository.findByAreaGu(area).orElseThrow(AreaNotFoundException::new);
        List<WorkerEntity> workerList = workerRepository.findAllByAreaId(areaEntity.getId());
        if (!workerList.isEmpty()) {
            return workerList.stream()
                    .map(workerEntity -> GetWorkerResponseDto.builder()
                            .memberId(Optional.ofNullable(workerEntity.getMemberEntity())
                                    .map(MemberEntity::getId)
                                    .orElse(null))
                            .workerName(workerEntity.getWorkerName())
                            .teamId(Optional.ofNullable(workerEntity.getTeamEntity())
                                    .map(TeamEntity::getId)
                                    .orElse(null))
                            .build())
                    .toList();

        } else return new ArrayList<GetWorkerResponseDto>();

    }

    @Override
    public Long deleteTeam(Long teamId) {

        // 해당 team에 해당되는 작업자의 team 필드를 null로 변경
        List<WorkerEntity> workerList = workerRepository.findAllByteamId(teamId);
        for (WorkerEntity worker : workerList) {
            worker.setTeamNull();
            // memberId가 존재하면서 다른 팀에 할당되어있는 worker의 경우 중복되지 않도록 삭제
            if (worker.getMemberEntity() != null
                    && workerRepository.checkByMemberIdWhereTeamIsExist(worker.getMemberEntity().getId())) {
                workerRepository.delete(worker);
            }
        }

        teamRepository.deleteById(teamId);
        duplicatedWorkerCheck();

        return teamId;
    }

    // 팀 삭제 후 중복된 memberId를 가진 팀이 없는 작업자가 생겼다면 하나만 남도록 확인
    public void duplicatedWorkerCheck() {
        List<Long> duplicatedIdList = workerRepository.findMemberIdsWithDuplicatesWhereTeamIsNull();

        if (duplicatedIdList != null || !duplicatedIdList.isEmpty()) {
            for (Long memberId : duplicatedIdList) {
                List<WorkerEntity> workers = workerRepository.findAllByMemberId(memberId);

                // 첫 번째 엔티티를 제외하고 삭제
                if (!workers.isEmpty()) {
                    workers.remove(0); // 첫 번째 엔티티는 유지
                    workerRepository.deleteAll(workers); // 나머지는 삭제
                }
            }
        }
    }

}
