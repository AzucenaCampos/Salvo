
package com.codeoftheweb.salvo.Controller;

import com.codeoftheweb.salvo.Classes.*;
import com.codeoftheweb.salvo.Repositories.*;
import com.codeoftheweb.salvo.dtos.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class SalvoController {

    @Autowired
    private GameRepository repoGame;
    @Autowired
    private GamePlayerRepository repoGamePlayer;
    @Autowired
    private PlayerRepository repoPlayer;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ShipRepository repoShip;
    @Autowired
    private SalvoRepository repoSalvo;
    @Autowired
    private ScoreRepository scoreRepository;


    @RequestMapping(path = "/players", method = RequestMethod.POST)
    public ResponseEntity<Object> register(@RequestParam String email, @RequestParam String password) {
        if (email.isEmpty() || password.isEmpty()) {
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }

        if (repoPlayer.findByEmail(email) != null) {
            return new ResponseEntity<>("Name already in use", HttpStatus.FORBIDDEN);
        }

        repoPlayer.save(new Player(email, passwordEncoder.encode(password)));
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/games")
    public Map<String,Object> getAllGames(Authentication authentication){
        Map<String,Object> dto= new LinkedHashMap<String,Object>();
        if (!isGuest(authentication)== true){
            dto.put("player",new PlayerDTO(repoPlayer.findByEmail(authentication.getName())));
        }
        else{
           dto.put("player", "Guest");
        }
        dto.put("games",repoGame.findAll().stream().map(g -> new GameDTO(g)).collect(Collectors.toList()));
        return dto;
    }

    private boolean isGuest(Authentication authentication) {
        return authentication == null || authentication instanceof AnonymousAuthenticationToken;
    }

    @PostMapping(path = "/games")
    public ResponseEntity<Map<String, Object>> createGame(Authentication authentication) {
        if (isGuest(authentication)) {
            return new ResponseEntity<>(makeMap("Error", "No user"), HttpStatus.FORBIDDEN);
        }
        Player player = repoPlayer.findByEmail(authentication.getName());
        if (player == null) {
            return new ResponseEntity<>(makeMap("Error", "Player is not logged in"), HttpStatus.UNAUTHORIZED);
        }
        Game game = new Game(new Date());
        repoGame.save(game);
        GamePlayer gamePlayer = new GamePlayer(new Date(),player,game);
        repoGamePlayer.save(gamePlayer);

        return new ResponseEntity<>(makeMap("gpid", gamePlayer.getId()),HttpStatus.CREATED);
    }

    @PostMapping("/game/{gameId}/players")
    public ResponseEntity<Map<String, Object>> joinGame(@PathVariable Long gameId, Authentication authentication){
        Date joinDate = new Date();
        Game game = repoGame.findById(gameId).orElse(null);

        if(!isGuest(authentication)){
            if(game != null){
                if(game.getGamePlayers().size()==1){
                    GamePlayer gamePlayer = game.getGamePlayers().stream().findFirst().get();
                    Player player = repoPlayer.findByEmail(authentication.getName());
                    if(gamePlayer.getPlayer()!= player){
                        GamePlayer gamePlayer1 = new GamePlayer(new Date(),player,game);
                        repoGamePlayer.save(gamePlayer1);
                        return new ResponseEntity<>(makeMap("gpid", gamePlayer1.getId()),HttpStatus.CREATED);
                    }else{
                        return new ResponseEntity<>(makeMap("error","You can not rejoin a game"),HttpStatus.FORBIDDEN);
                    }
                } else{
                    return new ResponseEntity<>(makeMap("error", "The game is full, please join or create another one, Thanks"),HttpStatus.FORBIDDEN);
                }
            } else{
                return new ResponseEntity<>(makeMap("error","The game does not exists"), HttpStatus.NOT_FOUND);
            }
        }else{
            return new ResponseEntity<>(makeMap("error","You must login"),HttpStatus.UNAUTHORIZED);
        }
    }

    @RequestMapping("/game_view/{gamePlay_id}")
    public ResponseEntity<?> getGame(@PathVariable Long gamePlay_id, Authentication authentication){
        Optional<GamePlayer> gameP= repoGamePlayer.findById(gamePlay_id);

        Player player1= gameP.get().getPlayer();
        Player player = repoPlayer.findByEmail(authentication.getName());
        if(gameP.get().getPlayer().getId()!= player.getId()){
            return new ResponseEntity<>(makeMap("error", "unauthorized"), HttpStatus.UNAUTHORIZED);
        }else{
            MakeGameAuxDTO gameAuxDTO = new MakeGameAuxDTO( gameP.get());
            if(gameAuxDTO.getGameState()=="WON"){
                scoreRepository.save(new Score(gameP.get().getPlayer(), 1, new Date(), gameP.get().getGame()));
            }
            if(gameAuxDTO.getGameState()=="TIE"){
                scoreRepository.save(new Score(gameP.get().getPlayer(), 0.5, new Date(), gameP.get().getGame()));
            }
            if(gameAuxDTO.getGameState()=="LOST"){
                scoreRepository.save(new Score(gameP.get().getPlayer(), 0, new Date(), gameP.get().getGame()));
            }
            return new ResponseEntity<>(new MakeGameAuxDTO(gameP.get()), HttpStatus.CREATED);
        }
    }

    @PostMapping("/games/players/{gamePlay_id}/ships")
    public ResponseEntity<Map<String, Object>> PlaceShip(@PathVariable Long gamePlay_id, @RequestBody  List<Ship> ships, Authentication authentication){
        if (repoGamePlayer.findById(gamePlay_id).isPresent()){
            GamePlayer gameP = repoGamePlayer.findById(gamePlay_id).get();
            if(!isGuest(authentication)){
                Player player = repoPlayer.findByEmail(authentication.getName());
                if(gameP.getPlayer().getId() == player.getId()){
                    if(gameP.getShips().size()==0){
                        if(ships.size()==5){
                            ships.forEach(ship -> repoShip.save(new Ship(ship.getType(), ship.getShipLocations(),gameP)));
                            return new ResponseEntity<>(makeMap("OK","Ships successfully added"),HttpStatus.CREATED);
                        }
                        else if (ships.size()<5){
                            return new ResponseEntity<>(makeMap("error","you have to set 5 ships"),HttpStatus.FORBIDDEN);
                        }
                        return new ResponseEntity<>(makeMap("error","please,you set all the ships!"),HttpStatus.FORBIDDEN);
                        }
                    return new ResponseEntity<>(makeMap("error","you have 5 ships! "),HttpStatus.FORBIDDEN);
                    }
                return new ResponseEntity<>(makeMap("error","authentication error"),HttpStatus.UNAUTHORIZED);
                }
            return new ResponseEntity<>(makeMap("error","you have to login"),HttpStatus.UNAUTHORIZED);
            }
        return new ResponseEntity<>(makeMap("error","match not found"),HttpStatus.FORBIDDEN);
        }

    @PostMapping("/games/players/{gamePlay_id}/salvoes")
    public ResponseEntity<?> createSalvoes(@PathVariable Long gamePlay_id, @RequestBody Salvo salvo, Authentication authentication){
        GamePlayer gameP = repoGamePlayer.findById(gamePlay_id).orElse(null);
        Player player = repoPlayer.findByEmail(authentication.getName());

        if(isGuest(authentication)){
            return new ResponseEntity<>(makeMap("error","User not logged"),HttpStatus.UNAUTHORIZED);
        }
        if(gameP== null){
            return new ResponseEntity<>(makeMap("error", "No such GamePlayer"),HttpStatus.UNAUTHORIZED);
        }
        if(gameP.getPlayer().getId() != player.getId()){
            return new ResponseEntity<>(makeMap("error","authentication error"),HttpStatus.UNAUTHORIZED);
        }
        Optional<GamePlayer> opponent = gameP.getOpponentGameP();
        if (!opponent.isPresent()) {
            return new ResponseEntity<>(makeMap("error", "no opponent"),HttpStatus.FORBIDDEN);
        }
        if(gameP.getSalvoes().size()!=opponent.get().getSalvoes().size() && gameP.getSalvoes().size()>opponent.get().getSalvoes().size()){
            return new ResponseEntity<>(makeMap("error","Salvoes already submitted for turn "+ gameP.getSalvoes().size()),HttpStatus.FORBIDDEN);
        }
        if(salvo.getSalvoLocations().size()==0){
            return new ResponseEntity<>(makeMap("error","you have to fire at least 1 salvo"),HttpStatus.FORBIDDEN);
        }
        if(salvo.getSalvoLocations().size() > 5){
            return new ResponseEntity<>(makeMap("error","You have to submit a max of 5"),HttpStatus.FORBIDDEN);
        }
        if(gameP.getSalvoes().size()<= gameP.getOpponentGameP().get().getSalvoes().size()){
            repoSalvo.save(new Salvo(gameP.getSalvoes().size()+1,salvo.getSalvoLocations(),gameP));
            return new ResponseEntity<>(makeMap("OK","yours salvoes were fired"),HttpStatus.CREATED);
        }
        return new ResponseEntity<>(makeMap("error","match not found"),HttpStatus.FORBIDDEN);
    }

    private Map<String, Object> makeMap(String key, Object value) {
        Map<String, Object> map = new HashMap<>();
        map.put(key, value);
        return map;
    }
    /*private Map<String, Object>makeGameDTO(Game game){
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("id", game.getId());
        dto.put("created", game.getDate());
        dto.put("gamePlayers", game.getGamePlayers().stream().map(g -> makeGamePlayerDTO(g)).collect(Collectors.toList()));
        dto.put("scores", game.getGamePlayers().stream().map(g-> makeScoreDTO(g)).collect(Collectors.toList()));
        return dto;
    }
    private Map<String, Object>makeGameAuxDTO(Game game){
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("id", game.getId());
        dto.put("created", game.getDate());
        dto.put("gameState", "PLACESHIPS");
        dto.put("gamePlayers", game.getGamePlayers().stream().map(g -> makeGamePlayerDTO(g)).collect(Collectors.toList()));
        return dto;
    }
    private Map<String, Object>makeGamePlayerDTO(GamePlayer gamePlayer){
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("id", gamePlayer.getId());
        dto.put("player", makePlayerDTO(gamePlayer.getPlayer()));
        return dto;
    }
    private Map<String, Object>makePlayerDTO(Player player){
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("id", player.getId());
        dto.put("email", player.getEmail());
        return dto;
    }
    private Map<String, Object>makeShipDTO(Ship ship){
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("type", ship.getType());
        dto.put("locations", ship.getShipLocations());
        return dto;
    }
    private Map<String, Object>makeSalvoesDTO(Salvo salvo) {
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("type", salvo.getTurn());
        dto.put("locations", salvo.getSalvoLocation());
        dto.put("player", salvo.getGamePlayer().getId());
        return dto;
    }

    private Map<String, Object>makeScoreDTO(GamePlayer gamePlayer) {
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        if (gamePlayer.getScore().isPresent()) {
            dto.put("player", gamePlayer.getPlayer().getId());
            dto.put("score", gamePlayer.getScore().get().getScore());
            dto.put("finishDate", gamePlayer.getScore().get().getFinishDate());
            return dto;
        } else {
            dto.put("score", "el juego no tiene puntaje");
            return dto;
        }
    }
    private Map<String, Object>makeHitsDTO(){
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("self", new ArrayList<>());
        dto.put("opponent", new ArrayList<>());
        return dto;
    }*/
}