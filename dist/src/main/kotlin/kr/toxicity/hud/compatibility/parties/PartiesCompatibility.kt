package kr.toxicity.hud.compatibility.parties

import com.alessiodp.parties.bukkit.BukkitPartiesPlugin
import kr.toxicity.hud.api.listener.HudListener
import kr.toxicity.hud.api.placeholder.HudPlaceholder
import kr.toxicity.hud.api.player.HudPlayer
import kr.toxicity.hud.api.trigger.HudTrigger
import kr.toxicity.hud.api.update.UpdateEvent
import kr.toxicity.hud.compatibility.Compatibility
import org.bukkit.Bukkit
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.entity.Player
import java.util.UUID
import java.util.function.Function

class PartiesCompatibility: Compatibility {
    override val triggers: Map<String, (ConfigurationSection) -> HudTrigger<*>>
        get() = mapOf()
    override val listeners: Map<String, (ConfigurationSection) -> (UpdateEvent) -> HudListener>
        get() = mapOf()
    override val numbers: Map<String, HudPlaceholder<Number>>
        get() = mapOf()
    override val strings: Map<String, HudPlaceholder<String>>
        get() = mapOf(
            "member" to object : HudPlaceholder<String> {
                override fun getRequiredArgsLength(): Int = 1
                override fun invoke(args: MutableList<String>, reason: UpdateEvent): Function<HudPlayer, String> {
                    val int = args[0].toInt()
                    return Function get@ {
                        var i = 0
                        getPlayerPartyMember(it.bukkitPlayer.uniqueId).forEach { online ->
                            if (++i == int) return@get online.name
                        }
                        return@get "<none>"
                    }
                }
            },
            "member_exclude_mine" to object : HudPlaceholder<String> {
                override fun getRequiredArgsLength(): Int = 1
                override fun invoke(args: MutableList<String>, reason: UpdateEvent): Function<HudPlayer, String> {
                    val int = args[0].toInt()
                    return Function get@ {
                        var i = 0
                        val name = it.bukkitPlayer.name
                        getPlayerPartyMember(it.bukkitPlayer.uniqueId).forEach { online ->
                            if (name != online.name && ++i == int) return@get online.name
                        }
                        return@get "<none>"
                    }
                }
            }
        )
    override val booleans: Map<String, HudPlaceholder<Boolean>>
        get() = mapOf()

    private val parties = Class.forName("com.alessiodp.parties.core.bukkit.bootstrap.ADPBukkitBootstrap").getDeclaredField("plugin").run {
        isAccessible = true
        get(Bukkit.getPluginManager().getPlugin("Parties"))
    } as BukkitPartiesPlugin

    private fun getPlayerPartyMember(uuid: UUID): Set<Player> {
        return parties.playerManager.getPlayer(uuid)?.partyId?.let {
            parties.partyManager.getParty(it)?.members?.mapNotNull { id ->
                Bukkit.getPlayer(id)
            }?.toSet()
        } ?: emptySet()
    }
}