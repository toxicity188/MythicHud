package kr.toxicity.hud.element

import kr.toxicity.hud.api.yaml.YamlObject
import kr.toxicity.hud.configuration.HudConfiguration
import kr.toxicity.hud.placeholder.ConditionSource

class HeadElement(
    override val path: String,
    val name: String,
    yaml: YamlObject
) : HudConfiguration, HudElement, ConditionSource by ConditionSource.Impl(yaml) {
    val pixel = yaml.getAsInt("pixel", 1).coerceAtLeast(1)
}