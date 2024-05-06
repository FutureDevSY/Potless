package com.potless.backend.member.repository.worker.custom;

import com.potless.backend.member.dto.WorkerInfoDto;
import com.potless.backend.member.entity.MemberEntity;
import com.potless.backend.member.entity.WorkerEntity;
import org.springframework.data.jpa.repository.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface WorkerRepositoryCustom {
    public Long deleteByNameAtOnce(List<WorkerInfoDto> workerList);

    public List<WorkerEntity> findAllByAreaId(Long areaId);

    public void resetTeamFromWorkers(Long teamId);

    public List<WorkerEntity> findAllByteamId(Long teamId);

    public List<WorkerEntity> findAllByMemberId(Long memberId);

    public Optional<WorkerEntity> findByMemberIdAndAreaWhereTeamIsEmpty(Long memberId, Long areaId);

    List<Long> findMemberIdsWithDuplicatesWhereTeamIsNull();

    boolean checkByMemberIdWhereTeamIsExist(Long memberId);
}
