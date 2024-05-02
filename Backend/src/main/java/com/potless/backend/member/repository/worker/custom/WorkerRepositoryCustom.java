package com.potless.backend.member.repository.worker.custom;

import com.potless.backend.member.dto.WorkerInfoDto;
import com.potless.backend.member.entity.WorkerEntity;

import java.util.ArrayList;
import java.util.List;

public interface WorkerRepositoryCustom {
    public Long deleteByNameAtOnce(List<WorkerInfoDto> workerList);

    public List<WorkerEntity> findAllByAreaId(Long areaId);
}
