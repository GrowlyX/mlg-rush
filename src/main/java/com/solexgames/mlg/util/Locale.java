package com.solexgames.mlg.util;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import com.solexgames.mlg.CorePlugin;
import lombok.Getter;
import org.bukkit.ChatColor;

/**
 * From Zoot by Joelioli
 */

@Getter
public enum Locale {

	//  Arena
	STARTING_ARENA_BOT("arena.starting-bot", CC.SECONDARY + "Starting " + CC.PRIMARY + "ArenaBot v1.0" + CC.SECONDARY + "..."),
	GAME_SHUTDOWN("arena.game-shutdown", CC.RED + "This game has been shut down due to it being deleted by an administrator."),
	ARENA_DELETED("arena.arena-deleted", CC.SECONDARY + "You''ve just deleted the arena with the name " + CC.PRIMARY + "{0}" + CC.SECONDARY + "."),
	ALREADY_IN_ARENA("arena.already-in-arena", CC.RED + "Error: You are already in an arena."),
	CURRENTLY_NOT_IN_ARENA("arena.currently-not-in-arena", CC.RED + "You aren''t currently in an arena."),
	PLAYER_JOIN_ARENA("arena.player-join", CC.PRIMARY + "{0}" + CC.SECONDARY + " has joined the arena. " + CC.GRAY + "({1}/{2})"),
	PLAYER_LEAVE_ARENA("arena.player-leave", CC.PRIMARY + "{0}" + CC.SECONDARY + " has left the arena. " + CC.GRAY + "({1}/{2})"),
	NOT_ENOUGH_PLAYERS("arena.not-enough-players", CC.RED + "The game starting state has been cancelled as there aren''t enough players to start the game!"),
	ARENA_REGENERATING("arena.regenerating", CC.RED + "You cannot join this arena at the moment."),
	ARENA_MAX_PLAYERS("arena.max-players", CC.RED + "This arena is currently at max capacity!"),

	//  Game
	FORCE_ENDED("game.force-ended", CC.RED + "This game has been force ended by ArenaBot as you''ve been playing for more than fifteen minutes."),
	BLOCK_PLACE_DENY("game.block-place-deny", CC.RED + "You cannot place blocks here."),
	PLAYER_KILLED_BY("game.player-killed-by", CC.RED + "{0}" + CC.SECONDARY + " was killed by " + CC.GREEN + "{1}" + CC.SECONDARY + "!"),
	PLAYER_DIED("game.player-died", CC.RED + "{0}" + CC.SECONDARY + " died."),
	PLAYER_POINT_SCORED("game.player-point-scored", CC.RED + "{0}" + CC.SECONDARY + " has scored a point! " + CC.GRAY + "(" + CC.BLUE + "{1}" + CC.GRAY + "/" + CC.RED + "{2}" + CC.GRAY + ")"),
	STARTED_SPECTATING("game.started-spectating", CC.PRIMARY + "{0}" + CC.SECONDARY + " has started spectating the match."),
	STOPPED_SPECTATING("game.stopped-spectating", CC.PRIMARY + "{0}" + CC.SECONDARY + " has stopped spectating the match."),
	GAME_COUNTDOWN("game.countdown", CC.SECONDARY + "The game will be starting in " + CC.PRIMARY + "{0} " + CC.SECONDARY + "."),
	GAME_COUNTDOWN_END("game.countdown-end", CC.SECONDARY + "The game has started!"),
	ROUND_COUNTDOWN("game.round-countdown", CC.SECONDARY + "The round will be starting in " + CC.PRIMARY + "{0} " + CC.SECONDARY + "."),
	ROUND_COUNTDOWN_END("game.round-countdown-end", CC.SECONDARY + "The round has started!"),
	PLAYER_WON("game.player-won", CC.PRIMARY + "{0} " + CC.SECONDARY + "has won the game!"),
	TEAM_WON("game.team-won", CC.SECONDARY + "Team {0} " + CC.SECONDARY + " has won the game!"),
	WINNER_TITLE("game.winner-title", CC.GOLD + CC.BOLD + "VICTORY"),
	WINNER_SUBTITLE("game.winner-subtitle", CC.GRAY + "You''ve won the game!"),
	LOSER_TITLE("game.loser-title", CC.RED + CC.BOLD + "DEFEAT"),
	LOSER_SUBTITLE("game.loser-subtitle", CC.GRAY + "You''ve lost the game!"),

	//  Player
	PLAYER_NOT_IN_MATCH("player.not-in-match", CC.RED + "Error: That player is not in a match."),
	MATCH_ERROR("player.match-error", CC.RED + "Error: That match has not started yet or the arena bound to it is regenerating."),
	STATS_RESET("player.stats-reset", CC.SECONDARY + "Statistics have been reset for: {0}"),

	//  Duel
	UUID_INVALID("duel.uuid-invalid", CC.RED + "The UUID " + CC.SECONDARY + "{0}" + CC.RED + " is not valid."),
	CANT_ACCEPT_REQUEST("duel.cant-accept-request", CC.RED + "You''re currently in-game so you cannot accept this duel request."),
	REQUEST_DOESNT_EXIST("duel.request-doesnt-exist", CC.RED + "The duel request with the uuid " + CC.SECONDARY + "{0}" + CC.RED + " does not exist."),
	ARENA_CURRENTLY_BUSY("duel.arena-busy", CC.RED + "The arena with the name " + CC.SECONDARY + "{0}" + CC.RED + " is currently busy."),
	REQUEST_PLAYER_OFFLINE("duel.request-player-offline", CC.RED + "The player who sent you that duel request is currently offline."),
	REQUEST_PLAYER_INGAME("duel.request-player-ingame", CC.RED + "The player who sent you that duel request is currently in-game."),
	REQUEST_SENT("duel.request-sent", CC.SECONDARY + "You''ve sent out a duel request to {0}" + CC.SECONDARY + " on the map " + CC.PRIMARY + "{1}" + CC.SECONDARY + "!"),
	REQUEST_TO_EXPIRED("duel.request-to-expired", CC.SECONDARY + "Your duel request to {0}" + CC.SECONDARY + " has expired."),
	REQUEST_FROM_EXPIRED("duel.request-from-expired", CC.SECONDARY + "The duel request from {0}" + CC.SECONDARY + " has expired."),
	DUEL_ACCEPTED("duel.accepted", CC.SECONDARY + "You''ve accepted {0}" + CC.SECONDARY + "''s duel request!"),
	DUEL_ACCEPTED_ISSUER("duel.accepted-issuer", "{0}" + CC.SECONDARY + " has accepted your duel request!"),
	KICKED_FROM_ARENA("duel.kicked-from-arena", CC.RED + "You''ve been kicked from this arena as someone is dueling another person."),

	//  Loadout
	CANT_EDIT_INGAME("layout.cant-edit-ingame", CC.RED + "You cannot edit layouts during games."),
	CANT_EDIT_SPEC("layout.cant-edit-spec", CC.RED + "You cannot edit layouts while spectating."),
	LAYOUT_OPEN_EDITOR("layout.open-editor", CC.GREEN + "Welcome to the loadout editor!", CC.GRAY + " * " + CC.SECONDARY + "To save your loadout, close your inventory."),
	LAYOUT_MODIFIED("layout.modified", CC.SECONDARY + "You''ve modified your loadout!", CC.GRAY + CC.ITALIC + "If you need to reset your loadout, try " + CC.SECONDARY + "/resetloadout" + CC.GRAY + CC.ITALIC + "."),
	LAYOUT_RESET("layout.reset", CC.GREEN + "You''ve reset your layout!"),

	//  Prompt
	PROMPT_CANCELLED("prompt.cancelled", CC.RED + "Cancelled arena creation procedure."),
	PROMPT_ERROR("prompt.error", CC.SECONDARY + "I couldn''t understand what you said."),
	PROMPT_INVALID_INT("prompt.invalid-int", CC.RED + "Sorry, that''s not a valid integer! Try again!"),
	PROMPT_BUILDABLE_ONE("prompt.buildable-one", CC.SECONDARY + "Please type " + CC.PRIMARY + "''here''" + CC.SECONDARY + " when you are at the arena BUILDING MINIMUM location! " + CC.GRAY + "(" + CC.PRIMARY + "Type ''cancel'' to exit at any time!" + CC.GRAY + ")"),
	PROMPT_BUILDABLE_TWO("prompt.buildable-two", CC.SECONDARY + "Please type " + CC.PRIMARY + "''here''" + CC.SECONDARY + " when you are at the BUILDING MAXIMUM location! " + CC.GRAY + "(" + CC.PRIMARY + "Type ''cancel'' to exit at any time!" + CC.GRAY + ")"),
	PROMPT_LOCATION_ONE("prompt.location-one", CC.SECONDARY + "Please type " + CC.PRIMARY + "''here''" + CC.SECONDARY + " when you are at the arena minimum location! " + CC.GRAY + "(" + CC.PRIMARY + "Type ''cancel'' to exit at any time!" + CC.GRAY + ")"),
	PROMPT_LOCATION_TWO("prompt.location-two", CC.SECONDARY + "Please type " + CC.PRIMARY + "''here''" + CC.SECONDARY + " when you are at the arena maximum location! " + CC.GRAY + "(" + CC.PRIMARY + "Type ''cancel'' to exit at any time!" + CC.GRAY + ")"),
	PROMPT_SPAWN_ONE("prompt.spawn-one", CC.SECONDARY + "Please type " + CC.PRIMARY + "''here''" + CC.SECONDARY + " when you are at the arena " + ChatColor.BLUE + "Blue" + CC.SECONDARY + " location! " + CC.GRAY + "(" + CC.PRIMARY + "Type ''cancel'' to exit at any time!" + CC.GRAY + ")"),
	PROMPT_SPAWN_TWO("prompt.spawn-two", CC.SECONDARY + "Please type " + CC.PRIMARY + "''here''" + CC.SECONDARY + " when you are at the arena " + CC.RED + "Red" + CC.SECONDARY + " location! " + CC.GRAY + "(" + CC.PRIMARY + "Type ''cancel'' to exit at any time!" + CC.GRAY + ")"),
	PROMPT_TEAM_SIZE("prompt.team-size", CC.SECONDARY + "What do you want the arena team size to be? " + CC.GRAY + "(" + CC.PRIMARY + "Type ''cancel'' to exit at any time!" + CC.GRAY + ")"),

	//  Leaderboard
	LEADERBOARD_UPDATED("leaderboard.updated", CC.GREEN + "You''ve updated all of the leaderboards."),
	LEADERBOARD_FORMAT("leaderboard.format", CC.GRAY + "({0}) " + CC.SECONDARY + "{1}: " + CC.PRIMARY + "{2}"),
	LEADERBOARD_MESSAGE("leaderboard.message", CC.GRAY + CC.S + "-------------------------", CC.B_PRIMARY + "Top {0} {1}:", "{2}", CC.GRAY + CC.S + "-------------------------"),
	LEADERBOARD_HOLOGRAM("leaderboard.hologram", "cba to copy"),

	//  MLG Rush
	PLUGIN_RELOADED("plugin.reloaded", CC.PRIMARY + "MLGRush " + CC.SECONDARY + "has been reloaded!"),
	SET_WORLD_SPAWN("plugin.set-spawn", CC.SECONDARY + "You''ve set the world spawn!"),
	SET_HOLO_SPAWN("plugin.set-holo", CC.SECONDARY + "You''ve set the hologram spawn!"),
	ENTERED_BUILDMODE("plugin.entered-build", CC.GREEN + "You've entered build mode."),
	LEFT_BUILDMODE("plugin.left-build", CC.RED + "You've left build mode.");

	private final String path;
	private final String[] defaultMessages;

	Locale(String path, String... defaultMessages) {
		this.path = path;
		this.defaultMessages = defaultMessages;
	}

	public String format(Object... objects) {
		return new MessageFormat(Color.translate(CorePlugin.getInstance().getConfigHandler().getLangConfig().getString(this.path))).format(objects);
	}

	public List<String> formatLines(Object... objects) {
		final List<String> lines = new ArrayList<>();
		final Config config = CorePlugin.getInstance().getConfigHandler().getLangConfig();

		if (config.get(this.path) instanceof String) {
			lines.add(new MessageFormat(Color.translate(config.getString(this.path))).format(objects));
		} else {
			for (String string : config.getStringList(this.path)) {
				lines.add(new MessageFormat(Color.translate(string)).format(objects));
			}
		}

		return lines;
	}

	public String[] formatLinesArray(Object... objects) {
		return this.formatLines(objects).toArray(new String[0]);
	}
}
