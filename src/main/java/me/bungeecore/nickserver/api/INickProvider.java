package me.bungeecore.nickserver.api;

import me.bungeecore.nickserver.model.nick.Nick;
import me.bungeecore.nickserver.model.nick.NickPlayer;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/service/")
public interface INickProvider {

    @GetMapping("/status")
    ResponseEntity<String> status();

    @GetMapping("/nick/get/uuid")
    ResponseEntity<NickPlayer> getNickPlayer(@RequestParam("uuid") UUID uuid);

    @GetMapping("/nick/get/nick/uuid")
    ResponseEntity<NickPlayer> getNickPlayerWithNickUUID(@RequestParam("nickUUID") UUID nickUUID);

    @GetMapping("/nick/get/nickInfo/uuid")
    ResponseEntity<Nick> getNickname(@RequestParam("uuid") UUID uuid);

    @GetMapping("/nick/info/isNicked")
    ResponseEntity<Boolean> isNicked(@RequestParam("uuid") UUID uuid);

    @GetMapping("/nick/info/isNetworkNick")
    ResponseEntity<Boolean> isNetworkNick(@RequestParam("uuid") UUID uuid);

    @GetMapping("/nick/info/isNickedAndIsNetworkNick")
    ResponseEntity<Boolean> isNickedAndIsNetworkNick(@RequestParam("uuid") UUID uuid);

    @PutMapping("/nick/nick")
    ResponseEntity<NickPlayer> nick(@RequestParam("uuid") UUID uuid);

    @PutMapping("/nick/nickWithRank")
    ResponseEntity<NickPlayer> nick(@RequestParam("uuid") UUID uuid, @RequestParam("rank") String rank);

    @PutMapping("/nick/networkNick")
    ResponseEntity<NickPlayer> networkNick(@RequestParam("uuid") UUID uuid);

    @PutMapping("/nick/networkNickWithRank")
    ResponseEntity<NickPlayer> networkNick(@RequestParam("uuid") UUID uuid, @RequestParam("rank") String rank);

    @PutMapping("/nick/unnick")
    ResponseEntity<UUID> unnick(@RequestParam("uuid") UUID uuid);
}
