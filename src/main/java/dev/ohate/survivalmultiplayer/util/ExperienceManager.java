package dev.ohate.survivalmultiplayer.util;

import org.bukkit.entity.*;
import com.google.common.base.*;

import java.util.*;

public class ExperienceManager {

    private static int[] xpTotalToReachLevel;

    private static void initLookupTables(final int maxLevel) {
        xpTotalToReachLevel = new int[maxLevel];

        for (int i = 0; i < xpTotalToReachLevel.length; ++i) {
            xpTotalToReachLevel[i] = ((i >= 30) ? ((int) (3.5 * i * i - 151.5 * i + 2220.0)) : ((i >= 16) ? ((int) (1.5 * i * i - 29.5 * i + 360.0)) : (17 * i)));
        }
    }

    private static int calculateLevelForExp(int exp) {
        int level = 0;
        for (int curExp = 7, incr = 10; curExp <= exp; curExp += incr, ++level, incr += ((level % 2 == 0) ? 3 : 4)) {
        }
        return level;
    }

    public static void changeExp(Player player, int amt) {
        changeExp(player, (double) amt);
    }

    public static void changeExp(Player player, double amt) {
        setExp(player, getCurrentFractionalXP(player), amt);
    }

    public static void setExp(Player player, int amt) {
        setExp(player, 0.0, amt);
    }

    public static void setExp(Player player, double amt) {
        setExp(player, 0.0, amt);
    }

    private static void setExp(Player player, double base, double amt) {
        int xp = (int) Math.max(base + amt, 0.0);
        int curLvl = player.getLevel();
        int newLvl = getLevelForExp(player, xp);

        if (curLvl != newLvl) {
            player.setLevel(newLvl);
        }

        if (xp > base) {
            player.setTotalExperience(player.getTotalExperience() + xp - (int) base);
        }

        double pct = (base - getXpForLevel(newLvl) + amt) / getXpNeededToLevelUp(newLvl);

        player.setExp((float) pct);
    }

    public static int getCurrentExp(Player player) {
        int lvl = player.getLevel();
        return getXpForLevel(lvl) + Math.round(getXpNeededToLevelUp(lvl) * player.getExp());
    }

    private static double getCurrentFractionalXP(Player player) {
        int lvl = player.getLevel();
        return getXpForLevel(lvl) + getXpNeededToLevelUp(lvl) * player.getExp();
    }

    public static boolean hasExp(Player player, int amt) {
        return getCurrentExp(player) >= amt;
    }

    public static boolean hasExp(Player player, double amt) {
        return getCurrentFractionalXP(player) >= amt;
    }

    public static int getLevelForExp(Player player, int exp) {
        if (exp <= 0) {
            return 0;
        }

        if (exp > xpTotalToReachLevel[xpTotalToReachLevel.length - 1]) {
            int newMax = calculateLevelForExp(exp) * 2;
            initLookupTables(newMax);
        }

        int pos = Arrays.binarySearch(xpTotalToReachLevel, exp);

        return (pos < 0) ? (-pos - 2) : pos;
    }

    public static int getXpNeededToLevelUp(int level) {
        Preconditions.checkArgument(level >= 0, "Level may not be negative.");
        return (level > 30) ? (62 + (level - 30) * 7) : ((level >= 16) ? (17 + (level - 15) * 3) : 17);
    }

    public static int getXpForLevel(int level) {
        if (level >= xpTotalToReachLevel.length) {
            initLookupTables(level * 2);
        }

        return xpTotalToReachLevel[level];
    }

    static {
        initLookupTables(25);
    }

}