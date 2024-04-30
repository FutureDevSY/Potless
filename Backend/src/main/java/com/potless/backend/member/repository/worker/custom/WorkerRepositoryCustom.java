package com.potless.backend.member.repository.worker.custom;

import java.util.ArrayList;
import java.util.List;

public interface WorkerRepositoryCustom {
    public Long deleteByNameAtOnce(List<String> nameList);
}
