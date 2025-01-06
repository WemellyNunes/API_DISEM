package com.disem.API.services;

import com.disem.API.models.TeamModel;
import com.disem.API.repositories.TeamRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TeamService {

    @Autowired
    TeamRepository teamRepository;

    @Transactional
    public TeamModel save(TeamModel teamModel) {
        return teamRepository.save(teamModel);
    }

    public List<TeamModel> findAll() {
        return teamRepository.findAll();
    }

    @Transactional
    public Optional<TeamModel> findById(Long id) {
        return teamRepository.findById(id);
    }

    public void delete(TeamModel teamModel) {
        teamRepository.delete(teamModel);
    }

    @Transactional
    public List<TeamModel> saveAll(List<TeamModel> teamModels) {
        return teamRepository.saveAll(teamModels);
    }

}
