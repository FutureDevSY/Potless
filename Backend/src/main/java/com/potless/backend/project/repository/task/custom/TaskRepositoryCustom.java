package com.potless.backend.project.repository.task.custom;


import com.potless.backend.project.entity.TaskEntity;

import java.util.List;

public interface TaskRepositoryCustom {
    List<TaskEntity> findTasksByProjectId(Long projectId);
}
