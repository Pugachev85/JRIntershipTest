package com.game.service;

import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import com.game.exception.BadRequestException;
import com.game.exception.NotFoundRequestException;
import com.game.repository.PlayersRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;


@Service
public class PlayerService {

    private final PlayersRepository playerRepo;

    public PlayerService(PlayersRepository playerRepo) {
        this.playerRepo = playerRepo;
    }

    public List<Player> getPlayers(String name, String title,
                                   Race race, Profession profession,
                                   Long after, Long before,
                                   Boolean banned, Integer minExperience,
                                   Integer maxExperience, Integer minLevel,
                                   Integer maxLevel, Pageable pageable) {

        Date dateAfter = after != null ? new Date(after) : null;
        Date dateBefore = before != null ? new Date(before) : null;

        return playerRepo.findAll(name, title,
                race, profession,
                dateAfter, dateBefore,
                banned, minExperience,
                maxExperience, minLevel,
                maxLevel, pageable);

    }

    public Player addPlayer(Player player) {
        if (!isValidPlayer(player)) throw new BadRequestException("Data params not valid");

        if (player.getId() != null)
            player.setId(null);

        if (player.getBanned() == null)
            player.setBanned(false);

        calculateAndUpdateLevelAndUntilNextLevel(player);

        return playerRepo.save(player);
    }

    public Integer getCountOfPlayers(String name, String title,
                                     Race race, Profession profession,
                                     Long after, Long before,
                                     Boolean banned, Integer minExperience,
                                     Integer maxExperience, Integer minLevel,
                                     Integer maxLevel) {
        Date dateAfter = after != null ? new Date(after) : null;
        Date dateBefore = before != null ? new Date(before) : null;
        return playerRepo.countByParams(name, title,
                                        race, profession,
                                        dateAfter, dateBefore,
                                        banned, minExperience,
                                        maxExperience, minLevel,
                                        maxLevel);
    }

    public Player getPlayerById(Long id) {
        if (id <= 0) throw new BadRequestException("ID is not valid");

        Optional<Player> player = playerRepo.findById(id);

        if (!player.isPresent()) throw new NotFoundRequestException("Player not found to database");

        return player.get();
    }

    public Player updatePlayer(Long id, Player player) {
        Player playerEntity = getPlayerById(id);
        if (isEmptyPlayer(player)) return playerEntity;
        if (player.getName() != null) playerEntity.setName(player.getName());
        if (player.getTitle() != null) playerEntity.setTitle(player.getTitle());
        if (player.getRace() != null) playerEntity.setRace(player.getRace());
        if (player.getProfession() != null) playerEntity.setProfession(player.getProfession());
        if (player.getBirthday() != null) {
            if (!isValidDate(player.getBirthday())) throw new BadRequestException("Birthday is not valid");
            playerEntity.setBirthday(player.getBirthday());
        }
        if (player.getBanned() != null) playerEntity.setBanned(player.getBanned());
        if (player.getExperience() != 0) {
            if (!isValidExperience(player.getExperience())) throw new BadRequestException("Experience is not valid");
            playerEntity.setExperience(player.getExperience());
            calculateAndUpdateLevelAndUntilNextLevel(playerEntity);
        }

        return playerRepo.save(playerEntity);
    }

    private boolean isEmptyPlayer(Player player) {
        return player.getId() ==null &&
                player.getName() == null &&
                player.getTitle() == null &&
                player.getRace() == null &&
                player.getProfession() == null &&
                player.getExperience() == 0 &&
                player.getLevel() == 0 &&
                player.getBirthday() == null;
    }

    public void deletePlayer(Long id) {
        if (id <= 0) throw new BadRequestException("ID is not valid");

        Player playerEntity = getPlayerById(id);
        playerRepo.delete(playerEntity);
    }
    private boolean isValidPlayer(Player player) {
        return player != null &&
                player.getName() != null &&
                player.getTitle() != null &&
                player.getRace() != null &&
                player.getProfession() != null &&
                player.getBirthday() != null &&
                isValidExperience(player.getExperience()) &&
                !player.getName().trim().isEmpty() &&
                player.getName().length() <= 12 &&
                player.getTitle().length() <= 30;
    }

    private boolean isValidExperience(Integer experience) {
        return experience > 0 && experience <= 10000000;
    }

    private boolean isValidDate(Date date) {
        return date.getTime() >= 0;
    }


    private void calculateAndUpdateLevelAndUntilNextLevel(Player player) {
        int level = (int) (Math.sqrt(2500 + (200 * player.getExperience())) - 50) / 100;
        int untilNextLevel = 50 * (level + 1) * (level + 2) - player.getExperience();
        player.setLevel(level);
        player.setUntilNextLevel(untilNextLevel);
    }

}
