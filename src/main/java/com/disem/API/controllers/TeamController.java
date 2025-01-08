package com.disem.API.controllers;

import com.disem.API.dtos.TeamDTO;
import com.disem.API.models.TeamModel;
import com.disem.API.services.TeamService;
import jakarta.validation.Valid;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
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

    @PostMapping("/teams/upload")
    public ResponseEntity<Object> uploadTeam(@RequestBody List<@Valid TeamDTO> teamDTOList) {
        try {
            // Converte os DTOs para entidades
            List<TeamModel> teamModels = teamDTOList.stream().map(dto -> {
                var teamModel = new TeamModel();
                BeanUtils.copyProperties(dto, teamModel);
                return teamModel;
            }).toList();

            // Salva no banco de dados
            var savedTeams = teamService.saveAll(teamModels);
            return new ResponseEntity<>(savedTeams, HttpStatus.CREATED);

        } catch (Exception e) {
            return new ResponseEntity<>("Erro ao salvar os profissionais: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
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

    @DeleteMapping("team/{id}")
    public ResponseEntity<Object> deleteTeam(@PathVariable(value = "id") Long id) {
        Optional<TeamModel> teamModel = teamService.findById(id);
        if (teamModel.isEmpty()){
            return new ResponseEntity<>("Profssional não encontrado", HttpStatus.NOT_FOUND);
        }
        teamService.delete(teamModel.get());
        return new ResponseEntity<>("Profissional deletado com sucesso", HttpStatus.OK);
    }

    @PutMapping("team/{id}")
    public ResponseEntity<Object> updateTeam(@PathVariable(value = "id") Long id, @RequestBody TeamDTO teamDTO) {
        Optional<TeamModel> teamModelOptional = teamService.findById(id);

        if (teamModelOptional.isEmpty()) {
            return new ResponseEntity<>("Profissional não encontrado", HttpStatus.NOT_FOUND);
        }

        var teamModel = teamModelOptional.get();
        teamModel.setName(teamDTO.getName());
        teamModel.setRole(teamDTO.getRole());
        teamModel.setStatus(teamDTO.getStatus());

        return new ResponseEntity<>(teamService.save(teamModel), HttpStatus.OK);
    }


}
