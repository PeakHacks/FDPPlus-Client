/*
 * LiquidBounce+ Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/WYSI-Foundation/LiquidBouncePlus/
 */
package net.ccbluex.liquidbounce.features.module.modules.movement

import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.value.FloatValue
import net.ccbluex.liquidbounce.value.ListValue
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition
import java.util.*
import kotlin.math.cos
import kotlin.math.sin

@ModuleInfo(name = "Clip", description = "Allows you to clip through blocks.", category = ModuleCategory.MOVEMENT, onlyEnable = true)
class Clip : Module() {

    private val modeValue = ListValue("Mode", arrayOf("Teleport", "Flag"), "Teleport")
    private val horizontalValue = FloatValue("Horizontal", 0F, -10F, 10F, "m")
    private val verticalValue = FloatValue("Vertical", 5F, -10F, 10F, "m")

    override fun onEnable() {
        mc.thePlayer ?: return

        val yaw = Math.toRadians(mc.thePlayer.rotationYaw.toDouble())
        val x = -sin(yaw) * horizontalValue.get()
        val z = cos(yaw) * horizontalValue.get()

        when (modeValue.get().lowercase(Locale.getDefault())) {
            "teleport" -> mc.thePlayer.setPosition(mc.thePlayer.posX + x, mc.thePlayer.posY + verticalValue.get(),
                    mc.thePlayer.posZ + z)

            "flag" -> {
                val netHandler = mc.netHandler

                netHandler.addToSendQueue(C04PacketPlayerPosition(mc.thePlayer.posX,
                        mc.thePlayer.posY, mc.thePlayer.posZ, true))
                netHandler.addToSendQueue(C04PacketPlayerPosition(0.5, 0.0,
                        0.5, true))
                netHandler.addToSendQueue(C04PacketPlayerPosition(mc.thePlayer.posX,
                        mc.thePlayer.posY, mc.thePlayer.posZ, true))
                netHandler.addToSendQueue(C04PacketPlayerPosition(mc.thePlayer.posX + x,
                        mc.thePlayer.posY + verticalValue.get(), mc.thePlayer.posZ + z, true))
                netHandler.addToSendQueue(C04PacketPlayerPosition(0.5,
                        0.0, 0.5, true))
                netHandler.addToSendQueue(C04PacketPlayerPosition(mc.thePlayer.posX
                        + 0.5, mc.thePlayer.posY, mc.thePlayer.posZ + 0.5, true))

                mc.thePlayer.setPosition(mc.thePlayer.posX + -sin(yaw) * 0.04, mc.thePlayer.posY,
                        mc.thePlayer.posZ + cos(yaw) * 0.04)
            }
        }
    }

}