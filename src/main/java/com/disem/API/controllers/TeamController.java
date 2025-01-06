package com.disem.API.controllers;

import com.disem.API.dtos.TeamDTO;
import com.disem.API.models.TeamModel;
import com.disem.API.services.TeamService;
import jakarta.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class TeamController {

    @Autowired
    TeamService teamService;

    @PostMapping("/teams")
    public ResponseEntity<Object> saveTeams(@RequestBody @Valid TeamDTO teamDTO) {
        var teamModel = new TeamModel();
        BeanUtils.copyProperties(teamDTO, teamModel);

        var savedTeam = teamService.save(teamModel);
        return new ResponseEntity<>(savedTeam, HttpStatus.CREATED);

    }

    @PostMapping("teams/upload")
    public ResponseEntity<Object> uploadTeam(@RequestBody List<@Valid TeamDTO> teamDTOList) {
        List<TeamModel> teamModels = teamDTOList.stream().map(dto -> {
            var teamModel = new TeamModel();
            BeanUtils.copyProperties(dto, teamModel);
            return teamModel;
        }).toList();

        var savedTeams = teamService.saveAll(teamModels);
        return new ResponseEntity<>(savedTeams, HttpStatus.CREATED);
    }

    @GetMapping("teams")
    public ResponseEntity<Object> getAllTeams() {
        List<TeamModel> teamModels = teamService.findAll();
        if (teamModels.isEmpty()) {
            return new ResponseEntity<>("Profissionais não encontrados", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(teamModels, HttpStatus.OK);
    }

    @GetMapping("team/{id}")
    public ResponseEntity<Object> getTeamById(@PathVariable(value = "id") Long id) {
        Optional<TeamModel> teamModel = teamService.findById(id);
        if (teamModel.isEmpty()){
            return new ResponseEntity<>("Profissional não encontrado", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(teamModel.get(), HttpStatus.OK);
    }

    @DeleteMapping("item/{id}")
    public ResponseEntity<Object> deleteTeam(@PathVariable(value = "id") Long id) {
        Optional<TeamModel> teamModel = teamService.findById(id);
        if (teamModel.isEmpty()){
            return new ResponseEntity<>("Profssional não encontrado", HttpStatus.NOT_FOUND);
        }
        teamService.delete(teamModel.get());
        return new ResponseEntity<>("Profissional deletado com sucesso", HttpStatus.OK);
    }

}
