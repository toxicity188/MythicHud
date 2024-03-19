package kr.toxicity.hud.layout

import kr.toxicity.hud.equation.EquationLocation
import kr.toxicity.hud.equation.TEquation
import kr.toxicity.hud.image.ImageLocation
import kr.toxicity.hud.manager.ConfigManager
import kr.toxicity.hud.manager.ImageManager
import kr.toxicity.hud.manager.PlayerHeadManager
import kr.toxicity.hud.manager.TextManager
import kr.toxicity.hud.util.*
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextColor
import org.bukkit.configuration.ConfigurationSection
import java.text.DecimalFormat

class LayoutGroup(section: ConfigurationSection) {

    private val loc = ImageLocation(section)

    val align = section.getString("align")?.let {
        runCatching {
            LayoutAlign.valueOf(it.uppercase())
        }.getOrNull()
    } ?: LayoutAlign.LEFT

    val image: List<ImageLayout> = ArrayList<ImageLayout>().apply {
        section.getConfigurationSection("images")?.forEachSubConfiguration { s, configurationSection ->
            add(
                ImageLayout(
                    configurationSection.getString("name").ifNull("name value not set: $s").let { n ->
                        ImageManager.getImage(n).ifNull("this image doesn't exist: $n")
                    },
                    configurationSection.getString("color")?.toTextColor() ?: NamedTextColor.WHITE,
                    ImageLocation(configurationSection) + loc,
                    configurationSection.getDouble("scale", 1.0),
                    configurationSection.getBoolean("outline"),
                    configurationSection.getInt("layout"),
                    configurationSection.toConditions()
                )
            )
        }
    }
    val text: List<TextLayout> = ArrayList<TextLayout>().apply {
        section.getConfigurationSection("texts")?.forEachSubConfiguration { s, configurationSection ->
            add(
                TextLayout(
                    configurationSection.getString("pattern").ifNull("pattern value not set: $s"),
                    configurationSection.getString("name").ifNull("name value not set: $s").let { n ->
                        TextManager.getText(n).ifNull("this text doesn't exist: $n")
                    },
                    ImageLocation(configurationSection) + loc,
                    configurationSection.getDouble("scale", 1.0),
                    configurationSection.getInt("space", 2).coerceAtLeast(0),
                    configurationSection.getString("align").toLayoutAlign(),
                    configurationSection.getString("color")?.toTextColor() ?: NamedTextColor.WHITE,
                    configurationSection.getBoolean("outline"),
                    configurationSection.getInt("layer"),
                    configurationSection.getString("number-equation")?.let {
                        TEquation(it)
                    } ?: TEquation.t,
                    configurationSection.getString("number-format")?.let {
                        DecimalFormat(it).apply {
                            maximumFractionDigits = 0
                        }
                    } ?: ConfigManager.numberFormat,
                    configurationSection.toConditions()
                )
            )
        }
    }
    val head: List<HeadLayout> = ArrayList<HeadLayout>().apply {
        section.getConfigurationSection("heads")?.forEachSubConfiguration { s, configurationSection ->
            add(
                HeadLayout(
                    configurationSection.getString("name").ifNull("name value not set: $s").let {
                        PlayerHeadManager.getHead(it).ifNull("this head doesn't exist: $it in $s")
                    },
                    ImageLocation(configurationSection) + loc,
                    configurationSection.getBoolean("outline"),
                    configurationSection.getInt("layer"),
                    configurationSection.getString("align").toLayoutAlign(),
                    configurationSection.toConditions()
                )
            )
        }
    }
    val conditions = section.toConditions()

    val animation = section.getConfigurationSection("animations")?.let { animations ->
        EquationLocation(animations).location
    } ?: listOf(ImageLocation.zero)
}