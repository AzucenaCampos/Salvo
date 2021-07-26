package com.codeoftheweb.salvo;

import com.codeoftheweb.salvo.Classes.*;
import com.codeoftheweb.salvo.Repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;

@SpringBootApplication
public class SalvoApplication {
	@Autowired
	PasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(SalvoApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(PlayerRepository repoPlayer, GameRepository repoGame, GamePlayerRepository repoGamePlayer, ShipRepository repoShip, SalvoRepository repoSalvo, ScoreRepository repoScore) {
		return (args) -> {

			Game game1 = new Game();
			Game game2 = new Game();
			Game game3 = new Game();

			Date date1 = new Date();
			Date date2 = Date.from(date1.toInstant().plusSeconds(3600));
			Date date3 = Date.from(date1.toInstant().plusSeconds(7200));

			game1.setDate(date1);
			game2.setDate(date2);
			game3.setDate(date3);

			repoGame.save(game1);
			repoGame.save(game2);
			repoGame.save(game3);

			Player player1= new Player("j.bauer@ctu.gov", passwordEncoder.encode("24"));
			repoPlayer.save(player1);
			Player player2= new Player("c.obrian@ctu.gov", passwordEncoder.encode("42"));
			repoPlayer.save(player2);
			Player player3= new Player("kim_bauer@gmail.com", passwordEncoder.encode("kb"));
			repoPlayer.save(player3);
			Player player4= new Player("t.almeida@ctu.gov", passwordEncoder.encode("mole"));
			repoPlayer.save(player4);

			GamePlayer gamePlayer1 = new GamePlayer(new Date(),player1, game1);
			GamePlayer gamePlayer2 = new GamePlayer(new Date(),player2, game1);
			GamePlayer gamePlayer3 = new GamePlayer(new Date(),player3, game2);
			GamePlayer gamePlayer4 = new GamePlayer(new Date(),player4, game2);
			GamePlayer gamePlayer5 = new GamePlayer(new Date(),player1, game3);
			GamePlayer gamePlayer6 = new GamePlayer(new Date(),player3, game3);

			repoGamePlayer.save(gamePlayer1);
			repoGamePlayer.save(gamePlayer2);
			repoGamePlayer.save(gamePlayer3);
			repoGamePlayer.save(gamePlayer4);
			repoGamePlayer.save(gamePlayer5);
			repoGamePlayer.save(gamePlayer6);

			Ship ship1 = new Ship( "destroyer", List.of("H2", "H3", "H4"),gamePlayer1);
			Ship ship2 = new Ship( "submarine", List.of("E1", "F1", "G1"),gamePlayer1);
			Ship ship3 = new Ship( "patrolboat", List.of("B5", "B4"),gamePlayer1);
			Ship ship6 = new Ship( "carrier", List.of("J1", "J2", "J3", "J4", "J5"),gamePlayer1);
			Ship ship7 = new Ship("battleship", List.of("A5", "A6", "A7", "A8"),gamePlayer1);
			Ship ship4 = new Ship( "destroyer", List.of("B5", "C5", "D5"),gamePlayer2);
			Ship ship5 = new Ship( "patrolboat", List.of("F1", "F2"),gamePlayer2);
			Ship ship8 = new Ship( "submarine", List.of("J1", "J2", "J3"),gamePlayer2);
			Ship ship9 = new Ship("battleship", List.of("A1", "A2", "A3", "A4"),gamePlayer2);
			Ship ship10 = new Ship("carrier", List.of("G1", "G2", "G3", "G4", "G5"),gamePlayer2);

			repoShip.save(ship1);
			repoShip.save(ship2);
			repoShip.save(ship3);
			repoShip.save(ship4);
			repoShip.save(ship5);
			repoShip.save(ship6);
			repoShip.save(ship7);
			repoShip.save(ship8);
			repoShip.save(ship9);
			repoShip.save(ship10);

			Salvo salvo1 = new Salvo(1,List.of("H10","D7","A3","G8","E5"),gamePlayer1);
			//repoSalvo.save(salvo1);
			Salvo salvo2 = new Salvo(1,List.of("H1","F2","D3","G4","J9"),gamePlayer2);
			//repoSalvo.save(salvo2);
			Salvo salvo3 = new Salvo(2,List.of("J5","F4","A8","B4","C5"),gamePlayer1);
			//repoSalvo.save(salvo3);
			Salvo salvo4 = new Salvo(2,List.of("J10","D4","I5","B8","F9"),gamePlayer2);
			//repoSalvo.save(salvo4);

			Score score1 = new Score (player1,0.5, new Date(), game1);
			Score score2 = new Score (player2,0.5, new Date(), game1);
			Score score3 = new Score (player3,0, new Date(), game2);
			Score score4 = new Score (player4,1, new Date(), game2);
			Score score5 = new Score (player1,1, new Date(), game3);
			Score score6 = new Score (player3,0, new Date(), game3);

			repoScore.save(score1);
			repoScore.save(score2);
			repoScore.save(score3);
			repoScore.save(score4);
			repoScore.save(score5);
			repoScore.save(score6);
		};
	}

}
@Configuration
class WebSecurityConfiguration extends GlobalAuthenticationConfigurerAdapter {
	@Autowired
	PlayerRepository repoPlayer;
	@Bean
	public PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}
	@Override
	public void init(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(inputName-> {
			Player player = repoPlayer.findByEmail(inputName);
			if (player != null) {
				return new User(player.getEmail(), player.getPassword(), AuthorityUtils.createAuthorityList("USER"));
			} else {
				throw new UsernameNotFoundException("Unknown user: " + inputName);
			}
		});
	}
}

@EnableWebSecurity
@Configuration
class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	@Override
	protected void configure(HttpSecurity http) throws Exception {
    	http.authorizeRequests()
				.antMatchers("/web/**", "/h2-console/**", "/api/**").permitAll()
				/*.antMatchers(HttpMethod.POST, "/api/players").permitAll()
				.antMatchers(HttpMethod.POST,"/api/game_view/**").permitAll()
				.antMatchers(HttpMethod.POST,"/api/game/{gameID}/players").permitAll()*/
				.antMatchers("/**").hasAnyAuthority("USER")
				.antMatchers("/h2-console/**").permitAll().anyRequest().authenticated()
				.and().csrf().ignoringAntMatchers("/h2-console/**")
				.and().headers().frameOptions().sameOrigin();
		http.formLogin()
				.usernameParameter("name")
				.passwordParameter("pwd")
				.loginPage("/api/login");
		http.logout().logoutUrl("/api/logout");

		// turn off checking for CSRF tokens
		http.csrf().disable();

		// if user is not authenticated, just send an authentication failure response
		http.exceptionHandling().authenticationEntryPoint((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

		// if login is successful, just clear the flags asking for authentication
		http.formLogin().successHandler((req, res, auth) -> clearAuthenticationAttributes(req));

		// if login fails, just send an authentication failure response
		http.formLogin().failureHandler((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

		// if logout is successful, just send a success response
		http.logout().logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler());
	}

	private void clearAuthenticationAttributes(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if (session != null) {
			session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
		}
	}
}
