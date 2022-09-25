package com.game.controller;

import com.game.entity.*;
import com.game.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rest")
public class PlayersController {

    @Autowired
    private PlayerService playerService;


    @GetMapping("/players")
    public ResponseEntity<List<Player>> getPlayers(@RequestParam(required = false) String name,
                                                   @RequestParam(required = false) String title,
                                                   @RequestParam(required = false) Race race,
                                                   @RequestParam(required = false) Profession profession,
                                                   @RequestParam(required = false) Long after,
                                                   @RequestParam(required = false) Long before,
                                                   @RequestParam(required = false) Boolean banned,
                                                   @RequestParam(required = false) Integer minExperience,
                                                   @RequestParam(required = false) Integer maxExperience,
                                                   @RequestParam(required = false) Integer minLevel,
                                                   @RequestParam(required = false) Integer maxLevel,
                                                   @RequestParam(value = "order", required = false, defaultValue = "ID") PlayerOrder order,
                                                   @RequestParam(value = "pageNumber", required = false, defaultValue = "0") Integer pageNumber,
                                                   @RequestParam(value = "pageSize", required = false, defaultValue = "3") Integer pageSize){
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.Direction.ASC, order.getFieldName());
        List<Player> list =  playerService.getPlayers(name, title, race, profession,  after,
                                                    before, banned, minExperience, maxExperience,
                                                    minLevel, maxLevel, pageable);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/players/count")
    public Integer getPlayersCount(@RequestParam(required = false) String name,
                                   @RequestParam(required = false) String title,
                                   @RequestParam(required = false) Race race,
                                   @RequestParam(required = false) Profession profession,
                                   @RequestParam(required = false) Long after,
                                   @RequestParam(required = false) Long before,
                                   @RequestParam(required = false) Boolean banned,
                                   @RequestParam(required = false) Integer minExperience,
                                   @RequestParam(required = false) Integer maxExperience,
                                   @RequestParam(required = false) Integer minLevel,
                                   @RequestParam(required = false) Integer maxLevel) {
        return playerService.getCountOfPlayers(name, title, race, profession,  after,
                before, banned, minExperience, maxExperience,
                minLevel, maxLevel);
    }

    @PostMapping("/players")
    public Player registration(@RequestBody Player player) {
        return playerService.addPlayer(player);
    }

    @GetMapping("/players/{id}")
    public Player getPlayerById(@PathVariable Long id) {
        return playerService.getPlayerById(id);
    }

    @PostMapping("/players/{id}")
    public ResponseEntity<Player> updatePlayerById(@PathVariable Long id,
                                                   @RequestBody Player player) {
        Player updated = playerService.updatePlayer(id, player);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @DeleteMapping("/players/{id}")
    public ResponseEntity<Object> deletePlayer(@PathVariable Long id) {
        playerService.deletePlayer(id);
        return ResponseEntity.ok().build();
    }
}
