package me.bungeecore.nickserver.api.impl;

import me.bungeecore.nickserver.api.INickProvider;
import me.bungeecore.nickserver.api.nick.NickSqlHandler;
import me.bungeecore.nickserver.model.nick.Nick;
import me.bungeecore.nickserver.model.nick.NickPlayer;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class NickProvider implements INickProvider {

    private final NickSqlHandler nickSqlHandler;

    public NickProvider(NickSqlHandler nickSqlHandler) {
        this.nickSqlHandler = nickSqlHandler;
    }

    @Override
    public ResponseEntity<String> status() {
        return ResponseEntity.ok("RUNNING");
    }

    @Override
    public ResponseEntity<NickPlayer> getNickPlayer(UUID uuid) {
        return ResponseEntity.ok(this.nickSqlHandler.getNickPlayer(uuid));
    }

    @Override
    public ResponseEntity<NickPlayer> getNickPlayerWithNickUUID(UUID nickUUID) {
        return ResponseEntity.ok(this.nickSqlHandler.getNickPlayerWithNickUUID(nickUUID));
    }

    @Override
    public ResponseEntity<Nick> getNickname(UUID uuid) {
        return ResponseEntity.ok(this.nickSqlHandler.getNickname(uuid));
    }

    @Override
    public ResponseEntity<Boolean> isNicked(UUID uuid) {
        return ResponseEntity.ok(this.nickSqlHandler.isNicked(uuid));
    }

    @Override
    public ResponseEntity<Boolean> isNetworkNick(UUID uuid) {
        return ResponseEntity.ok(this.nickSqlHandler.isNetworkNick(uuid));
    }

    @Override
    public ResponseEntity<Boolean> isNickedAndIsNetworkNick(UUID uuid) {
        return ResponseEntity.ok(this.nickSqlHandler.isNickedAndIsNetworkNick(uuid));
    }

    @Override
    public ResponseEntity<NickPlayer> nick(UUID uuid) {
        return ResponseEntity.ok(this.nickSqlHandler.nick(uuid, "Spieler", false));
    }

    @Override
    public ResponseEntity<NickPlayer> nick(UUID uuid, String rank) {
        return ResponseEntity.ok(this.nickSqlHandler.nick(uuid, rank, false));
    }

    @Override
    public ResponseEntity<NickPlayer> networkNick(UUID uuid) {
        return ResponseEntity.ok(this.nickSqlHandler.nick(uuid, "Spieler", true));
    }

    @Override
    public ResponseEntity<NickPlayer> networkNick(UUID uuid, String rank) {
        return ResponseEntity.ok(this.nickSqlHandler.nick(uuid, rank, true));
    }

    @Override
    public ResponseEntity<UUID> unnick(UUID uuid) {
        return ResponseEntity.ok(this.nickSqlHandler.unnick(uuid));
    }
}
