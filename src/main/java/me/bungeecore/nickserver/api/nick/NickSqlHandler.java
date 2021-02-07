package me.bungeecore.nickserver.api.nick;

import me.bungeecore.nickserver.model.nick.Nick;
import me.bungeecore.nickserver.model.nick.NickPlayer;
import me.xkuyax.utils.mysql.MysqlConnection;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Component
public class NickSqlHandler {

    private final MysqlConnection.ResultSetTransFormer<Nick> nickResultSetTransFormer = resultSet -> new Nick(resultSet.getString("name"),
            UUID.fromString(resultSet.getString("uuid")), resultSet.getString("value"), resultSet.getString("signature"));
    private final MysqlConnection.ResultSetTransFormer<NickPlayer> playerResultSetTransFormer = resultSet -> new NickPlayer(UUID.fromString(resultSet.getString("uuid")),
            UUID.fromString(resultSet.getString("nickUUID")), resultSet.getString("rank"), resultSet.getBoolean("networkNick"));
    private final MysqlConnection mysqlConnection;

    public NickSqlHandler(final MysqlConnection mysqlConnection) {
        this.mysqlConnection = mysqlConnection;
        this.setupTables();
    }

    private void setupTables() {
        this.mysqlConnection.prepare("create table if not exists network_nicknames (id int(11) not null auto_increment, name varchar(16), uuid varchar(36), value text, signature text, primary key (id))");
        this.mysqlConnection.prepare("create table if not exists network_nickedPlayers (id int(11) not null auto_increment, uuid varchar(36), nickUUID varchar(36), rank text, networkNick boolean, primary key (id))");
    }

    public boolean isNicked(final UUID uuid) {
        return this.mysqlConnection.hasEntries("select uuid from network_nickedPlayers where uuid = ?", preparedStatement -> preparedStatement.setString(1, uuid.toString()));
    }

    public boolean isNickUUID(final UUID uuid) {
        return this.mysqlConnection.hasEntries("select uuid from network_nicknames where uuid = ?", preparedStatement -> preparedStatement.setString(1, uuid.toString()));
    }

    public boolean isNetworkNick(final UUID uuid) {
        if (!isNicked(uuid)) return false;
        return this.getNickPlayer(uuid).isNetworkNick();
    }

    public boolean isNickedAndIsNetworkNick(final UUID uuid) {
        return isNickUsed(uuid) && isNetworkNick(uuid);
    }

    public Nick getNickname(final UUID uuid) {
        return this.mysqlConnection.query("select * from network_nicknames where uuid = ?", preparedStatement -> preparedStatement.setString(1, uuid.toString()), this.nickResultSetTransFormer);
    }

    public boolean isNickUsed(final UUID uuid) {
        return this.mysqlConnection.hasEntries("select nickUUID from network_nickedPlayers where nickUUID = ?", preparedStatement -> preparedStatement.setString(1, uuid.toString()));
    }

    public NickPlayer getNickPlayer(final UUID uuid) {
        return this.mysqlConnection.query("select * from network_nickedPlayers where uuid = ?", preparedStatement -> preparedStatement.setString(1, uuid.toString()), this.playerResultSetTransFormer);
    }

    public NickPlayer getNickPlayerWithNickUUID(final UUID nickUUID) {
        return this.mysqlConnection.query("select * from network_nickedPlayers where nickUUID = ?", preparedStatement -> preparedStatement.setString(1, nickUUID.toString()), this.playerResultSetTransFormer);
    }

    public NickPlayer nick(final UUID uuid, final String rank, final boolean networkNick) {
        NickPlayer nickPlayer = new NickPlayer(uuid, getRandomNick().getUuid(), rank, networkNick);
        this.mysqlConnection.prepare("insert into network_nickedPlayers (uuid, nickUUID, rank, networkNick) values (?, ?, ?, ?)", preparedStatement -> {
            preparedStatement.setString(1, uuid.toString());
            preparedStatement.setString(2, nickPlayer.getNickUUID().toString());
            preparedStatement.setString(3, rank);
            preparedStatement.setBoolean(4, networkNick);
        });
        return nickPlayer;
    }

    public UUID unnick(final UUID uuid) {
        this.mysqlConnection.prepare("delete from network_nickedPlayers where uuid = ?", preparedStatement -> preparedStatement.setString(1, uuid.toString()));
        return uuid;
    }

    public List<Nick> getNicks() {
        return this.mysqlConnection.queryList("select * from network_nicknames", MysqlConnection.EMPTY_FILLER, this.nickResultSetTransFormer);
    }

    public Nick getRandomNick() {
        final Random random = new Random();
        List<Nick> list = this.getAvailableNicks();

        if (list.isEmpty()) {
            return new Nick();
        }

        return list.get(random.nextInt(list.size()));
    }

    public List<Nick> getAvailableNicks() {
        final List<Nick> availableNicks = new ArrayList<>();

        this.getNicks().forEach(nick -> {
            if (!this.isNickUsed(nick.getUuid())) {
                availableNicks.add(nick);
            }
        });
        return availableNicks;
    }
}
