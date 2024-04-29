package com.potless.backend.member.service;

import com.potless.backend.global.exception.member.TeamNotFoundException;
import com.potless.backend.global.exception.project.ProjectNotFoundException;
import com.potless.backend.member.dto.TeamAddRequestDto;
import com.potless.backend.member.entity.TeamEntity;
import com.potless.backend.member.repository.team.TeamRepository;
import com.potless.backend.project.entity.ProjectEntity;
import com.potless.backend.project.repository.project.ProjectRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@RequiredArgsConstructor
public class TeamServiceImpl implements TeamService{

    private ProjectRepository projectRepository;
    private TeamRepository teamRepository;


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
}
