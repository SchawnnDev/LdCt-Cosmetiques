/*
 * ******************************************************
 *  * Copyright (C) 2015 SchawnnDev <contact@schawnndev.fr>
 *  *
 *  * This file (fr.schawnndev.particules.particules.ParticleMagicien) is part of LCCosmetiques.
 *  *
 *  * Created by SchawnnDev on 21/05/15 21:53.
 *  *
 *  * LCCosmetiques can not be copied and/or distributed without the express
 *  * permission of SchawnnDev.
 *  ******************************************************
 */

package fr.schawnndev.particules.particules;

import fr.schawnndev.CosmetiqueManager;
import fr.schawnndev.LCCosmetiques;
import fr.schawnndev.math.FastMath;
import fr.schawnndev.math.RotateVector;
import fr.schawnndev.particules.Particle;
import fr.schawnndev.particules.ParticleEffect;
import fr.schawnndev.particules.ParticleManager;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ParticleMagicien extends Particle {

    @Getter
    public CosmetiqueManager.Cosmetique cosmetique = CosmetiqueManager.Cosmetique.MAGICIEN;

    @Getter
    private Map<UUID, Integer> tasks = new HashMap<>();

    @Override
    public void startParticle(final UUID uuid) {

        ParticleManager.getActiveParticles().put(uuid, "magicien");


        tasks.put(uuid,

                Bukkit.getScheduler().runTaskTimer(LCCosmetiques.getInstance(), new Runnable() {

                    Player player = Bukkit.getPlayer(uuid);
                    private double xRotation;
                    private double yRotation;
                    private double zRotation = 0.0d;
                    private int step;

                    @Override
                    public void run() {
                        if (player != null && player.isOnline()) {

                            Location l = player.getLocation();
                            Vector v = new Vector();

                            for (int i = 0; i < 12; i++) {
                                step += 1;

                                float t = PI / 150 * step;
                                float r = FastMath.sin(t * 2.718f * 12 / 150);
                                float s = r * PI * t;

                                v.setX(1.0f * r * -FastMath.cos(s));
                                v.setZ(1.0f * r * -FastMath.sin(s));
                                v.setY(1.0f * r + 2.6f);

                                RotateVector.rotateVector(v, xRotation, yRotation, zRotation);
                                ParticleEffect.ENCHANTMENT_TABLE.display(0f, 0f, 0f, 0f, 1, l.add(v), 128);

                                l.subtract(v);
                            }
                        } else {
                            stopParticle(uuid);
                        }
                    }
                }, 0l, 2l).getTaskId());
    }

    @Override
    public void stopParticle(UUID uuid) {
        if (tasks.containsKey(uuid)) {
            Bukkit.getScheduler().cancelTask(tasks.get(uuid));
            tasks.remove(uuid);
        }
    }
}
